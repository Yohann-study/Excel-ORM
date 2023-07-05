package org.yohann.excel.io;

import org.apache.poi.util.IOUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


/**
 * The CopyFileInputStream class extends FileInputStream and creates a copy of a file
 * before reading it. The copy is stored in a temporary file that is deleted when the
 * input stream is closed.
 */
public class CopyFileInputStream extends FileInputStream {

    // The suffix for the temporary copy of the file
    private static final String SUFFIX = ".read";

    // The name of the temporary file containing the copied data
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
     * Creates a new CopyFileInputStream object for the specified file.
     * The file is copied to a temporary file before the input stream is created.
     *
     * @param fileName the name of the file to be read
     * @return a new CopyFileInputStream object for the specified file
     * @throws FileNotFoundException if the file specified by the file name cannot be opened
     */
    public static FileInputStream create(String fileName) throws FileNotFoundException {
        // Create a temporary copy of the file
        String tempFilename = fileName + SUFFIX;
        try (FileInputStream in = new FileInputStream(fileName);
             FileOutputStream out = new FileOutputStream(tempFilename)) {
            IOUtils.copy(in, out);
        } catch (Exception e) {
            throw new RuntimeException("create CopyFileInputStream failed", e);
        }
        // Return a new CopyFileInputStream object for the specified file
        return new CopyFileInputStream(tempFilename);
    }

}