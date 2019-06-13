package com.epam.severalsubtypes;

import com.epam.daggerbinds.annotation.BindTo;

@BindTo(interfaceType = Connection.class)
public class ConnectionImp implements Connection {
}
