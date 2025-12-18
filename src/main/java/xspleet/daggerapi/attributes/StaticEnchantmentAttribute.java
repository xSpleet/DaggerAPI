package xspleet.daggerapi.attributes;

public class StaticEnchantmentAttribute implements Attribute<Integer> {

    private final String name;
    private boolean tracked = true;

    public StaticEnchantmentAttribute(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Integer getDefaultValue() {
        return 0;
    }

    @Override
    public Class<Integer> getType() {
        return Integer.class;
    }

    @Override
    public Integer clamp(Integer value) {
        return Math.max(0, value);
    }

    @Override
    public Attribute<Integer> setUntracked() {
        tracked = true;
        return this;
    }

    @Override
    public boolean isTracked() {
        return tracked;
    }

}
