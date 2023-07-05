package org.yohann.excel.mapper;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.FileMagic;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.yohann.excel.annotation.ExcelFile;
import org.yohann.excel.entity.Excel;
import org.yohann.excel.io.CopyFileInputStream;
import org.yohann.excel.io.ReplaceFileOutputStream;
import org.yohann.excel.listener.DataListener;
import org.yohann.excel.listener.HeaderListener;
import org.yohann.excel.query.Criteria;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

import static com.alibaba.excel.support.ExcelTypeEnum.XLS;
import static com.alibaba.excel.support.ExcelTypeEnum.XLSX;
import static org.apache.poi.poifs.filesystem.FileMagic.OOXML;

/**
 * The AbstractMapper class is an abstract implementation of the Mapper interface that provides basic functionalities for Excel file mapping.
 *
 * @param <T> The type of the Excel object that this mapper handles.
 */
@Slf4j
public abstract class AbstractExcelMapper<T extends Excel> implements ExcelMapper<T> {

    /**
     * The Class object of the generic type T.
     */
    protected final Class<T> _class;
    /**
     * The file path of the Excel file that this mapper handles.
     */
    protected final String _filePath;
    /**
     * A volatile map that stores the header information of the Excel file.
     */
    protected volatile Map<String, Integer> headerMap;

    /**
     * Constructor for the AbstractExcelMapper class.
     * It initializes the _class and _filePath fields by reading the @ExcelFile annotation on the generic type T.
     * If the Excel file does not exist, it creates a new Excel file with the given file name and path.
     *
     * @throws IllegalArgumentException if there is an error with the reflection or file creation process.
     */
    @SuppressWarnings("unchecked")
    public AbstractExcelMapper() {
        // Get the generic superclass of the current class
        Type superClass = this.getClass().getGenericSuperclass();

        // If the superclass is not parameterized, throw an exception
        if (superClass instanceof Class) {
            throw new IllegalArgumentException("Internal error: TypeReference constructed without actual type information");
        } else {
            // Get the actual type argument of the superclass
            Type argument = ((ParameterizedType) superClass).getActualTypeArguments()[0];
            try {
                // Get the class of the actual type argument
                Class<T> clazz = (Class<T>) Class.forName(argument.getTypeName());
                // Get the ExcelFile annotation for the class, if it exists
                ExcelFile fileName = clazz.getAnnotation(ExcelFile.class);
                // Get the file path and name based on the annotation and class name
                String path = Optional.ofNullable(fileName)
                        .map(ExcelFile::path)
                        .orElse("");
                String filename = Optional.ofNullable(fileName)
                        .map(excelFile -> {
                            String name = excelFile.filename();
                            // Make sure the file name does not contain a directory
                            if (name.contains("\\") || name.contains("/")) {
                                throw new IllegalArgumentException("file name must be not contains directory");
                            }
                            // Make sure the file type is either XLS or XLSX
                            if ((!name.endsWith(XLS.getValue()) && !name.endsWith(XLSX.getValue()))) {
                                throw new IllegalArgumentException("file type must be either XLS or XLSX");
                            }
                            return name;
                        })
                        .orElse(clazz.getSimpleName() + XLSX.getValue());
                // Combine the path and filename into a full file path
                String filePath = path.equals("") ? filename : path + "/" + filename;

                // Set the class and file path variables
                this._class = clazz;
                this._filePath = filePath;

                File directory = new File(path);
                File file = new File(filePath);
                // If the file does not exist, create a new Excel file with the header row
                if (!file.exists()) {
                    if (directory.mkdirs()) {
                        log.info("created directory: " + directory);
                    }
                    if (file.createNewFile()) {
                        EasyExcel.write(file)
                                .head(_class)
                                .excelType(filePath.endsWith(XLS.getValue()) ? XLS : XLSX)
                                .sheet()
                                .doWrite(Collections.emptyList());
                        log.info("created file: " + filePath);
                    }
                }
            } catch (ClassNotFoundException | IOException e) {
                throw new IllegalArgumentException(e.getMessage(), e);
            }
        }
    }

    @Override
    public synchronized List<T> getAll() {
        DataListener<T> listener = new DataListener<>();
        EasyExcel.read(_filePath)
                .sheet()
                .head(_class)
                .registerReadListener(listener)
                .doRead();
        return listener.getDataList();
    }

    @Override
    public synchronized List<T> get(Criteria criteria) {
        return this.getAll()
                .stream()
                .filter(criteria::isMatch)
                .collect(Collectors.toList());
    }

