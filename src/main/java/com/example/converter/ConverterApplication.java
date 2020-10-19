package com.example.converter;

import com.example.converter.model.OrderOut;
import com.example.converter.service.parser.Parser;
import com.example.converter.service.parser.ParserFactory;
import com.example.converter.utils.ConverterUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@SpringBootApplication
public class ConverterApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ConverterApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (args.length != 0) {
            try {
                Stream.of(args).distinct().forEach(fileName -> {
                    getParsedOrders(fileName)
                            .stream()
                            .distinct()
                            .forEach(System.out::println);
                });
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Source files not specified");
        }
    }

    private List<OrderOut> getParsedOrders(String fileName) {
        List<OrderOut> orders = new ArrayList<>();
        try {
            Parser parser = ParserFactory.getParser(fileName);
            orders.addAll(parser.doParse(fileName));
        } catch (Exception e) {
            System.out.println(ConverterUtils.buildErrorString(fileName, null, e.getMessage()));
        }
        return orders;
    }
}
