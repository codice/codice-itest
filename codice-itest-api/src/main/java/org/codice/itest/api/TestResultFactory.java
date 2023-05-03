/**
 * Copyright (c) Codice Foundation

 * This is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation, either version 3 of the License, or any later version.

 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details. A copy of the GNU Lesser General Public License is distributed along with this program and can be found at
 * http://www.gnu.org/licenses/lgpl.html.
 */
package org.codice.itest.api;

import java.time.Instant;

/**
 * A factory interface for creating TestResult objects. codice-itest provides a default implementation
 * of this interface but it's included here so that its behavior can be customized.
 */
public interface TestResultFactory {

    /**
     * @param testName - the name of the test that passed.
     * @param startTime - the start time of the test
     * @return an appropriate TestResult object.
     */
    TestResult pass(String testName, Instant startTime, Instant endTime);

    /**
     * @param testName - the name of the test that failed.
     * @param throwable - the exception or error that was raised.
     * @param startTime - the start time of the test
     * @return an appropriate TestResult object.
     */
    TestResult fail(String testName, Throwable throwable, Instant startTime, Instant endTime);

    /**
     * @param testName - the name of the test that failed.
     * @param throwable - the exception or error that was raised.
     * @param startTime - the start time of the test
     * @return an appropriate TestResult object.
     */
    TestResult error(String testName, Throwable throwable, Instant startTime, Instant endTime);
}
