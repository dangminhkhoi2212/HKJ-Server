package com.server.hkj.service.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ResponseCT<T> {

    int status;
    String message;
    T data;
    ResponseErrorCT error;
}
