package example;

public strictfp class Cal {

    public static int subFloatFromInt(int op1, float op2)
            throws ArithmeticException {

        // The significand can store at most 23 bits
//        if ((op2 > 0x007fffff) || (op2 < -0x800000)) {
//            throw new ArithmeticException("Insufficient precision");
//        }

        return op1 - (int)op2;
    }

    public static void main(String[] args) {
//        int result = subFloatFromInt(1234567890, 9000000);
        int result = subFloatFromInt(1234567890, 1234567890);

//        int x = 9000000;
//        System.out.println(1234567890 - 9000000);
//        System.out.println(1234567890 - (int)((float)9000000));
        System.out.println(result);
//        System.out.println((float)x);
    }
}
