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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.AnnotationUtils;

import com.google.common.collect.Lists;

public class MockedOrSpiedInjectionPointScanner {

    public List<InjectionPointDefinition> scanForMock(Class<?> aClass) {

        ArrayList<InjectionPointDefinition> result = Lists.newArrayList();

        for (Field field : aClass.getDeclaredFields()) {
            Mock annotation = field.getAnnotation(Mock.class);

            if (annotation != null) {
                List<Annotation> annotations = getQualifierOrNamed(field);
                result.add(new InjectionPointDefinition(annotations, field.getType()));
            }
        }

        return result;
    }

    public List<InjectionPointDefinition> scanForSpies(Class<?> aClass) {

        ArrayList<InjectionPointDefinition> result = Lists.newArrayList();

        for (Field field : aClass.getDeclaredFields()) {
            Spy annotation = field.getAnnotation(Spy.class);

            if (annotation != null) {
                List<Annotation> annotations = getQualifierOrNamed(field);
                result.add(new InjectionPointDefinition(annotations, field.getType()));
            }
        }

        return result;
    }

    private List<Annotation> getQualifierOrNamed(Field field) {
        List<Annotation> annotations = new ArrayList<>();

        Qualifier qualifier = AnnotationUtils.getAnnotation(field, Qualifier.class);
        if (qualifier != null) {
            annotations.add(new NamedImpl(qualifier.value()));
        } else {

            Named named = AnnotationUtils.getAnnotation(field, Named.class);
            if (named != null) {
                annotations.add(new NamedImpl(named.value()));
            }
        }
        return annotations;
    }

}
