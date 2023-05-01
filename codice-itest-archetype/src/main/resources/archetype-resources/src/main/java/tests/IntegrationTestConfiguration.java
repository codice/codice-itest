package ${package}.tests;

import org.codice.itest.api.IntegrationTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IntegrationTestConfiguration {

    @Bean
    public IntegrationTest failingIntegrationTest() {
        return new IntegrationTest() {
            @Override
            public void test() throws Exception {
                throw new AssertionError("Assertion Failure");
            }

            @Override
            public String getName() {
                return "FailingIntegrationTest";
            }
        };
    }

    @Bean
    public IntegrationTest passingIntegrationTest() {
        return new IntegrationTest() {
            @Override
            public void test() throws Exception {
            }

            @Override
            public String getName() {
                return "PassingIntegrationTest";
            }
        };
    }
}
