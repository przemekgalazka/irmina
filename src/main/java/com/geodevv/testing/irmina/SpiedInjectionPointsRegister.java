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

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Named;

@Named
public class SpiedInjectionPointsRegister {

    private Map<InjectionPointDefinition, Object> registerOfSpiedBean = new HashMap<>();
    private Set<InjectionPointDefinition> registerOfDefinitions = new HashSet<>();

    public Object registerSpy(InjectionPointDefinition definition, Object bean) {

        Object spiedBean = spy(bean);
        registerOfSpiedBean.put(definition, spiedBean);

        return spiedBean;
    }

    public Object findBy(InjectionPointDefinition definition) {
        return registerOfSpiedBean.get(definition);
    }

    public void registerSpyDefinition(InjectionPointDefinition injectionPointDefinition) {
        registerOfDefinitions.add(injectionPointDefinition);
    }

    public boolean hasDefinition(InjectionPointDefinition injectionPointDefinition) {
        return registerOfDefinitions.contains(injectionPointDefinition);
    }

    public void resetAllSpies() {
        for (Object spy : registerOfSpiedBean.values()) {
            reset(spy);
        }
    }
}
