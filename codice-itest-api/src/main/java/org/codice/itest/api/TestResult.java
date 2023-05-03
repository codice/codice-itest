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
 * This interface describes the result of each test.
 */
public interface TestResult {
    /**
     * @return the classname of the test.
     */
    String getTestName();

    /**
     * @return Return the start time of the test
     */
    Instant getStartTime();

    /**
     * @return Returns the duration of the test measured in milliseconds
     */
    long getDuration();

    /**
     * @return Return the end time of the test
     */
    Instant getEndTime();

    /**
     * @return A status indicating whether test passed, failed or errored-out.
     */
    TestStatus getTestStatus();
}
