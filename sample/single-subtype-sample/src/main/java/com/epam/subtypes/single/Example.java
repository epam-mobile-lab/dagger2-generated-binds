package com.epam.subtypes.single;

public class Example {
    public static void main(String[] args) {
        RootComponent rootComponent = DaggerRootComponent.create();
        System.out.println(rootComponent.service().toString());
    }
}
