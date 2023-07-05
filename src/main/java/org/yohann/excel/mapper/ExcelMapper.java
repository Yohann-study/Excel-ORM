package org.yohann.excel.mapper;

import org.yohann.excel.entity.Excel;
import org.yohann.excel.query.Criteria;

import java.util.Arrays;
import java.util.List;

/**
 * This interface defines methods for mapping Java objects to Excel files.
 *
 * @param <T> a generic type extending the Excel class that maps to an Excel file
 */
public interface ExcelMapper<T extends Excel> {

    /**
     * Retrieves all objects of the specified type from the Excel file.
     *
     * @return a list of objects of the specified type
     */
    List<T> getAll();

    /**
     * Retrieves objects of the specified type that match the given criteria from the Excel file.
     *
     * @param criteria an instance of criteria used to filter the objects
     * @return a list of objects of the specified type that match the given criteria
     */
    List<T> get(Criteria criteria);

    /**
     * Inserts one or more objects of the specified type into the Excel file.
     *
     * @param t one or more objects of the specified type to insert
     */
    default void insert(T... t) {
        insertBatch(Arrays.asList(t));
    }

    /**
     * Inserts a list of objects of the specified type into the Excel file.
     *
     * @param list a list of objects of the specified type to insert
     */
    void insertBatch(List<T> list);

    /**
     * Updates one or more objects of the specified type in the Excel file.
     *
     * @param t one or more objects of the specified type to update
     */
    default void update(T... t) {
        updateBatch(Arrays.asList(t));
    }

    /**
     * Updates a list of objects of the specified type in the Excel file.
     *
     * @param list a list of objects of the specified type to update
     */
    void updateBatch(List<T> list);

    /**
     * Deletes one or more rows from the Excel file.
     *
     * @param rowNum one or more row numbers to delete
     */
    default void delete(Integer... rowNum) {
        deleteBatch(Arrays.asList(rowNum));
    }

    /**
     * Deletes a list of rows from the Excel file.
     *
     * @param rowNumList a list of row numbers to delete
     */
    void deleteBatch(List<Integer> rowNumList);
}