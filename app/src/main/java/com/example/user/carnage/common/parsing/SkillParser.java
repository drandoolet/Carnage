package com.example.user.carnage.common.parsing;

import com.example.user.carnage.common.logic.skills.SkillFactory;
import com.example.user.carnage.common.logic.skills.SkillNew;

import org.json.JSONException;
import org.json.JSONObject;

class SkillParser implements JsonParser {
    private SkillParser() {}

    static JSONObject toJson(SkillNew.SkillTypes skill) throws JSONException {
        return new JSONObject().put(JsonField.TYPE.toString(), skill.toString());
    }

    static SkillNew.SkillTypes fromJson(JSONObject object) throws JSONException {
        return SkillNew.SkillTypes.valueOf(
                        object.getString(JsonField.TYPE.toString()));
    }

    static boolean isSkillNull(JSONObject object) throws JSONException {
        return false;
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
