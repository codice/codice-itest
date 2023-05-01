/**
 * Copyright (c) Codice Foundation

 * This is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation, either version 3 of the License, or any later version.

 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details. A copy of the GNU Lesser General Public License is distributed along with this program and can be found at
 * http://www.gnu.org/licenses/lgpl.html.
 */
package org.codice.itest.reporter;

import org.codice.itest.api.TestStatus;
import org.springframework.boot.ExitCodeGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExitCodeReporter implements ExitCodeGenerator {

    private List<Integer> exitCodeMappings = new ArrayList<>();

    public void register(TestStatus testStatus) {
        exitCodeMappings.add(testStatus.getReturnCode());
    }

    public int getExitCode() {
        return Collections.max(exitCodeMappings);
    }
}
