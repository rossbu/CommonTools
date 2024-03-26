package benchmark;

import java.lang.invoke.MethodType;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.VerboseMode;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 10, time = 5, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 5, timeUnit = TimeUnit.SECONDS)
public class PrimitiveToWrapper {

    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder().include(PrimitiveToWrapper.class.getSimpleName())
                .verbosity(VerboseMode.EXTRA)
                .build();
        new Runner(opt).run();
    }

    @State(Scope.Benchmark)
    public static class InnerScope {

        static final Map<Integer, Class<?>> MAP = Map.of(
                0, int.class,
                1, long.class,
                2, boolean.class,
                3, byte.class,
                4, char.class,
                5, float.class,
                6, double.class,
                7, short.class,
                8, void.class
        );

        Class<?> clazz;

        @Setup(Level.Invocation)
        public void setUp() {
            int index = ThreadLocalRandom.current().ints(0, 9).findFirst().orElse(-1);
            clazz = MAP.get(index);
        }
    }

    @Benchmark
    @Fork(3)
    public Class<?> ifStatements(InnerScope scope) {

        if (scope.clazz == int.class) { return Integer.class; }
        if (scope.clazz == long.class) { return Long.class; }
        if (scope.clazz == boolean.class) { return Boolean.class; }
        if (scope.clazz == byte.class) { return Byte.class; }
        if (scope.clazz == char.class) { return Character.class; }
        if (scope.clazz == float.class) { return Float.class; }
        if (scope.clazz == double.class) { return Double.class; }
        if (scope.clazz == short.class) { return Short.class; }
        if (scope.clazz == void.class) { return Void.class; }

        throw new RuntimeException("just because");
    }

    private static final Map<Class<?>, Class<?>> PRIMITIVE_TO_CLASS = Map.of(
            int.class, Integer.class,
            long.class, Long.class,
            boolean.class, Boolean.class,
            byte.class, Byte.class,
            char.class, Character.class,
            float.class, Float.class,
            double.class, Double.class,
            short.class, Short.class,
            void.class, Void.class
    );

    @Benchmark
    @Fork(3)
    public Class<?> map(InnerScope scope) {
        return PRIMITIVE_TO_CLASS.get(scope.clazz);
    }

    @Benchmark
    @Fork(3)
    @SuppressWarnings("unchecked")
    public static <T> Class<T> wrap(InnerScope scope) {
        return (Class<T>) MethodType.methodType(scope.clazz).wrap().returnType();
    }

}