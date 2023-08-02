package user;

import java.text.DecimalFormat;
import java.time.LocalTime;
import java.util.ArrayList;

public class User {
    private String name;
    private String post;
    private ArrayList monthStat;
    private ArrayList workTimes;
    private double paymentPerDay;
    private double paymentPerHour;
    public static ArrayList<User> users = new ArrayList();

    public User(String name, String post, double paymentPerDay, double paymentPerHour) {
        this.name = name;
        this.post = post;
        this.paymentPerDay = paymentPerDay;
        this.paymentPerHour = paymentPerHour;
        this.monthStat = new ArrayList();
        this.workTimes = new ArrayList();
    }


    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList getMonthTimes() {
        return this.monthStat;
    }

    public void setMonthStat(String monthTime) {
        this.monthStat.add(monthTime);
    }

    public ArrayList getWorkTimes() {
        return this.workTimes;
    }

    public void setWorkTimes(String workTime) {

        this.workTimes.add(workTime);
    }

    public static void addUser(User user) {
        users.add(user);
    }

    public double getPaymentPerDay() {
        return paymentPerDay;
    }

    public void setPaymentPerDay(double paymentDay) {
        this.paymentPerDay = paymentDay;
    }

    public double getPaymentPerHour() {
        return paymentPerHour;
    }

    public void setPaymentPerHour(double paymentPerHour) {
        this.paymentPerHour = paymentPerHour;
    }


    public int quantityOfWorkDays() {
        int count = 0;
        for (int i = 0; i < this.workTimes.size(); i++) {
            if (workTimes.get(i).equals("0")) {
                continue;
            } else {
                count++;
            }
        }
        return count;
    }


    public void monthStat(ArrayList monthTimes) {
        ArrayList result = new ArrayList<>();
        int workDays = 0;
        LocalTime wageWorkTimesMonth = LocalTime.of(0, 0);
        LocalTime elaborTimes = LocalTime.of(0, 0);
        double wageElabors = 0.0;
        for (Object time : this.workTimes) {
            if (Double.valueOf(time.toString())> 9) {
                workDays++;
                wageWorkTimesMonth = (wageWorkTimesMonth + this.paymentPerDay); /*Исправить временной формат*/
                double elaborTime = Double.valueOf(time.toString())-9;
                elaborTimes = elaborTimes + elaborTime;
                wageElabors = wageElabors + (elaborTime * this.paymentPerHour);
            } else if (Double.valueOf(time.toString()) <= 9 && Double.valueOf(time.toString()) > 1) {
                workDays++;
                double workTime = Double.valueOf(time.toString()) - 1;
                double paymentForHouer = this.paymentPerDay / 8;
                wageWorkTimesMonth = wageWorkTimesMonth + workTime * paymentForHouer;
            }

        }
        DecimalFormat df = new DecimalFormat("#.##");
        double fullWage = wageWorkTimesMonth+wageElabors;
        fullWage = Double.parseDouble(String.valueOf(fullWage));
        elaborTimes = Double.parseDouble(String.valueOf(elaborTimes).replace(",","."));
        wageWorkTimesMonth = Double.parseDouble(String.valueOf(wageWorkTimesMonth).replace(",","."));
        wageElabors = Double.parseDouble(String.valueOf(wageElabors).replace(",","."));
        elaborTimes = Double.parseDouble(String.valueOf(elaborTimes).replace(",","."));
        System.out.printf("Работник %s\nотработал в этом месяце - %s дней\nПереработка составила - %s часов\n" +
                "Зарплата за полные рабочие дни - %s р.\nЗарплата за переработку - %s р.\nИтого за месяц - %s" +
                        "\n----------------------\n", this.name, workDays, df.format(elaborTimes),
                df.format(wageWorkTimesMonth), df.format(wageElabors), df.format(fullWage));


    }

    @Override
    public String toString() {
        return String.format("Работник - %s  должность - %s\nОтработал в этом месяце - %s дней\nРазмер оплаты работы за день - %s р.\nРазмер почасовой оплаты " +
                "переработки - %s р.", this.name, this.post, quantityOfWorkDays(), this.paymentPerDay, this.paymentPerHour);
    }
}
