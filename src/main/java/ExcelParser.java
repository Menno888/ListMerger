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

    private SongList songList = new SongList();
    private final int MAX_EXPECTED_COLUMNS = 1000;
    private final int HEADER_ROW_NUM = 0;
    private final int COLUMN_START_NUM = 0;

    public SongList parseExcel(String inFile) {
        ArrayList<String> listAbbreviations = new ArrayList<>(MAX_EXPECTED_COLUMNS);

        try {
            if (!inFile.endsWith(".xlsx")) {
                inFile = inFile + ".xlsx";
            }
            OPCPackage opcPackage = OPCPackage.open(new File(inFile).getAbsolutePath());
            XSSFWorkbook myWorkBook = new XSSFWorkbook(opcPackage);
            XSSFSheet sheet = myWorkBook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();
            for (int rowNum = 0; rowNum < sheet.getPhysicalNumberOfRows(); rowNum++) {
                Row row = sheet.getRow(rowNum);
                Record record = new Record();
                for (int cellNum = 0; cellNum < sheet.getRow(0).getPhysicalNumberOfCells(); cellNum++) {
                    Cell cell = row.getCell(cellNum);
                    if (rowNum == HEADER_ROW_NUM) {
                        String headerValue = cell.getStringCellValue();
                        if (cellNum >= COLUMN_START_NUM + 2) {
                            String[] headerValues = headerValue.split("\\(");
                            headerValue = headerValues[headerValues.length - 1];
                            headerValue = headerValue.substring(0, headerValue.length() - 1);
                        }
                        listAbbreviations.add(headerValue);
                    }
                    else {
                        String column = listAbbreviations.get(cellNum);
                        if ("Artiest".equals(column)) {
                            record.setArtiest(formatter.formatCellValue(cell).replace("&", "&amp;"));
                        }
                        else if ("Nummer".equals(column)) {
                            record.setNummer(formatter.formatCellValue(cell).replace("&", "&amp;"));
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
                    System.out.println("Added: " + record.showSong());
                }
            }

        } catch (InvalidFormatException | IOException | IllegalStateException e) {
            System.out.println("File not found, try again");
        }

        return songList;
    }
}
