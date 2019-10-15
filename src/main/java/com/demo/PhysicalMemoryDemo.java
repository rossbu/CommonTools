package com.demo;

import com.sun.management.ThreadMXBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.InvocationTargetException;

/**
 *
 */
public class PhysicalMemoryDemo {

    private static final Logger LOGGER = LoggerFactory.getLogger(PhysicalMemoryDemo.class);
    private static final OperatingSystemMXBean OS_BEAN = ManagementFactory.getOperatingSystemMXBean();

    public static Long totalPhysicalMemory() {
        return getAttribute("getTotalPhysicalMemorySize");
    }

    public static Long freePhysicalMemory() {
        return getAttribute("getFreePhysicalMemorySize");
    }

    public static Long totalSwapSpace() {
        return getAttribute("getTotalSwapSpaceSize");
    }

    public static Long freeSwapSpace() {
        return getAttribute("getFreeSwapSpaceSize");
    }

    public static Long ourCommittedVirtualMemory() {
        return getAttribute("getCommittedVirtualMemorySize");
    }

    private static <T> T getAttribute(String name) {
        LOGGER.trace("Bean lookup for {}", name);
        for (Class<?> s = OS_BEAN.getClass(); s != null; s = s.getSuperclass()) {
            @SuppressWarnings("unchecked")
            T result = invokeyMethod(name, s);
            if (result != null) {
                return result;
            }
        }
        for (Class<?> i : OS_BEAN.getClass().getInterfaces()) {
            @SuppressWarnings("unchecked")
            T result = invokeyMethod(name, i);
            return result;
        }
        LOGGER.trace("Returning null for {}", name);
        return null;
    }

    private static <T> T invokeyMethod(String name, Class<?> s) {
        System.out.println("--class is :" + s.getSimpleName());
        try {
            @SuppressWarnings("unchecked")
            T result = (T) s.getMethod(name).invoke(OS_BEAN);
            LOGGER.trace("Bean lookup successful using {}, got {}", s, result);
            return result;
        } catch (SecurityException | InvocationTargetException | IllegalArgumentException | IllegalAccessException | NoSuchMethodException e) {
            LOGGER.trace("Bean lookup failed on {}", s, e);
        }
        return null;
    }

    public static void main(String[] args) {
        sun.management.BaseOperatingSystemImpl baseOperatingSystem;
        Object o;
        System.out.println("Total Physical Memory: " + DebuggingUtils.toBase2SuffixedString(totalPhysicalMemory()) + "B");
        System.out.println("Free Physical Memory: " + DebuggingUtils.toBase2SuffixedString(freePhysicalMemory()) + "B");
        System.out.println("Total Swap Space: " + DebuggingUtils.toBase2SuffixedString(totalSwapSpace()) + "B");
        System.out.println("Free Swap Space: " + DebuggingUtils.toBase2SuffixedString(freeSwapSpace()) + "B");
        System.out.println("Committed Virtual Memory: " + DebuggingUtils.toBase2SuffixedString(ourCommittedVirtualMemory()) + "B");


        ThreadMXBean threadMXBean = (ThreadMXBean)ManagementFactory.getThreadMXBean();
        long bytes = threadMXBean.getThreadAllocatedBytes(Thread.currentThread().getId());
        System.out.println(bytes);
    }
}

final class DebuggingUtils {

    private static final String[] BASE_10_SUFFIXES = new String[] { "", "k", "M", "G", "T", "P", "E" };
    //Also use the non i syntax to refer to base 2 as generally accepted. See http://en.wikipedia.org/wiki/Gigabyte
    private static final String[] BASE_2_SUFFIXES = new String[] { "", "K", "M", "G", "T", "P", "E" };

    private static final long[] BASE_10_DIVISORS = new long[BASE_10_SUFFIXES.length];
    static {
        for (int i = 0; i < BASE_10_DIVISORS.length; i++) {
            long n = 1;
            for (int j = 0; j < i; j++) {
                n *= 1000;
            }
            BASE_10_DIVISORS[i] = n;
        }
    }

    public static String toBase2SuffixedString(long n) {
        if (n > 0 && Long.bitCount(n) == 1) {
            int i = Long.numberOfTrailingZeros(Math.abs(n)) / 10;
            return (n >> (i * 10)) + BASE_2_SUFFIXES[i];
        } else {
            int i = (63 - Long.numberOfLeadingZeros(n)) / 10;

            long factor = 1L << (i * 10);
            long leading = n / factor;
            long decimalFactor = factor / 10;
            if (decimalFactor == 0) {
                return leading + BASE_2_SUFFIXES[i];
            } else {
                long decimal = (n - (leading * factor)) / (factor / 10);
                return leading + "." + decimal + BASE_2_SUFFIXES[i];
            }
        }
    }

    public static String toBase10SuffixedString(long n) {
        for (int i = 0; i < BASE_10_SUFFIXES.length; i++) {
            long d = (n / 1000) / BASE_10_DIVISORS[i];

            if (d == 0) {
                return (n / BASE_10_DIVISORS[i]) + BASE_10_SUFFIXES[i];
            }
        }

        throw new AssertionError();
    }

    private DebuggingUtils() {
        //
    }
}