package springApplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import springApplication.db.model.CharactersStatsEntity;
import springApplication.db.model.LogEntity;
import springApplication.db.repository.LogRepository;
import springApplication.db.service.CharacterStatsService;
import springApplication.db.service.DbLoggerService;

import java.util.List;

@Component
public class SchedulerTest {

    @Autowired
    LogRepository logRepository;

    @Autowired
    DbLoggerService dbLoggerService;

    @Autowired
    CharacterStatsService statService;

    @Scheduled(fixedDelay = 100000)
    void scheduling() {
//        dbLoggerService.log("qwrqweqw");
//        System.out.println("start");
//        List<LogEntity> logs = logRepository.findAll();
//        System.out.println(logs);
        statService.addOrUpdateCharacterStat("Darth Vader");
    }
}
