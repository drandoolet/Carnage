package com.example.user.carnage.common.logic.main;

import com.example.user.carnage.client.animation.AnimateGame;

import java.security.SecureRandom;
import java.util.ArrayList;

public class PlayCharacterHelper {
    private SecureRandom random = new SecureRandom();
    private final double enemy_crit_dmg;
    private final int dodgeRate, incomingCritRate, defence;
    private final PlayCharacter playerChar, enemyChar;
    private final int[] enemy_power;
    private ArrayList<Result> results = new ArrayList<>();

    public PlayCharacterHelper(PlayCharacter player, PlayCharacter enemy) {
        playerChar = player;
        enemyChar = enemy;
        enemy_crit_dmg = enemy.getCriticalDmg();
        enemy_power = enemy.getPower();
        defence = player.getDefence();

        if (player.getDodgeRate() == enemy.getAntiDodgeRate()) {
            dodgeRate = 10;
        } else {
            int rate;
            rate = ((player.getDodgeRate() - enemy.getAntiDodgeRate()) / player.getDodgeRate());
            rate = rate *50 /2 +30;
            if (rate <10) rate = 10;
            dodgeRate = rate;
        }

        if (player.getAntiCritical() == enemy.getCritical()) {
            incomingCritRate = 10;
        } else {
            int rate;
            rate = ((enemy.getCritical() - player.getAntiCritical()) / enemy.getCritical());
            rate = rate *50 /2 +30;
            if (rate <10) rate = 10;
            incomingCritRate = rate;
        }
    }

    public ArrayList<Result> handle(PlayerChoice plc, PlayerChoice enc) {
        results.clear();
        for (BodyPart.BodyPartNames attack : enc.getAttacked()) {
            AnimateGame.AnimationTypes roundStatus;

            if (hasDodged()) { // player dodged
                System.out.println("RH "+playerChar.getName()+" dodged!");
                roundStatus = AnimateGame.AnimationTypes.ANIMATION_BATTLE_DODGE;
            } else if (hasBlocked(attack, plc.getDefended())) {
                System.out.println("RH "+playerChar.getName()+" blocks!");
                roundStatus = AnimateGame.AnimationTypes.ANIMATION_BATTLE_BLOCK;
                if (hasReceivedCritical()) {
                    System.out.println("RH "+playerChar.getName()+"'s block is broken!");
                    roundStatus = AnimateGame.AnimationTypes.ANIMATION_BATTLE_BLOCK_BREAK;
                }
            } else {
                if (hasReceivedCritical()) {
                    System.out.println("RH "+playerChar.getName()+" received a critical dmg!");
                    roundStatus = AnimateGame.AnimationTypes.ANIMATION_BATTLE_CRITICAL;
                } else {
                    System.out.println("RH "+playerChar.getName()+" receives a normal attack!");
                    roundStatus = AnimateGame.AnimationTypes.ANIMATION_BATTLE_HIT;
                }
            }

            results.add(new Result.Builder()
                    .setAttackResult(calculateKick(roundStatus, attack))
                    .setBodyPart(attack)
                    .setRoundStatus(roundStatus)
                    .build());
        }

        return results;
    }

    private int calculateKick(AnimateGame.AnimationTypes status, BodyPart.BodyPartNames bodyPart) {
        double criticalMag = 1.0;

        System.out.println();

        switch (status) {
            case ANIMATION_BATTLE_DODGE: criticalMag = 0.0; break;
            case ANIMATION_BATTLE_BLOCK: criticalMag = 0.0; break;
            case ANIMATION_BATTLE_CRITICAL: criticalMag = enemy_crit_dmg; break;
            case ANIMATION_BATTLE_BLOCK_BREAK: criticalMag = enemy_crit_dmg * 1.5; break;
        }

        int kick = (int) (((enemy_power[0] + random.nextInt(enemy_power[1] - enemy_power[0]))
                * bodyPart.getAdjustion() - defence) * criticalMag);
        if (kick < 0) kick = 1;
        System.out.println(enemyChar.getName()+" kicks: "+kick);

        playerChar.reduceHPby(kick);

        return kick;
    }

    private boolean hasDodged() {
        return random.nextInt(100) < dodgeRate;
    }

    private boolean hasReceivedCritical() { return random.nextInt(100) < incomingCritRate; }

    private boolean hasBlocked(BodyPart.BodyPartNames attack,
                                       ArrayList<BodyPart.BodyPartNames> defended) {
        return defended.contains(attack);
    }

    public static class Result {
        private final int attack;
        private final AnimateGame.AnimationTypes roundStatus;
        private final BodyPart.BodyPartNames bodyPart;

        public BodyPart.BodyPartNames getBodyPart() {
            return bodyPart;
        }

        public int getAttack() {
            return attack;
        }

        public AnimateGame.AnimationTypes getRoundStatus() {
            return roundStatus;
        }

        private Result (Builder builder) {
            attack = builder.result;
            roundStatus = builder.status;
            bodyPart = builder.bodyPart;
        }

        static class Builder {
            private int result;
            private AnimateGame.AnimationTypes status;
            private BodyPart.BodyPartNames bodyPart;

            Builder() {}
            Result build() {
                return new Result(this);
            }

            Builder setAttackResult(int result) {
                this.result = result; return this;
            }
            Builder setRoundStatus(AnimateGame.AnimationTypes status) {
                this.status = status; return this;
            }
            Builder setBodyPart(BodyPart.BodyPartNames name) {
                bodyPart = name; return this;
            }
        }
    }
}
