/**
 * Copyright (c) Codice Foundation

 * This is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation, either version 3 of the License, or any later version.

 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details. A copy of the GNU Lesser General Public License is distributed along with this program and can be found at
 * http://www.gnu.org/licenses/lgpl.html.
 */
package ${package};

import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication(scanBasePackages = {"org.codice", "${package}"})
public class IntegrationTestApplication {
    public static void main(String... args) {
        ApplicationContext context = SpringApplication.run(IntegrationTestApplication.class, args);
        ExitCodeGenerator exitCodeGenerator = context.getBean(ExitCodeGenerator.class);

        if (exitCodeGenerator != null)
            System.exit(exitCodeGenerator.getExitCode());
    }
}
