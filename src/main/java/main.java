import java.awt.print.PrinterException;
import java.io.IOException;
import java.util.ArrayList;

import methods.SQLSender;
import methods.GetTime;
import workers.Worker;

import static methods.PrintFile.printTo;

public class main {
    public static void main(String[] args) throws IOException, PrinterException {
        SQLSender sql = new SQLSender();

        ArrayList<Worker> workers = GetTime.setUser("src/main/resources/files/3.xlsx");
        ArrayList hollydays = new ArrayList<>();
        hollydays.add(1);
        hollydays.add(2);
        hollydays.add(3);
        hollydays.add(4);

//        sql.createWorkerDB();
//        sql.createMonthDB("07_2023");

        for (Worker worker : Worker.users) {
            sql.insertWorker(worker);
            worker.monthStat("07_2023", hollydays);
            sql.insertMonthValue(worker, "07_2023");

        }
       printTo(workers);
//        System.out.println("------------------------------");
//        for (Object worker:
//        sql.selectAllWorkersWithMonthStat("07_2023")) {
//            System.out.println(worker.toString());
//            System.out.println("------------------------------");
//        }
//        ExcelSender excel = new ExcelSender();
//        excel.sendTo(workers, "./зарплата за июль 2023.xls");




//        System.out.println(sql.selectAllWorkers().toString());
//

//    }
        }}







