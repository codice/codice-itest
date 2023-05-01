package org.codice.itest.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.UUID;

@ConfigurationProperties(prefix = "codice.itest")
public record ITestConfigurationProperties ( Integer threads,
                                             String runId,
                                             Integer maxExecutionMinutes,
                                             String loggerName,
                                             List<String> tests) {
    public ITestConfigurationProperties {
        if (threads == null) {
            threads = 1;
        }

        if (runId == null) {
            runId = UUID.randomUUID().toString();
        }

        if (maxExecutionMinutes == null) {
            maxExecutionMinutes = Integer.MAX_VALUE;
        }

        if (loggerName == null) {
            loggerName = "codice.itest";
        }
    }
}
