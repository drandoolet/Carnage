package com.example.user.carnage.logic.skills;


import com.example.user.carnage.animation.SkillsAnimator;
import com.example.user.carnage.logic.main.PlayCharacter;

public class Skill { // [0] STA, [1] STR, [2] AGI, [3] LUCK, [4] INT
    protected final PlayCharacter playerChar; // TODO: create Builder
    protected final PlayCharacter enemyChar;
    protected final PlayCharacter.Stats magnifiedStat;
    protected final PlayCharacter.Substats affectedSubstat;
    protected final int defBoundTaker, atkBoundTaker;
    protected final double magnification;
    protected final double addition;
    protected final boolean isEffectOnPlayer;
    protected final String info;
    public SkillsAnimator.SkillsAnimations name;

    protected Skill(PlayCharacter playCharacter, PlayCharacter enemyChar,
                    double magnification, double addition,
                    PlayCharacter.Stats magnifiedStat, PlayCharacter.Substats affectedSubstat,
                    int defBoundTaker, int atkBoundTaker, boolean isEffectOnPlayer, String info) {
        playerChar = playCharacter;
        this.enemyChar = enemyChar;
        this.magnifiedStat = magnifiedStat;
        this.isEffectOnPlayer = isEffectOnPlayer;
        this.magnification = magnification;
        this.addition = addition;
        this.affectedSubstat = affectedSubstat;
        this.atkBoundTaker = atkBoundTaker;
        this.defBoundTaker = defBoundTaker;
        this.info = info;
    }

    public void use() {
        throw new AssertionError("Needs to be overridden");
    }

    public int getEffect() {
        //return (int) (playerChar.getMainStats()[statIdx]*magnification + addition);
        return (int) (playerChar.valueOf(magnifiedStat) * magnification + addition
                - enemyChar.valueOf(affectedSubstat));
        //return -100;
    }

    public boolean isEffectOnPlayer() {
        return isEffectOnPlayer;
    }

    public String getInfo() {
        return info;
    }

    public int[] getBoundTakers() {
        return new int[]{defBoundTaker, atkBoundTaker};
    }

    public static class Builder {

    }
}

class AttackingSkill extends Skill {


    public AttackingSkill(PlayCharacter playCharacter, PlayCharacter enemyChar,
                          double magnification, double addition,
                          PlayCharacter.Stats magnifiedStat, PlayCharacter.Substats affectedSubstat,
                          int defBoundTaker, int atkBoundTaker, String info) {
        super(playCharacter, enemyChar, magnification, addition, magnifiedStat, affectedSubstat,
                defBoundTaker, atkBoundTaker, false, info);
    }

    @Override
    public void use() {

    }

}



