package com.example.user.carnage.common.parsing;

import com.example.user.carnage.logic.main.BodyPart;
import com.example.user.carnage.logic.main.PlayCharacter;
import com.example.user.carnage.logic.main.attack.AttackFactory;
import com.example.user.carnage.logic.main.attack.NormalAttack;
import com.example.user.carnage.logic.main.attack.SkillAttack;
import com.example.user.carnage.logic.main.attack.effect.Subtraction;
import com.example.user.carnage.logic.main.attack.effect.Subtractor;

import org.json.JSONException;
import org.json.JSONObject;

public class SubtractorParser implements JsonParser {
    private SubtractorParser() {}

    public static JSONObject toJson(Subtractor subtractor) throws JSONException {
        JSONObject object = new JSONObject();

        if (subtractor instanceof NormalAttack) {
            object
                    .put(JsonField.STATUS.toString(), ((NormalAttack) subtractor).getRoundStatus().toString())
                    .put(JsonField.TARGET.toString(), ((NormalAttack) subtractor).getTarget().toString());
        } else if (subtractor instanceof SkillAttack) {
            object
                    .put(JsonField.SKILL.toString(), SkillParser.toJson(((SkillAttack) subtractor).getSkill()));
        } else throw new IllegalArgumentException();

        return object
                .put(JsonField.SUBTRACTION_PLAYER_1.toString(), SubtractionParser.toJson(subtractor.getActorSubtraction()))
                .put(JsonField.SUBTRACTION_PLAYER_2.toString(), SubtractionParser.toJson(subtractor.getEnemySubtraction()));
    }

    public static Subtractor fromJson(JSONObject object) throws JSONException {
        Subtraction sp1 = SubtractionParser.fromJson(object.getJSONObject(JsonField.SUBTRACTION_PLAYER_1.toString()));
        Subtraction sp2 = SubtractionParser.fromJson(object.getJSONObject(JsonField.SUBTRACTION_PLAYER_2.toString()));

        if (object.get(JsonField.SKILL.toString()) != null) {
            return AttackFactory.newSkillAttack(
                    sp1, sp2,
                    SkillParser.fromJson(object.getJSONObject(JsonField.SKILL.toString()))
            );
        } else { // TODO can Target be null if no attack? (only def)
            return AttackFactory.newNormalAttack(
                    sp1, sp2,
                    PlayCharacter.RoundStatus.valueOf(object.getString(JsonField.STATUS.toString())),
                    BodyPart.BodyPartNames.valueOf(object.getString(JsonField.TARGET.toString()))
            );
        }
    }

    private enum JsonField {
        SUBTRACTION_PLAYER_1("sub_player1"),
        SUBTRACTION_PLAYER_2("sub_player2"),
        SKILL("skill"),
        TARGET("target"),
        STATUS("status");

        private String name;

        JsonField(String name) {
            this.name = name;
        }
    }
}
