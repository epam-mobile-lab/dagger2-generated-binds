package com.epam.subtypes.severalfirst;

import dagger.Module;
import dagger.Provides;

@Module
public class RootModule {
    @Provides
    public RepositoryImpFirst provideRepository1(){
        return new RepositoryImpFirst();
    }

    @Provides
    public RepositoryImpSecond provideRepository2(){
        return new RepositoryImpSecond();
    }

    @Provides
    public ServiceImpFirst provideService1(){
        return new ServiceImpFirst();
    }

    @Provides
    public ServiceImpSecond provideService2(){
        return new ServiceImpSecond();
    }

    @Provides
    public ConnectionImp provideConnectionImp(){
        return new ConnectionImp();
    }
}
