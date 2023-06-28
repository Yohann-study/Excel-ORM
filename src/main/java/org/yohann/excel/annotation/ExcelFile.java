package org.yohann.excel.annotation;

import java.lang.annotation.*;

/**
 * This annotation is used to mark a class as representing an Excel file. It can be applied to a class definition.
 * The 'path' and 'filename' attributes specify the path and file name of the Excel file to be read or written.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelFile {

    /**
     * The file path of the Excel file.
     *
     * @return the file path of the Excel file
     */
    String path() default "";

    /**
     * The file name of the Excel file.
     *
     * @return the file name of the Excel file
     */
    String filename();

}
