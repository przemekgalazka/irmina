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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;

import com.geodevv.testing.sampledomain.Engine;
import com.geodevv.testing.sampledomain.SampleConfiguration;
import com.geodevv.testing.sampledomain.Vehicle;
import com.geodevv.testing.sampledomain.impl.bmw.Bmw;
import com.geodevv.testing.sampledomain.impl.bmw.BmwX5;


@ContextConfiguration(classes = SampleConfiguration.class)
@TestExecutionListeners(IrminaTestContextListener.class)
@RunWith(IrminaRunner.class)
public class MockInjectionTest {

    @Inject
    @Bmw
    Vehicle bmw;

    @Inject
    @Named("Mustang")
    Vehicle mustang;

    @Inject
    @Named("V6")
    Engine mustangEngine;

    @Test
    public void shouldInjectBmwX5() {

        //expect
        assertThat(bmw).isInstanceOf(BmwX5.class);

    }

    @Test
    public void shouldInjectMockEngineForFordMustang() {

        // when
        mustang.start();

        //expect
        verify(mustangEngine).turnOn();
    }
}
