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
import org.slf4j.Logger;

import java.util.function.Consumer;
import java.util.function.Function;

final class LoggingDiagnosticTestReporter implements Consumer<TestResult> {

    private Logger logger;

    private Function<TestResult, String> testResultFormatter;

    public LoggingDiagnosticTestReporter(Logger logger, Function<TestResult, String> testResultFormatter) {
        this.logger = logger;
        this.testResultFormatter = testResultFormatter;
    }

    public void accept(TestResult testResult) {
        String formattedResult = testResultFormatter.apply(testResult);

        switch (testResult.getTestStatus()) {
            case PASS: logger.info(formattedResult);
                break;
            case FAIL: logger.warn(formattedResult);
                break;
            case ERROR, NOT_EXECUTED: logger.error(formattedResult);
        }
    }
}
