/* (C)2021 */
package com.demo.func;

@FunctionalInterface
public interface IfElseHandler {
    /**
     * if else
     *
     * @param trueHandle when true
     * @param falseHandle when false
     * @return void
     */
    void trueOrFalseHandle(Runnable trueHandle, Runnable falseHandle);
}
