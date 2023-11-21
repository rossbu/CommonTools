package com.demo.string;


/**
 * Running the above for different N shows that both behave linearly, but String.format is 5-30 times slower.
 * The reason is that in the current implementation String.format first parses the input with regular expressions and then fills in the parameters.
 * Concatenation with plus, on the other hand, gets optimized by javac (not by the JIT) and uses StringBuilder.append directly.
 * https://stackoverflow.com/questions/513600/should-i-use-javas-string-format-if-performance-is-important
 */
public class StringPerformanceTest {
    public static void main( String[] args ){
//        compareStringPlusAndStringFormat();
        testStringBuilderStringFormaterAndStringPlus();
    }

    private static void compareStringPlusAndStringFormat() {
        int i = 0;
        long prev_time = System.currentTimeMillis();
        long time;

        for( i = 0; i< 100000; i++){
            String s = "Blah" + i + "Blah";
        }
        time = System.currentTimeMillis() - prev_time;

        System.out.println("Time after for loop " + time);

        prev_time = System.currentTimeMillis();
        for( i = 0; i<100000; i++){
            String s = String.format("Blah %d Blah", i);
        }
        time = System.currentTimeMillis() - prev_time;
        System.out.println("Time after for loop " + time);
    }
    public static final String BLAH = "Blah ";
    public static final String BLAH2 = " Blah";
    public static final String BLAH3 = "Blah %d Blah";
    private static void testStringBuilderStringFormaterAndStringPlus() {
        int i = 0;
        long prev_time = System.currentTimeMillis();
        long time;
        int numLoops = 1000000;

        for( i = 0; i< numLoops; i++){
            String s = BLAH + i + BLAH2;
        }
        time = System.currentTimeMillis() - prev_time;

        System.out.println("Time String Plus after for loop " + time);

        prev_time = System.currentTimeMillis();
        for( i = 0; i<numLoops; i++){
            String s = String.format(BLAH3, i);
        }
        time = System.currentTimeMillis() - prev_time;
        System.out.println("Time String Format after for loop " + time);

        prev_time = System.currentTimeMillis();
        for( i = 0; i<numLoops; i++){
            StringBuilder sb = new StringBuilder();
            sb.append(BLAH);
            sb.append(i);
            sb.append(BLAH2);
            String s = sb.toString();
        }
        time = System.currentTimeMillis() - prev_time;
        System.out.println("Time String Builder after for loop " + time);
    }
}
