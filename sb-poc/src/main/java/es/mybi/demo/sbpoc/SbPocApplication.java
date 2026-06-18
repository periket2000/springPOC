package es.mybi.demo.sbpoc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "es.mybi.demo")
public class SbPocApplication {

    public static void main(String[] args) {
        SpringApplication.run(SbPocApplication.class, args);
    }
}
