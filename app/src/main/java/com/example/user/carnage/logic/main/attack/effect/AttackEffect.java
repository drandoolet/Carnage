package com.example.user.carnage.logic.main.attack.effect;

import com.example.user.carnage.logic.main.PlayCharacter.Stats;
import com.example.user.carnage.logic.main.PlayCharacter.Substats;


import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Every move should be equipped with an AttackEffect object,
 * containing Subtractions for both players and affected stats (if any)
 */
public class AttackEffect {
    private final Subtraction subtraction_actor, subtraction_enemy;
    private final Map<Stats, Integer> statsAffectedMap_player, statsAffectedMap_enemy;
    private final Map<Substats, Integer> substatsAffectedMap_player, substatsAffectedMap_enemy;

    public boolean anyPlayerStatAffected() {
        return (statsAffectedMap_player != null &&
                substatsAffectedMap_player != null);
    }

    public boolean anyEnemyStatAffected() {
        return (statsAffectedMap_enemy != null &&
                substatsAffectedMap_enemy != null);
    }

    public Subtraction getSubtraction_actor() {
        if (subtraction_actor == null) return Subtraction.SubtractionFactory.empty();
        return subtraction_actor;
    }

    public Subtraction getSubtraction_enemy() {
        if (subtraction_enemy == null) return Subtraction.SubtractionFactory.empty();
        return subtraction_enemy;
    }

    public Map<Stats, Integer> getPlayerAffectedStats() throws NoSuchElementException {
        if (statsAffectedMap_player == null) throw new NoSuchElementException("No stats of Player affected");
        return statsAffectedMap_player;
    }

    public Map<Stats, Integer> getEnemyAffectedStats() throws NoSuchElementException {
        if (statsAffectedMap_enemy == null) throw new NoSuchElementException("No stats of Enemy affected");
        return statsAffectedMap_enemy;
    }

    public Map<Substats, Integer> getPlayerAffectedSubstats() throws NoSuchElementException {
        if (substatsAffectedMap_player == null) throw new NoSuchElementException("No substats of Player affected");
        return substatsAffectedMap_player;
    }

    public Map<Substats, Integer> getEnemyAffectedSubstats() throws NoSuchElementException {
        if (substatsAffectedMap_enemy == null) throw new NoSuchElementException("No substats of Enemy affected");
        return substatsAffectedMap_enemy;
    }

    private AttackEffect(Builder builder) {
        subtraction_actor = builder.subtraction_actor;
        subtraction_enemy = builder.subtraction_enemy;
        statsAffectedMap_player = builder.statsAffectedMap_player;
        substatsAffectedMap_player = builder.substatsAffectedMap_player;
        statsAffectedMap_enemy = builder.statsAffectedMap_enemy;
        substatsAffectedMap_enemy = builder.substatsAffectedMap_enemy;
    }

    class Builder {
        private Subtraction subtraction_actor = null, subtraction_enemy = null;
        private Map<Stats, Integer> statsAffectedMap_player = null, statsAffectedMap_enemy = null;
        private Map<Substats, Integer> substatsAffectedMap_player = null, substatsAffectedMap_enemy = null;

        public Builder() {}

        public Builder setSubtraction_actor(Subtraction s) {
            subtraction_actor = s;
            return this;
        }

        public Builder setSubtraction_enemy(Subtraction s) {
            subtraction_enemy = s;
            return this;
        }

        public Builder setStatsAffectedMap_player(Map<Stats, Integer> statsAffectedMap_player) {
            this.statsAffectedMap_player = statsAffectedMap_player;
            return this;
        }

        public Builder setStatsAffectedMap_enemy(Map<Stats, Integer> statsAffectedMap_enemy) {
            this.statsAffectedMap_enemy = statsAffectedMap_enemy;
            return this;
        }

        public Builder setSubstatsAffectedMap_player(Map<Substats, Integer> substatsAffectedMap_player) {
            this.substatsAffectedMap_player = substatsAffectedMap_player;
            return this;
        }

        public Builder setSubstatsAffectedMap_enemy(Map<Substats, Integer> substatsAffectedMap_enemy) {
            this.substatsAffectedMap_enemy = substatsAffectedMap_enemy;
            return this;
        }

        public AttackEffect build() {
            return new AttackEffect(this);
        }
    }
}
