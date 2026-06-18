package es.mybi.demo.sbsender;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "es.mybi.demo")
@EnableScheduling
public class SbSenderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SbSenderApplication.class, args);
    }
}
