package parser;

import dto.Song;
import dto.SongList;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;

public class ExcelParser {

    private final SongList songList = new SongList();
    private static final int HEADER_ROW_NUM = 0;
    private static final int COLUMN_START_NUM = 0;
    private static final String INFO_COLUMN_MARKER = "ADD-";

    private static final String SEPARATOR_CHARACTER_AMPERSAND = "&";
    private static final String SEPARATOR_CHARACTER_AMPERSAND_XML_SAFE = "&amp;";

    private XSSFSheet sheet;

    public SongList parseExcel(String inFile) {
        final ArrayList<String> listAbbreviations = new ArrayList<>();
        InputStream inputStream;

        try {
            if (!inFile.endsWith(".xlsx")) {
                inFile += ".xlsx";
            }
            final File excelFile = new File(inFile);
            inputStream = new FileInputStream(excelFile);
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't find file: " + inFile + ", returning empty list");
            return new SongList();
        }

        try (XSSFWorkbook myWorkBook = new XSSFWorkbook(inputStream)) {
            sheet = myWorkBook.getSheetAt(0);
            final DataFormatter formatter = new DataFormatter();

            final int numOfRows = getActualNumberOfRows();
            final int numOfCols = getActualNumberOfColumns();

            for (int rowNum = 0; rowNum < numOfCols; rowNum++) {
                final Row row = sheet.getRow(rowNum);
                final Song song = new Song();
                for (int cellNum = 0; cellNum < numOfRows; cellNum++) {
                    final Cell cell = row.getCell(cellNum);
                    if (rowNum == HEADER_ROW_NUM) {
                        final String headerValue = cell.getStringCellValue();
                        listAbbreviations.add(getListAbbreviationIfParenthesesElseFullName(headerValue));
                    }
                    else {
                        if (cellNum == COLUMN_START_NUM) {
                            song.setArtist(formatter.formatCellValue(cell).replace(SEPARATOR_CHARACTER_AMPERSAND, SEPARATOR_CHARACTER_AMPERSAND_XML_SAFE));
                        }
                        else if (cellNum == COLUMN_START_NUM + 1) {
                            song.setTitle(formatter.formatCellValue(cell).replace(SEPARATOR_CHARACTER_AMPERSAND, SEPARATOR_CHARACTER_AMPERSAND_XML_SAFE));
                        }
                        else {
                            String abbreviation = listAbbreviations.get(cellNum);
                            if (abbreviation.startsWith(INFO_COLUMN_MARKER)) {
                                addInfoDataToAdditionalInformationMap(cell, song, abbreviation);
                            } else {
                                addNumericDataToPositionMap(cell, song, abbreviation);
                            }
                        }
                    }
                }
                if (rowNum != HEADER_ROW_NUM) {
                    songList.add(song);
                }
            }

            System.out.println("Successfully parsed " + inFile);

        } catch (IOException | IllegalStateException e) {
            System.out.println("File not found, try again");
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return songList;
    }

    private String getListAbbreviationIfParenthesesElseFullName(final String headerValue) {
        if (headerValue.contains("(") && headerValue.contains(")")) {
            try {
                final String[] headerValues = headerValue.split("\\(");
                final String partAfterParentheses = headerValues[headerValues.length - 1];
                return partAfterParentheses.substring(0, partAfterParentheses.length() - 1);
            } catch (Exception e) {
                return headerValue;
            }
        } else {
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

    private boolean checkIfCellNonEmpty(final int row, final int col) {
        try {
            final Cell cell = sheet.getRow(row).getCell(col);
            return cell != null && cell.getCellType() != CellType.BLANK;
        } catch (final NullPointerException e) {
            return false;
        }
    }

    private void addNumericDataToPositionMap(final Cell cell, final Song song, final String abbreviation) {
        if (cell != null && cell.getCellType() == CellType.NUMERIC && (int) cell.getNumericCellValue() != 0) {
            song.addPositionToMap(abbreviation, (int) cell.getNumericCellValue());
        }
    }

    private void addInfoDataToAdditionalInformationMap(final Cell cell, final Song song, final String abbreviation) {
        if (cell != null) {
            if (cell.getCellType() == CellType.NUMERIC && (int) cell.getNumericCellValue() != 0) {
                song.addInfoToMap(abbreviation, cell.getNumericCellValue());
            } else if (cell.getCellType() == CellType.STRING && cell.getStringCellValue() != null) {
                song.addInfoToMap(abbreviation, cell.getStringCellValue());
            } else if (cell.getCellType() == CellType.FORMULA && cell.getCellFormula() != null) {
                song.addInfoToMap(abbreviation, cell.getCellFormula());
            }
        }
    }
}
