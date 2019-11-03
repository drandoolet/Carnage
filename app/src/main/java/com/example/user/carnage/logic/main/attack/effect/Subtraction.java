package com.example.user.carnage.logic.main.attack.effect;

import com.example.user.carnage.logic.main.PlayCharacter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

abstract class Subtraction <T extends PlayCharacter.SubtractableValue> {
    private final Map<T, Entry> entries;

    static class Entry {
        private int effect;
        private SubtractionType type;

        private Entry(int effect, SubtractionType type) {
            this.effect = effect;
            this.type = type;
        }

        int getEffect() { return effect; }
        SubtractionType getType() { return type; }

        static Entry newEntry(int effect, SubtractionType type) {
            return new Entry(effect, type);
        }

        static Entry empty() {
            return newEntry(0, SubtractionType.ABSOLUTE);
        }

        static Entry valueOf(JSONObject object) throws JSONException {
            int effect = object.getInt("effect");
            SubtractionType type = SubtractionType.valueOf(object.getString("type"));
            return newEntry(effect, type);
        }

        JSONObject toJson() throws JSONException {
            return new JSONObject()
                    .put("type", type)
                    .put("effect", effect);
        }
    }

    public static class Builder <T> {
        private final Map<T, Entry> map;

        public Builder() {
            map = new HashMap<>();
        }

        public Builder setSubtraction(T effectType, int effect, SubtractionType subtractionType) {
            map.put(effectType, new Entry(effect, subtractionType));
            return this;
        }

        public Builder setSubtraction(T effectType, Entry entry) {
            map.put(effectType, entry);
            return this;
        }

        public Subtraction build() {
            return null;
        }

    }

    public enum SubtractionType {
        ABSOLUTE, RELATIVE_MAX, RELATIVE_CURRENT
    }
}
