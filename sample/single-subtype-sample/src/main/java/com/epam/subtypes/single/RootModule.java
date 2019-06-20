package com.epam.subtypes.single;

import dagger.Module;
import dagger.Provides;

@Module
public class RootModule {
    @Provides
    public ServiceImp provideService() {
        return new ServiceImp();
    }
}
