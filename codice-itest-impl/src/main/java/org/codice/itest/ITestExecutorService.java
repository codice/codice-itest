/**
 * Copyright (c) Codice Foundation

 * This is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation, either version 3 of the License, or any later version.

 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details. A copy of the GNU Lesser General Public License is distributed along with this program and can be found at
 * http://www.gnu.org/licenses/lgpl.html.
 */
package org.codice.itest;

import org.codice.itest.api.TestResult;
import org.codice.itest.api.TestResultFactory;
import org.codice.itest.config.ITestConfigurationProperties;
import org.codice.itest.reporter.RemainingTestsReporter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Component("ITestExecutorService")
public class ITestExecutorService extends ThreadPoolExecutor {

    @Value("${itest.tests:#{null}}")
    private String tests;

    private List<Consumer<TestResult>> testResultListenerList;
    private final ITestConfigurationProperties iTestConfigurationProperties;

    private List<String> testNameList;

    private RemainingTestsReporter remainingTestsReporter;

    private TestResultFactory testResultFactory;

    public ITestExecutorService(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
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
