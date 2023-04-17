package serializer;

import javafx.util.Pair;
import transport.Transport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class TextSerializer implements Serializer {
    @Override
    public void serialize(ArrayList<Pair<String, Transport>> transport, File file) {
        try {
            FileOutputStream stream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {

        }
    }

    @Override
    public ArrayList<Pair<String, Transport>> deserialize(File file) {
        return null;
    }
}
