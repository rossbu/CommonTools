package benchmark;



import com.google.common.base.Joiner;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.VerboseMode;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 2, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 2, timeUnit = TimeUnit.SECONDS)
public class DifferentConcats {

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder().include(DifferentConcats.class.getSimpleName())
                .verbosity(VerboseMode.EXTRA)
                .build();
        new Runner(opt).run();
    }

    @Param(value = {"1", "10", "100", "1000", "10000"})
    private int howMany;

    private static final Joiner JOINER = Joiner.on(",");

    @Benchmark
    @Fork(3)
    public String guavaJoiner() {

        List<String> list = new ArrayList<>(howMany);

        for (int i = 0; i < howMany; ++i) {
            list.add("" + i);
        }
        return JOINER.join(list);
    }

    @Benchmark
    @Fork(3)
    public String java9Default() {

        List<String> list = new ArrayList<>(howMany);

        for (int i = 0; i < howMany; ++i) {
            list.add("" + i);
        }

        String result = "";

        for (String s : list) {
            result += s;
        }

        return result;
    }
}