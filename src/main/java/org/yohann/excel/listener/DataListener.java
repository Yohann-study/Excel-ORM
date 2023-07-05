package org.yohann.excel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelAnalysisStopException;
import com.alibaba.excel.read.metadata.holder.ReadRowHolder;
import org.yohann.excel.entity.Excel;
import org.yohann.excel.query.Criteria;

import java.util.ArrayList;
import java.util.List;

/**
 * Event listener for reading Excel data using EasyExcel library.
 *
 * @param <T> the type of Excel data to read
 */
public class DataListener<T extends Excel> extends AnalysisEventListener<T> {

    // A list to store the data read from the Excel file
    private final List<T> dataList = new ArrayList<T>();

    /**
     * Gets the list of data read from the Excel file.
     *
     * @return the list of data read from the Excel file
     */
    public List<T> getDataList() {
        return dataList;
    }

    // Criteria object used to filter the data
    private Criteria criteria;

    // Number of rows to skip
    private Integer skip;

    // Maximum number of rows to read
    private Integer limit;

    /**
     * Constructs a new DataListener with no filtering criteria.
     */
    public DataListener() {
    }

    /**
     * Constructs a new DataListener with the specified filtering criteria.
     *
     * @param criteria the Criteria object used to filter the data
     */
    public DataListener(Criteria criteria) {
        this.criteria = criteria;
        this.skip = criteria.getSkip();
        this.limit = criteria.getLimit();
    }

    /**
     * Invoked for every row of data read from the Excel file.
     *
     * @param data    the data read from the Excel file
     * @param context the analysis context
     */
    @Override
    public void invoke(T data, AnalysisContext context) {
        // Get the index of the current row
        ReadRowHolder rowHolder = context.readRowHolder();
        Integer rowIndex = rowHolder.getRowIndex();

        // Set the row number to the current row index + 1
        data.setRowNum(rowIndex + 1);

        if (criteria == null) {
            // If no criteria is specified, add all data to the list
            dataList.add(data);
        } else if (criteria.isMatch(data)) {
            // If the data matches the criteria, add it to the list
            if (skip > 0) {
                skip--;
            } else if (limit == 0) {
                dataList.add(data);
            } else {
                dataList.add(data);
                limit--;
                if (limit < 1) {
                    // Stop reading the Excel file if the maximum number of rows has been reached
                    throw new ExcelAnalysisStopException("reading completed");
                }
            }
        }
    }

    /**
     * Invoked after all data has been read from the Excel file.
     *
     * @param context the analysis context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }

}