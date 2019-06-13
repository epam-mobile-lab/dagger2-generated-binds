package com.epam.subtypes.single;

public class Example {
    public static void main(String[] args){
        NetworkComponent networkComponent = DaggerNetworkComponent.create();
        System.out.println(networkComponent.network().toString());
    }
}
