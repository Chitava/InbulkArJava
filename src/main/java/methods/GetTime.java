package methods;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import workers.Worker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.*;
import java.util.*;

//Получение статистики за указанный месяц
public class GetTime {
    public static ArrayList setUser(String path) { //Метод добавления сотрудника в базу
        File file = new File(path);
        try (FileInputStream stream = new FileInputStream(file);) {
            XSSFWorkbook wb = new XSSFWorkbook(stream);
            XSSFSheet sheet = wb.getSheetAt(0);
            Iterator<Row> itr = sheet.iterator();
            Worker user = null;
            while (itr.hasNext()) {
                Row row = itr.next();
                int lenRow = 0;
                if (row.getRowNum() % 2 == 0) {
                    user = null;
                    lenRow = 18;
                } else {
                    lenRow = 19;
                }
                Iterator<Cell> cellIterator = row.cellIterator();
                for (int i = 0; i < lenRow; i++) {
                    Cell cell = cellIterator.next();
                    String value = cell.getStringCellValue();
                    try {
                        Integer.parseInt(value);
                        user = new Worker(cellIterator.next().getStringCellValue().replace(" ", "")
                                .replace("\n", " "), true, 1500.00,
                                200.00, 3000.00);
                        Worker.addUser(user);
                    } catch (NumberFormatException e) {
                        if (!value.isBlank()) {
                            if (value.contains(":") || value.contains("--")) {
                                if (value.contains("--\n--\n--")) user.setWorkTimes("0");
                                else {
                                    String[] tempTime = value.split("\n");
                                    for (int j = 0; j < tempTime.length; j++) {
                                        tempTime[j] = tempTime[j].replace(":", ".")
                                                .replace("--", "0");
                                    }
                                    if (Float.valueOf(tempTime[0]) > Float.valueOf(tempTime[1])) {
                                        //Если сотрудник пришел сегодня а ушел на следующий день
                                        if (tempTime[1].equals("0")) {
                                            continue;
                                        } else {
                                            int houer1 = Integer.parseInt(tempTime[0].substring(0, tempTime[0]
                                                    .indexOf(".")));
                                            int minute1 = Integer.parseInt(tempTime[0].substring(tempTime[0]
                                                    .indexOf(".") + 1, tempTime[0].length()));
                                            int houer2 = Integer.parseInt(tempTime[1].substring(0, tempTime[1]
                                                    .indexOf(".")));
                                            int minute2 = Integer.parseInt(tempTime[1].substring(tempTime[1]
                                                    .indexOf(".") + 1, tempTime[1].length()));
                                            LocalTime time1 = LocalTime.of(houer1, minute1);
                                            LocalTime time2 = LocalTime.of(houer2, minute2);
                                            LocalTime total = time1.minusHours(time2.getHour())
                                                    .minusMinutes(time2.getMinute());
                                            user.setWorkTimes(String.valueOf(total).replace(":", ".")
                                                    .replace("00", "0"));
                                        }
                                    } else {
                                        user.setWorkTimes(tempTime[2]);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Worker.users;
    }

    //    Получения дня недели по заданному числу
    public static int getDayNumber(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        return day.getValue();
    }

    //Расчет времени переработки (когда больше 9 часов)
    public static LocalDateTime getElaborTime(String time, String dayWorkTime) {
        int minuteWorkTime;
        int minuteDayWorkTime;
        int houerWorkTime = Integer.parseInt(time.substring(0, (time.indexOf("."))));
        int houerDayWorkTime = Integer.parseInt(dayWorkTime.substring(0, (dayWorkTime.indexOf("."))));
        if (time.indexOf(time.indexOf(".") + 1) == 0) {
            minuteWorkTime = Integer.parseInt(time.substring(time.indexOf(".") + 2, time.length()));
        } else {
            minuteWorkTime = Integer.parseInt(time.substring(time.indexOf(".") + 1, time.length()));
        }
        if (time.indexOf(time.indexOf(".") + 1) == 0) {
            minuteDayWorkTime = Integer.parseInt(dayWorkTime.substring(dayWorkTime.indexOf(".") + 2, dayWorkTime.length()));
        } else {
            minuteDayWorkTime = Integer.parseInt(dayWorkTime.substring(dayWorkTime.indexOf(".") + 1, dayWorkTime.length()));
        }
        LocalDateTime WorkTime = LocalDateTime.of(1, 1, 1, houerWorkTime, minuteWorkTime);
        LocalDateTime DayWorkTime = LocalDateTime.of(1, 1, 1, houerDayWorkTime, minuteDayWorkTime);
        LocalDateTime elaborTime = WorkTime.minusHours(DayWorkTime.getHour()).minusMinutes(DayWorkTime.getMinute());
        return elaborTime;
    }

    //    Расчет отработанного времени когда отработано меньше 9 часов
    public static double getTimeNotFullWorkDay(String time) {
        int houer = Integer.parseInt((time.substring(0, time.indexOf("."))));
        int minute = Integer.parseInt(time.substring(time.indexOf(".") + 1));
        LocalTime workTime = LocalTime.of(houer, minute);
        workTime = workTime.minusHours(1);
        String result = String.format(workTime.getHour() + "." + workTime.getMinute());
        return Double.parseDouble(result);

    }
}
