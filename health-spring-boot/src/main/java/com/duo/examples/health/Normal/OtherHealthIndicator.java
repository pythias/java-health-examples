package com.duo.examples.health.Normal;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

/**
 * @author pythias
 * @since 2019-06-06
 */
@Component
public class OtherHealthIndicator extends AbstractHealthIndicator {
    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        // 做各种检查
        builder.up();
    }
}
