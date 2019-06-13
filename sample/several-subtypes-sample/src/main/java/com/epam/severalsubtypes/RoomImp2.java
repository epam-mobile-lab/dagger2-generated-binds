package com.epam.severalsubtypes;

import com.epam.daggerbinds.annotation.BindTo;

import javax.inject.Named;

@BindTo(interfaceType = Room.class, qualifier = Named.class)
public class RoomImp2 implements Room {
}
