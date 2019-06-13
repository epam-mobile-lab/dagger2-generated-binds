package com.epam.severalsubtypes2;

import com.epam.daggerbinds.annotation.BindTo;

import javax.inject.Named;

@BindTo(interfaceType = Repository.class, qualifier = Named.class)
public class RepositoryImp2 implements Repository {
}
