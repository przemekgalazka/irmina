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

import static org.springframework.core.annotation.AnnotationUtils.getAnnotation;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;

public class InjectionPointDefinitionRegistrationProxy {

    private Class type;
    private List<Annotation> annotations = new ArrayList<>();
    private IrminaApplicationContext context;

    public InjectionPointDefinitionRegistrationProxy(IrminaApplicationContext context) {
        this.context = context;
    }

    public InjectionPointDefinitionRegistrationProxy define(Class type) {
        this.type = type;
        return this;
    }

    public InjectionPointDefinitionRegistrationProxy annotated(Class<? extends Annotation> annotation) {
        Qualifier qualifier = getAnnotation(annotation, Qualifier.class);

        if (qualifier == null) {
            throw new RuntimeException("Expected annotation extending" +
                    " @org.springframework.beans.factory.annotation.Qualifier");
        }
        annotations.add(new NamedImpl(qualifier.value()));
        return this;
    }

    public InjectionPointDefinitionRegistrationProxy named(String name) {
        annotations.add(new NamedImpl(name));
        return this;
    }


    public void asMock() {
        context.getBean(MockedInjectionPointsRegister.class)
                .registerMock(new InjectionPointDefinition(annotations, type));
    }

    public void asMockWithBehavior(Behavior behavior) {
        context.getBean(MockedInjectionPointsRegister.class)
                .registerMockWithBehavior(new InjectionPointDefinition(annotations, type), behavior);
    }

    public void asSpy() {
        context.getBean(SpiedInjectionPointsRegister.class)
                .registerSpyDefinition(new InjectionPointDefinition(annotations, type));
    }

}
