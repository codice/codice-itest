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

import java.util.List;
import java.util.function.Consumer;

public class RemainingTestsReporter implements Consumer<TestResult> {
    private List<String> testNames;
    private List<String> remainingTestNames;

    public RemainingTestsReporter(List<String> testNames){
        this.testNames = testNames;
        this.remainingTestNames = testNames;
    }

    @Override
    public void accept(TestResult testResult) {
        if(testNames.contains(testResult.getTestName())){
            remainingTestNames.remove(testResult.getTestName());
        }
    }

    public List<String> getRemainingList(){return remainingTestNames;}

}
