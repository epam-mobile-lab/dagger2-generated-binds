package com.epam.sample;

public class Example {
    public static void main(String[] args) {
        RootComponent rootComponent = DaggerRootComponent.create();
        System.out.println(rootComponent.service().toString());
    }
}
