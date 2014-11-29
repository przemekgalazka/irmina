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

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfigurationAttributes;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.test.context.support.AbstractContextLoader;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import com.geodevv.testing.irmina.bootstrap.IrminaBootstrap;

public class IrminaContextLoader extends AbstractContextLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(IrminaContextLoader.class);
    private IrminaApplicationContext context;

    public IrminaContextLoader() {

        IrminaBootstrap irminaBootstrap = new IrminaBootstrap();

        context = irminaBootstrap.getIrminaApplicationContext();
        context.setParent(irminaBootstrap.getInternalContext());

        defineMocksAndSpies();
    }

    protected void defineMocksAndSpies() {
        // go and overwrite
    }

    protected InjectionPointDefinitionRegistrationProxy define(Class type) {
        return new InjectionPointDefinitionRegistrationProxy(context).define(type);
    }


    @Override
    public ApplicationContext loadContext(MergedContextConfiguration mergedConfig) throws Exception {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("Loading ApplicationContext for merged context configuration [%s].",
                    mergedConfig));
        }

        context.getEnvironment().setActiveProfiles(mergedConfig.getActiveProfiles());
        loadBeanDefinitions(context, mergedConfig);
        AnnotationConfigUtils.registerAnnotationConfigProcessors(context);
        context.refresh();
        context.registerShutdownHook();

        return context;
    }

    public void registerSpiesAndMockByScanningTestClass(Class<?> testClass) {

        MockedOrSpiedInjectionPointScanner scanner = new MockedOrSpiedInjectionPointScanner();

        for (InjectionPointDefinition definition : scanner.scanForMock(testClass)) {
            InjectionPointDefinitionRegistrationProxy proxy = define(definition.getType());

            if (definition.getQualifier() != null) {
                proxy.named(definition.getQualifier().value());
            }

            proxy.asMock();
        }

        for (InjectionPointDefinition definition : scanner.scanForSpies(testClass)) {
            InjectionPointDefinitionRegistrationProxy proxy = define(definition.getType());

            if (definition.getQualifier() != null) {
                proxy.named(definition.getQualifier().value());
            }

            proxy.asSpy();
        }

    }


    @Override
    public ApplicationContext loadContext(String... locations) throws Exception {
        throw new UnsupportedOperationException("IrminaContextLoader does not support the xml configurations");
    }

    // --- SmartContextLoader -----------------------------------------------

    /**
     * Process configuration classes in the supplied {@link org.springframework.test.context.ContextConfigurationAttributes}.
     * <p>If the configuration classes are <code>null</code> or empty and
     * {@link #isGenerateDefaultLocations()} returns <code>true</code>, this
     * <code>SmartContextLoader</code> will attempt to {@link
     * #detectDefaultConfigurationClasses detect default configuration classes}.
     * If defaults are detected they will be
     * {@link org.springframework.test.context.ContextConfigurationAttributes#setClasses(Class[]) set} in the
     * supplied configuration attributes. Otherwise, properties in the supplied
     * configuration attributes will not be modified.
     *
     * @param configAttributes the context configuration attributes to process
     * @see org.springframework.test.context.SmartContextLoader#processContextConfiguration()
     * @see #isGenerateDefaultLocations()
     * @see #detectDefaultConfigurationClasses()
     */
    public void processContextConfiguration(ContextConfigurationAttributes configAttributes) {
        if (ObjectUtils.isEmpty(configAttributes.getClasses()) && isGenerateDefaultLocations()) {
            Class<?>[] defaultConfigClasses = detectDefaultConfigurationClasses(configAttributes.getDeclaringClass());
            configAttributes.setClasses(defaultConfigClasses);
        }
    }

    // --- AnnotationConfigContextLoader ---------------------------------------

    private boolean isStaticNonPrivateAndNonFinal(Class<?> clazz) {
        Assert.notNull(clazz, "Class must not be null");
        int modifiers = clazz.getModifiers();
        return (Modifier.isStatic(modifiers) && !Modifier.isPrivate(modifiers) && !Modifier.isFinal(modifiers));
    }

    /**
     * Determine if the supplied {@link Class} meets the criteria for being
     * considered a <em>default configuration class</em> candidate.
     * <p>Specifically, such candidates:
     * <ul>
     * <li>must not be <code>null</code></li>
     * <li>must not be <code>private</code></li>
     * <li>must not be <code>final</code></li>
     * <li>must be <code>static</code></li>
     * <li>must be annotated with {@code @Configuration}</li>
     * </ul>
     *
     * @param clazz the class to check
     * @return <code>true</code> if the supplied class meets the candidate criteria
     */
    private boolean isDefaultConfigurationClassCandidate(Class<?> clazz) {
        return clazz != null && isStaticNonPrivateAndNonFinal(clazz) && clazz.isAnnotationPresent(Configuration.class);
    }

    /**
     * Detect the default configuration classes for the supplied test class.
     * <p>The returned class array will contain all static inner classes of
     * the supplied class that meet the requirements for {@code @Configuration}
     * class implementations as specified in the documentation for
     * {@link Configuration @Configuration}.
     * <p>The implementation of this method adheres to the contract defined in the
     * {@link org.springframework.test.context.SmartContextLoader SmartContextLoader}
     * SPI. Specifically, this method uses introspection to detect default
     * configuration classes that comply with the constraints required of
     * {@code @Configuration} class implementations. If a potential candidate
     * configuration class does meet these requirements, this method will log a
     * warning, and the potential candidate class will be ignored.
     *
     * @param declaringClass the test class that declared {@code @ContextConfiguration}
     * @return an array of default configuration classes, potentially empty but
     *         never <code>null</code>
     */
    protected Class<?>[] detectDefaultConfigurationClasses(Class<?> declaringClass) {
        Assert.notNull(declaringClass, "Declaring class must not be null");

        List<Class<?>> configClasses = new ArrayList<Class<?>>();

        for (Class<?> candidate : declaringClass.getDeclaredClasses()) {
            if (isDefaultConfigurationClassCandidate(candidate)) {
                configClasses.add(candidate);
            } else {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(String.format(
                            "Ignoring class [%s]; it must be static, non-private, non-final, and annotated "
                                    + "with @Configuration to be considered a default configuration class.",
                            candidate.getName()));
                }
            }
        }

        if (configClasses.isEmpty()) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(String.format("Could not detect default configuration classes for test class [%s]: "
                        + "%s does not declare any static, non-private, non-final, inner classes "
                        + "annotated with @Configuration.", declaringClass.getName(), declaringClass.getSimpleName()));
            }
        }

        return configClasses.toArray(new Class<?>[configClasses.size()]);
    }


    protected void loadBeanDefinitions(IrminaApplicationContext context, MergedContextConfiguration mergedConfig) {
        Class<?>[] configClasses = mergedConfig.getClasses();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Registering configuration classes: " + ObjectUtils.nullSafeToString(configClasses));
        }
        new AnnotatedBeanDefinitionReader(context).register(configClasses);
    }

    @Override
    protected String[] modifyLocations(Class<?> clazz, String... locations) {
        throw new UnsupportedOperationException(
                "AnnotationConfigContextLoader does not support the modifyLocations(Class, String...) method");
    }

    @Override
    protected String[] generateDefaultLocations(Class<?> clazz) {
        throw new UnsupportedOperationException(
                "AnnotationConfigContextLoader does not support the generateDefaultLocations(Class) method");
    }


    @Override
    protected String getResourceSuffix() {
        throw new UnsupportedOperationException(
                "AnnotationConfigContextLoader does not support the getResourceSuffix() method");
    }


}
