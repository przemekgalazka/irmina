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

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.QualifierAnnotationAutowireCandidateResolver;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

@Named
public class IrminaApplicationContext extends AbstractApplicationContext implements BeanDefinitionRegistry {

    private DefaultListableBeanFactory beanFactory;

    @Inject
    public IrminaApplicationContext(IrminaListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        this.beanFactory.setAutowireCandidateResolver(new QualifierAnnotationAutowireCandidateResolver());
    }

    @Override
    public void setParent(ApplicationContext parent) {
        super.setParent(parent);
        this.beanFactory.setParentBeanFactory(getInternalParentBeanFactory());
    }

    @Override
    protected final void refreshBeanFactory() throws IllegalStateException {
        this.beanFactory.setSerializationId(getId());
    }

    @Override
    protected void cancelRefresh(BeansException ex) {
        this.beanFactory.setSerializationId(null);
        super.cancelRefresh(ex);
    }

    @Override
    protected final void closeBeanFactory() {
        this.beanFactory.setSerializationId(null);
    }

    @Override
    public final ConfigurableListableBeanFactory getBeanFactory() {
        return this.beanFactory;
    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition)
            throws BeanDefinitionStoreException {

        this.beanFactory.registerBeanDefinition(beanName, beanDefinition);
    }

    @Override
    public void removeBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
        this.beanFactory.removeBeanDefinition(beanName);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
        return this.beanFactory.getBeanDefinition(beanName);
    }

    @Override
    public boolean isBeanNameInUse(String beanName) {
        return this.beanFactory.isBeanNameInUse(beanName);
    }

    @Override
    public void registerAlias(String beanName, String alias) {
        this.beanFactory.registerAlias(beanName, alias);
    }

    @Override
    public void removeAlias(String alias) {
        this.beanFactory.removeAlias(alias);
    }

    @Override
    public boolean isAlias(String beanName) {
        return this.beanFactory.isAlias(beanName);
    }

}