    @Override
    public synchronized void insertBatch(List<T> list) {
        try (InputStream in = new BufferedInputStream(CopyFileInputStream.create(_filePath));
             Workbook workbook = FileMagic.valueOf(in) == OOXML ? new XSSFWorkbook(in) : new HSSFWorkbook(in);
             OutputStream out = new BufferedOutputStream(ReplaceFileOutputStream.create(_filePath))) {
            // Get the sheet and last row number of the Excel file
            Sheet sheet = workbook.getSheetAt(0);

            for (T t : list) {
                int lastRowNum = sheet.getLastRowNum();
                // Create a new row and fill it with the values from the mapped object
                Row row = sheet.createRow(lastRowNum + 1);
                fillCell(t, row);
            }

            // Write the updated workbook back to the Excel file
            workbook.write(out);
        } catch (Exception e) {
            throw new RuntimeException("insert failed, filename: " + _filePath, e);
        }
    }

    @Override
    public synchronized void updateBatch(List<T> list) {
        try (InputStream in = new BufferedInputStream(CopyFileInputStream.create(_filePath));
             Workbook workbook = FileMagic.valueOf(in) == OOXML ? new XSSFWorkbook(in) : new HSSFWorkbook(in);
             OutputStream out = new BufferedOutputStream(ReplaceFileOutputStream.create(_filePath))) {
            // Get the sheet and row number of the record to update
            Sheet sheet = workbook.getSheetAt(0);

            for (T t : list) {
                Integer rowNum = t.getRowNum();
                Row row = sheet.getRow(rowNum - 1);
                // Fill the row with the updated values from the mapped object
                fillCell(t, row);
            }

            // Write the updated workbook back to the Excel file
            workbook.write(out);
        } catch (Exception e) {
            throw new RuntimeException("update failed, filename: " + _filePath, e);
        }
    }

    @Override
    public synchronized void deleteBatch(List<Integer> rowNumList) {
        try (InputStream in = new BufferedInputStream(CopyFileInputStream.create(_filePath));
             Workbook workbook = FileMagic.valueOf(in) == OOXML ? new XSSFWorkbook(in) : new HSSFWorkbook(in);
             OutputStream out = new BufferedOutputStream(ReplaceFileOutputStream.create(_filePath))) {
            // Get the sheet and row of the record to delete
            Sheet sheet = workbook.getSheetAt(0);

            for (Integer rowNum : rowNumList) {
                Row row = sheet.getRow(rowNum - 1);
                // Remove the row and shift the remaining rows up
                sheet.removeRow(row);
                int lastRowNum = sheet.getLastRowNum();
                if (rowNum - 1 < lastRowNum) {
                    sheet.shiftRows(rowNum, lastRowNum, -1);
                }
            }

            // Write the updated workbook back to the Excel file
            workbook.write(out);
        } catch (Exception e) {
            throw new RuntimeException("delete failed, filename: " + _filePath, e);
        }
    }

    /**
     * Get the map of header names to column indexes for the Excel file
     */
    private synchronized Map<String, Integer> getHeaderMap() {
        if (this.headerMap == null) {
            HeaderListener<T> listener = new HeaderListener<>();
            EasyExcel.read(_filePath)
                    .sheet()
                    .head(_class)
                    .registerReadListener(listener)
                    .doRead();
            this.headerMap = listener.getHeaderMap();
        }

        return this.headerMap;
    }

    /**
     * Fill a row in the Excel file with values from the mapped object
     */
    private void fillCell(T t, Row row) throws IllegalAccessException {
        Field[] fields = _class.getDeclaredFields();
        Field.setAccessible(fields, true);
        for (Field field : fields) {
            ExcelIgnore excelIgnore = field.getAnnotation(ExcelIgnore.class);
            if (excelIgnore != null) {
                continue;
            }
            // Get the column header
            String headerName = Optional.ofNullable(field.getAnnotation(ExcelProperty.class))
                    .map(excelProperty -> {
                        String value = excelProperty.value()[0];
                        return "".equals(value) ? null : value;
                    })
                    .orElse(field.getName());
            // Get the column index for the header name of this field
            Integer cellIndex = this.getHeaderMap().get(headerName);
            // Get the cell for this field and set its value to the value of the field in the mapped object
            Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            Object object = field.get(t);
            if (object == null) {
                continue;
            }
            // If the field is a Date, format it according to the DateTimeFormat annotation (if it exists)
            if (object instanceof Date) {
                String pattern = Optional.ofNullable(field.getAnnotation(DateTimeFormat.class))
                        .map(DateTimeFormat::value)
                        .orElse("yyyy-MM-dd HH:mm:ss");
                object = DateUtils.format((Date) object, pattern);
            }
            cell.setCellValue(object.toString());
        }
    }

}