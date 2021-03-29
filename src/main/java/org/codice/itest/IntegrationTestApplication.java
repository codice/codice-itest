/**
 * Copyright (c) Codice Foundation

 * This is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation, either version 3 of the License, or any later version.

 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details. A copy of the GNU Lesser General Public License is distributed along with this program and can be found at
 * http://www.gnu.org/licenses/lgpl.html.
 */
package org.codice.itest;

import org.codice.itest.reporter.ExitCodeReporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;



@SpringBootApplication
@ComponentScan(basePackages = {"org.codice", "${itest.packagescan:org.codice}"})
public class IntegrationTestApplication implements ExitCodeGenerator {

    @Autowired
    private ExitCodeReporter exitCodeReporter;

    public static void main(String... args) {
        System.exit(SpringApplication.exit(SpringApplication.run(IntegrationTestApplication.class, args)));
    }

    @Override
    public int getExitCode() {
        return exitCodeReporter.getExitCode();
    }

}