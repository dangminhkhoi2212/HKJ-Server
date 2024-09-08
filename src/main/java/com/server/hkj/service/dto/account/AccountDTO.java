package com.server.hkj.service.dto.account;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountDTO {

    Long id;
    String name;
    String email;
    String phoneNumber;
}
