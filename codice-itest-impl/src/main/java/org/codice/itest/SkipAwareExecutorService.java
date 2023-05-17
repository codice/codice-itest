package org.codice.itest;

import org.codice.itest.api.TestResult;
import org.codice.itest.api.TestResultFactory;
import org.codice.itest.reporter.RemainingTestsReporter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Arrays;
import java.util.concurrent.*;
import java.util.function.Consumer;

import static org.codice.itest.api.TestStatus.NOT_EXECUTED;

@Component("SkipAwareExecutorService")
public class SkipAwareExecutorService extends ThreadPoolExecutor {

    @Value("${itest.tests:#{null}}")
    private String tests;

    private List<Consumer<TestResult>> testResultListenerList;

    private List<String> testNameList = Arrays.asList(tests.split(","));

    private RemainingTestsReporter remainingTestsReporter;

    private TestResultFactory testResultFactory;

    public SkipAwareExecutorService(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                    BlockingQueue<Runnable> workQueue, List<Consumer<TestResult>> testResultListenerList, RemainingTestsReporter remainingTestsReporter, TestResultFactory testResultFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        this.testResultListenerList = testResultListenerList;
        this.remainingTestsReporter = remainingTestsReporter;
        this.testResultFactory = testResultFactory;
    }

    @Override
    public List<Runnable> shutdownNow() {
        remainingTestsReporter.getRemainingList().forEach(testName -> {
            TestResult testResult = testResultFactory.notExecuted(testName);
            testResultListenerList.forEach(listener -> listener.accept(testResult));
        });

        return super.shutdownNow();
    }
}
