import java.io.IOException;
import java.util.ArrayList;

import interfaces.ExcelSender;
import methods.GetWorkTime;
import workers.Worker;

public class main {
    public static void main(String[] args) throws IOException {
       ArrayList workers = GetWorkTime.setUser("src/main/resources/files/1.xlsx");
        for (Worker worker: Worker.users) {
            worker.monthStat();
        }
        ExcelSender excel = new ExcelSender();
        excel.sendTo("./Excel.xls");
        excel.sendTo(workers, "./Excel.xls");
        }
}




