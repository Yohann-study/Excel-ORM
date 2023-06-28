package org.yohann.excel.entity;

import com.alibaba.excel.annotation.ExcelIgnore;

/**
 * This class is an abstract base class for defining data objects that represent rows in an Excel file.
 * It provides a rowNum property to store the row number of the data object in the Excel file.
 */
public abstract class Excel {

    /**
     * The row number of the data object in the Excel file.
     */
    @ExcelIgnore
    private Integer rowNum;

    /**
     * Returns the row number of the data object in the Excel file.
     *
     * @return the row number of the data object
     */
    public Integer getRowNum() {
        return rowNum;
    }

    /**
     * Sets the row number of the data object in the Excel file.
     *
     * @param rowNum the row number of the data object
     */
    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

}
