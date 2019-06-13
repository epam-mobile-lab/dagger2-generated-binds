package com.epam.singlesubtype;

public class Example {
    public static void main(String[] args){
        NetworkComponent networkComponent = DaggerNetworkComponent.create();
        System.out.println(networkComponent.network().toString());
    }
}
