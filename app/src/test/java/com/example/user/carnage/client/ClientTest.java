package com.example.user.carnage.client;

import com.example.user.carnage.common.logic.main.BodyPart;
import com.example.user.carnage.common.logic.main.PlayCharacter;
import com.example.user.carnage.common.logic.main.PlayerChoice;
import com.example.user.carnage.common.logic.skills.SkillFactory;
import com.example.user.carnage.common.logic.skills.SkillNew;
import com.example.user.carnage.common.parsing.QueryParser;
import com.example.user.carnage.common.parsing.ResponseParser;
import com.example.user.carnage.server.roundprocessor.BattleRoundProcessor;
import com.example.user.carnage.server.roundprocessor.Query;
import com.example.user.carnage.server.roundprocessor.roundelement.GameOverException;
import com.example.user.carnage.server.roundprocessor.roundelement.RoundResults;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

@RunWith(RobolectricTestRunner.class)
public class ClientTest {
    @Test
    public void testDoesClientGetResponseFromServer() throws JSONException {
        /* ArrayList<BodyPart.BodyPartNames> attacked = new ArrayList<>(
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
        PlayerChoice playerChoice1 = new PlayerChoice(new ArrayList<>(
                Arrays.asList(
                        BodyPart.BodyPartNames.HEAD,
                        BodyPart.BodyPartNames.WAIST
                )), defended, skill.getType());
        PlayerChoice playerChoice2 = new PlayerChoice(attacked, defended);

        Query query = new Query(playerChoice1, playerChoice2, RoundResults.Players.PLAYER_1);

        System.out.println(query);

        JSONObject object = QueryParser.toJson(query);

        BattleRoundProcessor processor = new BattleRoundProcessor(
                PlayCharacter.newTestPlayer("BUCKLYA"),
                PlayCharacter.newTestPlayer("PAVUK"));

        //Query queryParserBack = QueryParser.fromJson(object);
        //System.out.println(queryParserBack);

        JSONObject response = processor.getResponse(object);
        System.out.println(response);

        RoundResults roundResults = ResponseParser.fromJson(response);
*/
        System.out.println(getRoundResults(PlayCharacter.newTestPlayer("BUCKLYA"), PlayCharacter.newTestPlayer("PAVUK")));
    }

    public static PlayerChoice getTestPlayerChoice() {
        ArrayList<BodyPart.BodyPartNames> attacked = new ArrayList<>(
                Arrays.asList(
                        BodyPart.BodyPartNames.LEGS
                ));
        ArrayList<BodyPart.BodyPartNames> defended = new ArrayList<>(
                Arrays.asList(
                        BodyPart.BodyPartNames.HEAD,
                        BodyPart.BodyPartNames.LEGS
                )
        );
        SkillNew skill = SkillFactory.newSkill(SkillNew.SkillTypes.FIREBALL);
        return PlayerChoice.of(attacked, defended, skill.getType());
    }

    public static PlayerChoice getTestPlayerChoice2() {
        ArrayList<BodyPart.BodyPartNames> attacked = new ArrayList<>(
                Arrays.asList(
                        BodyPart.BodyPartNames.WAIST,
                        BodyPart.BodyPartNames.LEGS
                ));
        ArrayList<BodyPart.BodyPartNames> defended = new ArrayList<>(
                Arrays.asList(
                        BodyPart.BodyPartNames.BODY,
                        BodyPart.BodyPartNames.WAIST
                )
        );
        return PlayerChoice.of(attacked, defended, SkillNew.SkillTypes.NULL);
    }

    public static RoundResults getRoundResults(PlayCharacter character, PlayCharacter enemy) throws JSONException {
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
        PlayerChoice playerChoice1 = PlayerChoice.of(new ArrayList<>(
                Arrays.asList(
                        BodyPart.BodyPartNames.HEAD,
                        BodyPart.BodyPartNames.WAIST
                )), defended, skill.getType());
        PlayerChoice playerChoice2 = PlayerChoice.of(attacked, defended);

        Query query = new Query(playerChoice1, playerChoice2, RoundResults.Players.PLAYER_1);

        System.out.println(query);

        JSONObject object = QueryParser.toJson(query);

        BattleRoundProcessor processor = new BattleRoundProcessor(character, enemy);

        JSONObject response = null;
        try {
            response = processor.getResponse(object);
        } catch (GameOverException e) {
            System.out.println("GAME OVER in getResponse() test");
        }
        System.out.println(response);

        RoundResults roundResults = ResponseParser.fromJson(response);

        System.out.println(roundResults);

        return roundResults;
    }


    @Test
    public void testServerConnection() throws Exception {
        ServerConnector connector = ServerConnector.connectSinglePlayer(MainActivity.RPG_PROFILE_1);

        System.out.println("Player1 choice: \n"+getTestPlayerChoice()+"\n\n");

        RoundResults results = connector.getResults(getTestPlayerChoice());

        System.out.println(results);
    }

    @Test
    public void testServerConnectionManyTimes() {
        ServerConnector connector = ServerConnector.connectSinglePlayer(MainActivity.RPG_PROFILE_1);

        for (int i = 0; i < 100; i++) {
            try {
                connector.getResults(getTestPlayerChoice());
            } catch (GameOverException e) {
                break;
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
        System.out.println("    ----  Game finished  ----");
    }
}
