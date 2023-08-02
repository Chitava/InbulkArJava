import java.io.IOException;
import java.util.ArrayList;

import methods.GetWorkTime;
import user.User;

public class main {
    public static void main(String[] args) throws IOException {
       ArrayList workers = GetWorkTime.setUser("src/main/resources/files/2.xlsx");
        for (User user: User.users
             ) {

            user.monthStat(user.getMonthTimes());

        }
    }
}




