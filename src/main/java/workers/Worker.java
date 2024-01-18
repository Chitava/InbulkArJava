package workers;

import methods.GetTime;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;


public class Worker {
    //region Поля
    private String name;
    private boolean post;
    private ArrayList monthStat = new ArrayList<>();
    private ArrayList workTimes = new ArrayList<>();
    private double paymentPerDay;
    private double paymentPerHour;
    private double peymentForHollydays;
    private int workDays = 0;
    private int workHolydays =0;
    private double elaborTimes = 0.0;
    private double wage = 0.0;
    private double wageElaborTime = 0.0;
    private double prepayment = 0.0;
    private double fullWage;
    //endregion


    //region Конструкторы
    public Worker(String name, boolean post, double paymentPerDay,
                  double paymentPerHour, double peymentForHollydays) {
        this.name = name;
        this.post = post;
        this.paymentPerDay = paymentPerDay;
        this.paymentPerHour = paymentPerHour;
        this.peymentForHollydays = peymentForHollydays;
        }

    public Worker() {
    }


    //endregion

    //region Getters Setters
    public void setPost() {
        this.post = true;
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
    public boolean getPost() {
        return post;
    }
    public static ArrayList<Worker> users = new ArrayList();
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
    public void setWorkTimes(String workTime) {this.workTimes.add(workTime);}
    public static void addUser(Worker user) {users.add(user);}
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
    public boolean isPost() {
        return post;
    }
    public void setPost(boolean post) {
        this.post = post;
    }
    public int getWorkDays() {
        return workDays;
    }
    public void setWorkDays(int workDays) {
        this.workDays = workDays;
    }
    public int getWorkHolydays() {
        return workHolydays;
    }
    public void setWorkHolydays(int workHolydays) {
        this.workHolydays = workHolydays;
    }
    public double getElaborTimes() {
        return elaborTimes;
    }
    public void setElaborTimes(double elaborTimes) {
        this.elaborTimes = elaborTimes;
    }
    public double getWage() {
        return wage;
    }
    public void setWage(double wage) {
        this.wage = wage;
    }
    public double getWageElaborTime() {
        return wageElaborTime;
    }
    public void setWageElaborTime(double wageElaborTime) {
        this.wageElaborTime = wageElaborTime;
    }
    public double getPrepayment() {
        return prepayment;
    }
    public void setPrepayment(double prepayment) {
        this.prepayment = prepayment;
    }
    public double getFullWage() {
        return fullWage;
    }
    public void setFullWage(double fullWage) {
        this.fullWage = fullWage;
    }

//endregion



    public void monthStat(String month, ArrayList hollydays) {
        int workDays = 0;
        int workHolydays = 0;
        LocalDateTime elaborTimes = LocalDateTime.of(1, 1, 1, 0, 0);
        LocalTime workTime = LocalTime.of(9, 0);
        LocalTime workTimeHollidays = LocalTime.of(6, 0);
        double wageElabors = 0.0;
        double wage = 0.0;
        int year = Integer.parseInt(month.substring(month.indexOf("_") + 1, month.length()));
        int mon = Integer.parseInt(month.substring(0, month.indexOf("_")));
        LocalDate dayOfWeek;
        for (int i = 0; i < this.workTimes.size(); i++) {
            try{
            dayOfWeek = LocalDate.of(year, mon, i + 1);}
            catch (DateTimeException e){
                throw new RuntimeException();
            }
            if (this.post) {
                if (GetTime.getDayNumber(dayOfWeek) == 6 || GetTime.getDayNumber(dayOfWeek) == 7 ||
                        hollydays.contains(i+1)) {
                    if (Double.valueOf(workTimes.get(i).toString()) > workTimeHollidays.getHour()) {
                        workDays++;
                        workHolydays++;
                        wage = wage + this.peymentForHollydays;
                        LocalDateTime elaborTime = GetTime.getElaborTime(workTimes.get(i).toString(), "6.0");
                        elaborTimes = elaborTimes.plusHours(elaborTime.getHour()).plusMinutes(elaborTime.getMinute());
                    } else if (Double.valueOf(workTimes.get(i).toString()) > 0){
                        workDays++;
                        workHolydays++;
                        wage = wage + this.peymentForHollydays;
                    }
                }else{
                    if (Double.valueOf(workTimes.get(i).toString()) > workTime.getHour()) {
                        workDays++;
                        wage = wage + this.paymentPerDay;
                        LocalDateTime elaborTime = GetTime.getElaborTime((String) workTimes.get(i), "9.0");
                        elaborTimes = elaborTimes.plusHours(elaborTime.getHour()).plusMinutes(elaborTime.getMinute());
                    } else if (Double.valueOf(workTimes.get(i).toString()) <= 9
                                && Double.valueOf(workTimes.get(i).toString()) > 1) {
                        workDays++;
                        double paymentForHouer = this.paymentPerDay / 8;
                        wage = wage + (GetTime.getTimeNotFullWorkDay(String.valueOf(workTimes.get(i)))
                                * paymentForHouer);
                    }
                }
            } else {
                if (Double.valueOf(workTimes.get(i).toString()) > workTime.getHour()) {
                    workDays++;
                    wage = wage + this.paymentPerDay;
                    LocalDateTime elaborTime = GetTime.getElaborTime((String) workTimes.get(i), "9.0");
                    elaborTimes = elaborTimes.plusHours(elaborTime.getHour()).plusMinutes(elaborTime.getMinute());
                } else if (Double.valueOf(workTimes.get(i).toString()) <= workTime.getHour()
                            && Double.valueOf(workTimes.get(i).toString()) > 1) {
                    workDays++;
                    double paymentForHouer = this.paymentPerDay / 8;
                    wage = wage + (GetTime.getTimeNotFullWorkDay(String.valueOf(workTimes.get(i))) * paymentForHouer);
                }
            }
        }
        String elaborTime = (((elaborTimes.getDayOfMonth() - 1) * 24 + elaborTimes.getHour()) + "."
                + elaborTimes.getMinute());
        setWorkDays(Integer.parseInt(String.valueOf(workDays)));
        setWorkHolydays(workHolydays);
        setElaborTimes(Double.parseDouble(elaborTime));
        setWage(wage);
        setWageElaborTime(wageElabors + Double.parseDouble(elaborTime) * this.paymentPerHour);
//        setPrepayment(10000);
        double fullWage = this.getWage() + this.getWageElaborTime()- this.getPrepayment();
        setFullWage(Double.parseDouble(String.valueOf(fullWage)));
    }


    //region перегрузки
    @Override
    public String toString() {
        return String.format("Работник - %s\nОтработал в этом месяце - %s д.\nИз них выходные и праздничные - %s д.\n" +
                             "Время переработки - %s ч.\nЗарплата за полные дни - %s р.\nЗарплата за переработку - %s р." +
                             "\nПолучен аванс - %s р.\nИтого за месяц с учетом аванса - %s р.", this.name,
                             this.getWorkDays(), this.getWorkHolydays(), this.getElaborTimes(), this.getWage(),
                             String.format("%.2f",this.getWageElaborTime()), getPrepayment(), Math.round(this.getFullWage()));
    }

    //endregion

}


