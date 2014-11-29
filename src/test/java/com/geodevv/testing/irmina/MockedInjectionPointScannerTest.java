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

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.assertj.core.api.Condition;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Spy;

import com.geodevv.testing.sampledomain.Engine;
import com.geodevv.testing.sampledomain.impl.bmw.Bmw;

public class MockedInjectionPointScannerTest {

    MockedOrSpiedInjectionPointScanner scanner = new MockedOrSpiedInjectionPointScanner();

    @Test
    public void shouldScanAndFindAMockInjectionPoint() {

        //when
        List<InjectionPointDefinition> result = scanner.scanForMock(ATestClassWithMockField.class);

        //then
        assertThat(result)
                .hasSize(1)
                .have(new Condition<InjectionPointDefinition>() {
                    @Override public boolean matches(InjectionPointDefinition injectionPointDefinition) {
                        return injectionPointDefinition.getType().equals(Engine.class);
                    }
                });

        assertThat(result.get(0).getQualifier()).is(new Condition<NamedImpl>() {
            @Override public boolean matches(NamedImpl named) {
                return named.value().equals("bmw");
            }
        });
    }

    @Test
    public void shouldScanAndFindAMockInjectionPointWithNamedField() {

        //when
        List<InjectionPointDefinition> result = scanner.scanForMock(ATestClassWithMockFieldButNamed.class);

        //then
        assertThat(result)
                .hasSize(1)
                .have(new Condition<InjectionPointDefinition>() {
                    @Override public boolean matches(InjectionPointDefinition injectionPointDefinition) {
                        return injectionPointDefinition.getType().equals(Engine.class);
                    }
                });

        assertThat(result.get(0).getQualifier()).is(new Condition<NamedImpl>() {
            @Override public boolean matches(NamedImpl named) {
                return named.value().equals("some-name");
            }
        });
    }

    @Test
    public void shouldScanAndFindASpyInjectionPoint() {

        //when
        List<InjectionPointDefinition> result = scanner.scanForSpies(ATestClassWithSpyField.class);

        //then
        assertThat(result)
                .hasSize(1)
                .have(new Condition<InjectionPointDefinition>() {
                    @Override public boolean matches(InjectionPointDefinition injectionPointDefinition) {
                        return injectionPointDefinition.getType().equals(Engine.class);
                    }
                });

        assertThat(result.get(0).getQualifier()).is(new Condition<NamedImpl>() {
            @Override public boolean matches(NamedImpl named) {
                return named.value().equals("bmw");
            }
        });

    }


    public class ATestClassWithMockField {

        @Inject @Mock
        @Bmw Engine mockEngine;

    }

    public class ATestClassWithMockFieldButNamed {

        @Inject @Mock
        @Named("some-name") Engine mockEngine;

    }

    public class ATestClassWithSpyField {

        @Inject @Spy
        @Bmw Engine spyEngine;

    }

}
