package jzap.benchmarks;

import jzap.Logger;
import jzap.Zap;
import jzap.zapcore.DurationEncoder;
import jzap.zapcore.Level;
import jzap.zapcore.TimeEncoder;
import jzap.zapcore.ZapCore;
import jzap.ztest.Discarder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.profile.GCProfiler;
import org.openjdk.jmh.profile.StackProfiler;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import static jzap.zapcore.DurationEncoder.NanosDurationEncoder;
import static jzap.zapcore.Level.*;
import static jzap.zapcore.TimeEncoder.EpocMilliTimeEncoder;


public class ScenarioBenchTest {

    private static final int CORES = Runtime.getRuntime().availableProcessors();
    private static final Logger logger = newZapLogger(DEBUG);

    private static Logger newZapLogger(Level lvl) {
        var ec = Zap.newProductionEncoderConfig();
        ec.setEncodeDuration(NanosDurationEncoder);
        ec.setEncodeTime(EpocMilliTimeEncoder);
        var enc = ZapCore.newJSONEncoder(ec);
        return Zap.newLogger(ZapCore.newCore(enc, new Discarder(), lvl));
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @BenchmarkMode(Mode.AverageTime)
    @Fork(1)
    @Warmup(iterations = 1, time = 5, timeUnit = TimeUnit.SECONDS)
    @Measurement(time = 5, timeUnit = TimeUnit.SECONDS)
    public void benchmarkWithoutFields() {
        logger.info("Test logging, but use a somewhat realistic message length.");
    }

    @Test
    public void testBenchmarks() throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(ScenarioBenchTest.class.getSimpleName())
                .threads(CORES)
                .addProfiler(GCProfiler.class)
                .addProfiler(StackProfiler.class)
                .build();
        Collection<RunResult> runResults = new Runner(opt).run();
        Assertions.assertFalse(runResults.isEmpty());
    }
}
