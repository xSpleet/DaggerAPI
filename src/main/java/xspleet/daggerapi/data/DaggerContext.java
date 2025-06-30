package xspleet.daggerapi.data;

public interface DaggerContext
{
    public DaggerContext addData(String key, String value);
    public String getData(String key);
}
