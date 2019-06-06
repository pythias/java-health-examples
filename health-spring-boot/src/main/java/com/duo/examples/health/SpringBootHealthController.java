package com.duo.examples.health;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.*;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

/**
 * @author pythias
 * @since 2019-06-03
 */
@RestController
@RequestMapping("/")
public class SpringBootHealthController {
    private final OrderedHealthAggregator aggregate = new OrderedHealthAggregator();

    @Autowired
    ApplicationContext applicationContext;

    @GetMapping("/reactive/health")
    public Mono<Health> reactive() {
        Map<String, ReactiveHealthIndicator> indicators = applicationContext.getBeansOfType(ReactiveHealthIndicator.class);
        indicators.put("t1", new TimeoutReactHealthIndicator(10));
        indicators.put("t2", new TimeoutReactHealthIndicator(500));
        indicators.put("t3", new TimeoutReactHealthIndicator(800));

        ReactiveHealthIndicatorRegistry registry = new DefaultReactiveHealthIndicatorRegistry(indicators);
        CompositeReactiveHealthIndicator composite = new CompositeReactiveHealthIndicator(this.aggregate, registry);
        composite.timeoutStrategy(1000, Health.down().withDetail("reason", "timeout").build());
        return composite.health();
    }

    @GetMapping("/normal/health")
    public Health health1() {
        Map<String, HealthIndicator> indicators = applicationContext.getBeansOfType(HealthIndicator.class);
        indicators.put("t1", new TimeoutHealthIndicator(10));
        indicators.put("t2", new TimeoutHealthIndicator(500));
        indicators.put("t3", new TimeoutHealthIndicator(800));

        CompositeHealthIndicator composite = new CompositeHealthIndicator(this.aggregate, indicators);
        return composite.health();
    }

    static private class TimeoutReactHealthIndicator implements ReactiveHealthIndicator {
        long timeout;
        TimeoutReactHealthIndicator(long timeout) {
            this.timeout = timeout;
        }

        @Override
        public Mono<Health> health() {
            return Mono.delay(Duration.ofMillis(this.timeout)).map((l) -> Health.up().withDetail("time", this.timeout).build());
        }
    }

    static private class TimeoutHealthIndicator implements HealthIndicator {
        long timeout;
        TimeoutHealthIndicator(long timeout) {
            this.timeout = timeout;
        }

        @Override
        public Health health() {
            try {
                Thread.sleep(this.timeout);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return Health.up().withDetail("time", this.timeout).build();
        }
    }
}
