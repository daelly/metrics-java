package com.daelly.sample.metrics;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Slf4jReporter;
import com.codahale.metrics.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class TestTimers {

    private static final Logger logger = LoggerFactory.getLogger(TestTimers.class);

    private static final MetricRegistry metrics = new MetricRegistry();

    private static final Slf4jReporter reporter = Slf4jReporter.forRegistry(metrics).build();

    private static final Timer requests = metrics.timer(MetricRegistry.name(TestTimers.class, "request"));

    public static void handleRequest(int sleep) {
        Timer.Context context = requests.time();
        try {
            Thread.sleep(sleep);
        } catch (InterruptedException e) {
            logger.error("handleRequest sleep exception:", e);
        } finally {
            context.stop();
        }
    }

    public static void main(String[] args) {
        reporter.start(3, TimeUnit.SECONDS);
        Random random = new Random();
        while (true) {
            handleRequest(random.nextInt(1000));
        }
    }
}
