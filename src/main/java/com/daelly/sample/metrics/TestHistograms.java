package com.daelly.sample.metrics;

import com.codahale.metrics.Histogram;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Slf4jReporter;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class TestHistograms {

    private static final MetricRegistry metrics = new MetricRegistry();

    private static final Slf4jReporter reporter = Slf4jReporter.forRegistry(metrics).build();

    private static final Histogram randomNums = metrics.histogram(MetricRegistry.name(TestHistograms.class, "random"));

    public static void handleRequest(double random) {
        randomNums.update((int) (random * 100));
    }

    public static void main(String[] args) throws InterruptedException {
        reporter.start(3, TimeUnit.SECONDS);
        Random random = new Random();
        while (true) {
            handleRequest(random.nextDouble());
            TimeUnit.MILLISECONDS.sleep(100);
        }
    }
}
