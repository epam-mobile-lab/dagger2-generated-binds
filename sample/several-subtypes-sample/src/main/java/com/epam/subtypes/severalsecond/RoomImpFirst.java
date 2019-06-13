package com.epam.subtypes.severalsecond;

import com.epam.daggerbinds.annotation.BindTo;

import javax.inject.Named;

@BindTo(interfaceType = Room.class, qualifier = Named.class)
public class RoomImpFirst implements Room {
}
