package com.epam.subtypes.severalsecond;

import com.epam.daggerbinds.bindto.Generated_BindsModule;
import dagger.Component;

import javax.inject.Named;

@Component(modules = {RootModule.class, Generated_BindsModule.class})
public interface RootComponent {
    @Named("com.epam.subtypes.severalsecond.RepositoryImpSecond")
    Repository repo();

    @ServiceImpQFirst
    Service serv();

    Connection connect();
}
