package com.epam.subtypes.severalfirst;

import com.epam.daggerbinds.annotation.BindTo;

import javax.inject.Named;

@BindTo(interfaceType = Room.class, qualifier = Named.class)
public class RoomImpFirst implements Room {
}
