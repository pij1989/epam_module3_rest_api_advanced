package com.epam.esm.model.error;

public class MessageKeyError {
    public static final String TAG_BAD_REQUEST = "error.400.tag";
    public static final String TAG_NOT_FOUND = "error.404.tag";
    public static final String TAG_BAD_REQUEST_PARAMETERS = "error.400.tags";
    public static final String CERTIFICATE_BAD_REQUEST = "error.400.certificate";
    public static final String CERTIFICATE_NOT_FOUND_ID = "error.404.certificate.id";
    public static final String CERTIFICATE_BAD_REQUEST_TAG_CREATED = "error.400.certificate.tag_not_created";
    public static final String CERTIFICATE_NOT_FOUND_TAG_NAME = "error.404.certificate.tag_name";
    public static final String CERTIFICATE_OR_TAG_NOT_FOUND = "error.404.certificate.tag";
    public static final String NOT_FOUND = "error.404.common";
    public static final String BAD_REQUEST = "error.400.common";
    public static final String METHOD_NOT_ALLOWED = "error.405.common";
    public static final String INTERNAL_SERVER_ERROR = "error.500.common";

    private MessageKeyError() {
    }
}
