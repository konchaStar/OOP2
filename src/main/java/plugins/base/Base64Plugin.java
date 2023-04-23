package plugins.base;

import plugins.Plugin;

import java.util.Base64;

public class Base64Plugin implements Plugin {

    @Override
    public String getExtension() {
        return ".base64";
    }

    @Override
    public String getName() {
        return "Base 64";
    }

    @Override
    public byte[] encode(byte[] value) {
        return Base64.getEncoder().encode(value);
    }

    @Override
    public byte[] decode(byte[] value) {
        return Base64.getDecoder().decode(value);
    }
}
