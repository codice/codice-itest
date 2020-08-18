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
package org.codice.integrationtest.framework;

import org.codice.integrationtest.api.IntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

@Configuration
public class IntegrationTestConfiguration {
    @Value("${itest.threads:#{1}}")
    private int threads;

    @Value("${itest.tests:#{null}}")
    private String tests;

    @Autowired
    private List<IntegrationTest> diagnosticTestList;

    /**
     * @return the 'ExecutorService' instance to be used by the service. The number of threads to
     * be used is defined by the value of '${threads}'. The default is 1.
     */
    @Bean
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(threads);
    }

    /**
     * @return a stream of all the 'DiagnosticTest' instances found within the application context,
     * filtered by the value of 'itest.tests' if provided.
     */
    @Bean
    public Stream<IntegrationTest> diagnosticTestStream() {
        if (tests == null) {
            return diagnosticTestList.stream();
        }

        List<String> testNameList = Arrays.asList(tests.split(","));
        return diagnosticTestList.stream()
                .filter(t -> testNameList.contains(t.getClass().getName()))
                .sorted(Comparator.comparingInt(t -> testNameList.indexOf(t.getClass().getName())));
    }
}
