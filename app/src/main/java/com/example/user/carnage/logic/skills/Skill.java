package com.example.user.carnage.logic.skills;


import com.example.user.carnage.animation.SkillsAnimator;
import com.example.user.carnage.logic.main.PlayCharacter;

public class Skill { // [0] STA, [1] STR, [2] AGI, [3] LUCK, [4] INT
    SkillsAnimator animator;
    protected PlayCharacter playerChar;
    protected PlayCharacter enemyChar;
    protected int statIdx;
    protected int defBoundTaker = 0, atkBoundTaker = 0;
    protected double magnification;
    protected double addition;
    public boolean isEffectOnPlayer;
    protected String info;
    public SkillsAnimator.SkillsAnimations name;

    protected Skill(PlayCharacter playCharacter, PlayCharacter enemyChar, int statIdx, boolean isEffectOnPlayer) {
        playerChar = playCharacter;
        this.enemyChar = enemyChar;
        this.statIdx = statIdx;
        this.isEffectOnPlayer = isEffectOnPlayer;
    }

    public void use() {
        if (isEffectOnPlayer) {
            playerChar.setHP(playerChar.getHP() + getEffect());
        } else
            enemyChar.setHP(enemyChar.getHP() + getEffect());
    }

    public int getEffect() {
        //return (int) (playerChar.getMainStats()[statIdx]*magnification + addition);
        return -100;
    }

    public String getInfo() {
        return info;
    }

    public int[] getBoundTakers() {
        return new int[]{defBoundTaker, atkBoundTaker};
    }
}



