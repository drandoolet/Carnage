package com.example.user.carnage.server.roundprocessor.roundelement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.user.carnage.logic.main.BodyPart;
import com.example.user.carnage.logic.main.PlayCharacter;
import com.example.user.carnage.logic.main.Subtraction;
import com.example.user.carnage.logic.main.Subtractor;
import com.example.user.carnage.logic.main.attack.AttackFactory;
import com.example.user.carnage.logic.main.Subtraction.SubtractionFactory;
import com.example.user.carnage.logic.main.PlayCharacter.RoundStatus;
import com.example.user.carnage.logic.main.BodyPart.BodyPartNames;
import com.example.user.carnage.logic.skills.SkillFactory;
import com.example.user.carnage.server.roundprocessor.roundelement.RoundResults.RoundStage;
import com.example.user.carnage.server.roundprocessor.roundelement.RoundResults.Players;


public class RoundResultsJsonParser {
    private static final String SUBTRACTION = "subtraction";
    public static final String PLAYER_1 = "player1";
    public static final String PLAYER_2 = "player2";
    private static final String ACTOR = "actor";
    private static final String SKILL = "skill";
    private static final String STATUS = "status";
    private static final String TARGET = "target";


    static JSONObject toJson(RoundStage stage) throws JSONException {
        return new JSONObject()
                .put(SUBTRACTION, new JSONObject()
                        .put(PLAYER_1, stage.getSubtraction_player_1().toJson())
                        .put(PLAYER_2, stage.getSubtraction_player_2().toJson()))
                .put(ACTOR, stage.getActor())
                .put(SKILL, stage.getSkill())
                .put(STATUS, stage.getStatus())
                .put(TARGET, stage.getTarget());
    }

    static RoundStage fromJson(JSONObject object) throws JSONException {
        return RoundStage.newStageBuilder(
                parseSubtractor(object),
                parsePlayers(object)
        ).build();
    }

    private static Subtractor parseSubtractor(JSONObject object) throws JSONException {
        if (object.isNull(SKILL)) {
            return AttackFactory.newNormalAttack(
                    SubtractionFactory.newSubtraction(object.getJSONObject(SUBTRACTION).getJSONObject(PLAYER_1)),
                    SubtractionFactory.newSubtraction(object.getJSONObject(SUBTRACTION).getJSONObject(PLAYER_2)),
                    RoundStatus.valueOf(object.getString(STATUS)),
                    BodyPartNames.valueOf(object.getString(TARGET))
            );
        } else return AttackFactory.newSkillAttack(
                SubtractionFactory.newSubtraction(object.getJSONObject(SUBTRACTION).getJSONObject(PLAYER_1)),
                SubtractionFactory.newSubtraction(object.getJSONObject(SUBTRACTION).getJSONObject(PLAYER_2)),
                SkillFactory.newSkill()
        );
    }

    private static Players parsePlayers(JSONObject object) throws JSONException {
        return Players.find(object.getString(ACTOR));
    }
}
