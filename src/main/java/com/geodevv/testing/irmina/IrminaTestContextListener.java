/*
 * Copyright (c) 2014 Geodevv.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under the License.
 */

package com.geodevv.testing.irmina;

import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

public class IrminaTestContextListener implements TestExecutionListener {

    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {
        ApplicationContext applicationContext = testContext.getApplicationContext();
        applicationContext.getBean(TestExecutionListenersRegister.class)
                .beforeTestClass(testContext);
    }

    @Override
    public void prepareTestInstance(TestContext testContext) throws Exception {
        ApplicationContext applicationContext = testContext.getApplicationContext();
        applicationContext.getBean(TestExecutionListenersRegister.class)
                .prepareTestInstance(testContext);
    }

    @Override
    public void beforeTestMethod(TestContext testContext) throws Exception {
        ApplicationContext applicationContext = testContext.getApplicationContext();
        applicationContext.getBean(TestExecutionListenersRegister.class)
                .beforeTestMethod(testContext);
    }

    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        ApplicationContext applicationContext = testContext.getApplicationContext();
        applicationContext.getBean(TestExecutionListenersRegister.class)
                .afterTestMethod(testContext);
    }

    @Override
    public void afterTestClass(TestContext testContext) throws Exception {
        ApplicationContext applicationContext = testContext.getApplicationContext();
        applicationContext.getBean(TestExecutionListenersRegister.class)
                .afterTestClass(testContext);
    }

}
