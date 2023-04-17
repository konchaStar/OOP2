package serializer;

import javafx.util.Pair;
import transport.Transport;

import java.io.File;
import java.util.ArrayList;

public interface Serializer {
    public void serialize(ArrayList<Pair<String, Transport>> transport, File file);
    public ArrayList<Pair<String, Transport>> deserialize(File file);
}
