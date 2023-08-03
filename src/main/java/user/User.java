package user;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
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

    public ArrayList getMonthStat() {
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

    public String monthStat() {
        int workDays = 0;
        LocalDateTime elaborTimes = LocalDateTime.of(1, 1, 1, 0, 0);
        LocalTime workTime = LocalTime.of(9, 0);
        double wageElabors = 0.0;
        double wage = 0.0;
        DecimalFormat df = new DecimalFormat("#.##");
        for (Object time : this.workTimes) {
            if (Double.valueOf(time.toString()) > 9) {
                workDays++;
                wage = wage + this.paymentPerDay;
                String tempTime = (String) time;
                int houer = Integer.parseInt((tempTime.substring(0, ((String) time).indexOf("."))));
                int minute = Integer.parseInt(tempTime.substring(tempTime.indexOf(".") + 1, tempTime.length()));
                LocalDateTime tempWorkTime = LocalDateTime.of(1, 1, 1, houer, minute);
                LocalDateTime resultTime = tempWorkTime.minusHours(workTime.getHour()).minusMinutes(workTime.getMinute());
                elaborTimes = elaborTimes.plusHours(resultTime.getHour()).plusMinutes(resultTime.getMinute());
            } else if (Double.valueOf(time.toString()) <= 9 && Double.valueOf(time.toString()) > 1) {
                workDays++;
                String tempTime = (String) time;
                int houer = Integer.parseInt((tempTime.substring(0, ((String) time).indexOf("."))));
                int minute = Integer.parseInt(tempTime.substring(tempTime.indexOf(".") + 1, tempTime.length()));
                LocalTime tempWorkTime = LocalTime.of(houer, minute);
                tempWorkTime = tempWorkTime.minusHours(1);
                double paymentForHouer = this.paymentPerDay / 8;
                wage = wage + (Double.valueOf(tempWorkTime.format(DateTimeFormatter.ofPattern("H.mm"))) * paymentForHouer);
            }
        }
        String elaborTime = (String.valueOf((elaborTimes.getDayOfMonth()-1) * 24 + elaborTimes.getHour()) + "." + elaborTimes.getMinute());
        wageElabors = wageElabors + Double.parseDouble(elaborTime) * this.paymentPerHour;
        double fullWage = wage + wageElabors;
        fullWage = Double.parseDouble(String.valueOf(fullWage));
        setMonthStat(String.valueOf(workDays));
        setMonthStat(String.valueOf(wage).substring(0, String.valueOf(wage).indexOf(".") + 2));
        setMonthStat(elaborTime);
        setMonthStat(String.valueOf(wageElabors).substring(0, String.valueOf(wageElabors).indexOf(".") + 2));
        setMonthStat(String.valueOf(fullWage).substring(0, String.valueOf(fullWage).indexOf(".") + 2));
        return String.format("Отработал в этом месяце - %s дней\nЗаработал - %s р.\n" +
                "Переработка составила - %s ч.\nЗарплата за переработку - %s р.\nИтого за месяц - %s р.",
                this.monthStat.get(0), this.monthStat.get(1), this.monthStat.get(2),this.monthStat.get(3),
                this.monthStat.get(4));
    }



        @Override
        public String toString () {
            return String.format("Работник - %s\nдолжность - %s\nРазмер оплаты работы за день - %s р.\nРазмер почасовой оплаты " +
                    "переработки - %s р.", this.name, this.post, this.paymentPerDay, this.paymentPerHour);
        }
    }

