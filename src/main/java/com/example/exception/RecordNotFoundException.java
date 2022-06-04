/*******************************************************************************
 * MeasureOne, Inc . 
 * CONFIDENTIAL
 * __________________
 * Copyright 2019 MeasureOne, Inc.
 * All Rights Reserved.
 *
 * NOTICE:  
 * All information contained herein is, and remains the property of MeasureOne, Inc. 
 * The intellectual and technical concepts contained herein are proprietary to MeasureOne Inc
 * and may be covered by governing laws and  are protected by trade secret or copyright law. 
 * Dissemination of this information or reproduction of this material is strictly forbidden 
 * unless prior written permission is obtained from MeasureOne, Inc.
 *******************************************************************************/
package com.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Record not found")
public class RecordNotFoundException extends Exception {

    public RecordNotFoundException(String message) {
        this(message, null);
    }

    public RecordNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
