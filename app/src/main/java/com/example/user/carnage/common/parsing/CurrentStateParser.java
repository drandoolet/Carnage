package com.example.user.carnage.common.parsing;

import com.example.user.carnage.common.logic.main.PlayCharacter;
import com.example.user.carnage.common.logic.main.State;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

class CurrentStateParser implements JsonParser {
    private CurrentStateParser() {
    }

    public static JSONObject toJson(State.MainScalesState state) throws JSONException {
        JSONObject object = new JSONObject();
        for (PlayCharacter.MainScales scale : PlayCharacter.MainScales.values())
            object.put(scale.toString(), state.getState(scale));

        return object;
    }

    public static State.MainScalesState fromJson(JSONObject object) throws JSONException {
        Map<PlayCharacter.MainScales, Integer> map = new HashMap<>();

        for (PlayCharacter.MainScales scale : PlayCharacter.MainScales.values())
            map.put(scale, object.getInt(scale.toString()));

        return new State.MainScalesState(map);
    }
}
