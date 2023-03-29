package com.example.test.component.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JsonConverter {

    public JSONObject toJson(ArrayList<String> keys, ArrayList<Object> values) throws JSONException {
        JSONObject jsonObject = new JSONObject();

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            Object value = values.get(i);
            jsonObject.put(key, toJsonValue(value));
        }

        return jsonObject;
    }

    private Object toJsonValue(Object value) throws JSONException {
        if (value == null) {
            return "null";
        }

        if (value instanceof Map) {
            JSONObject jo = new JSONObject();
            Set<? extends Map.Entry<?, ?>> entries = ((Map<?, ?>) value).entrySet();
            for (Map.Entry<?, ?> entry : entries) {
                if (entry.getValue() instanceof Map) {
                    jo.put(entry.getKey().toString(), toJsonValue(entry.getValue()));
                } else {
                    if (entry.getValue() != null) {
                        jo.put(entry.getKey().toString(), entry.getValue());
                    } else {
                        jo.put(entry.getKey().toString(), "null");
                    }
                }
            }
            return jo;
        } else if (value instanceof List) {
            JSONArray ja = new JSONArray();
            List<?> list = (List<?>) value;
            for (Object o : list) {
                ja.put(toJsonValue(o));
            }
        }
        return value;
    }

}
