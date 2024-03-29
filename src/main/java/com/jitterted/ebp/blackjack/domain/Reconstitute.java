package com.jitterted.ebp.blackjack.domain;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Applies to static factory creation methods in Domain Objects
 * (Aggregates) that are loaded (reconstituted) by a Repository
 */

@Target(ElementType.METHOD)
public @interface Reconstitute {
}
