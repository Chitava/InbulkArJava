package interfaces;
import workers.Worker;
import java.io.IOException;
import java.util.ArrayList;

public interface SendTo {
    public void sendTo(ArrayList<Worker> worker, String path) throws IOException;
}
