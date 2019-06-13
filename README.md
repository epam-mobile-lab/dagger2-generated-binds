# DaggerBinds helper generator

## Boilerplate problem

As you know Dagger matches types exactly. Therefore when you would like to provide an implementation of some interface, you should use `@Binds` annotation. Lest see the example:

```java
interface Service {}

class ServiceImp implements Service {

    @Inject
    ServiceImp()
}
```

Now we have possibility to inject `ServiceImp`, but have no possibility to inject `Service`. To have that possibility we need to create Dagger Module like this:

```java
@Module
interface BindsModule {

    @Binds
    Service bindService(ServiceImp service)
}
```

That approach requires writing a large number of boilerplate code.

## Solution

We propose to generate `Generated_BindsModule` based on [Java Annotation Processor](https://docs.oracle.com/javase/8/docs/api/javax/annotation/processing/Processor.html) (APT).

## Usage of DaggerBinds

In order to use DaggerBinds library a few simple steps should be done:

* Mark corresponding Classes with `@BindTo` annotation:

```java
interface Service {}

@BindTo(interfaceType = Service.class)
public class ServiceImp implements Service {

    @Inject
    ServiceImp()
}
```

* Add generated module to the root (app) component:

```java
@Component(modules = {Generated_BindsModule.class})
public interface RootComponent {

    Service service();
}
```

Generated `Generated_BindsModule` module will be:

```java
@Module
public interface Generated_BindsModule {
  @Binds
  Service binds_com_epam_subtypes_single_ServiceImp(ServiceImp com_epam_subtypes_single_ServiceImp);
}
```

## Usage of Qualifiers

In order to be able to use multi binds DaggerBinds supports 'javax.inject.Named' and your own 'qualifier annotation'.
Let see how to use qualifiers annotation:

* Usage of `@Named` qualifier annotation:

```java
@BindTo(interfaceType = Repository.class, qualifier = Named.class)
public class RepositoryImpFirst implements Repository {
}
```

Generated `Generated_BindsModule` module will be:

```java
@Module
public interface Generated_BindsModule {
  @Binds
  @Named("com.epam.subtypes.severalfirst.RepositoryImpFirst")
  Repository binds_com_epam_subtypes_severalfirst_RepositoryImpFirst(RepositoryImpFirst com_epam_subtypes_severalfirst_RepositoryImpFirst);
}
```

* Usage of your own Qualifier annotation:

```java
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceImpQFirst {
}

@BindTo(interfaceType = Service.class, qualifier = ServiceImpQFirst.class)
public class ServiceImpFirst implements Service {
}
```

Generated `Generated_BindsModule` module will be:

```java
@Module
public interface Generated_BindsModule {
  @Binds
  @ServiceImpQFirst
  Service binds_com_epam_subtypes_severalfirst_ServiceImpFirst(ServiceImpFirst com_epam_subtypes_severalfirst_ServiceImpFirst);
}
```

## Download

Gradle dependency:

```groovy
dependencies {
    implementation 'com.epam.daggerbinds:annotation:1.0.0-alpha1'
    annotationProcessor 'com.epam.daggerbinds:processor:1.0.0-alpha1'
}
```

If you are using Kotlin, replace annotationProcessor with kapt.
Nevertheless using kapt has one limitation for now. Please see limitations below.

## Current limitations
* For the proper work minimum dagger version should be 2.11.
* DaggerBinds doesn't support work with own qualifier annotation with parameters for now.
* In case of kapt usage generated module should be specified in the dagger's [Module] annotation with full name, e.g.

```kotlin
@Component(modules = [com.epam.daggerbinds.bindto.Generated_BindsModule::class])
interface RootComponent {
    fun service(): Service
}
```