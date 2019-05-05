package utils;

import javafx.collections.ObservableList;
import model.Product;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;

public class UtilsExcel {
    public static HSSFWorkbook readWorkbook(String filename) {
        try {
            POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(filename));
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            return wb;
        }
        catch (Exception e) {
            return null;
        }
    }

    public static String getCellValue(Cell cell){
        if (cell != null) {
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_STRING:
                    return cell.getStringCellValue();
                case Cell.CELL_TYPE_NUMERIC:
                    return cell.getNumericCellValue() + "";
                case Cell.CELL_TYPE_FORMULA:
                    return cell.getNumericCellValue() + "";
                default:
                    return "";
            }
        }else {
            return "";
        }
    }

    public static void getProductsFromFile(File file, ArrayList<Product> products, HashSet<String> set) throws IOException {
        FileInputStream inputStream = new FileInputStream(file);
        // получаем экземпляр XSSFWorkbook для обработки xlsx файла
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);

        // выбираем первый лист для обработки
        // нумерация начинается из 0
        HSSFSheet sheet = workbook.getSheetAt(0);

        // получаем Iterator по всем строкам в листе
        //Iterator<Row> rowIterator = sheet.iterator();
        int lastRowNum = sheet.getLastRowNum();
        int firstRowNum = sheet.getFirstRowNum();
        for (int i = firstRowNum +2; i < lastRowNum ; i++) {
            HSSFRow row = sheet.getRow(i);
            model.Product product = new model.Product(getCellValue(row.getCell(2)), getCellValue(row.getCell(8)),
                    getCellValue(row.getCell(1)), getCellValue(row.getCell(3)),
                    getCellValue(row.getCell(4)), getCellValue(row.getCell(5)),
                    getCellValue(row.getCell(6)), getCellValue(row.getCell(7)));
            if (!product.getCode().equals(0)) {
                set.add(product.getSupplierName());
                products.add(product);
            }
        }
    }

    public static void createFile(ObservableList<Product> products, String supplier, String fileName) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("К заказу");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Код");
        header.createCell(1).setCellValue("Наименование");
        header.createCell(2).setCellValue("Ед. изм.");
        header.createCell(3).setCellValue("Расхос(Склад куда)");
        header.createCell(4).setCellValue("Остаток(Склад куда)");
        header.createCell(5).setCellValue("К заказу");
        header.createCell(6).setCellValue("Расхос(Склад откуда)");
        header.createCell(7).setCellValue("Остаток(Склад откуда)");
        header.createCell(8).setCellValue("Поставщик");

        int rowCount = 1;

        for (Product product: products) {
            if ((supplier.equals("Все поставщики") || product.getSupplierName().equals(supplier)) &&
                    product.getActive() && product.getOrder() != 0) {
                Row row = sheet.createRow(rowCount++);
                row.createCell(0).setCellValue(product.getCode());
                row.createCell(1).setCellValue(product.getName());
                row.createCell(2).setCellValue(product.getUom());
                row.createCell(3).setCellValue(product.getTurnOver1());
                row.createCell(4).setCellValue(product.getStock1());
                row.createCell(5).setCellValue(product.getOrder());
                row.createCell(6).setCellValue(product.getTurnOver2());
                row.createCell(7).setCellValue(product.getStock2());
                row.createCell(8).setCellValue(product.getSupplierName());
            }

        }
        FileOutputStream outputStream;
        if (!Files.exists(Paths.get(fileName + ".xls"))){
            outputStream = new FileOutputStream(fileName + ".xls");
        }else {
            int k = 0;
            while (Files.exists(Paths.get(fileName + "(" + k + ")" + ".xls"))){
                k++;
            }
            outputStream = new FileOutputStream(fileName + "(" + k + ")" + ".xls");
        }

        workbook.write(outputStream);
        outputStream.close();
    }
}
