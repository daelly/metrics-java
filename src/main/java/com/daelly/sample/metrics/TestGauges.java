package com.daelly.sample.metrics;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Slf4jReporter;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * 最简单的监控，定时打印状态
 */
public class TestGauges {

    private static final MetricRegistry metrics = new MetricRegistry();

    private static final Queue<String> queue = new LinkedBlockingDeque<>();

    private static final Slf4jReporter reporter = Slf4jReporter.forRegistry(metrics).build();

    public static void main(String[] args) throws Exception {
        reporter.start(3, TimeUnit.SECONDS);

        Gauge<Integer> gauge = new Gauge<Integer>() {
            @Override
            public Integer getValue() {
                return queue.size();
            }
        };

        metrics.register(MetricRegistry.name(TestGauges.class, "pending-job", "size"), gauge);

        for (int i = 0; i < 20; i++) {
            queue.add("a");
            Thread.sleep(1000);
        }
    }
}
