package fabrics;
import javafx.scene.layout.HBox;
import transport.*;

import java.util.Optional;

public interface Factory {
    Optional<Transport> getTransport(HBox box);
    void getFields(HBox box, Optional<? extends Transport> transport);
}
