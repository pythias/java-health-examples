package com.duo.examples.health;

import com.netflix.runtime.health.api.Health;
import com.netflix.runtime.health.api.HealthIndicator;
import com.netflix.runtime.health.api.HealthIndicatorCallback;

/**
 * @author pythias
 * @since 2019-06-06
 */
public class OtherHealthIndicator implements HealthIndicator {
    @Override
    public void check(HealthIndicatorCallback healthIndicatorCallback) {
        healthIndicatorCallback.inform(Health.healthy().build());
    }

    @Override
    public String getName() {
        return "other-health";
    }
}
