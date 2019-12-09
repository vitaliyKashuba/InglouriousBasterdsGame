package springApplication.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springApplication.db.model.CharactersStatsEntity;
import springApplication.db.repository.LogRepository;
import springApplication.db.service.CharacterStatsService;
import springApplication.db.service.DbLoggerService;

import java.util.List;

@RestController
public class DbController {

    @Autowired
    DbLoggerService dbLoggerService;

    @Autowired
    CharacterStatsService statsService;

    @RequestMapping("stats")
    public ResponseEntity getStats() {
        return new ResponseEntity<>(statsService.getAllStats(), HttpStatus.OK);
    }

    @RequestMapping("logs")
    public ResponseEntity getLogs() {
        return new ResponseEntity<>(dbLoggerService.getAllLogs(), HttpStatus.OK);
    }
}
