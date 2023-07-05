package org.yohann.excel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.metadata.holder.ReadRowHolder;
import org.yohann.excel.entity.Excel;
import org.yohann.excel.query.Criteria;

import java.util.ArrayList;
import java.util.List;


/**
 * This class is a listener for parsing Excel files. It extends the AnalysisEventListener class and is parameterized
 * with a type T that extends the Excel class.
 *
 * @param <T> the type of data to be parsed from the Excel file
 */
public class DataListener<T extends Excel> extends AnalysisEventListener<T> {

    /**
     * The list that holds the parsed data objects.
     */
    private final List<T> dataList = new ArrayList<T>();

    /**
     * Returns the list of parsed data objects.
     *
     * @return the list of parsed data objects
     */
    public List<T> getDataList() {
        return dataList;
    }

    private Criteria criteria;

    public DataListener() {
    }

    public DataListener(Criteria criteria) {
        this.criteria = criteria;
    }

    /**
     * This method is called for each row of data in the Excel file. It adds the parsed data object to the dataList list
     * and sets its row number to the current row index.
     *
     * @param data    the data to be parsed from the Excel file
     * @param context the context object for the parsing operation
     */
    @Override
    public void invoke(T data, AnalysisContext context) {
        ReadRowHolder rowHolder = context.readRowHolder();
        Integer rowIndex = rowHolder.getRowIndex();
        data.setRowNum(rowIndex + 1);
        if (criteria == null || criteria.isMatch(data)) {
            dataList.add(data);
        }
    }

    /**
     * This method is called after all data has been parsed from the Excel file. It does nothing in this implementation.
     *
     * @param context the context object for the parsing operation
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }

}
