package com.epam.sample;

import com.epam.daggerbinds.bindto.Generated_BindsModule;
import dagger.Component;

@Component(modules = {RootModule.class, Generated_BindsModule.class})
public interface RootComponent {
    Service service();
}
