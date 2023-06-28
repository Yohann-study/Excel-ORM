package org.yohann.excel.mapper;

import org.yohann.excel.query.Criteria;
import org.yohann.excel.entity.Excel;

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
     * Inserts a new object into the Excel file.
     *
     * @param t the object to be inserted
     */
    void insert(T t);

    /**
     * Updates an existing object in the Excel file.
     *
     * @param t the object to be updated
     */
    void update(T t);

    /**
     * Deletes an object at the specified row number from the Excel file.
     *
     * @param rowNum the row number of the object to be deleted
     */
    void delete(Integer rowNum);

}
