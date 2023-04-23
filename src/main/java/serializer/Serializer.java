package serializer;

import by.oop.oop2.MyPair;
import javafx.util.Pair;
import transport.Transport;

import java.io.File;
import java.util.ArrayList;

public interface Serializer {
    public String getExtension();
    public void serialize(ArrayList<MyPair<String, Transport>> transport, File file);
    public ArrayList<MyPair<String, Transport>> deserialize(File file);
}
