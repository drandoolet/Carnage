package com.example.user.carnage.common.parsing;

import com.example.user.carnage.logic.skills.SkillFactory;
import com.example.user.carnage.logic.skills.SkillNew;

import org.json.JSONException;
import org.json.JSONObject;

class SkillParser implements JsonParser {
    private SkillParser() {}

    static JSONObject toJson(SkillNew skill) throws JSONException {
        return new JSONObject().put(JsonField.TYPE.toString(), skill.getType().toString());
    }

    static SkillNew fromJson(JSONObject object) throws JSONException {
        return SkillFactory.newSkill(
                SkillNew.SkillTypes.valueOf(
                        object.getString(JsonField.TYPE.toString())
                )
        );
    }

    private enum JsonField {
        TYPE("type")
        ;

        private String name;

        JsonField(String name) {
            this.name = name;
        }
    }
}
