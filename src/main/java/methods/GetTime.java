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
import java.time.LocalTime;
import java.util.*;

public class GetWorkTime {
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
                }else{
                    lenRow = 19;
                }
                Iterator<Cell> cellIterator = row.cellIterator();
                for (int i = 0; i < lenRow; i++) {
                    Cell cell = cellIterator.next();
                    String value = cell.getStringCellValue();
                    try {
                        Integer.parseInt(value);
                        user = new Worker(cellIterator.next().getStringCellValue().replace(" ","")
                                .replace("\n"," "), "рабочий", 1500.00,
                                200.00);
                        Worker.addUser(user);
                    } catch (NumberFormatException e) {
                        if (!value.isBlank()) {
                            if (value.contains(":") || value.contains("--")) {
                                if (value.contains("--\n--\n--")) continue;
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
}