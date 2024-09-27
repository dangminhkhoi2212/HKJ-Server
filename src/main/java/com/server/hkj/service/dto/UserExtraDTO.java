package com.server.hkj.service.dto;

import java.io.Serializable;
import java.time.Instant;
import lombok.Data;

/**
 * A DTO for the {@link com.server.hkj.domain.UserExtra} entity.
 */

@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserExtraDTO implements Serializable {

    private Long id;

    private String phone;

    private String address;

    private Boolean isDeleted;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private UserDTO user;

    private HkjHireDTO hkjHire;
}
