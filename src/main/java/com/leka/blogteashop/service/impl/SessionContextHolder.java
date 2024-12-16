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
    private final Map<String, List<Instant>> mapHolder = new HashMap<>();

    //need testing todo
    public void checkSpamCounterForSession(HttpSession session) {
        cleanCache(mapHolder);
        String sessionId = session.getId();
        List<Instant> numberOfTries = mapHolder.getOrDefault(sessionId, new ArrayList<>());
        if (numberOfTries.isEmpty()) {
            numberOfTries.add(Instant.now());
            return;
        }
        Instant now = Instant.now();
        List<Instant> newTries = numberOfTries.stream()
                .filter(instant -> Duration.between(now, instant).toMillis() <= Duration.ofHours(1).toMillis())
                .collect(Collectors.toList());
        if (newTries.size() < counterThresholdInOneHour) {
            newTries.add(now);
            mapHolder.put(sessionId, newTries);
        } else {
            throw new CounterLimitException("Spam counter limit reached, try to contact the administrator later");
        }
    }

    private void cleanCache(Map<String, List<Instant>> mapHolder) {
        Instant now = Instant.now();
        List<String> oldSessionIds = mapHolder.entrySet().stream()
                .filter(entry -> getOld(entry, now))
                .map(Map.Entry::getKey)
                .toList();
        if (!oldSessionIds.isEmpty()) {
            oldSessionIds.forEach(mapHolder::remove);
        }
    }

    private static boolean getOld(Map.Entry<String, List<Instant>> entry, Instant now) {
        List<Instant> value = entry.getValue();
        Instant last = value.get(value.size() - 1);
        return Duration.between(now, last).toMillis() > Duration.ofHours(1).toMillis();
    }
}
