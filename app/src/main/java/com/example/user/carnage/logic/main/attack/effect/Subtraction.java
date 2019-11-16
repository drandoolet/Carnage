package com.example.user.carnage.logic.main.attack.effect;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.example.user.carnage.logic.main.PlayCharacter.SubtractableValue;

public class Subtraction {
    private final Map<SubtractableValue, Entry> entries;

    public Map<SubtractableValue, Entry> getEntries() {
        return entries;
    }

    private Subtraction(Builder builder) {
        entries = builder.map;
    }

    public static class Entry {
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

        public static Entry valueOf(JSONObject object) throws JSONException {
            int effect = object.getInt("effect");
            SubtractionType type = SubtractionType.valueOf(object.getString("type"));
            return newEntry(effect, type);
        }

        public JSONObject toJson() throws JSONException {
            return new JSONObject()
                    .put("type", type)
                    .put("effect", effect);
        }
    }

    public static class Builder {
        private final Map<SubtractableValue, Entry> map;

        public Builder(Subtraction subtraction) {
            map = subtraction.entries;
        }

        public Builder() {
            map = new HashMap<>();
        }

        public Builder setSubtraction(SubtractableValue effectType, int effect, SubtractionType subtractionType) {
            return setSubtraction(effectType, Entry.newEntry(effect, subtractionType));
        }

        public Builder setSubtraction(SubtractableValue effectType, Entry entry) {
            map.put(effectType, entry);
            return this;
        }

        public Subtraction build() {
            return new Subtraction(this);
        }

    }

    public enum SubtractionType {
        ABSOLUTE, RELATIVE_MAX, RELATIVE_CURRENT
    }

    public static class SubtractionFactory {
        public static Builder newSubtractionBuilder() {
            return new Builder();
        }

        public static Subtraction empty() {
            return new Builder().build();
        }
    }
}
