package springapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.ApiContextInitializer;
import springapp.db.service.DbLoggerService;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableScheduling
@EnableJpaRepositories(basePackages = "springapp.db.repository")
@EntityScan(basePackages = {"springApplication.db.model"})
public class Main {
    @Autowired
    private DbLoggerService dbLoggerService;

    public static void main(String[] args) {
        ApiContextInitializer.init();
        SpringApplication.run(Main.class, args);
    }

    @PostConstruct
    private void postConstruct() {
        dbLoggerService.log("main", "INFO", "App started!");
    }

}
