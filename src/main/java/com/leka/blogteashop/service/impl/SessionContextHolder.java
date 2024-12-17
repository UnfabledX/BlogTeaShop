package com.leka.blogteashop.service.impl;

import com.leka.blogteashop.exception.CounterLimitException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SessionContextHolder {

    @Value("${contact.tries.threshold}")
    private int counterThresholdInOneHour;
    @Value("${contact.tries.cooldown}")
    private Duration coolDownDuration;
    private final Map<String, List<Instant>> mapHolder = new HashMap<>();

    public void checkSpamCounterForSession(HttpSession session) {
        Instant now = Instant.now();
        if (mapHolder.size() > 50) {
            cleanCache(mapHolder, now);
        }
        String sessionId = session.getId();
        List<Instant> numberOfTries = mapHolder.getOrDefault(sessionId, new ArrayList<>());
        if (numberOfTries.isEmpty()) {
            numberOfTries.add(now);
            mapHolder.put(sessionId, numberOfTries);
            return;
        }
        List<Instant> newTries = numberOfTries.stream()
                .filter(instant -> Duration.between(now, instant).toMillis() <= coolDownDuration.toMillis())
                .collect(Collectors.toList());
        if (newTries.size() < counterThresholdInOneHour) {
            newTries.add(now);
            mapHolder.put(sessionId, newTries);
        } else {
            throw new CounterLimitException("Spam counter limit reached, try to contact the administrator later");
        }
    }

    public void cleanCache(Map<String, List<Instant>> mapHolder, Instant now) {
        List<String> oldSessionIds = new ArrayList<>();
        for (Map.Entry<String, List<Instant>> entry : mapHolder.entrySet()) {
            String sessionId = entry.getKey();
            List<Instant> tries = entry.getValue();
            if (areAllOldRecords(tries, now)) {
                oldSessionIds.add(sessionId);
                continue;
            }
            tries.removeIf(instant -> isOldRecord(instant, now));
        }
        if (!oldSessionIds.isEmpty()) {
            oldSessionIds.forEach(mapHolder::remove);
        }
    }

    private boolean areAllOldRecords(List<Instant> tries, Instant now) {
        Instant last = tries.get(tries.size() - 1);
        return isOldRecord(last, now);
    }

    private boolean isOldRecord(Instant record, Instant now) {
        return Duration.between(record, now).toMillis() > coolDownDuration.toMillis();
    }
}
