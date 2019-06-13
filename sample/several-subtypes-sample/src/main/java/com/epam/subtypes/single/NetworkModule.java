package com.epam.subtypes.single;

import dagger.Module;
import dagger.Provides;

@Module
public class NetworkModule {

    @Provides
    public PrivateNetwork providePrivateNetwork() {
        return new PrivateNetwork();
    }

    @Provides
    public PublicNetwork providePublicNetwork() {
        return new PublicNetwork();
    }
}
