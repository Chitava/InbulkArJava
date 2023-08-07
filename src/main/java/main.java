import java.io.IOException;
import java.util.ArrayList;

import interfaces.ExcelSender;
import methods.GetTime;
import workers.Worker;

public class main {
    public static void main(String[] args) throws IOException {
        ArrayList workers = GetTime.setUser("src/main/resources/files/1.xlsx");
//       ArrayList hollydays = new ArrayList<>();
//       hollydays.add(1);
//       hollydays.add(2);
//       hollydays.add(3);
//       hollydays.add(4);
//       hollydays.add(7);
        for (Worker worker : Worker.users) {
            worker.monthStat();
        }
        ExcelSender excel = new ExcelSender();

        excel.sendTo(workers, "./Excel.xls");
    }

}





