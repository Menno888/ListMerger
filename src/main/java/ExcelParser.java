import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ExcelParser {

    private ArrayList<Record> songList = new ArrayList<>();

    public void parseExcel(String inFile, String outFile) {
        String[] listAbbreviations = new String[1000];

        try {
            if (!inFile.endsWith(".xlsx")) {
                inFile = inFile + ".xlsx";
            }
            File myFile = new File(inFile);
            OPCPackage opcPackage = OPCPackage.open(myFile.getAbsolutePath());
            XSSFWorkbook myWorkBook = new XSSFWorkbook(opcPackage);
            XSSFSheet sheet = myWorkBook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();
            for (int r = 0; r < sheet.getPhysicalNumberOfRows(); r++) {
                Row row = sheet.getRow(r);
                Record record = new Record();
                for (int c = 0; c < sheet.getRow(0).getPhysicalNumberOfCells(); c++) {
                    Cell cell = row.getCell(c);
                    if (r == 0) {
                        String headerValue = cell.getStringCellValue();
                        if (c > 1) {
                            String[] headerValues = headerValue.split("\\(");
                            headerValue = headerValues[headerValues.length - 1];
                            headerValue = headerValue.substring(0, headerValue.length() - 1);
                        }
                        listAbbreviations[c] = headerValue;
                    }
                    else {
                        String column = listAbbreviations[c];
                        if ("Artiest".equals(column)) {
                            record.setArtiest(formatter.formatCellValue(cell).replace("&", "&amp;"));
                        }
                        else if ("Nummer".equals(column)) {
                            record.setNummer(formatter.formatCellValue(cell).replace("&", "&amp;"));
                        }
                        else {
                            if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                if ((int) cell.getNumericCellValue() != 0) {
                                    record.addPositionToMap(listAbbreviations[c], (int) cell.getNumericCellValue());
                                }
                            }
                        }
                    }
                }
                if (r != 0) {
                    songList.add(record);
                    System.out.println(record.showSong());
                }
            }
            Writer.output(songList, outFile, "y", "y", "n");

        } catch (InvalidFormatException | IOException | IllegalStateException e) {
            System.out.println("File not found, try again");
        }
    }
}
