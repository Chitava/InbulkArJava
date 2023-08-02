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
//        LocalTime wageWorkTimesMonth = LocalTime.of(0, 0);
        LocalDateTime elaborTimes = LocalDateTime.of(1,1,1, 0,0);
        LocalTime workTime = LocalTime.of(9, 0);
        double wageElabors = 0.0;
        double wage = 0.0;
        DecimalFormat df = new DecimalFormat("#.##");

        for (Object time : this.workTimes) {
            if (Double.valueOf(time.toString()) > 9) {
                workDays++;
                wage = wage +this.paymentPerDay;
                String tempTime = (String) time;
                int houer = Integer.parseInt((tempTime.substring(0, ((String) time).indexOf("."))));
                int minute = Integer.parseInt(tempTime.substring(tempTime.indexOf(".") + 1, tempTime.length()));
                LocalDateTime tempWorkTime = LocalDateTime.of(1,1, 1, houer, minute);
                LocalDateTime resultTime = tempWorkTime.minusHours(workTime.getHour()).minusMinutes(workTime.getMinute());
                elaborTimes = elaborTimes.plusHours(resultTime.getHour()).plusMinutes(resultTime.getMinute());
            } else if (Double.valueOf(time.toString()) <= 9 && Double.valueOf(time.toString()) > 1) {
                workDays++;
                String tempTime = (String) time;
                int houer = Integer.parseInt((tempTime.substring(0, ((String) time).indexOf("."))));
                int minute = Integer.parseInt(tempTime.substring(tempTime.indexOf(".") + 1, tempTime.length()));
                LocalTime tempWorkTime = LocalTime.of(houer, minute);
                tempWorkTime = tempWorkTime.minusHours(1);
                double paymentForHouer = this.paymentPerDay/8;
                wage = wage + (Double.valueOf(tempWorkTime.format(DateTimeFormatter.ofPattern("H.mm")))*paymentForHouer);


            }

            }
        elaborTimes = elaborTimes.minusDays(1);
        String a = String.valueOf(elaborTimes.getMinute());
        double elaborTimesSum = (double)elaborTimes.getDayOfMonth()*24 + (double)elaborTimes.getHour();
        String elaborTimeSumMinute = (elaborTimesSum) + a;
        System.out.println(elaborTimeSumMinute);

        wageElabors = wageElabors + (elaborTimesSum * this.paymentPerHour);

        double fullWage = wage+wageElabors;
        fullWage = Double.parseDouble(String.valueOf(fullWage));
        System.out.println(this.name);
        System.out.println("Отработал - " + workDays + " д.");
//        System.out.println("Перерработал - " + elaborTime + " ч.");
        System.out.println("Заработал - " + wage + " р.");
        System.out.println("Заработал за переработку - " + (df.format(wageElabors)) + " р.");
        System.out.println("Итого - " + fullWage + " р.");

//        elaborTimes = Double.parseDouble(String.valueOf(elaborTimes).replace(",","."));
//        wageWorkTimesMonth = Double.parseDouble(String.valueOf(wageWorkTimesMonth).replace(",","."));
//        wageElabors = Double.parseDouble(String.valueOf(wageElabors).replace(",","."));
//        elaborTimes = Double.parseDouble(String.valueOf(elaborTimes).replace(",","."));
//        System.out.printf("Работник %s\nотработал в этом месяце - %s дней\nПереработка составила - %s часов\n" +
//                "Зарплата за полные рабочие дни - %s р.\nЗарплата за переработку - %s р.\nИтого за месяц - %s" +
//                        "\n----------------------\n", this.name, workDays, df.format(elaborTimes),
//                df.format(wageWorkTimesMonth), df.format(wageElabors), df.format(fullWage));
        }



        @Override
        public String toString () {
            return String.format("Работник - %s  должность - %s\nОтработал в этом месяце - %s дней\nРазмер оплаты работы за день - %s р.\nРазмер почасовой оплаты " +
                    "переработки - %s р.", this.name, this.post, quantityOfWorkDays(), this.paymentPerDay, this.paymentPerHour);
        }
    }

