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

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.geodevv.testing.irmina.bootstrap.IrminaConfiguration;
import com.geodevv.testing.sampledomain.impl.bmw.BmwX5;

@ContextConfiguration(classes = IrminaConfiguration.class)
@TestExecutionListeners(IrminaTestContextListener.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class MockedInjectionPointsRegisterTest {

    @Inject
    MockedInjectionPointsRegister register;

    @Test
    public void shouldRegisterAndFindDefinition() {

        //given
        Object registeredBean = register.registerMock(createInjectionPoint());
        Object foundBean = register.findBy(createInjectionPoint());

        //expect
        assertThat(registeredBean).isEqualTo(foundBean);
    }


    public InjectionPointDefinition createInjectionPoint() {

        List<Annotation> annotations = new ArrayList<Annotation>();
        annotations.add(new NamedImpl("a_name"));

        return new InjectionPointDefinition(annotations, BmwX5.class);
    }


}
