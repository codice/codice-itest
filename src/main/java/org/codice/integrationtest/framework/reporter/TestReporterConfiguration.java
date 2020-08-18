/**
 * Copyright (c) Codice Foundation
 *
 * <p>This is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or any later version.
 *
 * <p>This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details. A copy of the GNU Lesser General Public
 * License is distributed along with this program and can be found at
 * <http://www.gnu.org/licenses/lgpl.html>.
 */
package org.codice.integrationtest.framework.reporter;

import java.util.function.Consumer;
import java.util.function.Function;

import org.codice.integrationtest.api.TestResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class TestReporterConfiguration {
    @Value("${itest.reporter.url:#{null}}")
    private String logstashUrl;

    @Value("${itest.reporter.logger:#{null}}")
    private String loggerName;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public Function<TestResult, String> toStringFormatter(){
        return new ToStringFormatter();
    }

    @Bean
    public SlackFormatter slackFormatter(){
        return new SlackFormatter();
    }

    @Bean("testReporter")
    @ConditionalOnProperty(prefix="itest.reporter", name="logger")
    public Consumer<TestResult> loggingDiagnosticTestReporter() {
        Logger logger = LoggerFactory.getLogger(loggerName);
        return new LoggingDiagnosticTestReporter(logger, toStringFormatter());
    }

    @Bean("testReporter")
    @ConditionalOnMissingBean
    public Consumer<TestResult> defaultLoggingDiagnosticTestReporter() {
        Logger logger = LoggerFactory.getLogger(LoggingDiagnosticTestReporter.class);
        return new LoggingDiagnosticTestReporter(logger, toStringFormatter());
    }

    @Bean
    @ConditionalOnProperty(prefix="itest.reporter", name="url")
    public Consumer<TestResult> restDiagnosticTestReporter() {
        return new RestDiagnosticTestReporter(restTemplate(), logstashUrl, slackFormatter());
    }
}
