package com.epam.subtypes.severalfirst;

import com.epam.daggerbinds.annotation.BindTo;

@BindTo(interfaceType = Service.class, qualifier = ServiceImpQFirst.class)
public class ServiceImpFirst implements Service {
}
