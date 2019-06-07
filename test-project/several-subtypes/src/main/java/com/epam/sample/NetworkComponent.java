package com.epam.sample;

import com.epam.daggerbinds.bindto.Generated_BindsModule;
import dagger.Component;

import javax.inject.Named;

@Component(modules = {NetworkModule.class, Generated_BindsModule.class})
public interface NetworkComponent {
    @Named("com.epam.sample.PrivateNetwork")
    Network network();
}
