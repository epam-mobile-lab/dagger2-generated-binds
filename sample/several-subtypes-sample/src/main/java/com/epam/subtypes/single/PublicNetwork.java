package com.epam.subtypes.single;

import com.epam.daggerbinds.annotation.BindTo;

import javax.inject.Named;

@BindTo(interfaceType = Network.class, qualifier = Named.class)
public class PublicNetwork implements Network {
    @Override
    public void doCall() {
        //does something
    }
}
