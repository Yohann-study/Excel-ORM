package org.yohann.excel.io;

import org.apache.poi.util.IOUtils;

import java.io.*;
import java.util.UUID;

import static com.alibaba.excel.support.ExcelTypeEnum.XLS;
import static com.alibaba.excel.support.ExcelTypeEnum.XLSX;


/**
 * The CopyFileInputStream class extends FileInputStream and creates a copy of a file
 * before reading it. The copy is stored in a temporary file that is deleted when the
 * input stream is closed.
 */
public class CopyFileInputStream extends FileInputStream {

    /**
     * The name of the temporary file where the copy of the input file is stored.
     */
    private final String temporary;

    /**
     * Constructs a new CopyFileInputStream object with the specified temporary file name.
     *
     * @param temporary the name of the temporary file
     * @throws FileNotFoundException if the file specified by the temporary file name cannot be opened
     */
    private CopyFileInputStream(String temporary) throws FileNotFoundException {
        super(temporary);
        this.temporary = temporary;
    }

    /**
     * Closes the input stream and deletes the temporary file containing the copied data.
     *
     * @throws IOException if an I/O error occurs while closing the input stream
     */
    @Override
    public void close() throws IOException {
        super.close();
        File file = new File(temporary);
        file.delete();
    }

    /**
     * Creates a new CopyFileInputStream object for the specified file.
     * The file is copied to a temporary file before the input stream is created.
     *
     * @param fileName the name of the file to be read
     * @return a new CopyFileInputStream object for the specified file
     * @throws FileNotFoundException if the file specified by the file name cannot be opened
     */
    public static FileInputStream create(String fileName) throws FileNotFoundException {
        String suffix = fileName.endsWith(XLS.getValue()) ? XLS.getValue() : XLSX.getValue();
        String tempFilename = UUID.randomUUID().toString().replace("-", "") + suffix;
        try (FileInputStream in = new FileInputStream(fileName);
             FileOutputStream out = new FileOutputStream(tempFilename)) {
            IOUtils.copy(in, out);
        } catch (Exception e) {
            throw new RuntimeException("create CopyFileInputStream failed", e);
        }
        return new CopyFileInputStream(tempFilename);
    }

}
