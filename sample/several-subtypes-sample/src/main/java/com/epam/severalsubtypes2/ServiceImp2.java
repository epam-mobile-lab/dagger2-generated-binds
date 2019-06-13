package com.epam.severalsubtypes2;

import com.epam.daggerbinds.annotation.BindTo;

@BindTo(interfaceType = Service.class, qualifier = ServiceImpQ2.class)
public class ServiceImp2 implements Service {
}
