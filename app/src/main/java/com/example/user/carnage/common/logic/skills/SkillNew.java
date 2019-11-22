package com.example.user.carnage.common.logic.skills;

import com.example.user.carnage.R;
import com.example.user.carnage.common.logic.main.PlayCharacter;
import com.example.user.carnage.common.logic.main.PlayCharacter.Stats;
import com.example.user.carnage.common.logic.main.PlayCharacter.Substats;
import com.example.user.carnage.common.logic.main.attack.effect.Subtraction;
import com.example.user.carnage.common.logic.main.attack.effect.Subtractor;

import java.util.HashMap;
import java.util.Map;


public class SkillNew { // For now - only HP damage to enemy, SP/MP/HP sub to player
    private final SkillTypes type;
    private final int info;
    private final String icon;
    private final boolean isEffectOnPlayer;
    private final SkillCalculations calculations;
    private final int defBoundTaker, atkBoundTaker;

    /**
     * Uses SkillCalculations to create an AttackEffect based on PlayCharacter (actor) stats
     *
     * @param actor
     * @param enemy
     * @return
     */
    public Subtractor getEffect(PlayCharacter actor, PlayCharacter enemy) {
        Subtraction enemySub = isEffectOnPlayer() ?
                Subtraction.SubtractionFactory.empty() :
                calculations.getEnemySubtraction(actor, enemy);

        return new SkillEffect.Builder(type)
                .setActorSubtraction(calculations.getPlayerSubtraction())
                .setEnemySubtraction(enemySub)
                .build();
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

    SkillNew(Builder builder) {
        isEffectOnPlayer = builder.isEffectOnPlayer;
        info = builder.info;
        icon = builder.icon;
        calculations = builder.calculations;
        type = builder.type;
        atkBoundTaker = builder.atkBoundTaker;
        defBoundTaker = builder.defBoundTaker;
    }

    public static class Builder {
        private boolean isEffectOnPlayer;
        private int info = R.string.app_name;
        private String icon = "skill/Fireball.png";
        private SkillCalculations calculations;
        private SkillTypes type;
        private int atkBoundTaker = 0, defBoundTaker = 0;

        public Builder(SkillTypes type) {
            this.type = type;
        }

        public Builder setAtkBoundTaker(int i) {
            atkBoundTaker = i;
            return this;
        }

        public Builder setDefBoundTaker(int i) {
            defBoundTaker = i;
            return this;
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

    static class SkillCalculations {
        private final double addition;
        private final Map<Stats, Double> statsEngaged;
        private final Subtraction subtraction;
        private final Substats defenceSubstat;

        private SkillCalculations(Builder builder) {
            addition = builder.addition;
            statsEngaged = builder.statsEngaged;
            subtraction = builder.subtractionBuilder.build();
            defenceSubstat = builder.defenceSubstat;
        }

        Subtraction getPlayerSubtraction() {
            return subtraction;
        }

        Subtraction getEnemySubtraction(PlayCharacter character, PlayCharacter enemy) {
            double hpCost = addition;

            for (Stats stat: statsEngaged.keySet()) {
                hpCost +=
                        character.getState(stat, PlayCharacter.SubtractableValue.Value.CURRENT_VALUE)
                                * statsEngaged.get(stat);
            }
            return new Subtraction.Builder()
                    .setSubtraction(
                            PlayCharacter.MainScales.HP,
                            calcHPCost((int) hpCost, enemy),
                            Subtraction.SubtractionType.ABSOLUTE
                    ).build();
        }

        private int calcHPCost(int cost, PlayCharacter enemy) {
            int current = enemy.getHP();
            int calc = cost - enemy.getState(defenceSubstat, PlayCharacter.SubtractableValue.Value.CURRENT_VALUE);
            if (calc < 0) calc = 1;

            return (calc < current) ? calc : (current - calc);
        }

        static class Builder {
            private double addition = 0.0;
            private Map<Stats, Double> statsEngaged = new HashMap<>();
            private Subtraction.Builder subtractionBuilder = new Subtraction.Builder();
            private Substats defenceSubstat;

            public Builder setAddition(double addition) {
                this.addition = addition;
                return this;
            }

            public Builder addStatEngaged(Stats stat, double value) {
                statsEngaged.put(stat, value);
                return this;
            }

            public Builder setDefenceSubstat(Substats substat) {
                defenceSubstat = substat;
                return this;
            }

            public Builder setManaCost(int cost, Subtraction.SubtractionType type) {
                return addSubtractionValue(PlayCharacter.MainScales.MP, cost, type);
            }

            public Builder addSubtractionValue(PlayCharacter.SubtractableValue value,
                                               int sub,
                                               Subtraction.SubtractionType type) {
                subtractionBuilder.setSubtraction(value, sub, type);
                return this;
            }

            public SkillCalculations build() {
                return new SkillCalculations(this);
            }
        }
    }

    private static class SkillEffect implements Subtractor {
        private final Subtraction actorSub, enemySub;
        private final SkillTypes type;

        private SkillEffect(Builder builder) {
            actorSub = builder.actorSub;
            enemySub = builder.enemySub;
            type = builder.type;
        }

        @Override
        public Subtraction getActorSubtraction() {
            return actorSub;
        }

        @Override
        public Subtraction getEnemySubtraction() {
            return enemySub;
        }

        private static class Builder {
            private Subtraction actorSub, enemySub;
            private final SkillTypes type;

             Builder(SkillTypes type) {
                this.type = type;
            }

            Builder setActorSubtraction(Subtraction subtraction) {
                 actorSub = subtraction;
                 return this;
            }

            Builder setEnemySubtraction(Subtraction subtraction) {
                 enemySub = subtraction;
                 return this;
            }

            SkillEffect build() {
                 return new SkillEffect(this);
            }
        }
    }

    public enum SkillTypes {
        NULL {
            @Override
            SkillNew getSkill() {
                return null;
            }
        },
        FIREBALL {
            @Override
            SkillNew getSkill() {
                return new FireballNew();
            }
        },
        HEAL_SMALL {
            @Override
            SkillNew getSkill() {
                return new SmallHealNew();
            }
        };

        abstract SkillNew getSkill();
    }
}
