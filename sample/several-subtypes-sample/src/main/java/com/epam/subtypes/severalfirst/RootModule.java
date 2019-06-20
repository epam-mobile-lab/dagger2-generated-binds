package com.epam.subtypes.severalfirst;

import dagger.Module;
import dagger.Provides;

@Module
public class RootModule {
    @Provides
    public RepositoryImpFirst provideRepositoryFirst(){
        return new RepositoryImpFirst();
    }

    @Provides
    public RepositoryImpSecond provideRepositorySecond(){
        return new RepositoryImpSecond();
    }

    @Provides
    public ServiceImpFirst provideServiceFirst(){
        return new ServiceImpFirst();
    }

    @Provides
    public ServiceImpSecond provideServiceSecond(){
        return new ServiceImpSecond();
    }

    @Provides
    public ConnectionImp provideConnectionImp(){
        return new ConnectionImp();
    }
}
