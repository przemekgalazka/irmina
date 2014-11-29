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

import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

@Named
public class IrminaListableBeanFactory extends DefaultListableBeanFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(IrminaListableBeanFactory.class);

    private MockedInjectionPointsRegister mockedInjectionPointsRegister;
    private SpiedInjectionPointsRegister spiedInjectionPointsRegister;

    @Inject
    public IrminaListableBeanFactory(MockedInjectionPointsRegister mockedInjectionPointsRegister,
                                     SpiedInjectionPointsRegister spiedInjectionPointsRegister) {
        this.mockedInjectionPointsRegister = mockedInjectionPointsRegister;
        this.spiedInjectionPointsRegister = spiedInjectionPointsRegister;
    }

    @Override
    public Object resolveDependency(DependencyDescriptor descriptor, String beanName,
                                    Set<String> autowiredBeanNames, TypeConverter typeConverter)
            throws BeansException {

        InjectionPointDefinition injectionPointDefinition = new InjectionPointDefinition(descriptor.getAnnotations(),
                descriptor.getDependencyType());

        Object mockedBean = mockedInjectionPointsRegister.findBy(injectionPointDefinition);

        if (mockedBean != null) {
            return mockedBean;
        }

        Object spiedBean = spiedInjectionPointsRegister.findBy(injectionPointDefinition);

        if (spiedBean != null) {
            return spiedBean;
        }

        try {

            Object resolvedBean = super.resolveDependency(descriptor, beanName, autowiredBeanNames, typeConverter);

            if (spiedInjectionPointsRegister.hasDefinition(injectionPointDefinition)) {
                LOGGER.warn("[SPYING DEPENDENCY] " + injectionPointDefinition + " in " + beanName);
                return spiedInjectionPointsRegister.registerSpy(injectionPointDefinition, resolvedBean);
            } else {
                return resolvedBean;
            }


        } catch (NoSuchBeanDefinitionException e) {

            LOGGER.warn("[MOCKING DEPENDENCY] " + injectionPointDefinition + " in " + beanName);
            return mockedInjectionPointsRegister.registerMock(injectionPointDefinition);
        }

    }

}
