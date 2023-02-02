package com.example.test.component.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class JsonDiffMaker {

    private ObjectMapper objectMapper = new ObjectMapper();


    public List<String> getUpdateDetailsWithExcludeKeyList(Object o1, Object o2, List<String> list) {
        List<String> updateDetailsList = new ArrayList<>();

        MapDifference<String, Object> difference = getDifferenceMap(
                getMapByObject(o1), getMapByObject(o2)
        );

        difference.entriesDiffering().forEach(
                (key, value) -> {
                    boolean addFlag = true;
                    for (String s : list) {
                        if(key.equals(s)) {
                            addFlag = false;
                        }
                    }
                    if (addFlag) {
                        String item = "{\"" + key + "\":\"" + value.rightValue() + "\"}";
                        updateDetailsList.add(item);
                    }
                }
        );

        return updateDetailsList;
    }

    private Map getMapByObject(Object object) {
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.convertValue(object, Map.class);
    }

    private MapDifference<String, Object> getDifferenceMap(Map m1, Map m2) {
        return Maps.difference(m1, m2);
    }

}
