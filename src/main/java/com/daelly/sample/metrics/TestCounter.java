package com.daelly.sample.metrics;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Slf4jReporter;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

public class TestCounter {

    private static final MetricRegistry metrics = new MetricRegistry();

    private static final Slf4jReporter reporter = Slf4jReporter.forRegistry(metrics).build();

    private static final Counter pendingJobs = metrics.counter(MetricRegistry.name(TestCounter.class, "pending-jobs"));

    private static final Queue<String> queue = new LinkedList<>();

    public static void add(String str) {
        pendingJobs.inc();
        queue.offer(str);
    }

    public static String take() {
        pendingJobs.dec();
        return queue.poll();
    }

    public static void main(String[] args) throws InterruptedException {
        reporter.start(3, TimeUnit.SECONDS);
        while (true) {
            add("1");
            TimeUnit.SECONDS.sleep(1);
        }
    }
}
