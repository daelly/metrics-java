package com.daelly.sample.metrics;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class DatabaseHealthCheck extends HealthCheck {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseHealthCheck.class);

    @Override
    protected Result check() throws Exception {
        if (database.ping()) {
            return Result.healthy();
        }

        return Result.unhealthy("Can't ping database.");
    }

    static class Database {
        public boolean ping() {
            Random random = new Random();
            return random.nextBoolean();
        }
    }

    private final Database database;

    public DatabaseHealthCheck(Database database) {
        this.database = database;
    }

    public static void main(String[] args) {
        HealthCheckRegistry registry = new HealthCheckRegistry();
        registry.register("database1", new DatabaseHealthCheck(new Database()));
        registry.register("database2", new DatabaseHealthCheck(new Database()));
        while (true) {
            for (Map.Entry<String, Result> entry : registry.runHealthChecks().entrySet()) {
                if (entry.getValue().isHealthy()) {
                    logger.info("{} : OK", entry.getKey());
                } else {
                    logger.error("{} : FAIL, error message:{}", entry.getKey(), entry.getValue().getMessage());
                    final Throwable e = entry.getValue().getError();
                    if (e != null) {
                        logger.error("health check exception:", e);
                    }
                }
            }

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                logger.info("sleep exception:", e);
            }
        }
    }
}
