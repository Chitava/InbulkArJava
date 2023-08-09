import java.io.IOException;
import java.util.ArrayList;

import interfaces.ExcelSender;
import interfaces.SQLSender;
import methods.GetTime;
import workers.Worker;

public class main {
    public static void main(String[] args) throws IOException {
        ArrayList workers = GetTime.setUser("src/main/resources/files/2.xlsx");
        ArrayList hollydays = new ArrayList<>();
        hollydays.add(1);
        hollydays.add(2);
        hollydays.add(3);
        hollydays.add(4);
        hollydays.add(7);
        SQLSender sql = new SQLSender();
        sql.createWorkerDB();
        sql.createMonthDB("07_2023");
        for (Worker worker : Worker.users) {
            sql.insertWorker(worker);
            worker.monthStat("07_2023", hollydays);
            sql.insertMonthValue(worker, "07_2023");
        }
        System.out.println("------------------------------");
        for (Object worker:
        sql.selectAllWorkersWithMonthStat("07_2023")) {
            System.out.println(worker.toString());
            System.out.println("------------------------------");
        }

//        System.out.println(sql.selectAllWorkers().toString());
//
//        ExcelSender excel = new ExcelSender();
//
//        excel.sendTo(workers, "./зарплата за июль 2023.xls");
//    }
        }
    }






