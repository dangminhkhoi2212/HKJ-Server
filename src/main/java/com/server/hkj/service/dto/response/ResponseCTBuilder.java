package com.server.hkj.service.dto.response;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseCTBuilder<T> {

    final ResponseCT<T> responseCT = new ResponseCT<T>();

    private ResponseCTBuilder<T> message(String message) {
        responseCT.setMessage(message);
        return this;
    }

    private ResponseCTBuilder<T> status(int status) {
        responseCT.setStatus(status);
        return this;
    }

    public ResponseCTBuilder<T> success() {
        return new ResponseCTBuilder<T>().message("Succeed").status(200);
    }

    public ResponseCTBuilder<T> fail() {
        return new ResponseCTBuilder<T>().message("Failed").status(500);
    }

    public ResponseCTBuilder<T> error(ResponseErrorCT error) {
        responseCT.setError(error);
        responseCT.setStatus(500);
        responseCT.setMessage("Failed");
        return this;
    }

    public ResponseCTBuilder<T> addData(final T body) {
        responseCT.setData(body);
        responseCT.setMessage("Succeeded");
        responseCT.setStatus(200);
        return this;
    }

    public ResponseCT<T> build() {
        return responseCT;
    }
}
