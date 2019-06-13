package com.epam.severalsubtypes;

public class Example {
    public static void main(String[] args){
        RootComponent rootComponent = DaggerRootComponent.create();
        System.out.println(rootComponent.repo().toString());
    }
}
