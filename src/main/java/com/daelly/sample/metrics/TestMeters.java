package com.daelly.sample.metrics;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Slf4jReporter;

import java.util.concurrent.TimeUnit;

public class TestMeters {

    private static final MetricRegistry metrics = new MetricRegistry();

    private static final Slf4jReporter reporter = Slf4jReporter.forRegistry(metrics).build();

    private static final Meter requests = metrics.meter(MetricRegistry.name(TestMeters.class, "request"));

    public static void handleRequest() {
        requests.mark();
    }

    public static void main(String[] args) throws InterruptedException {
        reporter.start(3, TimeUnit.SECONDS);
        while (true) {
            handleRequest();
            TimeUnit.MILLISECONDS.sleep(100);
        }
    }
}
