package com.example.user.carnage.logic.main;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.user.carnage.logic.main.PlayCharacter.MainScalesType;

import java.util.HashMap;
import java.util.Map;

public class Subtraction {
    private final Map<MainScalesType, Entry> map;

    public int subtract(PlayCharacter character, MainScalesType type) {
        Entry entry = map.get(type);

        switch (entry.type) {
            case ABSOLUTE:
                return entry.effect;
            case RELATIVE_MAX:
                return (character.getMainScaleState(
                    type, MainScalesType.Value.MAX_VALUE) * entry.effect) / 100;
            case RELATIVE_CURRENT:
                return (character.getMainScaleState(
                    type, MainScalesType.Value.CURRENT_VALUE) * entry.effect) / 100;
        }
        throw new IllegalStateException("Wrong Subtraction class state");
    }

    public JSONObject toJson() throws JSONException {
        return SubtractionFactory.jsonOf(this);
    }

    private Subtraction(Builder builder) {
        this.map = builder.map;
    }

    private static class Entry {
        private int effect;
        private SubtractionType type;

        private Entry(int effect, SubtractionType type) {
            this.effect = effect;
            this.type = type;
        }

        private static Entry newEntry(int effect, SubtractionType type) {
            return new Entry(effect, type);
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

    public static class Builder {
        private final Map<MainScalesType, Entry> map;

        public Builder() {
            map = new HashMap<>();
        }

        public Builder setSubtraction(MainScalesType effectType, int effect, SubtractionType subtractionType) {
            map.put(effectType, new Entry(effect, subtractionType));
            return this;
        }

        public Builder setSubtraction(MainScalesType effectType, Entry entry) {
            map.put(effectType, entry);
            return this;
        }

        public Subtraction build() {
            return new Subtraction(this);
        }

    }

    public static class SubtractionFactory {

        public static Subtraction newSubtraction(JSONObject object) throws JSONException {
            Builder builder = new Builder();
            Entry hp = Entry.valueOf(object.getJSONObject("hp"));
            Entry mp = Entry.valueOf(object.getJSONObject("mp"));
            Entry sp = Entry.valueOf(object.getJSONObject("sp"));


            return builder
                    .setSubtraction(MainScalesType.HP, hp)
                    .setSubtraction(MainScalesType.MP, mp)
                    .setSubtraction(MainScalesType.SP, sp)
                    .build();

        }

        public static Subtraction.Builder newSubtractionBuilder() {
            return new Builder();
        }

        /**
         * @param subtraction
         * @return { "hp": {Subtraction.Entry JSON}, "mp": {-//-}, "sp": {-//-} }
         * @throws JSONException
         */
        public static JSONObject jsonOf(Subtraction subtraction) throws JSONException {
            JSONObject object = new JSONObject();

            object.put("hp", subtraction.map.get(MainScalesType.HP).toJson());
            object.put("mp", subtraction.map.get(MainScalesType.MP).toJson());
            object.put("sp", subtraction.map.get(MainScalesType.SP).toJson());

            return object;
        }
    }

    public enum SubtractionType {
        ABSOLUTE, RELATIVE_MAX, RELATIVE_CURRENT
    }
}
