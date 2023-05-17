/**
 * Copyright (c) Codice Foundation

 * This is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation, either version 3 of the License, or any later version.

 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details. A copy of the GNU Lesser General Public License is distributed along with this program and can be found at
 * http://www.gnu.org/licenses/lgpl.html.
 */
package org.codice.itest.api;

/**
 * An enum that defines the possible states for tests that have completed.
 */

public enum TestStatus {
    PASS(0), FAIL(1), ERROR(2), NOT_EXECUTED(3);

    private int returnCode;
    TestStatus(int returnCode) {
        this.returnCode = returnCode;
    }

    public int getReturnCode() {
        return this.returnCode;
    }
}
