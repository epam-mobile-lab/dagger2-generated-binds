package com.epam.sample3;

import com.epam.daggerbinds.bindto.Generated_BindsModule;
import dagger.Component;

import javax.inject.Named;

@Component(modules = {RootModule.class, Generated_BindsModule.class})
public interface RootComponent {
    @Named("com.epam.sample3.RepositoryImp2")
    Repository repo();

    @ServiceImpQ1
    Service serv();

    Connection connect();
}
