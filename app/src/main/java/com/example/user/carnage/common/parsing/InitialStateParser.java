package com.example.user.carnage.common.parsing;

import com.example.user.carnage.common.logic.main.PlayCharacter;
import com.example.user.carnage.common.logic.main.State;
import com.example.user.carnage.server.roundprocessor.InitialGameState;
import com.example.user.carnage.server.roundprocessor.roundelement.RoundResults;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class InitialStateParser implements JsonParser {
    private InitialStateParser() {}

    public static JSONObject toJson(InitialGameState state) throws JSONException {
        return new JSONObject()
                .put(JsonField.PLAYER_1_NAME.toString(), state.getName(RoundResults.Players.PLAYER_1))
                .put(JsonField.PLAYER_2_NAME.toString(), state.getName(RoundResults.Players.PLAYER_2))
                .put(JsonField.PLAYER_1_STATE.toString(), stateToJson(state.getState(RoundResults.Players.PLAYER_1)))
                .put(JsonField.PLAYER_2_STATE.toString(), stateToJson(state.getState(RoundResults.Players.PLAYER_2)));
    }

    public static InitialGameState fromJson(JSONObject object) {

    }

    private static JSONArray stateToJson(State state) throws JSONException {
        JSONArray array = new JSONArray();

        for (PlayCharacter.MainScales scales: PlayCharacter.MainScales.values())
            array.put(new JSONObject().put(scales.toString(), state.getMax(scales)));
        for (PlayCharacter.Stats stats: PlayCharacter.Stats.values())
            array.put(new JSONObject().put(stats.toString(), state.getMax(stats)));
        for (PlayCharacter.Substats substats: PlayCharacter.Substats.values())
            array.put(new JSONObject().put(substats.toString(), state.getMax(substats)));
        return array;
    }

    private static State stateFromJson(JSONObject object) throws JSONException {
        Map<PlayCharacter.MainScales, Integer> mainScalesIntegerMap = new HashMap<>();
        Map<PlayCharacter.Stats, Integer> statsIntegerMap = new HashMap<>();
        Map<PlayCharacter.Substats, Integer> substatsIntegerMap = new HashMap<>();

        for (PlayCharacter.MainScales scales: PlayCharacter.MainScales.values())
            mainScalesIntegerMap.put(PlayCharacter.MainScales.valueOf(object.));
        for (PlayCharacter.Stats stats: PlayCharacter.Stats.values())
            statsIntegerMap.put();
        for (PlayCharacter.Substats substats: PlayCharacter.Substats.values())
            substatsIntegerMap.put();

        return new State(mainScalesIntegerMap, statsIntegerMap, substatsIntegerMap);
    }

    private enum JsonField {
        PLAYER_1_NAME,
        PLAYER_2_NAME,
        PLAYER_1_STATE,
        PLAYER_2_STATE
    }
}
