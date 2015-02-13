# Irmina
This README for test tool that support your spring integration tests

## What is it?

### Irmina is Spring integration tests runner
```java
@RunWith(IrminaRunner.class)
```
### Irmina mock all your new dependencies if needed
### Irmina adds extra behavior to your mocks when needed while spring context bootstrap
```
define(SuspensionDesign.class).named("AudiA4")
                    .asMockWithBehavior(new Behavior<SuspensionDesign>() {
                        @Override
                        public void teach(SuspensionDesign bean) {
                            given(bean.getVersion()).willReturn("v2");
                        }
                    });
```
### Irmina let you decide if you need mock, spy or implementation at certain injection point
```java
define(Engine.class).named("AudiA4-engine").asMock();
```
### Irmina will mock out all dependencies you did not included in configuration but are needed (like DAO layer or DataSource)

## Examples

### Irmina mock all not provieded injection points definitons
```java
@ContextConfiguration(classes = SampleConfiguration.class)
@TestExecutionListeners(IrminaTestContextListener.class)
@RunWith(IrminaRunner.class)
public class MockInjectionTest {

    @Inject
    @Bmw
    Vehicle bmw;

    @Inject
    @Named("Mustang")
    Vehicle mustang;

    @Inject
    @Named("V6")
    Engine mustangEngine;  // this is not defined in configuration - mock will be injected

    @Test
    public void shouldInjectBmwX5() {

        //expect
        assertThat(bmw).isInstanceOf(BmwX5.class);

    }

    @Test
    public void shouldInjectMockEngineForFordMustang() {

        // when
        mustang.start();

        //expect
        verify(mustangEngine).turnOn();
    }
}
```
### Irmina mock or spy what you want - any injection point 
```java
@ContextConfiguration(classes = SampleConfiguration.class,
        loader = MockingOutStandardInjectionPointsTest.ContextLoader.class)
@TestExecutionListeners(IrminaTestContextListener.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class MockingOutStandardInjectionPointsTest {

    @Inject
    @Bmw
    Vehicle bmw;

    @Inject
    @Bmw
    Engine bwmEngine;

    @Test
    public void shouldInjectMockEngineForFordBmw() {

        // when
        bmw.start();

        //expect
        verify(bwmEngine).turnOn();
    }

    static class ContextLoader extends IrminaContextLoader {

        @Override
        public void defineMocksAndSpies() {

            define(Engine.class).annotated(Bmw.class).asMock();  // this will be injected as mock eventhoug its implementation is available in configuration
        }
    }
}
```

## Maven dependency
```xml
        <dependency>
            <groupId>com.geodevv.testing</groupId>
            <artifactId>irmina</artifactId>
            <version>1.0.0</version>
            <scope>test</scope>
        </dependency>
```

## Usage 
### Add configuration to your test
```java
@ContextConfiguration(classes = SampleConfiguration.class,
        loader = MockingOutStandardInjectionPointsTest.ContextLoader.class)
@TestExecutionListeners(IrminaTestContextListener.class)
@RunWith(SpringJUnit4ClassRunner.class)
```
### Setup mock if needed
```java
   static class ContextLoader extends IrminaContextLoader {

        @Override
        public void defineMocksAndSpies() {

            define(Engine.class).annotated(Bmw.class).asMock();  // this will be injected as mock eventhoug its implementation is available in configuration
        }
    }
```
## Use mockito annotations on fields if you want
``` java
@ContextConfiguration(classes = SampleConfiguration.class,
        loader = MockingOutNamedBeansButWithScanningForMockTest.ContextLoader.class)
@TestExecutionListeners(IrminaTestContextListener.class)
@RunWith(IrminaRunner.class)
public class MockingOutNamedBeansButWithScanningForMockTest {

    @Inject
    @Named("AudiA4") Vehicle audi;

    @Inject @Mock
    @Named("AudiA4-engine") Engine audiEngine;

    @Test
    public void shouldInjectMockEngineForAudi() {

        // when
        audi.start();

        //expect
        verify(audiEngine).turnOn();
    }

    static class ContextLoader extends IrminaContextLoader {
        @Override
        public void defineMocksAndSpies() {
            registerSpiesAndMockByScanningTestClass(MockingOutNamedBeansButWithScanningForMockTest.class);
        }
    }

}
```

##Dependencies
* Spring 3.2.12.RELEASE
* Mockito mockito-all 1.9.5


##Latest Release
* 1.0.0


