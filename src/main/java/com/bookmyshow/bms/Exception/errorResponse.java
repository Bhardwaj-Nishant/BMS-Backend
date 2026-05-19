package com.bookmyshow.bms.Exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class errorResponse {
    private Date timestamp;
    private String error;
    private String message;
    private int status;
    private String path;

}
