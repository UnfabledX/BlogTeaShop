package com.leka.blogteashop.service.impl;

import com.leka.blogteashop.BaseItTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class SessionContextHolderTest extends BaseItTest {

    @Autowired
    private SessionContextHolder sessionContextHolder;

    @Test
    public void cleanCacheTest() {
        Map<String, List<Instant>> mapHolder = new HashMap<>();
        Instant now = Instant.now();
        //only one old record to get rid of (61 minutes)
        List<Instant> listFirst = getInstantList(now, new int[]{61, 5, 3});
        mapHolder.put("1", listFirst);
        //if all records are old, the whole sessionId key is deleted
        List<Instant> listTwo = getInstantList(now, new int[]{90, 70, 65});
        mapHolder.put("2", listTwo);
        //if all records are less than 60 minutes age, do nothing.
        List<Instant> listThree = getInstantList(now, new int[]{23, 16, 4});
        mapHolder.put("3", listThree);
        sessionContextHolder.cleanCache(mapHolder, Instant.now());
        assertEquals(2, mapHolder.size());
        assertFalse(mapHolder.containsKey("2"));
        assertEquals(2, listFirst.size());
        assertEquals(3, listThree.size());
    }

    private List<Instant> getInstantList(Instant now, int[] whenTriesOccurred) {
        List<Instant> listThree = new ArrayList<>();
        listThree.add(now.minus(whenTriesOccurred[0], ChronoUnit.MINUTES));
        listThree.add(now.minus(whenTriesOccurred[1], ChronoUnit.MINUTES));
        listThree.add(now.minus(whenTriesOccurred[2], ChronoUnit.MINUTES));
        return listThree;
    }


}
