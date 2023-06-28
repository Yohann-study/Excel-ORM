package org.yohann.excel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.metadata.holder.ReadRowHolder;
import org.yohann.excel.entity.Excel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class is a listener for parsing Excel files. It extends the AnalysisEventListener class and is parameterized
 * with a type T that extends the Excel class.
 *
 * @param <T> the type of data to be parsed from the Excel file
 */
public class HeaderListener<T extends Excel> extends AnalysisEventListener<T> {

    /**
     * A map that holds the header names and their corresponding column indices.
     */
    private Map<String, Integer> headerMap;

    /**
     * Returns the header map.
     *
     * @return the header map
     */
    public Map<String, Integer> getHeaderMap() {
        return headerMap;
    }

    /**
     * This method is called when the parser encounters the header row in the Excel file. It creates a map of the header
     * names and their corresponding column indices.
     *
     * @param headMap the map of header names and their corresponding column indices
     * @param context the context object for the parsing operation
     */
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        this.headerMap = headMap.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
    }

    /**
     * This method is called for each row of data in the Excel file. It does nothing in this implementation.
     *
     * @param data    the data to be parsed from the Excel file
     * @param context the context object for the parsing operation
     */
    @Override
    public void invoke(T data, AnalysisContext context) {
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