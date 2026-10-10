package extensions.modules.shapes;

class Arc {

//    public Arc(PVector A, PVector B, PVector C, Circle refCircle) {
//        this.A = A;
//        this.B = B;
//        this.C = C;
//        this.refCircle = refCircle;
//        this.refCircleAngleStart = Utils.angleForPointOnCircleInDegrees(refCircle, B);
//        this.refCircleAngleEnd = Utils.angleForPointOnCircleInDegrees(refCircle, C);
//    }
//
//    public ArrayList<Shape> drawTriangleSubdivisionFill(PApplet sketch, Shape shape, int depth, float sigma) {
//        String side;
//
//        if (depth >= 0) {
//
//            if (depth == 0) {
//                side = "a";
//            } else if (depth == 1) {
//                side = "b";
//            } else if (depth == 2) {
//                side = "a";
//            } else {
//                side = Utils.getRandomTriangleSide();
//            };
//
//            Random r = new Random();
//            r.setSeed(1);
//            PVector pointOnSide = shape.computeRandomGaussianPointOnSide(side, 0);
//            // machen: This needs to be done to resolve canot resolve symbol error
//            Shape left;
//            Shape right;
//            ArrayList<Shape> al = new ArrayList<>();
//
//            if (side.equals("a")) {
//
//                left = new NotReallyAnArc(shape.getA(), pointOnSide, shape.getC(), refCircle);
//                right = new NotReallyAnArc(shape.getA(), shape.getB(), pointOnSide, refCircle);
//
//            } else if (side.equals("b")) {
//
//
//                left = new NotReallyAnArc(shape.getA(), shape.getB(), pointOnSide, refCircle);
//                right = new Triangle(pointOnSide, shape.getB(), shape.getC());
//
//            } else if (side.equals("c")) {
//
//
//                left = new Triangle(pointOnSide, shape.getB(), shape.getC());
//                right = new NotReallyAnArc(shape.getA(), pointOnSide, shape.getC(), refCircle);
//
//            } else {
//                throw new IllegalArgumentException();
//            }
//
//            al.add(left);
//            al.add(right);
//            return al;
//        }
//
//        return new ArrayList<>();
//
//    }
//
//    ;



}
