import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import interfaces.SQLSender;
import methods.GetTime;
import workers.Worker;


public class main {
    public static void main(String[] args) throws IOException {
        ArrayList workers = GetTime.setUser("src/main/resources/files/1.xlsx");
        ArrayList hollydays = new ArrayList<>();
        hollydays.add(1);
        hollydays.add(2);
        hollydays.add(3);
        hollydays.add(4);
        hollydays.add(7);

//        for (Worker worker : Worker.users) {
//            System.out.println(worker.monthStat("07_2023", hollydays));
//        }
//        ExcelSender excel = new ExcelSender();
//
//        excel.sendTo(workers, "./зарплата за июль 2023.xls");
//    }
        SQLSender sql = new SQLSender();
        ArrayList<ArrayList> answer = sql.selectWorker("уль");
        System.out.println(answer);
        for (ArrayList list : answer) {
            System.out.printf("Сотрудник - %s\nОплата в день %s р.\nОплата в час %s р.\nОплата в праздники %s р.\n",
                    list.get(0), list.get(1), list.get(2), list.get(3));
            System.out.println("----------------------------");
        }
//
//        }
//        sql.createWorkerDB();
//        for (Worker worker : Worker.users) {
//            sql.insertWorker(worker);
//        }

    }
}






