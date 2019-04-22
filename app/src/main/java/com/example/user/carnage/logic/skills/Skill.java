package com.example.user.carnage.logic.skills;


import com.example.user.carnage.animation.SkillsAnimator;
import com.example.user.carnage.logic.main.PlayCharacter;
import com.example.user.carnage.logic.main.PlayCharacter.*;

public class Skill { // [0] STA, [1] STR, [2] AGI, [3] LUCK, [4] INT
    protected final PlayCharacter playerChar;
    protected final PlayCharacter enemyChar;
    protected final PlayCharacter.Stats magnifiedStat;
    protected final PlayCharacter.Substats affectedSubstat;
    protected final int defBoundTaker, atkBoundTaker;
    protected final int manaCost, staminaCost;
    protected final double magnification;
    protected final double addition;
    protected final boolean isEffectOnPlayer;
    protected final String info;
    protected final SkillTypes name;
    protected final String icon;

    @Deprecated
    protected Skill(PlayCharacter playCharacter, PlayCharacter enemyChar,
                    double magnification, double addition,
                    PlayCharacter.Stats magnifiedStat, PlayCharacter.Substats affectedSubstat,
                    int defBoundTaker, int atkBoundTaker, boolean isEffectOnPlayer,
                    int manaCost, int staminaCost, SkillTypes name, String info, String icon) {
        playerChar = playCharacter;
        this.enemyChar = enemyChar;
        this.magnifiedStat = magnifiedStat;
        this.isEffectOnPlayer = isEffectOnPlayer;
        this.magnification = magnification;
        this.addition = addition;
        this.affectedSubstat = affectedSubstat;
        this.atkBoundTaker = atkBoundTaker;
        this.defBoundTaker = defBoundTaker;
        this.name = name;
        this.info = info;
        this.manaCost = manaCost;
        this.staminaCost = staminaCost;
        this.icon = icon;
    }

    public void use() {
        throw new AssertionError("Needs to be overridden");
    }

    public int getEffect() {
        if (enemyChar == null)
            return (int) (playerChar.valueOf(magnifiedStat) * magnification + addition);

        return (int) -(playerChar.valueOf(magnifiedStat) * magnification + addition
                - enemyChar.valueOf(affectedSubstat));
    }

    public int getManaCost() {
        return manaCost;
    }

    public SkillTypes getName() { return name; }

    public boolean isEffectOnPlayer() {
        return isEffectOnPlayer;
    }

    public String getInfo() {
        return info;
    }

    public String getIcon() {
        return icon;
    }

    public int[] getBoundTakers() {
        return new int[]{defBoundTaker, atkBoundTaker};
    }

    public static class Builder {
        private final PlayCharacter player, enemy;
        private final SkillTypes skillType;

        private PlayCharacter.Stats magnifiedStat = Stats.INTELLIGENCE;
        private PlayCharacter.Substats affectedSubstat = Substats.MAGICAL_DEFENCE;
        private int defBoundTaker = 0, atkBoundTaker = 0;
        private double magnification = 0;
        private double addition = 0;
        private boolean isEffectOnPlayer = false;
        private String info = "please replace with R.string value";
        private int manaCost = 0;
        private int staminaCost = 0;
        private String icon = "skill/Fireball.png";

        public Builder(PlayCharacter player, PlayCharacter enemy, SkillTypes type) {
            this.player = player;
            this.enemy = enemy;
            skillType = type;
        }

        public Skill build() {
            return new Skill(this);
        }

        public enum Values {
            MAGNIFIED_STAT, AFFECTED_STAT,
            DEF_BOUND_TAKER, ATK_BOUND_TAKER,
            MAGNIFICATION, ADDITION,
            IS_EFFECT_ON_PLAYER, INFO, MANA_COST, STAMINA_COST
        }

        public Builder setManaCost(int cost) {
            manaCost = cost; return this;
        }

        public Builder setStaminaCost(int cost) {
            staminaCost = cost; return this;
        }

        public Builder setMagnifiedStat(Stats stat) {
            magnifiedStat = stat;
            return this;
        }

        public Builder setAffectedSubstat(Substats substat) {
            affectedSubstat = substat;
            return this;
        }

        public Builder setDefBoundTaker(int i) {
            defBoundTaker = i;
            return this;
        }

        public Builder setAtkBoundTaker(int i) {
            atkBoundTaker = i;
            return this;
        }

        public Builder setMagnification(double d) {
            magnification = d;
            return this;
        }

        public Builder setAddition(double d) {
            addition = d;
            return this;
        }

        public Builder setIsEffectOnPlayer(boolean b) {
            isEffectOnPlayer = b;
            return this;
        }

        public Builder setInfo(String s) {
            info = s;
            return this;
        }

        public Builder setIcon(String s) {
            icon = s; return this;
        }

        public Builder setValue(Values value, Object o) {
            switch (value) {
                case MAGNIFIED_STAT:
                    if (o instanceof Stats) magnifiedStat = (Stats) o;
                    else throw new IllegalArgumentException();
                    break;
                case AFFECTED_STAT:
                    if (o instanceof Substats) affectedSubstat = (Substats) o;
                    else throw new IllegalArgumentException();
                    break;
                case DEF_BOUND_TAKER:
                    if (o instanceof Integer) defBoundTaker = (int) o;
                    else throw new IllegalArgumentException();
                    break;
                case ATK_BOUND_TAKER:
                    if (o instanceof Integer) atkBoundTaker = (int) o;
                    else throw new IllegalArgumentException();
                    break;
                case MAGNIFICATION:
                    if (o instanceof Double) magnification = (double) o;
                    else throw new IllegalArgumentException();
                    break;
                case ADDITION:
                    if (o instanceof Double) addition = (double) o;
                    else throw new IllegalArgumentException();
                    break;
                case IS_EFFECT_ON_PLAYER:
                    if (o instanceof Boolean) isEffectOnPlayer = (boolean) o;
                    else throw new IllegalArgumentException();
                    break;
                case INFO:
                    if (o instanceof String) info = (String) o;
                    else throw new IllegalArgumentException();
                    break;
            }
            return this;
        }
    }

    Skill(Builder builder) {
        playerChar = builder.player;
        enemyChar = builder.enemy;
        magnifiedStat = builder.magnifiedStat;
        isEffectOnPlayer = builder.isEffectOnPlayer;
        magnification = builder.magnification;
        addition = builder.addition;
        affectedSubstat = builder.affectedSubstat;
        atkBoundTaker = builder.atkBoundTaker;
        defBoundTaker = builder.defBoundTaker;
        name = builder.skillType;
        info = builder.info;
        manaCost = builder.manaCost;
        staminaCost = builder.staminaCost;
        icon = builder.icon;
    }

    public enum SkillTypes {
        HEAL_SMALL,
        FIREBALL
    }
}

/*
class AttackingSkill extends Skill {


    public AttackingSkill(PlayCharacter playCharacter, PlayCharacter enemyChar,
                          double magnification, double addition,
                          PlayCharacter.Stats magnifiedStat, PlayCharacter.Substats affectedSubstat,
                          int defBoundTaker, int atkBoundTaker, SkillTypes name, String info) {
        super(playCharacter, enemyChar, magnification, addition, magnifiedStat, affectedSubstat,
                defBoundTaker, atkBoundTaker, false, name, info);
    }

    @Override
    public void use() {

    }

}
*/


