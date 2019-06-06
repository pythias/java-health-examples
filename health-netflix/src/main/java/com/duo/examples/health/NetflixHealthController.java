package com.duo.examples.health;

import com.netflix.runtime.health.api.Health;
import com.netflix.runtime.health.api.HealthCheckStatus;
import com.netflix.runtime.health.api.HealthIndicator;
import com.netflix.runtime.health.api.HealthIndicatorCallback;
import com.netflix.runtime.health.core.SimpleHealthCheckAggregator;
import com.netflix.runtime.health.core.caching.DefaultCachingHealthCheckAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author pythias
 * @since 2019-06-04
 */
@RestController
@RequestMapping("/")
public class NetflixHealthController {
    @Autowired
    ApplicationContext applicationContext;

    private List<HealthIndicator> getIndicators() {
        Map<String, HealthIndicator> beans = applicationContext.getBeansOfType(HealthIndicator.class);
        List<HealthIndicator> indicators = new ArrayList<>(beans.values());
        indicators.add(new TimeoutHealthIndicator(10));
        indicators.add(new TimeoutHealthIndicator(500));
        indicators.add(new TimeoutHealthIndicator(800));
        return indicators;
    }

    @GetMapping("/netflix/cache/health")
    public HealthCheckStatus cacheHealth() throws ExecutionException, InterruptedException {
        DefaultCachingHealthCheckAggregator aggregator = new DefaultCachingHealthCheckAggregator(this.getIndicators(), 1000, TimeUnit.MILLISECONDS, 1000, TimeUnit.MILLISECONDS, null);
        return aggregator.check().get();
    }

    @GetMapping("/netflix/simple/health")
    public HealthCheckStatus simpleHealth() throws ExecutionException, InterruptedException {
        SimpleHealthCheckAggregator aggregator = new SimpleHealthCheckAggregator(this.getIndicators(), 1000, TimeUnit.MILLISECONDS);
        return aggregator.check().get();
    }

    static private class TimeoutHealthIndicator implements HealthIndicator {
        long timeout;
        TimeoutHealthIndicator(long timeout) {
            this.timeout = timeout;
        }

        @Override
        public void check(HealthIndicatorCallback healthIndicatorCallback) {
            try {
                Thread.sleep(this.timeout);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            healthIndicatorCallback.inform(Health.healthy().build());
        }

        @Override
        public String getName() {
            return "timeout-health-" + this.timeout;
        }
    }
}
