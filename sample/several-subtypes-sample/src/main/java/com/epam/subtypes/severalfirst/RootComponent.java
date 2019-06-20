package com.epam.subtypes.severalfirst;

import com.epam.daggerbinds.bindto.Generated_BindsModule;
import dagger.Component;

import javax.inject.Named;

@Component(modules = {RootModule.class, Generated_BindsModule.class})
public interface RootComponent {
    @Named("com.epam.subtypes.severalfirst.RepositoryImpSecond")
    Repository repo();

    @ServiceImpQFirst
    Service serv();

    Connection connect();
}
