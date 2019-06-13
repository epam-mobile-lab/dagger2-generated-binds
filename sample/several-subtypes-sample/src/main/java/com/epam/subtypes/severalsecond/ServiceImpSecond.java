package com.epam.subtypes.severalsecond;

import com.epam.daggerbinds.annotation.BindTo;

@BindTo(interfaceType = Service.class, qualifier = ServiceImpQSecond.class)
public class ServiceImpSecond implements Service {
}
