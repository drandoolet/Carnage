package com.example.user.carnage.common.parsing;

import com.example.user.carnage.logic.main.PlayCharacter;
import com.example.user.carnage.logic.main.PlayCharacter.SubtractableValue;
import com.example.user.carnage.logic.main.attack.effect.Subtraction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

class SubtractionParser implements JsonParser {
    private SubtractionParser() {}

    static JSONObject toJson(Subtraction subtraction) throws JSONException {
        Map<SubtractableValue, Subtraction.Entry> map = subtraction.getEntries();
        JSONArray array = new JSONArray();

        for (SubtractableValue value : map.keySet()) {
            array.put(new JSONObject()
                    .put(JsonField.SUBTRACTION_TYPE.toString(), value.toString())
                    .put(JsonField.SUBTRACTION_VALUE.toString(), subtractionEntryToJson(map.get(value))));
        }

        return new JSONObject()
                .put(JsonField.SUBTRACTION_MAP.toString(), array);
    }

    static Subtraction fromJson(JSONObject object) throws JSONException {
        JSONArray array = object.getJSONArray(JsonField.SUBTRACTION_MAP.toString());
        Subtraction.Builder builder = new Subtraction.Builder();

        for (int i = 0; i < array.length(); i++) {
            JSONObject o = array.getJSONObject(i);
            builder.setSubtraction(
                    subtractableValueFromJson(o),
                    subtractionEntryFromJson(o.getJSONObject(JsonField.SUBTRACTION_VALUE.toString())));
        }

        return builder.build();
    }

    private static SubtractableValue subtractableValueFromJson(JSONObject o) throws JSONException {
        return PlayCharacter.findValue(o.getString(JsonField.SUBTRACTION_TYPE.toString()));
    }

    private static JSONObject subtractionEntryToJson(Subtraction.Entry entry) throws JSONException {
        return entry.toJson();
    }

    private static Subtraction.Entry subtractionEntryFromJson(JSONObject object) throws JSONException {
        return Subtraction.Entry.valueOf(object);
    }

    private enum JsonField {
        SUBTRACTION_TYPE("type"),
        SUBTRACTION_VALUE("value"),
        SUBTRACTION_MAP("map");

        private final String name;
        JsonField(String n) {
            name = n;
        }
    }
}
