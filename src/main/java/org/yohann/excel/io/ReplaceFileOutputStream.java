package org.yohann.excel.io;

import org.apache.poi.util.IOUtils;

import java.io.*;

/**
 * The ReplaceFileOutputStream class extends FileOutputStream and replaces the original file
 * with a new file when the output stream is closed. The original file is deleted and the new
 * file is renamed to the original file name.
 */
public class ReplaceFileOutputStream extends FileOutputStream {

    private static final String SUFFIX = ".copy";

    /**
     * The name of the original file.
     */
    private final String fileName;

    /**
     * Constructs a new ReplaceFileOutputStream object with the specified file name.
     * The output stream writes to a temporary copy of the file.
     *
     * @param fileName the name of the original file
     * @throws FileNotFoundException if the specified file cannot be opened for writing
     */
    private ReplaceFileOutputStream(String fileName) throws FileNotFoundException {
        super(fileName + ".copy");
        this.fileName = fileName;
    }

    /**
     * Closes the output stream and replaces the original file with the temporary copy.
     * The original file is deleted and the temporary copy is renamed to the original file name.
     *
     * @throws IOException if an I/O error occurs while closing the output stream
     */
    @Override
    public void close() throws IOException {
        super.close();
        String tempFilename = fileName + SUFFIX;
        try (FileInputStream in = new FileInputStream(tempFilename);
             FileOutputStream out = new FileOutputStream(fileName)) {
            IOUtils.copy(in, out);
        }
        File tempFile = new File(tempFilename);
        tempFile.delete();
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
        String tempFilename = fileName + SUFFIX;
        try (FileInputStream in = new FileInputStream(fileName);
             FileOutputStream out = new FileOutputStream(tempFilename)) {
            IOUtils.copy(in, out);
        } catch (Exception e) {
            throw new RuntimeException("create CopyFileInputStream failed", e);
        }
        return new ReplaceFileOutputStream(fileName);
    }

}
