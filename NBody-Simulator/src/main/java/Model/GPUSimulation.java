package Model;

import com.aparapi.Kernel;
import com.aparapi.Range;
import com.aparapi.device.Device;

import java.util.ArrayList;
import java.util.List;

public class GPUSimulation implements SimulationEngine {

    private static final float BOUNCE_FACTOR = -0.8f; 
    private int subSteps = 100;

    private float width = 800.0f;
    private float height = 600.0f;

    static {
        // 🔇 DESATIVA LOGS REPETITIVOS NO TERMINAL
        System.setProperty("com.aparapi.enableExecutionModeReporting", "false");
        System.setProperty("com.aparapi.enableShowGeneratedOpenCL", "false");
        System.setProperty("com.aparapi.enableProfiling", "false");
    }

    private final List<Body> bodies;

    private float[] posX;
    private float[] posY;
    private float[] velX;
    private float[] velY;
    private float[] massas;

    private NBodyKernel kernel;
    private boolean statusImpresso = false;

    public GPUSimulation() {
        this.bodies = new ArrayList<>();
    }

    public GPUSimulation(List<Body> bodies) {
        this.bodies = bodies;
    }

    @Override
    public void addBody(Body body) {
        this.bodies.add(body);
    }

    @Override
    public List<Body> getBodies() {
        return bodies;
    }

    public void setDimensions(double width, double height) {
        this.width = (float) width;
        this.height = (float) height;
    }

    public void setSubSteps(int subSteps) {
        this.subSteps = Math.max(1, subSteps);
    }

    @Override
    public void update(double dt) {
        int n = bodies.size();
        if (n == 0) return;

        if (posX == null || posX.length != n) {
            posX = new float[n];
            posY = new float[n];
            velX = new float[n];
            velY = new float[n];
            massas = new float[n];
        }

        for (int i = 0; i < n; i++) {
            Body b = bodies.get(i);
            posX[i] = (float) b.getPosition().getX();
            posY[i] = (float) b.getPosition().getY();
            velX[i] = (float) b.getVelocity().getX();
            velY[i] = (float) b.getVelocity().getY();
            massas[i] = (float) b.getMass();
        }

        float miniDt = (float) (dt / subSteps);

        if (kernel == null || kernel.getTotal() != n) {
            if (kernel != null) {
                kernel.dispose();
            }
            kernel = new NBodyKernel(posX, posY, velX, velY, massas, miniDt, width, height, BOUNCE_FACTOR);
            
            // Tenta forçar o modo GPU
            kernel.setExecutionMode(Kernel.EXECUTION_MODE.GPU);
        } else {
            kernel.updateDt(miniDt);
            kernel.updateBounds(width, height);
        }

        // Executa os sub-passos na GPU
        for (int step = 0; step < subSteps; step++) {
            kernel.execute(Range.create(n));
        }

        // 📢 IMPRIME APENAS UMA VEZ NO PRIMEIRO FRAME O STATUS REAL
        if (!statusImpresso) {
            statusImpresso = true;
            System.out.println("\n==================================================");
            Kernel.EXECUTION_MODE modoAtivo = kernel.getExecutionMode();
            System.out.println("🚀 [DIAGNÓSTICO FINAL] Modo Ativo do Kernel: " + modoAtivo);
            
            if (modoAtivo == Kernel.EXECUTION_MODE.GPU) {
                System.out.println("✅ O código está sendo executado diretamente na GPU!");
                System.out.println("👉 Nota: No Gerenciador de Tarefas, altere o gráfico da GPU de '3D' para 'Compute_0' para ver o uso.");
            } else {
                System.out.println("⚠️ O Aparapi recusou a GPU e fez fallback para CPU (" + modoAtivo + ").");
                System.out.println("   Isso ocorre quando os drivers OpenCL da placa são mais recentes (OpenCL 3.0) do que a biblioteca JNI do Aparapi.");
            }
            System.out.println("==================================================\n");
        }

        for (int i = 0; i < n; i++) {
            Body b = bodies.get(i);
            b.setPosition(new Vector(posX[i], posY[i]));
            b.setVelocity(new Vector(velX[i], velY[i]));
        }
    }

    private static class NBodyKernel extends Kernel {
        private final float[] posX;
        private final float[] posY;
        private final float[] velX;
        private final float[] velY;
        private final float[] massas;
        private final int total;
        
        private float dt;
        private float limitX;
        private float limitY;
        private final float bounceFactor;

        public NBodyKernel(float[] posX, float[] posY, float[] velX, float[] velY, float[] massas, 
                           float dt, float limitX, float limitY, float bounceFactor) {
            this.posX = posX;
            this.posY = posY;
            this.velX = velX;
            this.velY = velY;
            this.massas = massas;
            this.total = posX.length;
            this.dt = dt;
            this.limitX = limitX;
            this.limitY = limitY;
            this.bounceFactor = bounceFactor;
        }

        public int getTotal() {
            return total;
        }

        public void updateDt(float dt) {
            this.dt = dt;
        }

        public void updateBounds(float limitX, float limitY) {
            this.limitX = limitX;
            this.limitY = limitY;
        }

        @Override
        public void run() {
            int i = getGlobalId();

            if (i == 0) {
                posX[0] = 0.0f;
                posY[0] = 0.0f;
                velX[0] = 0.0f;
                velY[0] = 0.0f;
            } else {
                float px = posX[i];
                float py = posY[i];
                float vx = velX[i];
                float vy = velY[i];

                float ax = 0.0f;
                float ay = 0.0f;
                float G = 1.0f;
                float softeningSq = 0.0001f;

                for (int j = 0; j < total; j++) {
                    if (i != j) {
                        float dx = posX[j] - px;
                        float dy = posY[j] - py;

                        float rSq = dx * dx + dy * dy + softeningSq;
                        float r = sqrt(rSq); 

                        float factor = (G * massas[j]) / (rSq * r);

                        ax += factor * dx;
                        ay += factor * dy;
                    }
                }

                vx += ax * dt;
                vy += ay * dt;
                px += vx * dt;
                py += vy * dt;

                float halfX = limitX / 2.0f;
                float halfY = limitY / 2.0f;

                if (px < -halfX) {
                    px = -halfX;
                    vx = vx * bounceFactor;
                } else if (px > halfX) {
                    px = halfX;
                    vx = vx * bounceFactor;
                }

                if (py < -halfY) {
                    py = -halfY;
                    vy = vy * bounceFactor;
                } else if (py > halfY) {
                    py = halfY;
                    vy = vy * bounceFactor;
                }

                velX[i] = vx;
                velY[i] = vy;
                posX[i] = px;
                posY[i] = py;
            }
        }
    }
}