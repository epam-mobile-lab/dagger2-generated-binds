package com.epam.subtypes.severalsecond;

import com.epam.daggerbinds.annotation.BindTo;

@BindTo(interfaceType = Service.class, qualifier = ServiceImpQFirst.class)
public class ServiceImpFirst implements Service {
}
