package com.example.user.carnage.server.roundprocessor;

import com.example.user.carnage.logic.main.BodyPart;
import com.example.user.carnage.logic.main.PlayCharacter;
import com.example.user.carnage.logic.main.PlayerChoice;
import com.example.user.carnage.logic.main.attack.effect.MainScalesSubtraction;
import com.example.user.carnage.logic.main.attack.AttackFactory;
import com.example.user.carnage.logic.main.attack.NormalAttack;
import com.example.user.carnage.logic.main.attack.SkillAttack;
import com.example.user.carnage.logic.main.attack.effect.Subtraction;
import com.example.user.carnage.logic.skills.Skill;
import com.example.user.carnage.logic.skills.SkillFactory;
import com.example.user.carnage.server.roundprocessor.roundelement.RoundResults;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;


/**
 * Get JSON from Server
 * Parse JSON , using: Parser
 * Process , using: Processor
 * Return: RoundResults
 * Pass to Parser
 * Create JSON, return to Server
 */
class BattleRoundProcessor {
    private final PlayCharacter player_1, player_2;
    private final Random random = new Random();

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
     *      player 1 subtract (can be negative, means addition) : {MainScalesSubtraction hp, sp, mp}, p2 {-//-},
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
        /*
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
        */
        for (RoundResults.RoundStage stage : results.getStages())
            array.put(stage.toJson());

        return main.put("stages", array);
    }

    private RoundResults process(PlayerChoice player1Choice, PlayerChoice player2Choice) {

    }

    private RoundResults.RoundStage getStage(
            BodyPart.BodyPartNames attack,
            List<BodyPart.BodyPartNames> defended,
            PlayCharacter pc1,
            PlayCharacter pc2) {
        PlayCharacter.RoundStatus status = getRoundStatus(pc1, pc2, attack, defended);
        Subtractions subtractions = calculateAttack(pc1, pc2, status, attack);
    }

    private Subtractions calculateAttack(PlayCharacter pc1, PlayCharacter pc2,
                                         PlayCharacter.RoundStatus status,
                                         BodyPart.BodyPartNames target) {
        double criticalMag = 1.0;

        switch (status) {
            case DODGE:
            case BLOCK: criticalMag = 0.0; break;
            case CRITICAL: criticalMag = pc1.getCriticalDmg(); break;
            case BLOCK_BREAK: criticalMag = pc1.getCriticalDmg() * 1.5; break;
        }

        int[] power = pc1.getPower();
        int kick = (int) (((power[0] + random.nextInt(power[1] - power[0]))
                * target.getAdjustion() - pc2.getDefence()) * criticalMag);
        if (kick < 0) kick = 1;

        return new Subtractions(
                MainScalesSubtraction.SubtractionFactory.empty(),
                MainScalesSubtraction.SubtractionFactory.newSubtractionBuilder()
                        .setSubtraction(PlayCharacter.MainScales.HP, kick, Subtraction.SubtractionType.ABSOLUTE)
                        .build());
    }

    private PlayCharacter.RoundStatus getRoundStatus(PlayCharacter pc1,
                                                     PlayCharacter pc2,
                                                     BodyPart.BodyPartNames attack,
                                                     List<BodyPart.BodyPartNames> defended) {
        PlayCharacter.RoundStatus status;
        if (hasDodged(pc1, pc2))
            status = PlayCharacter.RoundStatus.DODGE;
        else {
            boolean isCrit = isCritical(pc1, pc2);
            boolean isBlocked = isBlocked(attack, defended);

            if (isCrit) {
                if (isBlocked) status = PlayCharacter.RoundStatus.BLOCK_BREAK;
                else status = PlayCharacter.RoundStatus.CRITICAL;
            } else if (isBlocked) status = PlayCharacter.RoundStatus.BLOCK;
            else status = PlayCharacter.RoundStatus.NORMAL;
        }
        return status;
    }

    private boolean hasDodged(PlayCharacter pc1, PlayCharacter pc2) {
        int dodgeRate = 10;
        if (pc2.getDodgeRate() != pc1.getAntiDodgeRate()) {
            int rate;
            rate = ((pc2.getDodgeRate() - pc1.getAntiDodgeRate()) / pc2.getDodgeRate());
            rate = rate *50 /2 +30;
            if (rate <10) rate = 10;
            dodgeRate = rate;
        }
        return random.nextInt(100) > dodgeRate;
    }

