import java.io.IOException;
import java.util.ArrayList;

import methods.GetWorkTime;
import user.User;

public class main {
    public static void main(String[] args) throws IOException {
       ArrayList workers = GetWorkTime.setUser("src/main/resources/files/1.xlsx");
        for (User user: User.users) {
            System.out.println(user.toString());
            System.out.println(user.monthStat());
            System.out.println("--------------------------");
        }
    }
}




