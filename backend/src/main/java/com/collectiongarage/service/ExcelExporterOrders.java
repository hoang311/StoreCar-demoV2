package com.collectiongarage.service;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.collectiongarage.repository.IReport;

public class ExcelExporterOrders {
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private Collection<IReport> reportOrders;

	public ExcelExporterOrders() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ExcelExporterOrders(Collection<IReport> reportOrders) {
		super();
		this.workbook = new XSSFWorkbook();
		this.reportOrders = reportOrders;
	}

	private void insertCell(Row row, int columnOrdinalNumber, Object value, CellStyle style) {
		sheet.autoSizeColumn(columnOrdinalNumber);

		Cell cell = row.createCell(columnOrdinalNumber);

		if (value instanceof Integer) {
			cell.setCellValue((Integer) value);
		} else if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		} else {
			cell.setCellValue(value.toString());
		}

		cell.setCellStyle(style);
	}

	private void writeHeader() {
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(16);

		CellStyle style = workbook.createCellStyle();
		style.setFont(font);

		sheet = workbook.createSheet("Report Orders");
		Row row = sheet.createRow(0);
		insertCell(row, 0, "Order Date", style);
		insertCell(row, 1, "Total Revenue", style);
	}

	private void writeBody() {
		XSSFFont font = workbook.createFont();
		font.setFontHeight(14);

		CellStyle style = workbook.createCellStyle();
		style.setFont(font);

		int rowCount = 1;
		for (IReport iReportOrders : reportOrders) {
			Row row = sheet.createRow(rowCount++);
			insertCell(row, 0, iReportOrders.getOrderDate(), style);
			insertCell(row, 1, iReportOrders.getTotalRevenue(), style);
		}
	}

	public void exportExcel(HttpServletResponse response) throws IOException {
		writeHeader();
		writeBody();

		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();

		outputStream.close();
	}

}
