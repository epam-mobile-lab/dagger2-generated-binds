package com.epam.subtypes.severalfirst;

import com.epam.daggerbinds.annotation.BindTo;

@BindTo(interfaceType = Service.class, qualifier = ServiceImpQSecond.class)
public class ServiceImpSecond implements Service {
}
