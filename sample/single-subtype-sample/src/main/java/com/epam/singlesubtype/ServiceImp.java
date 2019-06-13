package com.epam.singlesubtype;

import com.epam.daggerbinds.annotation.BindTo;

@BindTo(interfaceType = Service.class)
public class ServiceImp implements Service {
}
