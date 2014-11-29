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
import static org.mockito.BDDMockito.given;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;

import com.geodevv.testing.sampledomain.SampleConfiguration;
import com.geodevv.testing.sampledomain.impl.Suspension;
import com.geodevv.testing.sampledomain.impl.SuspensionDesign;


@ContextConfiguration(classes = SampleConfiguration.class,
        loader = MockBehaviorSetupTest.ContextLoader.class)
@TestExecutionListeners(IrminaTestContextListener.class)
@RunWith(IrminaRunner.class)
public class MockBehaviorSetupTest {

    @Inject
    @Named("AudiA4") Suspension suspension;

    @Test
    public void shouldSetupMockBehaviorInJavaConfig() {

        //expect
        assertThat(suspension.getSuspensionDesignVersion()).isEqualTo("v2");

    }

    static class ContextLoader extends IrminaContextLoader {
        @Override
        public void defineMocksAndSpies() {
            define(SuspensionDesign.class).named("AudiA4")
                    .asMockWithBehavior(new Behavior<SuspensionDesign>() {
                        @Override
                        public void teach(SuspensionDesign bean) {
                            given(bean.getVersion()).willReturn("v2");
                        }
                    });
        }
    }


}
