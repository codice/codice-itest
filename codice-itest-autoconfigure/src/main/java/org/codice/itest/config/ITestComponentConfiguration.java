package org.codice.itest.config;

import org.codice.itest.api.IntegrationTest;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

@AutoConfiguration
@EnableConfigurationProperties(ITestConfigurationProperties.class)
class ITestComponentConfiguration {
    private ITestConfigurationProperties iTestConfigurationProperties;

    private List<IntegrationTest> integrationTests;

    public ITestComponentConfiguration(ITestConfigurationProperties iTestConfigurationProperties,
                                       List<IntegrationTest> integrationTests) {
        this.iTestConfigurationProperties = iTestConfigurationProperties;
        this.integrationTests = integrationTests;
    }

    /**
     * @return the 'ExecutorService' instance to be used by the service. The number of threads to
     * be used is defined by the value of '${threads}'. The default is 1.
     */
    @Bean
    @ConditionalOnMissingBean
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(iTestConfigurationProperties.threads());
    }

    /**
     * @return a stream of all the 'IntegrationTest' instances found within the application context,
     * filtered by the value of 'codice.itest.tests', if provided.
     */
    @Bean
    @ConditionalOnMissingBean
    public Stream<IntegrationTest> integrationTestStream() {
        if (iTestConfigurationProperties.tests() == null) {
            return integrationTests.stream();
        }

        List<String> testNameList = iTestConfigurationProperties.tests();
        return integrationTests.stream()
                .filter(t -> testNameList.contains(t.getName()))
                .sorted(Comparator.comparingInt(t -> testNameList.indexOf(t.getName())));
    }
}
