package com.epam.severalsubtypes;

import com.epam.daggerbinds.annotation.BindTo;

@BindTo(interfaceType = Service.class, qualifier = ServiceImpQ1.class)
public class ServiceImp1 implements Service {
}