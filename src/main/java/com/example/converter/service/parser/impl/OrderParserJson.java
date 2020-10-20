package com.example.converter.service.parser.impl;

import com.example.converter.exceptions.ParserException;
import com.example.converter.model.OrderIn;
import com.example.converter.model.OrderOut;
import com.example.converter.service.parser.FileType;
import com.example.converter.service.parser.Parser;
import com.example.converter.utils.ConverterUtils;
import com.example.converter.utils.ParserUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

import static com.example.converter.utils.ParserUtils.FILE_IS_EMPTY;
import static com.example.converter.utils.ParserUtils.JSON_IS_INVALID;

@Service
public final class OrderParserJson implements Parser {

    private ForkJoinPool forkJoinPool;
    private ObjectMapper jsonMapper;

    @Autowired
    public OrderParserJson(ForkJoinPool forkJoinPool, ObjectMapper jsonMapper) {
        this.forkJoinPool = forkJoinPool;
        this.jsonMapper = jsonMapper;
    }

    @Override
    public List<OrderOut> doParse(String fileName) throws Exception {
        BufferedReader input = ParserUtils.readFileContent(fileName);
        String data = input.lines().collect(Collectors.joining());
        input.close();
        if (data.isEmpty()) {
            throw new ParserException(FILE_IS_EMPTY);
        }

        Object json = new JSONTokener(data).nextValue();
        if (json instanceof JSONObject) {
            OrderIn source = jsonMapper.readValue(data, OrderIn.class);
            return Collections.singletonList(ConverterUtils.convertInToOut(source, fileName, null, null));
        } else if (json instanceof JSONArray) {
            List<OrderIn> sourceArray = jsonMapper.readValue(data, new TypeReference<List<OrderIn>>(){});

            return forkJoinPool.submit(() -> sourceArray.parallelStream()
                    .map(s -> ConverterUtils.convertInToOut(s, fileName, null, null))
                    .collect(Collectors.toList())).join();
        } else {
            throw new ParserException(JSON_IS_INVALID);
        }
    }

    @Override
    public FileType getType() {
        return FileType.JSON;
    }
}
