package org.codice.itest;

import org.codice.itest.api.TestResult;
import org.codice.itest.api.TestResultFactory;
import org.codice.itest.config.ITestConfigurationProperties;
import org.codice.itest.reporter.RemainingTestsReporter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Arrays;
import java.util.concurrent.*;
import java.util.function.Consumer;

@Component("SkipAwareExecutorService")
public class SkipAwareExecutorService extends ThreadPoolExecutor {

    @Value("${itest.tests:#{null}}")
    private String tests;

    private List<Consumer<TestResult>> testResultListenerList;
    private final ITestConfigurationProperties iTestConfigurationProperties;

    private List<String> testNameList;

    private RemainingTestsReporter remainingTestsReporter;

    private TestResultFactory testResultFactory;

    public SkipAwareExecutorService(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                    BlockingQueue<Runnable> workQueue, List<Consumer<TestResult>> testResultListenerList, ITestConfigurationProperties iTestConfigurationProperties, RemainingTestsReporter remainingTestsReporter, TestResultFactory testResultFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        this.testResultListenerList = testResultListenerList;
        this.iTestConfigurationProperties = iTestConfigurationProperties;
        this.testNameList = iTestConfigurationProperties.tests();
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
