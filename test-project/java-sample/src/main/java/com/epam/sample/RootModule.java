package com.epam.sample;

import dagger.Module;
import dagger.Provides;

@Module
public class RootModule {
    @Provides
    public ServiceImp provideService() {
        return new ServiceImp();
    }
}
