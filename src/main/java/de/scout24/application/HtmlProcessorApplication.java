package de.scout24.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value = "de.scout24")
public class HtmlProcessorApplication {
    public static void main(String args[]) {
        SpringApplication.run(HtmlProcessorApplication.class, args);
    }
}
