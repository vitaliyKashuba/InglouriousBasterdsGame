package springApplication.db.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springApplication.db.model.LogEntity;
import springApplication.db.repository.LogRepository;

import java.time.LocalDateTime;

/**
 * because heroku logger add-ons keep logs only for 7 days for free
 * // TODO merge with or replace lombok @Slf4j ?
 */
@Service
public class DbLoggerService {

    @Autowired
    LogRepository logRepository;

    public void log(String message) {
        log(message, "", "");
    }

    public void log(String message, String severity) {
        log(message, severity, "");
    }

    public void log(String message, String severity, String method) {
        LogEntity log = new LogEntity();
        log.setMethod(method);
        log.setSeverity(severity);
        log.setMessage(message);
        log.setDateTime(LocalDateTime.now());
        System.out.println(log);
        logRepository.save(log);
    }
}
