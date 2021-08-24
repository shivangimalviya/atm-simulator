package com.machine.atm.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserErrorResponse {

    private String uri;
    private String failureReason;
}
