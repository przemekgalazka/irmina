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


import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

@Named
public class TestExecutionListenersRegister implements TestExecutionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestExecutionListenersRegister.class);
    private List<TestExecutionListener> listenerList;

    @Inject
    public TestExecutionListenersRegister(List<TestExecutionListener> listenerList) {
        this.listenerList = listenerList;

        for (TestExecutionListener testExecutionListener : listenerList) {
            LOGGER.info("Registering test execution listener={} ", testExecutionListener);
        }
    }

    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {
        for (TestExecutionListener listener : listenerList) {

            try {
                listener.beforeTestClass(testContext);
            } catch (Exception e) {
                LOGGER.error("Execution listener: " + listener + "exception were thrown", e);
            }
        }
    }

    @Override
    public void prepareTestInstance(TestContext testContext) throws Exception {
        for (TestExecutionListener listener : listenerList) {
            try {
                listener.prepareTestInstance(testContext);
            } catch (Exception e) {
                LOGGER.error("Execution listener: " + listener + "exception were thrown", e);
            }
        }
    }

    @Override
    public void beforeTestMethod(TestContext testContext) throws Exception {
        for (TestExecutionListener listener : listenerList) {
            try {
                listener.beforeTestMethod(testContext);
            } catch (Exception e) {
                LOGGER.error("Execution listener: " + listener + "exception were thrown", e);
            }
        }
    }

    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        for (TestExecutionListener listener : listenerList) {
            try {
                listener.afterTestMethod(testContext);
            } catch (Exception e) {
                LOGGER.error("Execution listener: " + listener + "exception were thrown", e);
            }
        }
    }

    @Override
    public void afterTestClass(TestContext testContext) throws Exception {
        for (TestExecutionListener listener : listenerList) {
            try {
                listener.afterTestClass(testContext);
            } catch (Exception e) {
                LOGGER.error("Execution listener: " + listener + "exception were thrown", e);
            }
        }
    }
}
