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

import static org.mockito.Mockito.verify;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.geodevv.testing.sampledomain.Engine;
import com.geodevv.testing.sampledomain.SampleConfiguration;
import com.geodevv.testing.sampledomain.Vehicle;
import com.geodevv.testing.sampledomain.impl.bmw.Bmw;

@ContextConfiguration(classes = SampleConfiguration.class,
        loader = MockingOutStandardInjectionPointsTest.ContextLoader.class)
@TestExecutionListeners(IrminaTestContextListener.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class MockingOutStandardInjectionPointsTest {

    @Inject
    @Bmw
    Vehicle bmw;

    @Inject
    @Bmw
    Engine bwmEngine;

    @Test
    public void shouldInjectMockEngineForFordBmw() {

        // when
        bmw.start();

        //expect
        verify(bwmEngine).turnOn();
    }

    static class ContextLoader extends IrminaContextLoader {

        @Override
        public void defineMocksAndSpies() {

            define(Engine.class).annotated(Bmw.class).asMock();
        }
    }
}
