package springApplication;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.ApiContextInitializer;
import springApplication.db.service.DbLoggerService;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableScheduling
@EnableJpaRepositories(basePackages = "springApplication.db.repository")
@EntityScan(basePackages = {"springApplication.db.model"})
public class Main
{
    @Autowired
    DbLoggerService dbLoggerService;

    public static void main(String[] args)
    {
        ApiContextInitializer.init();
        SpringApplication.run(Main.class, args);
    }

    @PostConstruct
    void postConstruct() {
        dbLoggerService.log("main", "INFO", "App started!");
    }

}
