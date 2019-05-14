package com.dannie.setrise.logic.networking;

import retrofit2.Response;

import java.util.Objects;

public final class ApiResponse<D> {

    private final D data;

    private final Throwable error;

    private final String errorMessage;

    public ApiResponse(final D Data) {
        Objects.requireNonNull(Data);
        this.data = Data;
        this.error = null;
        this.errorMessage = null;
    }

    ApiResponse(Response<D> response) {
        this.data = response.body();
        this.error = null;
        if (!response.isSuccessful()) {
            this.errorMessage = response.code() + " " + response.message();
        } else {
            this.errorMessage = null;
        }
    }

    ApiResponse(final Throwable error) {
        Objects.requireNonNull(error);

        this.data = null;
        this.error = error;
        this.errorMessage = null;
    }

    boolean isSuccessful() {
        return data != null && error == null;
    }

    D getData() {
        return data;
    }

    public Throwable getError() {
        if (error == null) {
            throw new IllegalStateException("Error is null");
        }
        return error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}

