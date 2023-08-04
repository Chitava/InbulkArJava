import java.util.ArrayList;

import interfaces.ExcellSender;
import interfaces.SQLSender;
import methods.GetWorkTime;
import workers.Worker;

public class main {
    public static void main(String[] args) {
        try{
       ArrayList workers = GetWorkTime.setUser("src/main/resources/files/1.xlsx");
            SQLSender sendsql = new SQLSender();
            System.out.println(sendsql.selectAll("06_2023"));
        }catch (Exception e){e.printStackTrace();}
    }
}




