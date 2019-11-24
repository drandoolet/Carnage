package com.example.user.carnage.common.parsing;

import com.example.user.carnage.common.logic.main.PlayCharacter;
import com.example.user.carnage.common.logic.main.State;
import com.example.user.carnage.server.roundprocessor.InitialGameState;
import com.example.user.carnage.server.roundprocessor.roundelement.RoundResults;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InitialStateParser implements JsonParser {
    private InitialStateParser() {}

    public static JSONObject toJson(InitialGameState state) throws JSONException {
        return new JSONObject()
                .put(JsonField.PLAYER_1_NAME.toString(), state.getName(RoundResults.Players.PLAYER_1))
                .put(JsonField.PLAYER_2_NAME.toString(), state.getName(RoundResults.Players.PLAYER_2))
                .put(JsonField.PLAYER_1_STATE.toString(), state.getState(RoundResults.Players.PLAYER_1))
                .put(JsonField.PLAYER_2_STATE.toString(), state.getState(RoundResults.Players.PLAYER_2));
    }

    public static InitialGameState fromJson(JSONObject object) {

    }

    private static JSONArray stateToJson(State state) {
        JSONArray array = new JSONArray();

        for (PlayCharacter.MainScales scales: PlayCharacter.MainScales.values()) {
            array.put(new JSONObject(state.))
        }
    }

    private enum JsonField {
        PLAYER_1_NAME,
        PLAYER_2_NAME,
        PLAYER_1_STATE,
        PLAYER_2_STATE
    }
}