    private boolean isCritical(PlayCharacter pc1, PlayCharacter pc2) {
        int critRate = 10;
        if (pc2.getCritical() != pc1.getAntiCritical()) {
            critRate = ((pc1.getCritical() - pc2.getAntiCritical()) / pc1.getCritical());
            critRate = critRate *50 /2 +30;
            if (critRate <10) critRate = 10;
        }
        return random.nextInt(100) > critRate;
    }

    private boolean isBlocked(BodyPart.BodyPartNames attack, List<BodyPart.BodyPartNames> defended) {
        return defended.contains(attack);
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
        /*
        JSONObject object = getResponse(new RoundResults.Builder()
                .addStage(RoundResults.RoundStage.newStageBuilder(
                        MainScalesSubtraction.SubtractionFactory.newSubtractionBuilder()
                                .setSubtraction(PlayCharacter.MainScales.MP, 30, MainScalesSubtraction.SubtractionType.ABSOLUTE)
                                .build(),
                        MainScalesSubtraction.SubtractionFactory.newSubtractionBuilder()
                                .setSubtraction(PlayCharacter.MainScales.HP, 50, MainScalesSubtraction.SubtractionType.RELATIVE_CURRENT)
                                .setSubtraction(PlayCharacter.MainScales.SP, 50, MainScalesSubtraction.SubtractionType.RELATIVE_CURRENT)
                                .build(),
                        RoundResults.Players.PLAYER_1
                )
                        .setSkill(SkillFactory.newSkill(Skill.SkillTypes.FIREBALL, player_1, player_2))
                        .build())
                //.addStage( bla bla line 2)
                .build());

        Skill fireball = SkillFactory.newSkill(Skill.SkillTypes.FIREBALL, player_1, player_2);
        SkillAttack skillAttack = AttackFactory.newSkillAttack(fireball);

        NormalAttack normalAttack = AttackFactory.newNormalAttack(
                MainScalesSubtraction.SubtractionFactory.empty(),
                MainScalesSubtraction.SubtractionFactory.newSubtractionBuilder()
                        .setSubtraction(PlayCharacter.MainScales.HP, 30, MainScalesSubtraction.SubtractionType.ABSOLUTE)
                        .build(),
                PlayCharacter.RoundStatus.NORMAL,
                BodyPart.BodyPartNames.BODY);

        JSONObject object1 = getResponse(new RoundResults.Builder()
                .addStage(RoundResults.RoundStage.newStageBuilder(normalAttack, RoundResults.Players.PLAYER_1).build())
                .addStage(RoundResults.RoundStage.newStageBuilder(skillAttack, RoundResults.Players.PLAYER_2).build())
                .build());


        NormalAttack normalAttack1 = AttackFactory.newNormalAttack(
                MainScalesSubtraction.SubtractionFactory.empty(),
                new Subtraction.Builder<PlayCharacter.MainScales>()
                        .setSubtraction(PlayCharacter.MainScales.HP, 50, Subtraction.SubtractionType.ABSOLUTE)
                        .build(),
                PlayCharacter.RoundStatus.CRITICAL,
                BodyPart.BodyPartNames.HEAD
        );

        SkillAttack skillAttack1 = AttackFactory.newSkillAttack(
                new Subtraction.Builder<PlayCharacter.MainScales>().build(),
                new Subtraction.Builder<PlayCharacter.MainScales>().build(),
                SkillFactory.newSkill(Skill.SkillTypes.FIREBALL, player_1)
        );

        JSONObject object2 = getResponse(new RoundResults.Builder()
                .addStage(RoundResults.RoundStage.newStageBuilder(normalAttack1, RoundResults.Players.PLAYER_1)
                        .build())
                .addStage(RoundResults.RoundStage.newStageBuilder(skillAttack1, RoundResults.Players.PLAYER_2)
                        .build())
                .build()); */
    }

    private class Subtractions {
        private final Subtraction pl1_sub, pl2_sub;

         Subtractions(Subtraction pl1_sub, Subtraction pl2_sub) {
            this.pl1_sub = pl1_sub;
            this.pl2_sub = pl2_sub;
        }

         Subtraction getPl1_sub() {
            return pl1_sub;
        }

         Subtraction getPl2_sub() {
            return pl2_sub;
        }
    }
}
