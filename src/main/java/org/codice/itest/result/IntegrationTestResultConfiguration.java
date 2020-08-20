/**
 * Copyright (c) Codice Foundation

 * This is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation, either version 3 of the License, or any later version.

 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details. A copy of the GNU Lesser General Public License is distributed along with this program and can be found at
 * http://www.gnu.org/licenses/lgpl.html.
 */
package org.codice.itest.result;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.codice.itest.api.TestResultFactory;

@Configuration
public class IntegrationTestResultConfiguration {
    @Value("${itest.result.runid:#{null}}")
    private String runId;

    /**
     * @return an implementation of TestResultFactory
     */
    @ConditionalOnMissingBean
    @Bean
    public TestResultFactory testResultFactory() {
        return new TestResultFactoryImpl(runId());
    }

    /**
     * @return the runId that all tests in this invocation of the service will inherit. This can be
     * provided externally by setting the value of '${itest.result.runId}'. The default is chosen
     * randomly.
     */
    @Bean
    public UUID runId() {
        return Optional.ofNullable(runId).map(UUID::fromString).orElse(UUID.randomUUID());
    }
}
