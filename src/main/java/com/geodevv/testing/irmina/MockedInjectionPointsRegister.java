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


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;

import org.mockito.exceptions.base.MockitoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
public class MockedInjectionPointsRegister {

    private static final Logger LOGGER = LoggerFactory.getLogger(MockedInjectionPointsRegister.class);

    private Map<InjectionPointDefinition, Object> register = new HashMap<>();

    public Object registerMock(InjectionPointDefinition definition) {

        Class typeToBeMocked = definition.getType();

        if (typeToBeMocked.equals(String.class)) {
            return "some mock value";
        }

        try {
            Object mockedBean = mock(definition.getType());
            register.put(definition, mockedBean);
            return mockedBean;

        } catch (MockitoException e) {
            LOGGER.error("Injection Point Definition Mocking failed for " + definition);
            throw e;
        }

    }

    public Object registerMockWithBehavior(InjectionPointDefinition definition, Behavior behavior) throws Exception {
        Object mock = registerMock(definition);
        behavior.teach(mock);
        return mock;
    }

    public Object findBy(InjectionPointDefinition definition) {
        return register.get(definition);
    }

    public void resetAllMocks() {
        for (Object mock : register.values()) {
            reset(mock);
        }
    }
}
