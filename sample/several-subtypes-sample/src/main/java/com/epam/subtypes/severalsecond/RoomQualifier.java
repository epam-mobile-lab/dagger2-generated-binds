package com.epam.subtypes.severalsecond;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface RoomQualifier {
    Class<?> value();
}