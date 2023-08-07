package interfaces;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import workers.Worker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ExcelSender implements SendTo {
    @Override
    public void sendTo(ArrayList<Worker> workers, String path) throws IOException {
        int rowIndex = 1;
        Row row;
        File file = new File(path);
        String[] nameCol = {"№", "ФИО", "Отработано дней", "Часов переработки", "Зарплата за дни",
                "Зарплата за переработку", "Зарплата за месяц", "Аванс", "Итого на руки"};
        try (FileOutputStream stream = new FileOutputStream(file)) {
            Workbook book = new HSSFWorkbook();
            Sheet sheet = book.createSheet("Зарплата за месяц");
            row = sheet.createRow(0);
            for (int i = 0; i < nameCol.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(nameCol[i]);
            }
            for (Worker worker : workers) {
                int colIndex = 0;
                row = sheet.createRow(rowIndex);
                Cell cell = row.createCell(colIndex);
                cell.setCellValue(rowIndex);
                rowIndex++;
                colIndex++;
                cell = row.createCell(colIndex);
                cell.setCellValue(worker.getName());
                colIndex++;
                for (int i = 0; i < worker.getSize(); i++) {
                    cell = row.createCell(colIndex);
                    cell.setCellValue((Double.parseDouble((String) worker.getMonthStat().get(i))));
                    colIndex++;
                }
                cell = row.createCell(colIndex);
                cell.setCellValue(0);
                colIndex++;
                cell = row.createCell(colIndex);
                cell.setCellValue("=G" + rowIndex + "-" + "H" + rowIndex);
                book.write(new FileOutputStream(file));
                book.write(stream);

            }
        }
        ArrayList indexs = new ArrayList<>();
        for (int i = 0; i < path.length(); i++) {
            if (path.charAt(i) == '/') {
                indexs.add(i);
            }
        }
        int index = (int) indexs.get(indexs.size() - 1);
        System.out.println("Данные сохранены в файл - " + path.substring(index+1));
    }
}

