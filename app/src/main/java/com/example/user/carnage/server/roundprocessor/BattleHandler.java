package com.example.user.carnage.server.roundprocessor;

import com.example.user.carnage.common.logic.main.BodyPart;
import com.example.user.carnage.common.logic.main.PlayCharacter;
import com.example.user.carnage.common.logic.main.PlayerChoice;
import com.example.user.carnage.common.logic.main.attack.AttackFactory;
import com.example.user.carnage.common.logic.main.attack.effect.Subtraction;
import com.example.user.carnage.common.logic.main.attack.effect.Subtractor;
import com.example.user.carnage.common.logic.skills.SkillFactory;
import com.example.user.carnage.common.logic.skills.SkillNew;
import com.example.user.carnage.server.roundprocessor.roundelement.GameOverException;
import com.example.user.carnage.server.roundprocessor.roundelement.RoundResults;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

final class BattleHandler {
    private final PlayCharacter player_1, player_2;
    private final Random random = new Random();
    private final PlayCharacterHolder holder;

    BattleHandler(PlayCharacter player_1, PlayCharacter player_2) {
        this.player_1 = player_1;
        this.player_2 = player_2;
        holder = new PlayCharacterHolder(player_1, player_2);
    }

    RoundResults process(Query query) throws GameOverException {
        return process(query.getPlayerChoice(), query.getEnemyChoice(), query.getFirstPlayer());
    }

    private RoundResults process(PlayerChoice player1Choice, PlayerChoice player2Choice,
                                 RoundResults.Players firstPlayer) throws GameOverException {
        RoundResults.Builder builder = RoundResults.Builder.newInstance();

        List<Atk> atks = mergePlayerChoicesToAtkList(player_1, player_2, player1Choice, player2Choice, firstPlayer);
        for (Atk atk : atks) {
            if (atk.skillType == SkillNew.SkillTypes.NULL)
                builder.addStage(getStage(atk.atk, atk.def, atk.player, atk.enemy, atk.firstPlayer));
            else
                builder.addStage(getStage(atk.skillType, atk.player, atk.enemy, atk.firstPlayer));
        }

        RoundResults roundResults = builder.build();
        holder.update(roundResults);

        return roundResults;
    }

    private List<Atk> mergePlayerChoicesToAtkList(PlayCharacter pc1, PlayCharacter pc2,
                                                  PlayerChoice player1Choice, PlayerChoice player2Choice,
                                                  RoundResults.Players firstPlayer) {
        RoundResults.Players player, enemy;
        PlayCharacter playerChar, enemyChar;
        PlayerChoice playerChoice, enemyChoice;

        if (firstPlayer == RoundResults.Players.PLAYER_1) {
            player = RoundResults.Players.PLAYER_1;
            playerChar = pc1;
            playerChoice = player1Choice;
            enemy = RoundResults.Players.PLAYER_2;
            enemyChar = pc2;
            enemyChoice = player2Choice;
        } else if (firstPlayer == RoundResults.Players.PLAYER_2) {
            player = RoundResults.Players.PLAYER_2;
            playerChar = pc2;
            playerChoice = player2Choice;
            enemy = RoundResults.Players.PLAYER_1;
            enemyChar = pc1;
            enemyChoice = player1Choice;
        } else throw new IllegalArgumentException("Wrong: RoundResults.Players");

        List<Atk> list = new ArrayList<>(6); // 4 is mostly enough

        int max = playerChoice.getAttacked().size();
        if (max < enemyChoice.getAttacked().size())
            max = enemyChoice.getAttacked().size();

        addSkillIfExists(list, playerChoice, player, playerChar, enemyChar);
        addSkillIfExists(list, enemyChoice, enemy, enemyChar, playerChar);

        for (int i = 0; i < max; i++) {
            if (i < playerChoice.getAttacked().size())
                list.add(new Atk(
                        playerChoice.getAttacked().get(i),
                        enemyChoice.getDefended(),
                        player, playerChar, enemyChar,
                        SkillNew.SkillTypes.NULL));
            if (i < enemyChoice.getAttacked().size())
                list.add(new Atk(
                        enemyChoice.getAttacked().get(i),
                        playerChoice.getDefended(),
                        enemy, enemyChar, playerChar,
                        SkillNew.SkillTypes.NULL));
        }

        return list;
    }

    private void addSkillIfExists(List<Atk> list, PlayerChoice choice, RoundResults.Players firstPlayer,
                                  PlayCharacter player, PlayCharacter enemy) {
        if (choice.getSkill() != SkillNew.SkillTypes.NULL) {
            list.add(new Atk(
                    null, null, firstPlayer, player, enemy, choice.getSkill()
            ));
        }
    }

    private RoundResults.RoundStage getStage(
            SkillNew.SkillTypes skill,
            PlayCharacter pc1,
            PlayCharacter pc2,
            RoundResults.Players turn)
    {
        return RoundResults.RoundStage.newStageBuilder(
                AttackFactory.newSkillAttack(
                        SkillFactory.newSkill(skill).getEffect(pc1, pc2),
                        SkillFactory.newSkill(skill)
                ), turn
        ).build();
    }

    private RoundResults.RoundStage getStage(
            BodyPart.BodyPartNames attack,
            List<BodyPart.BodyPartNames> defended,
            PlayCharacter pc1,
            PlayCharacter pc2,
            RoundResults.Players turn)
    {
        return RoundResults.RoundStage.newStageBuilder(
                calculateAttack(
                        pc1, pc2,
                        getRoundStatus(pc1, pc2, attack, defended),
                        attack),
                turn)
                .build();
    }

    private Subtractor calculateAttack(PlayCharacter pc1, PlayCharacter pc2,
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

        return AttackFactory.newNormalAttack(
                Subtraction.SubtractionFactory.empty(),
                Subtraction.SubtractionFactory.newSubtractionBuilder()
                        .setSubtraction(PlayCharacter.MainScales.HP, kick, Subtraction.SubtractionType.ABSOLUTE)
                        .build(),
                status, target
        );
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



    private class Atk {
        BodyPart.BodyPartNames atk;
        List<BodyPart.BodyPartNames> def;
        RoundResults.Players firstPlayer;
        PlayCharacter player, enemy;
        SkillNew.SkillTypes skillType;


        private Atk(BodyPart.BodyPartNames atk, List<BodyPart.BodyPartNames> def,
                    RoundResults.Players firstPlayer, PlayCharacter player, PlayCharacter enemy,
                    SkillNew.SkillTypes skill) {
            this.atk = atk;
            this.def = def;
            this.firstPlayer = firstPlayer;
            this.player = player;
            this.enemy = enemy;
            this.skillType = skill;
        }
    }
}
