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

import static java.util.Arrays.asList;
import static org.springframework.core.annotation.AnnotationUtils.getAnnotation;

import java.lang.annotation.Annotation;
import java.util.List;

import javax.inject.Named;

import org.springframework.beans.factory.annotation.Qualifier;

import com.google.common.base.Objects;

public class InjectionPointDefinition {

    private NamedImpl qualifier;
    private Class type;

    public InjectionPointDefinition(Annotation[] qualifiers, Class type) {
        this(asList(qualifiers), type);
    }

    public InjectionPointDefinition(List<Annotation> annotations, Class type) {
        this.type = type;
        for (Annotation annotation : annotations) {

            if (annotation instanceof Named) {
                String value = ((Named) annotation).value();
                this.qualifier = new NamedImpl(value);

            } else {
                Qualifier qualifier = getAnnotation(annotation.annotationType(), Qualifier.class);
                if (qualifier != null) {
                    this.qualifier = new NamedImpl(qualifier.value());
                }
            }
        }
    }

    public Class getType() {
        return type;
    }

    public NamedImpl getQualifier() {
        return qualifier;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(qualifier, type);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final InjectionPointDefinition other = (InjectionPointDefinition) obj;
        return Objects.equal(this.qualifier, other.qualifier) && Objects.equal(this.type, other.type);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("qualifier", qualifier)
                .add("type", type)
                .toString();
    }
}
