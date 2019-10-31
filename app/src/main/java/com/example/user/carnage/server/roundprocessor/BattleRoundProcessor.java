package com.example.user.carnage.server.roundprocessor;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.user.carnage.logic.main.BodyPart;
import com.example.user.carnage.logic.main.PlayCharacter;
import com.example.user.carnage.logic.main.Subtraction;
import com.example.user.carnage.logic.main.attack.AttackFactory;
import com.example.user.carnage.logic.main.attack.NormalAttack;
import com.example.user.carnage.logic.main.attack.SkillAttack;
import com.example.user.carnage.logic.skills.Fireball;
import com.example.user.carnage.logic.skills.Skill;
import com.example.user.carnage.logic.skills.SkillFactory;
import com.example.user.carnage.server.roundprocessor.roundelement.RoundResults;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class BattleRoundProcessor {
    private final PlayCharacter player_1, player_2;

    public BattleRoundProcessor(PlayCharacter player_1, PlayCharacter player_2) {
        this.player_1 = player_1;
        this.player_2 = player_2;
    }

    /**
     * Clients make plans, but actually do nothing
     *
     * Clients can have their own names, but internally are called player1 and player2
     *
     * Clients send (JSON):
     * atk, def, skills
     *
     * Server processes, creates a list of round elements (n)
     *
     * Server sends (JSON RoundStage):
     * {
     *      player 1 subtract (can be negative, means addition) : {Subtraction hp, sp, mp}, p2 {-//-},
     *      player 1 or 2 (who acts),
     *      Nullable Skill (if null == normal),
     *      Nullable RoundStatus (considered null if Skill != null),
     *      Nullable BodyPartNames target (considered null if Skill != null)
     * } * n
     *
     * First skills, then attacks, all in turns (order provided by Server)
     *
     * For Skills: Client checks if a Skill can be used => uses or skips (saying smth)
     */

    public JSONObject getResponse(RoundResults results) throws JSONException {
        JSONObject main = new JSONObject();
        JSONArray array = new JSONArray();
        for (RoundResults.RoundStage stage: results.getStages()) {
            array.put(new JSONObject()
                    .put("subtraction", new JSONArray().put(new JSONObject()
                            .put("player1", stage.getSubtraction_player_1().toJson())
                            .put("player2", stage.getSubtraction_player_2().toJson())))
                    .put("actor", stage.getActor())
                    .put("skill", stage.getSkill())
                    .put("status", stage.getStatus())
                    .put("target", stage.getTarget())
            );
        }
        return main.put("stages", array);
    }

    /**
     * Show-only
     *
     * Meaning: Player 1 spends 30 points of mana to inflict damage
     * that equals 50% of current HP and SP of Player 2
     *
     * Actor is Player 1 (and he is who spends mana, needs to be first)
     *
     * Then specifies a Skill - Fireball (TODO SkillFactory.class, add Subtractions to Skill.class)
     * @throws JSONException
     */
    private void t() throws JSONException {
        JSONObject object = getResponse(new RoundResults.Builder()
                .addStage(RoundResults.RoundStage.newStageBuilder(
                        Subtraction.SubtractionFactory.newSubtractionBuilder()
                                .setSubtraction(PlayCharacter.MainScalesType.MP, 30, Subtraction.SubtractionType.ABSOLUTE)
                                .build(),
                        Subtraction.SubtractionFactory.newSubtractionBuilder()
                                .setSubtraction(PlayCharacter.MainScalesType.HP, 50, Subtraction.SubtractionType.RELATIVE_CURRENT)
                                .setSubtraction(PlayCharacter.MainScalesType.SP, 50, Subtraction.SubtractionType.RELATIVE_CURRENT)
                                .build(),
                        new PlayCharacter(player_1, "test ACTOR")
                )
                        .setSkill(SkillFactory.newSkill(Skill.SkillTypes.FIREBALL, player_1, player_2))
                        .build())
                //.addStage( bla bla line 2)
                .build());

        Skill fireball = SkillFactory.newSkill(Skill.SkillTypes.FIREBALL, player_1, player_2);
        SkillAttack skillAttack = AttackFactory.newSkillAttack(fireball);

        NormalAttack normalAttack = AttackFactory.newNormalAttack(
                Subtraction.SubtractionFactory.empty(),
                Subtraction.SubtractionFactory.newSubtractionBuilder()
                        .setSubtraction(PlayCharacter.MainScalesType.HP, 30, Subtraction.SubtractionType.ABSOLUTE)
                        .build(),
                PlayCharacter.RoundStatus.NORMAL,
                BodyPart.BodyPartNames.BODY);

        JSONObject object1 = getResponse(new RoundResults.Builder()
                .addStage(RoundResults.RoundStage.newStageBuilder(normalAttack, player_1).build())
                .addStage(RoundResults.RoundStage.newStageBuilder(skillAttack, player_2).build())
                .build());
    }


    enum Players {
        PLAYER_1, PLAYER_2
    }
}
