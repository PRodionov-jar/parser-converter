package com.example.converter.service.parser;

import com.example.converter.model.OrderOut;

import java.util.List;

public interface Parser {
    List<OrderOut> doParse(String fileName) throws Exception;
    FileType getType();
}
