package workers;


import methods.GetTime;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class Worker{
    private String name;
    private String post;
    private ArrayList monthStat;
    private ArrayList workTimes;
    private double paymentPerDay;
    private double paymentPerHour;
    private double peymentForHollydays;

    public void setPost(String post) {
        this.post = post;
    }

    public void setMonthStat(ArrayList monthStat) {
        this.monthStat = monthStat;
    }

    public void setWorkTimes(ArrayList workTimes) {
        this.workTimes = workTimes;
    }

    public static ArrayList<Worker> getUsers() {
        return users;
    }

    public static void setUsers(ArrayList<Worker> users) {
        Worker.users = users;
    }

    public String getPost() {
        return post;
    }

    public static ArrayList<Worker> users = new ArrayList();

    public Worker(String name, String post, double paymentPerDay, double paymentPerHour, double peymentForHollydays) {
        this.name = name;
        this.post = post;
        this.paymentPerDay = paymentPerDay;
        this.paymentPerHour = paymentPerHour;
        this.monthStat = new ArrayList();
        this.workTimes = new ArrayList();
        this.peymentForHollydays = peymentForHollydays;
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

    public static void addUser(Worker user) {
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

    public int getSize() {
        return this.monthStat.size();
    }
    public double getPeymentForHollydays() {
        return peymentForHollydays;
    }

    public void setPeymentForHollydays(double peymentForHollydays) {
        this.peymentForHollydays = peymentForHollydays;
    }

    public String monthStat() {
        int workDays = 0;
        LocalDateTime elaborTimes = LocalDateTime.of(1, 1, 1, 0, 0);
        LocalTime workTime = LocalTime.of(9, 0);
        double wageElabors = 0.0;
        double wage = 0.0;
        DecimalFormat df = new DecimalFormat("#.##");
        for (int i = 0; i < this.workTimes.size(); i++) {
            if (Double.valueOf(workTimes.get(i).toString()) > 9) {
                workDays++;
                wage = wage + this.paymentPerDay;
                LocalDateTime elaborTime = GetTime.getElaborTime((String) workTimes.get(i), "9.0");
                elaborTimes = elaborTimes.plusHours(elaborTime.getHour()).plusMinutes(elaborTime.getMinute());
            } else if (Double.valueOf(workTimes.get(i).toString()) <= 9 && Double.valueOf(workTimes.get(i).toString()) > 1) {
                workDays++;
                double paymentForHouer = this.paymentPerDay / 8;
                wage = wage + (GetTime.getTimeNotFullWorkDay(String.valueOf(workTimes.get(i))) * paymentForHouer);
            }
        }
        String elaborTime = (((elaborTimes.getDayOfMonth() - 1) * 24 + elaborTimes.getHour()) + "." + elaborTimes.getMinute());
        wageElabors = wageElabors + Double.parseDouble(elaborTime) * this.paymentPerHour;
        double fullWage = wage + wageElabors;
        fullWage = Double.parseDouble(String.valueOf(fullWage));
        setMonthStat(String.valueOf(workDays));
        setMonthStat(elaborTime);
        setMonthStat(String.valueOf(wage).substring(0, String.valueOf(wage).indexOf(".") + 2));
        setMonthStat(String.valueOf(wageElabors).substring(0, String.valueOf(wageElabors).indexOf(".") + 2));
        setMonthStat(String.valueOf(fullWage).substring(0, String.valueOf(fullWage).indexOf(".") + 2));
        return String.format("Отработал в этом месяце - %s дней\nЗаработал - %s р.\n" +
                        "Переработка составила - %s ч.\nЗарплата за переработку - %s р.\nИтого за месяц - %s р.",
                this.monthStat.get(0), this.monthStat.get(1), this.monthStat.get(2), this.monthStat.get(3),
                this.monthStat.get(4));
    }

    @Override
    public String toString() {
        return String.format("Работник - %s\nдолжность - %s\nРазмер оплаты работы за день - %s р.\nРазмер почасовой оплаты " +
                "переработки - %s р.", this.name, this.post, this.paymentPerDay, this.paymentPerHour);
    }



}


