package com.epam.severalsubtypes;

import com.epam.daggerbinds.bindto.Generated_BindsModule;
import dagger.Component;

import javax.inject.Named;

@Component(modules = {RootModule.class, Generated_BindsModule.class})
public interface RootComponent {
    @Named("com.epam.severalsubtypes.RepositoryImp2")
    Repository repo();

    @ServiceImpQ1
    Service serv();

    Connection connect();
}
