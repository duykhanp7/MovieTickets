package com.example.movietickets.model;

import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class WriteFileExcel {


    XSSFWorkbook workbook = new XSSFWorkbook();

    // spreadsheet object

    // creating a row object
    XSSFRow row;

    // This data needs to be written (Object[])
    List<Customer> customers = new ArrayList<>();
    List<Bill> bills = new ArrayList<>();
    List<Staff> staffs = new ArrayList<>();

    int rowId = 10;
    FileOutputStream out;

    //XUẤT FILE DANH SÁCH THÔNG TIN KHÁCH HÀNG CÙNG VỚI HÓA ĐƠN KHÁCH THÀNH RA FILE EXCEL
    public void createFileExcelCustomerInformation(String path,List<Customer> customers,List<Bill> bills ){
        //TẠO SHEET THÔNG TIN KHÁCH HÀNG
        XSSFSheet spreadsheetCustomerInformation
                = workbook.createSheet("Customer's Information");
        //TẠO SHEET HÓA ĐƠN KHÁCH HÀNG
        XSSFSheet spreadsheetCustomerBill
                = workbook.createSheet("Customer's Bill");
        this.customers = customers;
        this.bills = bills;
        try {
            out = new FileOutputStream(
                    new File(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //FONT FOR HEADER
        Font fontTitle = spreadsheetCustomerInformation.getWorkbook().createFont();
        fontTitle.setFontName("Arial");
        fontTitle.setFontHeightInPoints((short) 22);
        fontTitle.setBold(true);
        //RBG
        byte[] rgb = new byte[3];
        rgb[0] = (byte) 250; // red
        rgb[1] = (byte) 208; // green
        rgb[2] = (byte) 92; // blue
        XSSFColor myColor = new XSSFColor(rgb);
        //CELL STYLE FOR HEADER
        XSSFCellStyle cellStyleTitle = spreadsheetCustomerInformation.getWorkbook().createCellStyle();
        cellStyleTitle.setAlignment(HorizontalAlignment.CENTER);
        cellStyleTitle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyleTitle.setFont(fontTitle);
        cellStyleTitle.setFillForegroundColor(myColor);
        cellStyleTitle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        row = spreadsheetCustomerInformation.createRow(6);
        Cell title = row.createCell(7);
        title.setCellStyle(cellStyleTitle);
        title.setCellValue("Customer's Information");
        spreadsheetCustomerInformation.addMergedRegion(CellRangeAddress.valueOf("H7:I8"));

        // writing the data into the sheets...
        spreadsheetCustomerInformation.setColumnWidth(5,25 * 256);
        spreadsheetCustomerInformation.setColumnWidth(6,25 * 256);
        spreadsheetCustomerInformation.setColumnWidth(7,25 * 256);
        spreadsheetCustomerInformation.setColumnWidth(8,25 * 256);
        spreadsheetCustomerInformation.setColumnWidth(9,25 * 256);
        spreadsheetCustomerInformation.setColumnWidth(10,25 * 256);

        //
        Font fontHeader = spreadsheetCustomerInformation.getWorkbook().createFont();
        fontHeader.setFontName("Arial");
        fontHeader.setFontHeightInPoints((short) 11);
        fontHeader.setBold(true);
        //
        XSSFCellStyle cellStyleHeader = spreadsheetCustomerInformation.getWorkbook().createCellStyle();
        cellStyleHeader.setAlignment(HorizontalAlignment.CENTER);
        cellStyleHeader.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyleHeader.setFont(fontHeader);

        cellStyleHeader.setWrapText(true);
        //BACKGROUND COLOR CELL HEADER
        //250, 208, 92
        rgb[0] = (byte) 250; // red
        rgb[1] = (byte) 208; // green
        rgb[2] = (byte) 92; // blue
        myColor.setRGB(rgb);
        //
        cellStyleHeader.setFillForegroundColor(myColor);
        cellStyleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyleHeader.setBorderTop(BorderStyle.THIN);
        cellStyleHeader.setBorderBottom(BorderStyle.THIN);
        cellStyleHeader.setBorderLeft(BorderStyle.THIN);
        cellStyleHeader.setBorderRight(BorderStyle.THIN);

        int cellHeaderID = 5;

        String[] headerText = new String[]{"ID","Full Name","Phone Number","Sex","Type Customer","Total Points"};

        row = spreadsheetCustomerInformation.createRow(rowId++);
        row.setHeight((short) (25*25));

        for (String i : headerText){
            Cell cell = row.createCell(cellHeaderID++);
            cell.setCellStyle(cellStyleHeader);
            cell.setCellValue(i);
        }
        //STYLE CELL
        //219, 215, 162
        Font fontCell = spreadsheetCustomerInformation.getWorkbook().createFont();
        fontCell.setFontName("Arial");
        fontCell.setFontHeightInPoints((short) 12);
        fontCell.setBold(false);
        //
        XSSFCellStyle cellStyleCell = spreadsheetCustomerInformation.getWorkbook().createCellStyle();
        cellStyleCell.setAlignment(HorizontalAlignment.CENTER);
        cellStyleCell.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyleCell.setFont(fontCell);
        cellStyleCell.setWrapText(true);
        //
        rgb[0] = (byte) 219; // red
        rgb[1] = (byte) 215; // green
        rgb[2] = (byte) 162;
        myColor.setRGB(rgb);
        cellStyleCell.setFillForegroundColor(myColor);
        cellStyleCell.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyleCell.setBorderTop(BorderStyle.THIN);
        cellStyleCell.setBorderBottom(BorderStyle.THIN);
        cellStyleCell.setBorderLeft(BorderStyle.THIN);
        cellStyleCell.setBorderRight(BorderStyle.THIN);



        for (Customer customer : customers) {

            row = spreadsheetCustomerInformation.createRow(rowId++);
            row.setHeight((short) (25*25));
            int cellId = 5;
            //id,name,phoneNumber,sex,totalPoint
            String[] strings = new String[]{customer.getId(),customer.getName(),customer.getPhoneNumber(),customer.getSex()
                    ,customer.getTypeCustomer(), String.valueOf(customer.getTotalPoint())};

            for (String i : strings){
                Cell cell = row.createCell(cellId++);
                cell.setCellStyle(cellStyleCell);
                cell.setCellValue(i);
            }

        }

        // .xlsx is the format for Excel Sheets...
        // writing the workbook into the file...

        try {
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        createAndSaveToSheetBillOfCustomer(path,spreadsheetCustomerBill,bills);
    }

    //XUẤT FILE HÓA ĐƠN VÀO FILE EXCEL
    public void createAndSaveToSheetBillOfCustomer(String path,XSSFSheet spreadsheetCustomerBill,List<Bill> bills){
        rowId = 10;

        try {
            out = new FileOutputStream(
                    new File(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //FONT
        Font fontTitle = spreadsheetCustomerBill.getWorkbook().createFont();
        fontTitle.setFontName("Arial");
        fontTitle.setFontHeightInPoints((short) 22);
        fontTitle.setBold(true);
        //RBG
        byte[] rgb = new byte[3];
        rgb[0] = (byte) 250; // red
        rgb[1] = (byte) 208; // green
        rgb[2] = (byte) 92; // blue
        XSSFColor myColor = new XSSFColor(rgb);
        //CELL STYLE
        XSSFCellStyle cellStyleTitle = spreadsheetCustomerBill.getWorkbook().createCellStyle();
        cellStyleTitle.setAlignment(HorizontalAlignment.CENTER);
        cellStyleTitle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyleTitle.setFont(fontTitle);
        cellStyleTitle.setFillForegroundColor(myColor);
        cellStyleTitle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        row = spreadsheetCustomerBill.createRow(6);
        Cell title = row.createCell(7);
        title.setCellStyle(cellStyleTitle);
        title.setCellValue("Customer's Bill");
        spreadsheetCustomerBill.addMergedRegion(CellRangeAddress.valueOf("H7:J8"));

        // writing the data into the sheets...
        spreadsheetCustomerBill.setColumnWidth(5,25 * 256);
        spreadsheetCustomerBill.setColumnWidth(6,25 * 256);
        spreadsheetCustomerBill.setColumnWidth(7,25 * 256);
        spreadsheetCustomerBill.setColumnWidth(8,25 * 256);
        spreadsheetCustomerBill.setColumnWidth(9,25 * 256);
        spreadsheetCustomerBill.setColumnWidth(10,25 * 256);
        spreadsheetCustomerBill.setColumnWidth(11,25 * 256);
        spreadsheetCustomerBill.setColumnWidth(12,25 * 256);

        //
        Font fontHeader = spreadsheetCustomerBill.getWorkbook().createFont();
        fontHeader.setFontName("Arial");
        fontHeader.setFontHeightInPoints((short) 11);
        fontHeader.setBold(true);
        //
        XSSFCellStyle cellStyleHeader = spreadsheetCustomerBill.getWorkbook().createCellStyle();
        cellStyleHeader.setAlignment(HorizontalAlignment.CENTER);
        cellStyleHeader.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyleHeader.setFont(fontHeader);

        cellStyleHeader.setWrapText(true);
        //BACKGROUND COLOR CELL HEADER
        //250, 208, 92
        rgb[0] = (byte) 250; // red
        rgb[1] = (byte) 208; // green
        rgb[2] = (byte) 92; // blue
        myColor.setRGB(rgb);
        //
        cellStyleHeader.setFillForegroundColor(myColor);
        cellStyleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyleHeader.setBorderTop(BorderStyle.THIN);
        cellStyleHeader.setBorderBottom(BorderStyle.THIN);
        cellStyleHeader.setBorderLeft(BorderStyle.THIN);
        cellStyleHeader.setBorderRight(BorderStyle.THIN);

        int cellHeaderID = 5;

        String[] headerText = new String[]{"Customer ID","Movie","Cinema","Time Slot","Seats","Date Bought","Price","Discount"};

        row = spreadsheetCustomerBill.createRow(rowId++);
        row.setHeight((short) (25*25));

        for (String i : headerText){
            Cell cell = row.createCell(cellHeaderID++);
            cell.setCellStyle(cellStyleHeader);
            cell.setCellValue(i);
        }
        //STYLE CELL
        //219, 215, 162
        Font fontCell = spreadsheetCustomerBill.getWorkbook().createFont();
        fontCell.setFontName("Arial");
        fontCell.setFontHeightInPoints((short) 12);
        fontCell.setBold(false);
        //
        XSSFCellStyle cellStyleCell = spreadsheetCustomerBill.getWorkbook().createCellStyle();
        cellStyleCell.setAlignment(HorizontalAlignment.CENTER);
        cellStyleCell.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyleCell.setFont(fontCell);
        cellStyleCell.setWrapText(true);
        //
        rgb[0] = (byte) 219; // red
        rgb[1] = (byte) 215; // green
        rgb[2] = (byte) 162;
        myColor.setRGB(rgb);
        cellStyleCell.setFillForegroundColor(myColor);
        cellStyleCell.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyleCell.setBorderTop(BorderStyle.THIN);
        cellStyleCell.setBorderBottom(BorderStyle.THIN);
        cellStyleCell.setBorderLeft(BorderStyle.THIN);
        cellStyleCell.setBorderRight(BorderStyle.THIN);

        //idCustomer,movieName,movieName,movieName,seatsText,dateBought,totalPrice,discount
        for (Bill bill : bills) {

            row = spreadsheetCustomerBill.createRow(rowId++);
            row.setHeight((short) (25*25));
            int cellId = 5;
            //"Customer ID","Movie","Cinema","Time Slot","Seats","Date Bought","Price","Discount"
            String[] strings = new String[]{bill.getIdCustomer(),bill.getMovieName(),bill.getCinemaName(),bill.getTimeSlot()
                    ,bill.getSeatsText(), bill.getDateBought(), String.valueOf(bill.getTotalPrice()), String.valueOf(bill.getDiscount())};

            for (String i : strings){
                Cell cell = row.createCell(cellId++);
                cell.setCellStyle(cellStyleCell);
                cell.setCellValue(i);
            }

        }

        // .xlsx is the format for Excel Sheets...
        // writing the workbook into the file...

        try {
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //XUẤT THÔNG TIN NHÂN VIÊN RA FILE EXCEL
    public void writeStaffInformation(String path, List<Staff> staffs){
        XSSFSheet spreadsheetStaffInformation
                = workbook.createSheet("Staff's Information");
        rowId = 10;
        this.staffs = staffs;
        try {
            out = new FileOutputStream(
                    new File(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //FONT
        Font fontTitle = spreadsheetStaffInformation.getWorkbook().createFont();
        fontTitle.setFontName("Arial");
        fontTitle.setFontHeightInPoints((short) 22);
        fontTitle.setBold(true);
        //RBG
        byte[] rgb = new byte[3];
        rgb[0] = (byte) 250; // red
        rgb[1] = (byte) 208; // green
        rgb[2] = (byte) 92; // blue
        XSSFColor myColor = new XSSFColor(rgb);
        //CELL STYLE
        XSSFCellStyle cellStyleTitle = spreadsheetStaffInformation.getWorkbook().createCellStyle();
        cellStyleTitle.setAlignment(HorizontalAlignment.CENTER);
        cellStyleTitle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyleTitle.setFont(fontTitle);
        cellStyleTitle.setFillForegroundColor(myColor);
        cellStyleTitle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        spreadsheetStaffInformation.addMergedRegion(CellRangeAddress.valueOf("G7:I8"));

        row = spreadsheetStaffInformation.createRow(6);
        Cell title = row.createCell(6);
        title.setCellStyle(cellStyleTitle);
        title.setCellValue("Staff's Information");

        // writing the data into the sheets...
        spreadsheetStaffInformation.setColumnWidth(5,25 * 256);
        spreadsheetStaffInformation.setColumnWidth(6,25 * 256);
        spreadsheetStaffInformation.setColumnWidth(7,25 * 256);
        spreadsheetStaffInformation.setColumnWidth(8,27 * 256);
        spreadsheetStaffInformation.setColumnWidth(9,25 * 256);

        //
        Font fontHeader = spreadsheetStaffInformation.getWorkbook().createFont();
        fontHeader.setFontName("Arial");
        fontHeader.setFontHeightInPoints((short) 11);
        fontHeader.setBold(true);
        //
        XSSFCellStyle cellStyleHeader = spreadsheetStaffInformation.getWorkbook().createCellStyle();
        cellStyleHeader.setAlignment(HorizontalAlignment.CENTER);
        cellStyleHeader.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyleHeader.setFont(fontHeader);

        cellStyleHeader.setWrapText(true);
        //BACKGROUND COLOR CELL HEADER
        //250, 208, 92
        rgb[0] = (byte) 250; // red
        rgb[1] = (byte) 208; // green
        rgb[2] = (byte) 92; // blue
        myColor.setRGB(rgb);
        //
        cellStyleHeader.setFillForegroundColor(myColor);
        cellStyleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyleHeader.setBorderTop(BorderStyle.THIN);
        cellStyleHeader.setBorderBottom(BorderStyle.THIN);
        cellStyleHeader.setBorderLeft(BorderStyle.THIN);
        cellStyleHeader.setBorderRight(BorderStyle.THIN);

        int cellHeaderID = 5;

        String[] headerText = new String[]{"Staff ID","Full Name","Phone Number","Email","Sex"};

        row = spreadsheetStaffInformation.createRow(rowId++);
        row.setHeight((short) (25*25));

        for (String i : headerText){
            Cell cell = row.createCell(cellHeaderID++);
            cell.setCellStyle(cellStyleHeader);
            cell.setCellValue(i);
        }
        //STYLE CELL
        //219, 215, 162
        Font fontCell = spreadsheetStaffInformation.getWorkbook().createFont();
        fontCell.setFontName("Arial");
        fontCell.setFontHeightInPoints((short) 12);
        fontCell.setBold(false);
        //
        XSSFCellStyle cellStyleCell = spreadsheetStaffInformation.getWorkbook().createCellStyle();
        cellStyleCell.setAlignment(HorizontalAlignment.CENTER);
        cellStyleCell.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyleCell.setFont(fontCell);
        cellStyleCell.setWrapText(true);
        //
        rgb[0] = (byte) 219; // red
        rgb[1] = (byte) 215; // green
        rgb[2] = (byte) 162;
        myColor.setRGB(rgb);
        cellStyleCell.setFillForegroundColor(myColor);
        cellStyleCell.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyleCell.setBorderTop(BorderStyle.THIN);
        cellStyleCell.setBorderBottom(BorderStyle.THIN);
        cellStyleCell.setBorderLeft(BorderStyle.THIN);
        cellStyleCell.setBorderRight(BorderStyle.THIN);



        for (Staff staff : staffs) {

            row = spreadsheetStaffInformation.createRow(rowId++);
            row.setHeight((short) (25*25));
            int cellId = 5;
            String[] strings = new String[]{staff.getId(),staff.getName(),staff.getPhoneNumber(),staff.getEmail(),staff.getSex()};

            for (String i : strings){
                Cell cell = row.createCell(cellId++);
                cell.setCellStyle(cellStyleCell);
                cell.setCellValue(i);
            }

        }

        // .xlsx is the format for Excel Sheets...
        // writing the workbook into the file...

        try {
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
