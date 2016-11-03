package com.turbointernational.metadata.exception;

/**
 * Created by trunikov on 16.06.16.
 */
public class PartNotFound extends Exception {

    public PartNotFound() {
        super();
    }

    public PartNotFound(String message) {
        super(message);
    }

    public PartNotFound(Long id) {
        super(String.format("Part [%1$d] not found.", id));
    }

    public PartNotFound(Long id, String manufacturerNumber) {
        super(String.format("Part [%1$d] - %2!s", id, manufacturerNumber));
    }

}
