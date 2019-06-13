package com.epam.severalsubtypes2;

import dagger.Module;
import dagger.Provides;

@Module
public class RootModule {
    @Provides
    public RepositoryImp1 provideRepository1(){
        return new RepositoryImp1();
    }

    @Provides
    public RepositoryImp2 provideRepository2(){
        return new RepositoryImp2();
    }

    @Provides
    public ServiceImp1 provideService1(){
        return new ServiceImp1();
    }

    @Provides
    public ServiceImp2 provideService2(){
        return new ServiceImp2();
    }

    @Provides
    public ConnectionImp provideConnectionImp(){
        return new ConnectionImp();
    }
}
