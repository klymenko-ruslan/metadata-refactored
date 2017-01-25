package com.turbointernational.metadata.service;

import java.io.File;
import java.io.IOException;

/**
 * A interface for file storage.
 *
 * Created by dmytro.trunykov@zorallabs.com on 2017-01-17.
 */
public interface FileStorageService {

    /**
     * Save file to the storage.
     *
     * The existing file is moved to the storage. It is saved to the storage under specified ID and
     * then erased on the file system. The saved file has the same name as original file.
     *
     * @param id identifier of the file
     * @param srcFile an existing file to store
     * @throws IOException
     */
    void moveFile(String id, File srcFile) throws IOException;

    /**
     * Save file to the storage.
     *
     * The existing file is moved to the storage. It is saved to the storage under specified ID and
     * then erased on the file system. The saved file has the same name as specified by the param 'renameTo'.
     *
     * @param id identifier of the file
     * @param srcFile an existing file to store
     * @param renameTo a new filename of stored file
     * @throws IOException
     */
    void moveFile(String id, File srcFile, String renameTo) throws IOException;

    /**
     * Delete file with specified ID in the storage.
     *
     * @param id ID of a file to be deleted
     * @return true if a file with specified ID found and deleted
     */
    boolean delete(String id);

    /**
     * Is this storage empty or not.
     *
     * This method is dedicated for functional test only.
     *
     * @return true if this storage does not contain file(s)
     */
    boolean isEmpty();

}
