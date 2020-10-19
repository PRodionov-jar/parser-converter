package com.example.converter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderIn {
    private String orderId;
    private String amount;
    private String currency;
    private String comment;
}
