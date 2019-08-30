package de.scout24.exception;

public enum ErrorCodes {

    INTERNAL_SERVER_ERROR("HP_1", "Error encountered"),
    INVALID_URL("HP_2", "Invalid URL"),
    CANNOT_READ_DOCUMENT("HP_3", "Cannot read document"),
    MALFORMED_URL("HP_4", "Malformed URL");

    private final String code;
    private final String description;

    private ErrorCodes(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return code + ": " + description;
    }
}
