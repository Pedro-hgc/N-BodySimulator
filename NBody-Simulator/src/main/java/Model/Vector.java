package Model;

public class Vector {

    private final double x;
    private final double y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Vector sum (Vector vector){
        return new Vector(this.x + vector.x, this.y + vector.y);
    }

    public Vector subs (Vector vector){
        return new Vector(this.x - vector.x, this.y - vector.y);
    }

    public Vector multiEscalar (double k){
        return new Vector(this.x * k, this.y * k);
    }

    public double magnitude (){
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }

    public Vector normalizacao (){
        double mag = magnitude();
        if (mag == 0){
            return new Vector(0,0);
        }
        return multiEscalar(1.0/mag);
    }

    public Double diferenceTo (Vector vector){
        Vector diference = this.subs(vector);
        return diference.magnitude();
    }

    public Vector divEscalar (double k){
        if (k == 0){
            throw new ArithmeticException("Divisor for 0");
        }
        return new Vector(this.x / k, this.y / k);
    }

    @Override
    public String toString() {
        return "(" + x + " , " + y + ")";
    }
}