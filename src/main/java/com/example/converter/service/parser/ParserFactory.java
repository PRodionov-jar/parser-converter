package com.example.converter.service.parser;

import com.example.converter.utils.ParserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ParserFactory {

    private final static Map<String, Parser> parsersCache = new HashMap<>();

    @Autowired
    public ParserFactory(List<Parser> parsers) {
        parsers.forEach(parser -> parsersCache.put(parser.getType().name(), parser));
    }

    public static Parser getParser(String fileName) {
        String fieType = ParserUtils.getParserTypeByFileName(fileName);
        Parser parser = parsersCache.get(fieType);
        if (parser == null) {
            throw new RuntimeException("Unknown service type: " + fieType);
        }
        return parser;
    }
}
