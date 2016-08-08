package com.yuyue.cu.core.bo;

import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelExporter {
	private ExcelExportConfig config;
	
	public ExcelExporter(ExcelExportConfig config) {
		super();
		this.config = config;
	}

	public ExcelExportConfig getConfig() {
		return config;
	}

	public void setConfig(ExcelExportConfig config) {
		this.config = config;
	}

	@SuppressWarnings("rawtypes")
	public Workbook export(List<String[]> allSn){
	    Workbook wb = new XSSFWorkbook();
	    CreationHelper createHelper = wb.getCreationHelper();
	    Sheet sheet = wb.createSheet(config.getSheetName());
	    Iterator it = allSn.iterator();
	    short r = 0;
	    while (it.hasNext()) {
	    	String[] rowdata = (String[]) it.next();
		    Row row = sheet.createRow(r++);
		    int c = 0;
		    for (String sn : rowdata) {
		    	Cell cell = row.createCell(c++);
		    	cell.setCellType(Cell.CELL_TYPE_STRING);
			    cell.setCellValue(createHelper.createRichTextString(sn));
		    }
	    }
	    return wb;
	}
}
