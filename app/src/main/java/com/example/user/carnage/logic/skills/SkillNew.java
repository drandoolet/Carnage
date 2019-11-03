package com.example.user.carnage.logic.skills;

import com.example.user.carnage.R;
import com.example.user.carnage.logic.main.PlayCharacter;
import com.example.user.carnage.logic.main.attack.effect.AttackEffect;
import com.example.user.carnage.logic.main.PlayCharacter.Stats;
import com.example.user.carnage.logic.main.PlayCharacter.Substats;
import com.example.user.carnage.logic.skills.Skill.SkillTypes;

import java.util.HashMap;
import java.util.Map;


class SkillNew {
    private final boolean isEffectOnPlayer;
    private final int info;
    private final String icon;
    private final SkillCalculations calculations;
    private final SkillTypes type;
    private final Map<>

    /**
     * Uses SkillCalculations to create an AttackEffect based on PlayCharacter (actor) stats
     *
     * @param character
     * @return
     */
    public AttackEffect getEffect(PlayCharacter character) {


        return null;
    }

    /**
     * Used for animating skills
     * @return
     */
    public boolean isEffectOnPlayer() {
        return isEffectOnPlayer;
    }

    public int getInfo() {
        return info;
    }

    public String getIcon() {
        return icon;
    }

    public SkillTypes getType() {
        return type;
    }

    private SkillNew(Builder builder) {
        isEffectOnPlayer = builder.isEffectOnPlayer;
        info = builder.info;
        icon = builder.icon;
        calculations = builder.calculations;
        type = builder.type;
    }

    public class Builder {
        private boolean isEffectOnPlayer;
        private int info = R.string.app_name;
        private String icon = "skill/Fireball.png";
        private SkillCalculations calculations;
        private SkillTypes type;

        public Builder(SkillTypes type) {
            this.type = type;
        }

        public Builder setSkillCalculations(SkillCalculations calculations) {
            this.calculations = calculations;
            return this;
        }

        public Builder setEffectOnPlayer(boolean effectOnPlayer) {
            isEffectOnPlayer = effectOnPlayer;
            return this;
        }

        public Builder setInfo(int info) {
            this.info = info;
            return this;
        }

        public Builder setIcon(String icon) {
            this.icon = icon;
            return this;
        }

        public SkillNew build() {
            return new SkillNew(this);
        }
    }

    private class SkillCalculations {
        private final double magnification;
        private final double addition;
        private final Map<Stats, Double> statsEngaged;
        private final Map<Substats, Double> substatsEngaged;

        private SkillCalculations(Builder builder) {
            magnification = builder.magnification;
            addition = builder.addition;
            statsEngaged = builder.statsEngaged;
            substatsEngaged = builder.substatsEngaged;
        }

        public class Builder {
            private double magnification = 1.0;
            private double addition = 0.0;
            private Map<Stats, Double> statsEngaged = null;
            private Map<Substats, Double> substatsEngaged = null;

            public Builder setMagnification(double magnification) {
                this.magnification = magnification;
                return this;
            }

            public Builder setAddition(double addition) {
                this.addition = addition;
                return this;
            }

            public Builder addStatEngaged(Stats stat, double value) {
                if (statsEngaged == null) statsEngaged = new HashMap<>();
                statsEngaged.put(stat, value);
                return this;
            }

            public Builder addSubstatEngaged(Substats substat, double value) {
                if (substatsEngaged == null) substatsEngaged = new HashMap<>();
                substatsEngaged.put(substat, value);
                return this;
            }

            public SkillCalculations build() {
                return new SkillCalculations(this);
            }
        }
    }
}
