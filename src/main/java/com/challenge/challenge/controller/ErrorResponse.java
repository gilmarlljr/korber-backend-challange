package com.challenge.challenge.controller;

import java.util.ArrayList;
import java.util.List;

public class ErrorResponse implements Response{
    private List<String> errors;

    public List<String> getErrors() {
        return errors;
    }

    public ErrorResponse addError(String error) {
        if (errors == null) {
            errors = new ArrayList<>();
        }
        this.errors.add(error);
        return this;
    }
}
