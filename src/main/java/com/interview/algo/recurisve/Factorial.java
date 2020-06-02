package com.interview.algo.recurisve;


/**
 * In mathematics, the factorial of a positive integer n, denoted by n!, is the product of all positive integers less than or equal to n:
 * For example, The value of 0! is 1, according to the convention for an empty product.
 *
 * The value of 0! is 1, according to the convention for an empty product.[1]
 */
class Factorial {

    static int factorial( int n ) {
        if (n != 0)  // termination condition
            return n * factorial(n-1); // recursive call
        else
            return 1;
    }

    public static void main(String[] args) {
        int number = 4, result;
        result = factorial(number);
        System.out.println(number + " factorial = " + result);
    }
}
