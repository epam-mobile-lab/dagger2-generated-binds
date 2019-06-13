package com.epam.severalsubtypes2;

import com.epam.daggerbinds.annotation.BindTo;

@BindTo(interfaceType = Connection.class)
public class ConnectionImp implements Connection {
}
