package org.yohann.excel.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * The ReplaceFileOutputStream class extends FileOutputStream and replaces the original file
 * with a new file when the output stream is closed. The original file is deleted and the new
 * file is renamed to the original file name.
 */
public class ReplaceFileOutputStream extends FileOutputStream {

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
        File file = new File(fileName);
        file.delete();
        File newFile = new File(fileName + ".copy");
        newFile.renameTo(file);
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
        return new ReplaceFileOutputStream(fileName);
    }

}
