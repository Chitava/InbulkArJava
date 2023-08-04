import java.util.ArrayList;

import interfaces.ExcellSender;
import interfaces.SQLSender;
import methods.GetWorkTime;
import workers.Worker;

public class main {
    public static void main(String[] args) {
        try{
       ArrayList workers = GetWorkTime.setUser("src/main/resources/files/1.xlsx");
        for (Worker worker: Worker.users) {
            worker.monthStat();
        }

        SQLSender sendsql = new SQLSender();
        sendsql.createMonthDB("06_2023");
        //        sendsql.sendToSQL(workers);
//        ExcellSender excel = new ExcellSender();
//        excel.sendTo("./Excel.xls");
//        excel.sendTo(workers, "./Excel.xls");
        }catch (Exception e){e.printStackTrace();}
    }
}




