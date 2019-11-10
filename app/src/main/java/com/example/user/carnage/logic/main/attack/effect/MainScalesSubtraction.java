package com.example.user.carnage.logic.main.attack.effect;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.user.carnage.logic.main.PlayCharacter;
import com.example.user.carnage.logic.main.PlayCharacter.MainScales;

import java.util.HashMap;
import java.util.Map;

public class MainScalesSubtraction extends Subtraction<MainScales> {
    private final Map<MainScales, Entry> map;

    public int subtract(PlayCharacter character, MainScales type) {
        Entry entry = map.get(type);

        if (entry == null) return 0;

        switch (entry.getType()) {
            case ABSOLUTE:
                return entry.getEffect();
            case RELATIVE_MAX:
                return (character.getMainScaleState(
                    type, MainScales.Value.MAX_VALUE) * entry.getEffect()) / 100;
            case RELATIVE_CURRENT:
                return (character.getMainScaleState(
                    type, MainScales.Value.CURRENT_VALUE) * entry.getEffect()) / 100;
        }
        throw new IllegalStateException("Wrong MainScalesSubtraction class state");
    }

    public JSONObject toJson() throws JSONException {
        return SubtractionFactory.jsonOf(this);
    }

    private MainScalesSubtraction(Builder builder) {

        this.map = builder.map;
    }

    /*
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

        private static Entry empty() {
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
    */

    public static class Builder {
        private final Map<MainScales, Entry> map;

        public Builder() {
            map = new HashMap<>();
        }

        public Builder setSubtraction(MainScales effectType, int effect, SubtractionType subtractionType) {
            return setSubtraction(effectType, Entry.newEntry(effect, subtractionType));
        }

        public Builder setSubtraction(MainScales effectType, Entry entry) {
            map.put(effectType, entry);
            return this;
        }

        public MainScalesSubtraction build() {
            return new MainScalesSubtraction(this);
        }

    }

    public static class SubtractionFactory {

        public static MainScalesSubtraction newSubtraction(JSONObject object) throws JSONException {
            Builder builder = new Builder();
            Entry hp = Entry.valueOf(object.getJSONObject("hp"));
            Entry mp = Entry.valueOf(object.getJSONObject("mp"));
            Entry sp = Entry.valueOf(object.getJSONObject("sp"));


            return builder
                    .setSubtraction(MainScales.HP, hp)
                    .setSubtraction(MainScales.MP, mp)
                    .setSubtraction(MainScales.SP, sp)
                    .build();

        }

        public static MainScalesSubtraction.Builder newSubtractionBuilder() {
            return new Builder();
        }

        public static MainScalesSubtraction empty() {
            return new Builder()
                    .setSubtraction(MainScales.HP, Entry.empty())
                    .setSubtraction(MainScales.SP, Entry.empty())
                    .setSubtraction(MainScales.MP, Entry.empty())
                    .build();
        }

        /**
         * @param subtraction
         * @return { "hp": {MainScalesSubtraction.Entry JSON}, "mp": {-//-}, "sp": {-//-} }
         * @throws JSONException
         */
        public static JSONObject jsonOf(MainScalesSubtraction subtraction) throws JSONException {
            JSONObject object = new JSONObject();

            object.put("hp", subtraction.map.get(MainScales.HP).toJson());
            object.put("mp", subtraction.map.get(MainScales.MP).toJson());
            object.put("sp", subtraction.map.get(MainScales.SP).toJson());

            return object;
        }
    }
}
