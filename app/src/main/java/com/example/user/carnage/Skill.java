package com.example.user.carnage;


public class Skill { // [0] STA, [1] STR, [2] AGI, [3] LUCK, [4] INT
    SkillsAnimator animator;
    protected PlayCharacter playerChar;
    protected PlayCharacter enemyChar;
    protected int statIdx;
    protected int defBoundTaker = 0, atkBoundTaker = 0;
    protected double magnification;
    protected double addition;
    protected boolean isEffectOnPlayer;
    protected String info;

    Skill(PlayCharacter playCharacter, PlayCharacter enemyChar, int statIdx, boolean isEffectOnPlayer) {
        playerChar = playCharacter;
        this.enemyChar = enemyChar;
        this.statIdx = statIdx;
        this.isEffectOnPlayer = isEffectOnPlayer;
        animator = new SkillsAnimator();
    }

    protected void use() {
        if (isEffectOnPlayer) {
            playerChar.setHP(playerChar.getHP() + getEffect());
        } else
            enemyChar.setHP(enemyChar.getHP() + getEffect());
    }

    protected int getEffect() {
        //return (int) (playerChar.getMainStats()[statIdx]*magnification + addition);
        return -100;
    }

    protected String getInfo() {
        return info;
    }

    protected int[] getBoundTakers() {
        return new int[]{defBoundTaker, atkBoundTaker};
    }
}

class SmallHeal extends Skill {
    SmallHeal(PlayCharacter playCharacter, PlayCharacter enemy) {
        super(playCharacter, enemy, 4, true);
        magnification = 1.0;
        addition = 0;
        defBoundTaker = 1;
    }
}

class FireBall extends Skill {
    FireBall(PlayCharacter playCharacter, PlayCharacter enemy) {
        super(playCharacter, enemy, 4, false);
        magnification = -1.0;
        addition = 0;
        atkBoundTaker = 1;
    }
}