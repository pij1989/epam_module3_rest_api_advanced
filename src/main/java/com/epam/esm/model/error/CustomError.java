package com.epam.esm.model.error;

public class CustomError {

    private String errorMessage;
    private String errorCode;

    public CustomError() {
    }

    public CustomError(String errorMessage, String errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomError error = (CustomError) o;

        if (errorMessage != null ? !errorMessage.equals(error.errorMessage) : error.errorMessage != null) return false;
        return errorCode != null ? errorCode.equals(error.errorCode) : error.errorCode == null;
    }

    @Override
    public int hashCode() {
        int result = errorMessage != null ? errorMessage.hashCode() : 0;
        result = 31 * result + (errorCode != null ? errorCode.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Error{");
        sb.append("errorMessage='").append(errorMessage).append('\'');
        sb.append(", errorCode='").append(errorCode).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
