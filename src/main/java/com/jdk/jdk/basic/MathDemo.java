package com.jdk.jdk.basic;

public class MathDemo {

    public static void main(String[] args) {
        // reminder ( MODULUS )
        System.out.println(22 % 3);  //  it produces the remainder of dividing the first value by the second value.

        // Quotient  22 is divided by 3, so the quotient is 3 and the remainder is 1.
        // Quatient:  22 is divided by 3, so the quatient is 7 and remainder is 1
        System.out.println(22 / 3);  //

        int dividend = 556, divisor = 9;
        int quotient = dividend / divisor;
        int remainder = dividend % divisor;

        System.out.println("The Quotient is = " + quotient);
        System.out.println("The Remainder is = " + remainder);

    }
}
