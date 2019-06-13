package com.epam.severalsubtypes2;

import com.epam.daggerbinds.annotation.BindTo;

import javax.inject.Named;

@BindTo(interfaceType = Room.class, qualifier = Named.class)
public class RoomImp1 implements Room {
}
