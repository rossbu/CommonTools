package com.jdk.jdk5.generic;

/**
 * Created by tbu on 7/15/2014.
 */

/**
 * http://www.angelikalanger.com/GenericsFAQ/FAQSections/ParameterizedTypes.html#What is a parameterized (or generic) type?
 *
 * @param <X>
 * @param <Y>
 * @author Administrator
 */
class Pair<X, Y> {
    private X first;
    private Y second;

    public Pair(X a1, Y a2) {
        first = a1;
        second = a2;
    }

    public X getFirst() {
        return first;
    }

    public Y getSecond() {
        return second;
    }

    public void setFirst(X arg) {
        first = arg;
    }

    public void setSecond(Y arg) {
        second = arg;
    }
}

class TestPair {
    public static void main(String[] arg) {
        Pair<String, String> pair = new Pair<String, String>("1", "1-1");
//		Pair<String,Long> limit = new Pair<String,Long> ("maximum",1024L);
        Pair<?, ?> limit = new Pair<String, Long>("maximum", 1024L);
        printPair(limit);
    }

    public static void printPair(Pair<?, ?> pair) {
        System.out.println("(" + pair.getFirst() + "," + pair.getSecond() + ")");
    }

}
