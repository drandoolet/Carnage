package com.example.user.carnage.common.parsing;

import android.os.Build;

import com.example.user.carnage.logic.main.BodyPart;
import com.example.user.carnage.logic.main.PlayerChoice;
import com.example.user.carnage.logic.skills.SkillNew;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlayerChoiceParser implements JsonParser {

    private PlayerChoiceParser() {}

    public static PlayerChoice fromJson(JSONObject object) throws JSONException {
        ArrayList<BodyPart.BodyPartNames> atk = new ArrayList<>();
        ArrayList<BodyPart.BodyPartNames> def = new ArrayList<>();
        SkillNew skill = SkillParser.fromJson(object.getJSONObject(JsonField.SKILL.toString()));
        JSONArray atkObj = object.getJSONArray(JsonField.ATTACKS.toString());
        JSONArray defObj = object.getJSONArray(JsonField.DEFENDS.toString());

        for (int i = 0; i < atkObj.length(); i++) {
            atk.add(BodyPart.BodyPartNames.valueOf(atkObj.getString(i)));
        }
        for (int i = 0; i < defObj.length(); i++) {
            def.add(BodyPart.BodyPartNames.valueOf(defObj.getString(i)));
        }

        return new PlayerChoice(atk, def, skill);
    }

    public static JSONObject toJson(PlayerChoice playerChoice) throws JSONException {
        SkillNew skillNew = playerChoice.getSkill();
        List<BodyPart.BodyPartNames> atk = new ArrayList<>(playerChoice.getAttacked());
        List<BodyPart.BodyPartNames> def = new ArrayList<>(playerChoice.getDefended());
        JSONArray atkArr = new JSONArray();
        JSONArray defArr = new JSONArray();

        if (Build.VERSION.SDK_INT > 23) {
            atk.forEach(atkArr::put);
            def.forEach(defArr::put);
        } else {
            for (BodyPart.BodyPartNames n : atk)
                atkArr.put(n);
            for (BodyPart.BodyPartNames n : def)
                defArr.put(n);
        }

        return new JSONObject()
                .put(JsonField.ATTACKS.toString(), atkArr)
                .put(JsonField.DEFENDS.toString(), defArr)
                .put(JsonField.SKILL.toString(), SkillParser.toJson(skillNew));
    }

    private enum JsonField {
        ATTACKS("atk"),
        DEFENDS("def"),
        SKILL("skill");

        private final String name;
        JsonField(String n) {
            name = n;
        }
    }
}
