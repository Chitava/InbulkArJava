package interfaces;

import workers.Worker;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public interface SendTo {
    public void sendTo(ArrayList<Worker> worker, String path);

}
//public interface
//    public void sendTo(ArrayList<Worker> worker, String path);
//    public void insertWorker (ArrayList<Worker> workers);
//
//    void sendToSQL(ArrayList<Worker> workers);
//}
