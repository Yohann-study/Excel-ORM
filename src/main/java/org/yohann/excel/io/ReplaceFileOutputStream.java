package org.yohann.excel.io;

import org.apache.poi.util.IOUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * The ReplaceFileOutputStream class extends FileOutputStream and replaces the original file
 * with a new file when the output stream is closed. The original file is deleted and the new
 * file is renamed to the original file name.
 */
public class ReplaceFileOutputStream extends FileOutputStream {

    // The suffix for the temporary copy of the file
    private static final String SUFFIX = ".write";

    // The name of the original file
    private final String fileName;

    /**
     * Constructs a new ReplaceFileOutputStream object with the specified file name.
     * The output stream writes to a temporary copy of the file.
     *
     * @param fileName the name of the original file
     * @throws FileNotFoundException if the specified file cannot be opened for writing
     */
    private ReplaceFileOutputStream(String fileName) throws FileNotFoundException {
        // Write to a temporary copy of the file
        super(fileName + SUFFIX);
        this.fileName = fileName;
    }

    /**
     * Closes the ReplaceFileOutputStream and replaces the original file with the temporary file.
     * The contents of the temporary file are copied to the original file.
     *
     * @throws IOException if an I/O error occurs while closing the output stream or copying the contents
     *                     of the temporary file to the original file
     */
    @Override
    public void close() throws IOException {
        super.close();
        // Copy the contents of the temporary file to the original file
        String tempFilename = fileName + SUFFIX;
        try (FileInputStream in = new FileInputStream(tempFilename);
             FileOutputStream out = new FileOutputStream(fileName)) {
            IOUtils.copy(in, out);
        }
    }

    /**
     * Creates a new ReplaceFileOutputStream object for the specified file.
     * The output stream writes to a temporary copy of the file.
     *
     * @param fileName the name of the file to be replaced
     * @return a new ReplaceFileOutputStream object for the specified file
     * @throws FileNotFoundException if the specified file cannot be opened for writing
     */
    public static ReplaceFileOutputStream create(String fileName) throws FileNotFoundException {
        // Create a temporary copy of the file
        String tempFilename = fileName + SUFFIX;
        try (FileInputStream in = new FileInputStream(fileName);
             FileOutputStream out = new FileOutputStream(tempFilename)) {
            IOUtils.copy(in, out);
        } catch (Exception e) {
            throw new RuntimeException("create CopyFileInputStream failed", e);
        }
        // Return a new ReplaceFileOutputStream object for the specified file
        return new ReplaceFileOutputStream(fileName);
    }

}