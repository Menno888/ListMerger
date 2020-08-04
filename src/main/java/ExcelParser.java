import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ExcelParser {

    private final SongList songList = new SongList();
    private final int HEADER_ROW_NUM = 0;
    private final int COLUMN_START_NUM = 0;
    private XSSFSheet sheet;

    public SongList parseExcel(String inFile) {
        ArrayList<String> listAbbreviations = new ArrayList<>();

        try {
            if (!inFile.endsWith(".xlsx")) {
                inFile = inFile + ".xlsx";
            }
            OPCPackage opcPackage = OPCPackage.open(new File(inFile).getAbsolutePath());
            XSSFWorkbook myWorkBook = new XSSFWorkbook(opcPackage);
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
            opcPackage.close();

        } catch (InvalidFormatException | IOException | IllegalStateException e) {
            System.out.println("File not found, try again");
        }

        return songList;
    }

    private String getListAbbreviation(String headerValue) {
        String originalValue = headerValue;
        try {
            String[] headerValues = headerValue.split("\\(");
            headerValue = headerValues[headerValue.length() - 1];
            headerValue = headerValue.substring(0, headerValue.length() - 1);
            return headerValue;
        } catch (Exception e) {
            return originalValue;
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
