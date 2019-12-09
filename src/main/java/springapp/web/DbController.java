package springapp.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springapp.db.service.CharacterStatsService;
import springapp.db.service.DbLoggerService;

@RestController
public class DbController {

    @Autowired
    private DbLoggerService dbLoggerService;

    @Autowired
    private CharacterStatsService statsService;

    @RequestMapping("stats")
    public ResponseEntity getStats() {
        return new ResponseEntity<>(statsService.getAllStats(), HttpStatus.OK);
    }

    @RequestMapping("logs")
    public ResponseEntity getLogs() {
        return new ResponseEntity<>(dbLoggerService.getAllLogs(), HttpStatus.OK);
    }
}
