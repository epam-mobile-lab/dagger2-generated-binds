package com.epam.sample;

import com.epam.daggerbinds.annotation.BindTo;

import javax.inject.Named;

@BindTo(qualifier = Named.class, interfaceType = Network.class)
public class PrivateNetwork implements Network {
    @Override
    public void doCall() {
        //does something
    }
}
