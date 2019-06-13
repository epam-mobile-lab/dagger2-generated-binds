package com.epam.severalsubtypes;

import com.epam.daggerbinds.annotation.BindTo;

import javax.inject.Named;

@BindTo(interfaceType = Repository.class, qualifier = Named.class)
public class RepositoryImp1 implements Repository {
}
