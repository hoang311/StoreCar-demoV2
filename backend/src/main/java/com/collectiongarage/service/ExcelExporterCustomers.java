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

public class ExcelExporterCustomers {
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private Collection<IReport> reportCustomers;

	public ExcelExporterCustomers(Collection<IReport> reportCustomers) {
		super();
		this.workbook = new XSSFWorkbook();
		this.reportCustomers = reportCustomers;
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

		sheet = workbook.createSheet("Report Customers");
		Row row = sheet.createRow(0);
		insertCell(row, 0, "Customer ID", style);
		insertCell(row, 1, "Last Name", style);
		insertCell(row, 2, "First Name", style);
		insertCell(row, 3, "Total Order", style);
		insertCell(row, 4, "Total Revenue", style);
	}

	private void writeBody() {
		XSSFFont font = workbook.createFont();
		font.setFontHeight(14);

		CellStyle style = workbook.createCellStyle();
		style.setFont(font);

		int rowCount = 1;
		for (IReport iReportCustomers : reportCustomers) {
			Row row = sheet.createRow(rowCount++);
			insertCell(row, 0, iReportCustomers.getCustomerId(), style);
			insertCell(row, 1, iReportCustomers.getLastName(), style);
			insertCell(row, 2, iReportCustomers.getFirstName(), style);
			insertCell(row, 3, iReportCustomers.getTotalOrder(), style);
			insertCell(row, 4, iReportCustomers.getTotalRevenue(), style);
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
