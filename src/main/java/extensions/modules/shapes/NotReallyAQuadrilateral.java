package extensions.modules.shapes;

import processing.core.PVector;

import java.util.Objects;

public class NotReallyAQuadrilateral {

    private final PVector A;
    private final PVector B;
    private final PVector C;
    private final PVector D;

    public NotReallyAQuadrilateral(final PVector a, final PVector b, final PVector c, final PVector d) {
        A = a;
        B = b;
        C = c;
        D = d;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotReallyAQuadrilateral that = (NotReallyAQuadrilateral) o;
        return Objects.equals(A, that.A) && Objects.equals(B, that.B) && Objects.equals(C, that.C) && Objects.equals(D, that.D);
    }

    @Override
    public int hashCode() {
        return Objects.hash(A, B, C, D);
    }

    @Override
    public String toString() {
        return "NotReallyAQuadrilateral{" +
                "A=" + A +
                ", B=" + B +
                ", C=" + C +
                ", D=" + D +
                '}';
    }

    public PVector getA(){
        return A;
    }

    public PVector getB() {
        return B;
    }

    public PVector getC() {
        return C;
    }

    public PVector getD() {
        return D;
    }

    public PVector getVertex(String vertex){
        if(vertex == "A"){
            return getA();
        } else if (vertex == "B"){
            return getB();
        } else if (vertex == "C"){
            return getB();
        } else if (vertex == "D"){
            return getB();
        } else {
            throw new IllegalArgumentException("Invalid vertex - Must A, B, C or D");
        }

    };

    public PVector getSegment(String segment){
        if(Objects.equals(segment, "a")){
            return getA();
        } else if (segment == "b"){
            return getB();
        } else if (segment == "c"){
            return getB();
        } else if (segment == "d"){
            return getB();
        } else {
            throw new IllegalArgumentException("Invalid vertex - Must A, B, C or D");
        }

    };




}
