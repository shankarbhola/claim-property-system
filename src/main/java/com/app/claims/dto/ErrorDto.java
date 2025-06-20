package com.app.claims.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class ErrorDto {
    private String message;
    private Date date;
    private String description;
}
