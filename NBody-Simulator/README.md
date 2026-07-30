# 🌌 NBody Simulator

<div align="center">

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-21.0.1-007396?style=for-the-badge&logo=java&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)

**Um simulador de N-corpos que calcula e visualiza as interações gravitacionais entre múltiplos objetos celestes em tempo real.**

[Instalação](#-instalação) • [Como Usar](#-uso) • [Exemplos](#-exemplos) • [Documentação](#-fundamentos-físicos)

</div>

---

## 📋 Índice

- [Sobre o Projeto](#-sobre-o-projeto)
- [Características](#-características)
- [Demo](#-demo)
- [Fundamentos Físicos](#-fundamentos-físicos)
- [Como Funciona](#️-como-funciona)
- [Instalação](#-instalação)
- [Uso](#-uso)
- [Exemplos](#-exemplos)
- [Arquitetura](#-arquitetura)
- [Tecnologias](#️-tecnologias)
- [Performance](#-performance)
- [Roadmap](#-roadmap)
- [Contribuindo](#-contribuindo)
- [Licença](#-licença)

---

## 🌟 Sobre o Projeto

O **NBody Simulator** é uma simulação física que demonstra como múltiplos corpos celestes interagem entre si através da força gravitacional. Cada corpo exerce uma força sobre todos os outros corpos, criando um sistema dinâmico complexo.

### Este projeto é ideal para:

- 🎓 **Estudantes** de física e astronomia
- 🔬 **Pesquisadores** em física computacional
- 💻 **Desenvolvedores** interessados em simulações físicas
- 🌠 **Entusiastas** de astronomia e sistemas dinâmicos
- 📚 **Educadores** demonstrando conceitos de gravitação

---

## ✨ Características

- ✅ Simulação em tempo real com JavaFX
- ✅ Suporte para milhares de partículas (1500+ corpos)
- ✅ Visualização interativa e colorida
- ✅ Implementação do Método de Euler
- ✅ Sistema de softening para estabilidade numérica
- ✅ Cenário pré-configurado (buraco negro + partículas orbitando)
- ✅ Arquitetura orientada a objetos limpa
- ✅ Código bem documentado e comentado

---

## 🎬 Demo

### Simulação de Buraco Negro

```
         🌟  🌟
      🌟       🌟
    🌟    ⚫    🌟
      🌟       🌟
         🌟  🌟

Buraco negro central (massa 5000) cercado por
1500 partículas orbitando com distribuição gaussiana
```

**Características visuais:**
- 🔵 **Partículas pequenas** (massa ≤ 1. 5): Azul escuro
- ⚫ **Buraco negro** (massa > 1000): Preto com halo laranja

---

## 🔬 Fundamentos Físicos

### Lei da Gravitação Universal de Newton

A força gravitacional entre dois corpos é dada por:

```
F = G × (m₁ × m₂) / r²
```

**Onde:**
- `F` = Força gravitacional (N)
- `G` = Constante gravitacional (neste simulador: `1.0`)
- `m₁, m₂` = Massas dos corpos (kg)
- `r` = Distância entre os corpos (m)

### Vetores de Força

Para cada par de corpos, calculamos um vetor de força:

```
       Corpo 2 (m₂)
            ●
           /|
          / |
     F⃗  /  | F_y
        /   |
       /    |
      /_____|
     ●      
Corpo 1    F_x
 (m₁)
```

**Componentes do vetor força:**
- `F_x = F × cos(θ) = F × (Δx / r)`
- `F_y = F × sin(θ) = F × (Δy / r)`

### Aceleração e Movimento

A partir da **Segunda Lei de Newton**:

```
a⃗ = F⃗ / m
v⃗(t+Δt) = v⃗(t) + a⃗ × Δt
r⃗(t+Δt) = r⃗(t) + v⃗ × Δt
```

**Diagrama de vetores em um sistema de 3 corpos:**

```
                    ●₃ (Corpo 3)
                   /|\
                  / | \
             F⃗₃₁/  |  \ F⃗₃₂
                /   |   \
               /    |    \
              ↙     ↓     ↘
         
    ●₁ ←────────────────────→ ●₂
   Corpo 1      F⃗₁₂          Corpo 2
        ↖                    ↗
         \                  /
      F⃗₁₃\                / F⃗₂₃
           \              /
            \            /
             \          /
              \        /
               \      /
                \    /
                 \  /
                  ↓
```

Cada corpo sente a força resultante de todos os outros corpos:

```
Corpo 1: F⃗_total₁ = F⃗₁₂ + F⃗₁₃
Corpo 2: F⃗_total₂ = F⃗₂₁ + F⃗₂₃
Corpo 3: F⃗_total₃ = F⃗₃₁ + F⃗₃₂
```

---

## ⚙️ Como Funciona

### Algoritmo de Simulação

O simulador utiliza o **Método de Euler** para integração numérica:

```
LOOP de simulação (a cada timestep Δt):

1. Para cada corpo i:
   a. Zerar aceleração: a⃗ᵢ = (0, 0)
   
2. Para cada par de corpos (i, j) onde i ≠ j:
   a. Calcular força: F⃗ᵢⱼ = G × mᵢ × mⱼ / (r² + ε²)
   b. Acumular força: F⃗ᵢ += F⃗ᵢⱼ
   
3. Para cada corpo i:
   a.  Calcular aceleração: a⃗ᵢ = F⃗ᵢ / mᵢ
   b. Atualizar velocidade: v⃗ᵢ(t+Δt) = v⃗ᵢ(t) + a⃗ᵢ × Δt
   c. Atualizar posição: r⃗ᵢ(t+Δt) = r⃗ᵢ(t) + v⃗ᵢ × Δt
```

**Nota:** `ε` (epsilon/softening) é um parâmetro de suavização (`1e-2`) que previne singularidades quando corpos se aproximam muito.

### Visualização do Ciclo de Atualização

```
Timestep t:                Timestep t+Δt:
                          
    v⃗(t)                      v⃗(t+Δt) = v⃗(t) + a⃗×Δt
     ↑                           ↗
     |                          /
     ●─────→ a⃗                ●
   r⃗(t)                     r⃗(t+Δt) = r⃗(t) + v⃗×Δt
```

---

## 🚀 Instalação

### Pré-requisitos

- ☕ **Java JDK 21** ou superior
- 📦 **Apache Maven 3.6+**
- 🖥️ **Sistema Operacional**: Windows, macOS ou Linux

### Verificar versões instaladas

```bash
java -version
# Deve mostrar: openjdk version "21" ou superior

mvn -version
# Deve mostrar: Apache Maven 3. 6.x ou superior
```

### Passos de Instalação

1️⃣ **Clone o repositório:**

```bash
git clone https://github.com/vitinh0z/NBody-Simulator.git
cd NBody-Simulator
```

2️⃣ **Compile o projeto com Maven:**

```bash
mvn clean compile
```

3️⃣ **Execute o simulador:**

```bash
mvn javafx:run
```

### Instalação Alternativa (JAR executável)

```bash
# Gerar JAR
mvn clean package

# Executar JAR
java -jar target/NBody-Simulator-1.0-SNAPSHOT.jar
```

---

## 💻 Uso

### Configuração Básica

#### Editando o Cenário Inicial

Abra o arquivo `src/main/java/view/Main.java` e modifique o método `criarCenarioInicial()`:

```java
public void criarCenarioInicial(){
    
    // 1. Criar corpo central (buraco negro)
    Body buracoNegro = new Body(
        5000,              // massa
        6.0,               // raio visual
        new Vector(0, 0),  // posição inicial (x, y)
        new Vector(0, 0)   // velocidade inicial (vx, vy)
    );
    simulation.addBody(buracoNegro);
    
    // 2. Gerar partículas ao redor
    Random random = new Random();
    long numeroDeParticulas = 1500L; // Ajuste aqui! 
    
    for (int i = 0; i < numeroDeParticulas; i++){
        double x = random.nextGaussian() * 120;
        double y = random. nextGaussian() * 120;
        double mass = 0.5 + random.nextDouble() * 2.0;
        
        Vector posicao = new Vector(x, y);
        Vector velocidade = new Vector(
            random.nextGaussian() * 0.5, 
            random.nextGaussian() * 0.5
        );
        
        Body body = new Body(mass, 0.9, posicao, velocidade);
        simulation.addBody(body);
    }
}
```

#### Parâmetros Ajustáveis

| Parâmetro | Localização | Efeito |
|-----------|-------------|--------|
| `dt` | `Main.java:51` | Timestep (menor = mais preciso, mas mais lento) |
| `G` | `Body.java:16` | Constante gravitacional (padrão: `1.0`) |
| `numeroDeParticulas` | `Main.java:74` | Quantidade de corpos (máx recomendado: 2000) |
| `softening` | `Body.java:76` | Parâmetro de suavização (padrão: `1e-2`) |
| `WIDTH`, `HEIGHT` | `Main.java:19-20` | Dimensões da janela |

---

## 🎯 Exemplos

### Exemplo 1: Sistema Sol-Terra Simplificado

```java
// Sol no centro
Body sol = new Body(
    1000,              // massa grande
    10.0,              // raio visual
    new Vector(0, 0),  // centro
    new Vector(0, 0)   // parado
);

// Terra orbitando
Body terra = new Body(
    1. 0,                      // massa pequena
    3.0,                      // raio visual
    new Vector(150, 0),       // posição inicial (à direita)
    new Vector(0, 5. 0)        // velocidade tangencial (para cima)
);

simulation.addBody(sol);
simulation.addBody(terra);
```

**Resultado esperado:** Terra orbita em elipse ao redor do Sol

```
        ↑ v⃗_terra
        |
    ☉ ←─── 🌍
   Sol    Terra
   
Órbita elíptica estável
```

### Exemplo 2: Sistema Binário (Duas Estrelas)

```java
// Estrela 1
Body estrela1 = new Body(
    500, 8.0,
    new Vector(-100, 0),  // esquerda
    new Vector(0, 3.0)    // velocidade para cima
);

// Estrela 2
Body estrela2 = new Body(
    500, 8.0,
    new Vector(100, 0),   // direita
    new Vector(0, -3.0)   // velocidade para baixo
);

simulation. addBody(estrela1);
simulation.addBody(estrela2);
```

**Resultado esperado:** Duas estrelas orbitam um centro de massa comum

```
    ●₁ ↑
      ╲│╱
       ⊗  ← centro de massa
      ╱│╲
    ●₂ ↓

Órbita circular ao redor do baricentro
```

### Exemplo 3: Problema dos 3 Corpos (Figura-8)

```java
// Configuração especial que gera órbita em forma de 8
double m = 1.0;
double v = 0.347111;

Body b1 = new Body(m, 3.0, 
    new Vector(-1, 0), 
    new Vector(v, v));
    
Body b2 = new Body(m, 3.0, 
    new Vector(1, 0), 
    new Vector(v, v));
    
Body b3 = new Body(m, 3.0, 
    new Vector(0, 0), 
    new Vector(-2*v, -2*v));

simulation.addBody(b1);
simulation. addBody(b2);
simulation.addBody(b3);
```

---

## 🏗️ Arquitetura

### Estrutura do Projeto

```
NBody-Simulator/
│
├── src/main/java/
│   ├── Model/
│   │   ├── Body.java          # Representa um corpo celeste
│   │   ├── Vector.java        # Operações vetoriais 2D
│   │   └── Simulation.java    # Engine de simulação
│   │
│   └── view/
│       └── Main.java          # Interface JavaFX + Loop principal
│
├── pom.xml                     # Configuração Maven
├── README.md
└── . gitignore
```

### Diagrama de Classes

```
┌─────────────────┐
│   Simulation    │
├─────────────────┤
│ - bodies: List  │
├─────────────────┤
│ + addBody()     │
│ + update(dt)    │
└────────┬────────┘
         │ 1
         │ contém
         │ *
         ▼
┌─────────────────┐         ┌─────────────────┐
│      Body       │         │     Vector      │
├─────────────────┤         ├─────────────────┤
│ - mass: double  │────────>│ - x: double     │
│ - radius: dbl   │ usa 3x  │ - y: double     │
│ - position: V   │         ├─────────────────┤
│ - velocity: V   │         │ + sum()         │
│ - accel: V      │         │ + subs()        │
├─────────────────┤         │ + magnitude()   │
│ + calculateF()  │         │ + normalize()   │
│ + applyForce()  │         └─────────────────┘
│ + update(dt)    │
└─────────────────┘
```

### Classes Principais

#### 1️⃣ `Vector. java` - Álgebra Vetorial

```java
public class Vector {
    private final double x, y;
    
    // Operações imutáveis
    public Vector sum(Vector v)          // Adição: a⃗ + b⃗
    public Vector subs(Vector v)         // Subtração: a⃗ - b⃗
    public Vector multiEscalar(double k) // Multiplicação: k × a⃗
    public double magnitude()            // Módulo: |a⃗|
    public Vector normalizacao()         // Unitário: â = a⃗/|a⃗|
}
```

#### 2️⃣ `Body.java` - Corpo Celeste

```java
public class Body {
    private double mass;
    private Vector position, velocity, acceleration;
    
    // Cálculo de força entre corpos
    public Vector calculateForceFrom(Body other) {
        Vector delta = other.position.subs(this.position);
        double r = delta.magnitude();
        double distanceSq = (r * r) + (softening * softening);
        double magnitude = (G * mass * other.mass) / distanceSq;
        return delta.normalizacao().multiEscalar(magnitude);
    }
    
    // Aplicar força e atualizar posição
    public void applyForce(Vector force) { ... }
    public void update(double dt) { ...  }
}
```

#### 3️⃣ `Simulation.java` - Motor da Simulação

```java
public class Simulation {
    private List<Body> bodies;
    
    public void update(double dt) {
        // 1. Resetar acelerações
        for (Body body : bodies) {
            body.resetAcceleration();
        }
        
        // 2.  Calcular forças (O(n²))
        for (Body bodyA : bodies) {
            for (Body bodyB : bodies) {
                if (bodyA != bodyB) {
                    Vector force = bodyA.calculateForceFrom(bodyB);
                    bodyA.applyForce(force);
                }
            }
        }
        
        // 3. Atualizar posições
        for (Body body : bodies) {
            body.update(dt);
        }
    }
}
```

#### 4️⃣ `Main.java` - Interface e Renderização

```java
public class Main extends Application {
    private Simulation simulation;
    
    // Loop de animação JavaFX
    new AnimationTimer() {
        @Override
        public void handle(long now) {
            simulation.update(0.1);      // Atualizar física
            desenharCorpos(pincel);      // Renderizar
        }
    }. start();
}
```

---

## 🛠️ Tecnologias

| Tecnologia | Versão | Uso |
|------------|--------|-----|
| **Java** | 21 | Linguagem principal |
| **JavaFX** | 21.0.1 | Interface gráfica e animação |
| **Maven** | 3.8+ | Gerenciamento de dependências |
| **OpenJFX** | 21. 0.1 | Biblioteca de UI |

### Por que JavaFX?

- ✅ Renderização de alta performance com `Canvas` e `GraphicsContext`
- ✅ `AnimationTimer` para loops de 60 FPS
- ✅ API moderna e orientada a objetos
- ✅ Multiplataforma (Windows, macOS, Linux)

---

## ⚡ Performance

### Complexidade Algorítmica

| Operação | Complexidade | Descrição |
|----------|--------------|-----------|
| Cálculo de forças | **O(n²)** | Cada corpo interage com todos os outros |
| Atualização de posições | **O(n)** | Linear no número de corpos |
| Renderização | **O(n)** | Desenhar cada corpo uma vez |

### Benchmarks

| Número de Corpos | FPS Esperado | Uso de RAM |
|------------------|--------------|------------|
| 100 | 60 FPS | ~50 MB |
| 500 | 60 FPS | ~100 MB |
| 1500 | 45-60 FPS | ~200 MB |
| 3000 | 20-30 FPS | ~400 MB |
| 5000+ | <15 FPS | >600 MB |

**Nota:** Benchmarks em Intel i5, 8GB RAM

### Dicas de Otimização

1. **Reduzir partículas**: `numeroDeParticulas = 500` para FPS mais alto
2. **Aumentar timestep**: `dt = 0. 2` (menos preciso, mais rápido)
3. **Desativar trails**: Se implementados, consomem memória
4. **Usar softening maior**: `softening = 0.1` reduz cálculos extremos

---

## 🔧 Roadmap

### ✅ Implementado

- [x] Simulação básica de N-corpos
- [x] Renderização em tempo real
- [x] Método de Euler
- [x] Sistema de softening
- [x] Interface JavaFX

### 🚧 Em Desenvolvimento

- [ ] Implementar **Runge-Kutta 4** (RK4) para maior precisão
- [ ] **Detecção de colisões** e fusão de corpos
- [ ] **Sistema de trails** (rastros de órbitas)
- [ ] **Zoom e pan** interativos com mouse
- [ ] **Pause/Resume** da simulação

### 🔮 Futuro

- [ ] **Suporte 3D** com renderização OpenGL
- [ ] **Barnes-Hut algorithm** para otimização O(n log n)
- [ ] **Salvar/carregar** configurações em JSON/XML
- [ ] **Presets de cenários** (Sistema Solar, galáxias, etc.)
- [ ] **Gráficos de energia** (cinética + potencial)
- [ ] **Modo VR** para visualização imersiva

---

## 📐 Fórmulas Importantes

### Distância Euclidiana

```
r = √[(x₂ - x₁)² + (y₂ - y₁)²]
```

### Vetor Unitário (Direção)

```
r⃗̂ = (x₂ - x₁, y₂ - y₁) / r
```

### Força Gravitacional Vetorial

```
F⃗ = (G × m₁ × m₂ / (r² + ε²)) × r⃗̂
```

### Conservação de Energia

```
E_total = E_cinética + E_potencial = constante

E_cinética = Σ(½ × mᵢ × vᵢ²)

E_potencial = -Σ(G × mᵢ × mⱼ / rᵢⱼ)  para i < j
```

### Método de Euler

```
v⃗ₙ₊₁ = v⃗ₙ + a⃗ₙ × Δt
r⃗ₙ₊₁ = r⃗ₙ + v⃗ₙ × Δt
```

---

## 🐛 Troubleshooting

### Problema: "JavaFX runtime components are missing"

**Solução:**
```bash
# Certifique-se de que o Maven está baixando as dependências
mvn clean install

# Execute com o plugin JavaFX
mvn javafx:run
```

### Problema: Simulação muito lenta

**Soluções:**
1.  Reduzir número de partículas em `Main.java:74`
2. Aumentar `dt` para `0.2` em `Main.java:51`
3. Fechar outros programas pesados
4. Verificar se está usando Java 21 (versões antigas são mais lentas)

### Problema: Corpos "explodem" ou voam para o infinito

**Causa:** Timestep muito grande ou softening muito pequeno

**Solução:**
```java
// Em Main.java, reduzir dt
simulation.update(0.05); // Ao invés de 0.1

// Em Body.java, aumentar softening
double softening = 0.1; // Ao invés de 1e-2
```

### Problema: Tela preta sem corpos visíveis

**Verificar:**
1. Os corpos estão sendo criados?  (Debug em `criarCenarioInicial()`)
2. As posições estão dentro da janela? ([-400, 400] para tela 800px)
3. Os raios são grandes o suficiente? (mínimo 1.0)

---

## 🤝 Contribuindo

Contribuições são muito bem-vindas!  🎉

### Como Contribuir

1. **Fork** o projeto
2. Crie uma **branch** para sua feature:
   ```bash
   git checkout -b feature/MinhaNovaFeature
   ```
3. **Commit** suas mudanças:
   ```bash
   git commit -m 'feat: Adiciona sistema de colisões'
   ```
4. **Push** para a branch:
   ```bash
   git push origin feature/MinhaNovaFeature
   ```
5.  Abra um **Pull Request**

### Padrões de Código

- ✅ Use **camelCase** para variáveis e métodos
- ✅ Use **PascalCase** para classes
- ✅ Adicione **comentários** em português
- ✅ Mantenha **responsabilidade única** por classe
- ✅ Escreva **código limpo e legível**

### Ideias para Contribuir

- 🐛 Reportar bugs
- 📝 Melhorar documentação
- ✨ Adicionar novos cenários/presets
- ⚡ Otimizar performance
- 🎨 Melhorar visualização
- 🧪 Adicionar testes unitários

---

## 📚 Referências

### Livros

- Newton, I. (1687). *Philosophiæ Naturalis Principia Mathematica*
- Feynman, R. (1963). *The Feynman Lectures on Physics*
- Press, W. et al. (2007). *Numerical Recipes: The Art of Scientific Computing*

### Papers

- Barnes, J. & Hut, P. (1986). "A hierarchical O(N log N) force-calculation algorithm"
- Hairer, E. et al. (1993). "Solving Ordinary Differential Equations I"

### Links

- [Three-body problem - Wikipedia](https://en.wikipedia.org/wiki/Three-body_problem)
- [N-body simulation - Wikipedia](https://en.wikipedia.org/wiki/N-body_simulation)
- [JavaFX Documentation](https://openjfx.io/)
- [Newton's law of universal gravitation](https://en.wikipedia.org/wiki/Newton%27s_law_of_universal_gravitation)

---

## 📝 Licença

Este projeto está sob a licença **MIT**.

```
MIT License

Copyright (c) 2024 vitinh0z

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
```

---

## 👤 Autor

**Victor Gabriel (vitinh0z)**

- GitHub: [@vitinh0z](https://github.com/vitinh0z)
- LinkedIn: [vitinh0z](https://linkedIn.com/in/vitinh0z)
- Email: [Email](victor10.vg23@gmail.com)

---
ALterações realizadas apra funcionamento em GPU
Pedro Henrique Gomes de Campos

## 🙏 Agradecimentos

- Isaac Newton, pela Lei da Gravitação Universal
- Comunidade JavaFX, pelas excelentes bibliotecas
- Todos os contribuidores do projeto

---

<div align="center">

### ⭐ Se este projeto te ajudou, considere dar uma estrela!

**[⬆ Voltar ao topo](#-nbody-simulator)**

---

Feito por [vitinh0z](https://github.com/vitinh0z)

</div>
