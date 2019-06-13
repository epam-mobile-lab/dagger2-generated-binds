package com.epam.subtypes.severalfirst;

import com.epam.daggerbinds.annotation.BindTo;

import javax.inject.Named;

@BindTo(interfaceType = Repository.class, qualifier = Named.class)
public class RepositoryImpFirst implements Repository {
}
