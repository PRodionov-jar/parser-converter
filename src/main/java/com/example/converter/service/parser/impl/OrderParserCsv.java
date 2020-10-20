package com.example.converter.service.parser.impl;

import com.example.converter.exceptions.ParserException;
import com.example.converter.model.OrderIn;
import com.example.converter.model.OrderOut;
import com.example.converter.service.parser.FileType;
import com.example.converter.service.parser.Parser;
import com.example.converter.utils.ConverterUtils;
import com.example.converter.utils.ParserUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.example.converter.utils.ParserUtils.COLUMNS_COUNT_IS_INVALID;

@Service
public final class OrderParserCsv implements Parser {

    private ForkJoinPool forkJoinPool;

    @Autowired
    public OrderParserCsv(ForkJoinPool forkJoinPool) {
        this.forkJoinPool = forkJoinPool;
    }

    @Override
    public List<OrderOut> doParse(String fileName) throws Exception {
        BufferedReader input = ParserUtils.readFileContent(fileName);
        Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(input);
        input.close();
        return forkJoinPool.submit(() -> StreamSupport.stream(records.spliterator(), true)
                .map(record -> parseRecord(record, fileName))
                .collect(Collectors.toList())).join();
    }

    @Override
    public FileType getType() {
        return FileType.CSV;
    }

    private OrderOut parseRecord (CSVRecord record, String fileName) {
        OrderOut target;
        if (record.size() == 4) {
            OrderIn source = new OrderIn(record.get(0), record.get(1), record.get(2), record.get(3));
            target = ConverterUtils.convertInToOut(source, fileName, record.getRecordNumber(), null);
        } else {
            target = ConverterUtils.convertInToOut(null, fileName,
                    record.getRecordNumber(), COLUMNS_COUNT_IS_INVALID + record.size());
        }
        return target;
    }
}
