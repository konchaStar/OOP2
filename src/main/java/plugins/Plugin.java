package plugins;

public interface Plugin {
    public String getExtension();
    public String getName();
    public byte[] encode(byte[] value);
    public byte[] decode(byte[] value);
}
