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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public final class RestDiagnosticTestReporter implements Consumer<TestResult> {

    private RestTemplate restTemplate;

    private String logstashUrl;

    private Function<TestResult, String> testResultFormatter;

    private Logger logger = LoggerFactory.getLogger(RestDiagnosticTestReporter.class);

    public RestDiagnosticTestReporter(RestTemplate restTemplate, String logstashUrl, Function<TestResult, String> testResultFormatter) {
        this.restTemplate = restTemplate;
        this.logstashUrl = logstashUrl;
        this.testResultFormatter = testResultFormatter;
    }

    @Override
    public void accept(TestResult testResult) {
        String formattedResult = testResultFormatter.apply(testResult);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(formattedResult, headers);
        ResponseEntity<String> response = restTemplate.exchange(logstashUrl,
                HttpMethod.POST, entity, String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            String error = String.format("Failed to send test result %s to %s, got response %s ",
                    formattedResult,
                    logstashUrl,
                    response);
            logger.error(error);
        }
    }
}