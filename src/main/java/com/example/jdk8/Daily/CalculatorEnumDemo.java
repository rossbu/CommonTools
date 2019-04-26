package com.example.jdk8.Daily;

import java.util.function.DoubleBinaryOperator;

enum CalculateEnum {
    ADD ((x, y) -> (x + y)),
    SUBTRACT ((x, y) -> ( x - y)),
    MULTIPLY ( (x, y) -> ( x * y)) ,
    DIVIDE ((x, y) -> ( x / y)),
    POWER (Math::pow);

    private final DoubleBinaryOperator function;

    CalculateEnum(DoubleBinaryOperator function) {
        this.function = function;
    }

    public DoubleBinaryOperator getFunction() {
        return function;
    }
}

enum CalculateEnum2 {
    ADD ((x, y) -> (x + y), "+"),
    SUBTRACT ((x, y) -> ( x - y), "-"),
    MULTIPLY ( (x, y) -> ( x * y), "*") ,
    DIVIDE ((x, y) -> ( x / y), "/"),
    POWER (Math::pow, "^");

    private final DoubleBinaryOperator function;
    private final String Op;


    CalculateEnum2(DoubleBinaryOperator function, String Op) {
        this.function = function;
        this.Op = Op;
    }

    public DoubleBinaryOperator getFunction() {
        return function;
    }

    public String getOp() { return Op; }
}

public class CalculatorEnumDemo {
    public static double calc(double x, double y, CalculateEnum operation) {
        return operation.getFunction().applyAsDouble(x, y);
    }
    public static void main(String[] args) {
        double a = 2;
        double b = 6;
        double c = calc(a, b, CalculateEnum.POWER);
        double d = calc(c, b, CalculateEnum.MULTIPLY);
        double calc = calc(d, a, CalculateEnum.DIVIDE);
        System.out.println(calc);

    }
}

