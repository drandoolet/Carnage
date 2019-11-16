package com.example.user.carnage.common.parsing;

import com.example.user.carnage.server.roundprocessor.Query;
import com.example.user.carnage.server.roundprocessor.roundelement.RoundResults;

import org.json.JSONException;
import org.json.JSONObject;

public class QueryParser implements JsonParser {
    private QueryParser() {}

    public static JSONObject toJson(Query query) throws JSONException {
        return new JSONObject()
                .put(JsonField.FIRST_PLAYER.toString(), query.getFirstPlayer())
                .put(JsonField.PLAYER_CHOICE.toString(), PlayerChoiceParser.toJson(query.getPlayerChoice()))
                .put(JsonField.ENEMY_CHOICE.toString(), PlayerChoiceParser.toJson(query.getEnemyChoice()));
    }

    public static Query fromJson(JSONObject object) throws JSONException {
        return new Query(
                PlayerChoiceParser.fromJson(object.getJSONObject(JsonField.PLAYER_CHOICE.toString())),
                PlayerChoiceParser.fromJson(object.getJSONObject(JsonField.ENEMY_CHOICE.toString())),
                RoundResults.Players.valueOf(object.getString(JsonField.FIRST_PLAYER.toString()))
        );
    }

    private enum JsonField {
        FIRST_PLAYER("first"),
        PLAYER_CHOICE("player choice"),
        ENEMY_CHOICE("enemy choice");

        private final String name;
        JsonField(String n) {
            name = n;
        }
    }
}
