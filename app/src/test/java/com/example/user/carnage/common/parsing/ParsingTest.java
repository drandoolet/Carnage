package com.example.user.carnage.common.parsing;

import com.example.user.carnage.common.logic.main.BodyPart;
import com.example.user.carnage.common.logic.main.PlayerChoice;
import com.example.user.carnage.common.logic.skills.SkillFactory;
import com.example.user.carnage.common.logic.skills.SkillNew;
import com.example.user.carnage.server.roundprocessor.Query;
import com.example.user.carnage.server.roundprocessor.roundelement.RoundResults;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;


import java.util.ArrayList;
import java.util.Arrays;

@RunWith(RobolectricTestRunner.class)
public class ParsingTest {
    @Test
    public void testIsQueryParsedRight() throws JSONException {
        ArrayList<BodyPart.BodyPartNames> attacked = new ArrayList<>(
                Arrays.asList(
                        BodyPart.BodyPartNames.BODY,
                        BodyPart.BodyPartNames.LEGS
                ));
        ArrayList<BodyPart.BodyPartNames> defended = new ArrayList<>(
                Arrays.asList(
                        BodyPart.BodyPartNames.HEAD,
                        BodyPart.BodyPartNames.LEGS
                )
        );
        SkillNew skill = SkillFactory.newSkill(SkillNew.SkillTypes.FIREBALL);
        PlayerChoice playerChoice1 = new PlayerChoice(attacked, defended, skill);
        PlayerChoice playerChoice2 = new PlayerChoice(attacked, defended, skill);

        Query query = new Query(playerChoice1, playerChoice2, RoundResults.Players.PLAYER_1);

        JSONObject object = QueryParser.toJson(query);
        Query query1 = QueryParser.fromJson(object);

        Assert.assertArrayEquals(query.getPlayerChoice().getAttacked().toArray(), query1.getPlayerChoice().getAttacked().toArray());
        Assert.assertArrayEquals(query.getEnemyChoice().getDefended().toArray(), query1.getEnemyChoice().getDefended().toArray());
        Assert.assertEquals(query.getFirstPlayer(), query1.getFirstPlayer());
    }
}
