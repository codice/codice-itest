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
 * codice-itest will execute any instances of this interface in the application
 * context. Methods are called in the following order: setup(), test(), cleanup().
 */
public interface IntegrationTest {

    /**
     * @return A name to uniquely identify the test
     */
    default String getName() {
        return this.getClass()
                .toString();
    }

    /**
     * This method is used to set up any test pre-conditions. The default implementation does
     * nothing.
     *
     * @throws Exception which is caught by the DiagnosticTestService.
     */
    default void setup() throws Exception {
    };

    /**
     * Defines the test steps.
     *
     * @throws Exception which is caught by the DiagnosticTestService.
     */
    void test() throws Exception;

    /**
     * This method is used to clean up any used resources or restore the system state after a test.
     * It is guaranteed to be called before the system exits. The default implementation does
     * nothing.
     *
     * @throws Exception which is caught by the DiagnosticTestService.
     */
    default void cleanup() throws Exception {
    };
}
