package xspleet.daggerapi.trigger;

import xspleet.daggerapi.data.TriggerData;

public class DataContainer
{
    //Singleton Thread Local TriggerData Container

    private static final ThreadLocal<TriggerData> THREAD_LOCAL = new ThreadLocal<>();

    public static TriggerData get() {
        return THREAD_LOCAL.get();
    }

    public static void set(TriggerData data) {
        THREAD_LOCAL.set(data);
    }
}
