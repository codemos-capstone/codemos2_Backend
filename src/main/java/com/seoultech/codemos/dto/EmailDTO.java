package com.seoultech.codemos.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EmailDTO {
    private String to;

    private String subject;

    private String message;
}
