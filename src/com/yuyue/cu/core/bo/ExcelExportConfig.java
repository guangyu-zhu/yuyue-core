package com.yuyue.cu.core.bo;

public class ExcelExportConfig {
	private int cols = 1;
	private String sheetName;
	public ExcelExportConfig(int cols, String sheetName) {
		super();
		this.cols = cols;
		this.sheetName = sheetName;
	}
	public int getCols() {
		return cols;
	}
	public void setCols(int cols) {
		this.cols = cols;
	}
	public String getSheetName() {
		return sheetName;
	}
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
}
