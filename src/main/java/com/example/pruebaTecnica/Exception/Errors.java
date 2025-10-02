package com.example.pruebaTecnica.Exception;

public enum Errors {
    NOT_FOUND("not_found"),
    BAD_REQUEST("bad_request");

    private final String code;

    Errors(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
