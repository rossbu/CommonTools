package com.demo.string;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@OutputTimeUnit(TimeUnit.SECONDS)
@BenchmarkMode({Mode.Throughput})
@Warmup(iterations = 10)
@Fork(value = 1)
@State(Scope.Benchmark)
public class StringBenchmark {

    private static final int LOOPS = 1000;

    @Benchmark
    public int testOld() {
        int counter = 0;
        for (int i = 0; i < LOOPS; i++) {
            String s = "What do you get if you multiply " + counter + " by " + counter + "?";
            counter += s.length();
        }
        return counter;
    }

    @Benchmark
    public int testNew() {
        int counter = 0;
        for (int i = 0; i < LOOPS; i++) {
            String s = String.format("What do you get if you multiply %d by %d?", counter, counter);
            counter += s.length();
        }
        return counter;
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(StringBenchmark.class.getSimpleName())
                .forks(1)
                .build();
        new Runner(opt).run();
    }
}