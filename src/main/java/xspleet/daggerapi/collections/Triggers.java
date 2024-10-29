package xspleet.daggerapi.collections;

import xspleet.jdagapi.base.Mapper;
import xspleet.jdagapi.base.Trigger;

import java.util.HashMap;
import java.util.Map;

public class Triggers
{
    public static final Trigger AFTER_HIT = Mapper.registerTrigger("afterHit");
    public static final Trigger BEFORE_HIT = Mapper.registerTrigger("beforeHit");
}
