import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ExcelParser {

    private final SongList songList = new SongList();
    private final int HEADER_ROW_NUM = 0;
    private final int COLUMN_START_NUM = 0;
    private XSSFSheet sheet;

    public SongList parseExcel(String inFile) {
        XSSFWorkbook myWorkBook = null;
        ArrayList<String> listAbbreviations = new ArrayList<>();

        try {
            if (!inFile.endsWith(".xlsx")) {
                inFile = inFile + ".xlsx";
            }
            File excelFile = new File(inFile);
            InputStream inputStream = new FileInputStream(excelFile);
            myWorkBook = new XSSFWorkbook(inputStream);
            sheet = myWorkBook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();

            int numOfRows = getActualNumberOfRows();
            int numOfCols = getActualNumberOfColumns();

            for (int rowNum = 0; rowNum < numOfCols; rowNum++) {
                Row row = sheet.getRow(rowNum);
                Record record = new Record();
                for (int cellNum = 0; cellNum < numOfRows; cellNum++) {
                    Cell cell = row.getCell(cellNum);
                    if (rowNum == HEADER_ROW_NUM) {
                        String headerValue = cell.getStringCellValue();
                        if (cellNum >= COLUMN_START_NUM + 2) {
                            headerValue = getListAbbreviation(headerValue);
                        }
                        listAbbreviations.add(headerValue);
                    }
                    else {
                        if (cellNum == COLUMN_START_NUM) {
                            record.setArtist(formatter.formatCellValue(cell).replace("&", "&amp;"));
                        }
                        else if (cellNum == COLUMN_START_NUM + 1) {
                            record.setTitle(formatter.formatCellValue(cell).replace("&", "&amp;"));
                        }
                        else {
                            if (cell != null) {
                                if (cell.getCellType() == CellType.NUMERIC) {
                                    if ((int) cell.getNumericCellValue() != 0) {
                                        record.addPositionToMap(listAbbreviations.get(cellNum), (int) cell.getNumericCellValue());
                                    }
                                }
                            }
                        }
                    }
                }
                if (rowNum != HEADER_ROW_NUM) {
                    songList.add(record);
                }
            }

            System.out.println("Successfully parsed " + inFile);

        } catch (IOException | IllegalStateException e) {
            System.out.println("File not found, try again");
        } finally {
            try {
                myWorkBook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return songList;
    }

    private String getListAbbreviation(String headerValue) {
        try {
            String[] headerValues = headerValue.split("\\(");
            String partAfterParentheses = headerValues[headerValues.length - 1];
            return partAfterParentheses.substring(0, partAfterParentheses.length() - 1);
        } catch (Exception e) {
            return headerValue;
        }
    }

    private int getActualNumberOfRows() {
        int cellNum = 0;
        while (checkIfCellNonEmpty(0, cellNum)) {
            cellNum++;
        }
        return cellNum;
    }

    private int getActualNumberOfColumns() {
        int cellNum = 0;
        while (checkIfCellNonEmpty(cellNum, 0)) {
            cellNum++;
        }
        return cellNum;
    }

    private boolean checkIfCellNonEmpty(int row, int col) {
        try {
            Cell cell = sheet.getRow(row).getCell(col);
            return cell != null && cell.getCellType() != CellType.BLANK;
        } catch (NullPointerException e) {
            return false;
        }
    }
}
