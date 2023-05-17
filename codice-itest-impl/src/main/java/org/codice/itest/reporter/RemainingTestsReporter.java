package org.codice.itest.reporter;

import org.codice.itest.api.TestResult;

import java.util.List;
import java.util.function.Consumer;

public class RemainingTestsReporter implements Consumer<TestResult> {
    private List<String> testNames;
    public RemainingTestsReporter(List<String> testNames){
        this.testNames = testNames;
    }

    @Override
    public void accept(TestResult testResult) {
        if(testNames.contains(testResult.getTestName())){
            testNames.remove(testResult.getTestName());
        }
    }

    public List<String> getRemainingList(){return testNames;}

}
