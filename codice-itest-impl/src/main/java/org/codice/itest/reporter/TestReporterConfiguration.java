/**
 * Copyright (c) Codice Foundation

 * This is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation, either version 3 of the License, or any later version.

 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details. A copy of the GNU Lesser General Public License is distributed along with this program and can be found at
 * http://www.gnu.org/licenses/lgpl.html.
 */
package org.codice.itest.reporter;

import org.codice.itest.api.TestResult;
import org.codice.itest.config.ITestConfigurationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

@Configuration
public class TestReporterConfiguration {
    private final ITestConfigurationProperties iTestConfigurationProperties;
    private List<String> testNameList;

    public TestReporterConfiguration(ITestConfigurationProperties iTestConfigurationProperties) {
        this.iTestConfigurationProperties = iTestConfigurationProperties;
        this.testNameList = iTestConfigurationProperties.tests();
    }

    @Bean
    public ExitCodeReporter exitCodeReporter(){
        return new ExitCodeReporter();
    }

    @Bean("testReporter")
    @ConditionalOnProperty(prefix="codice.itest", name="loggerName")
    public Consumer<TestResult> loggingDiagnosticTestReporter() {
        Logger logger = LoggerFactory.getLogger(iTestConfigurationProperties.loggerName());
        return new LoggingDiagnosticTestReporter(logger, Object::toString);
    }

    @Bean("testReporter")
    @ConditionalOnMissingBean
    public Consumer<TestResult> defaultLoggingDiagnosticTestReporter() {
        Logger logger = LoggerFactory.getLogger(LoggingDiagnosticTestReporter.class);
        return new LoggingDiagnosticTestReporter(logger, Object::toString);
    }

    @Bean("exitCodeReporterConsumer")
    public Consumer<TestResult> exitCodeReporterConsumer(ExitCodeReporter exitCodeReporter) {
        return (tr) -> exitCodeReporter.register(tr.getTestStatus());
    }

    @Bean("remainingTestsConsumer")
    public Consumer<TestResult> remainingTestsReporterConsumer() {
        return new RemainingTestsReporter(testNameList);
    }
}
