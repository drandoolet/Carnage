package com.example.user.carnage.common.parsing;

import com.example.user.carnage.server.roundprocessor.roundelement.RoundResults;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ResponseParser implements JsonParser {
    private ResponseParser() {}

    public static JSONObject toJson(RoundResults results) throws JSONException {
        JSONArray array = new JSONArray();

        for (RoundResults.RoundStage stage: results.getStages())
            array.put(roundStageToJson(stage));
        return new JSONObject()
                .put(JsonField.STAGES.toString(), array);
    }

    public static RoundResults fromJson(JSONObject object) throws JSONException {
        RoundResults.Builder builder = RoundResults.Builder.newInstance();
        JSONArrayAdapter.Iterator<JSONObject> iterator = new JSONArrayAdapter(
                object.getJSONArray(JsonField.STAGES.toString())).iterator();

        while (iterator.hasNext())
            builder.addStage(roundStageFromJson(iterator.next()));

        return builder.build();
    }

    private static JSONObject roundStageToJson(RoundResults.RoundStage stage) throws JSONException {
        return new JSONObject()
                .put(JsonField.SUBTRACTOR.toString(), SubtractorParser.toJson(stage.getSubtractor()))
                .put(JsonField.FIRST_PLAYER.toString(), stage.getActor());
    }

    private static RoundResults.RoundStage roundStageFromJson(JSONObject object) throws JSONException {
        return RoundResults.RoundStage.newStageBuilder(
                SubtractorParser.fromJson(object.getJSONObject(JsonField.SUBTRACTOR.toString())),
                RoundResults.Players.valueOf(object.getString(JsonField.FIRST_PLAYER.toString()))
        )
                .build();
    } 

    private enum JsonField {
        STAGES("stages"),
        SUBTRACTOR("subtractor"),
        FIRST_PLAYER("actor");

        private final String name;

        JsonField(String name) {
            this.name = name;
        }
    }
}
