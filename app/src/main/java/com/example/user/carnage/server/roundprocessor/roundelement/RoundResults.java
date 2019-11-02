package com.example.user.carnage.server.roundprocessor.roundelement;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.user.carnage.logic.main.BodyPart;
import com.example.user.carnage.logic.main.PlayCharacter;
import com.example.user.carnage.logic.main.Subtraction;
import com.example.user.carnage.logic.main.Subtractor;
import com.example.user.carnage.logic.main.attack.NormalAttack;
import com.example.user.carnage.logic.main.attack.SkillAttack;
import com.example.user.carnage.logic.skills.Skill;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoundResults {
    private final List<RoundStage> stages;

    private RoundResults(Builder builder) {
        this.stages = builder.stages;
    }

    public static class Builder {
        private List<RoundStage> stages;

        public Builder() {
            stages = new ArrayList<>();
        }

        public Builder addStage(RoundStage stage) {
            stages.add(stage);
            return this;
        }

        public RoundResults build() {
            return new RoundResults(this);
        }
    }

    public List<RoundStage> getStages() {
        return stages;
    }

    /**
     * An entity that represents a single kick or magic usage.
     * i.e. "Player 1 aims at Head, dealing 100 points of damage."
     *
     * Transferred to Server to make JSON for Clients
     */
    public static class RoundStage {
        @NonNull private final Subtraction subtraction_player_1, subtraction_player_2;
        @NonNull private final Players actor; // or maybe a String? boolean?
        @Nullable private final Skill skill;
        @Nullable private final PlayCharacter.RoundStatus status;
        @Nullable private final BodyPart.BodyPartNames target;

        @NonNull
        public Subtraction getSubtraction_player_1() {
            return subtraction_player_1;
        }

        @NonNull
        public Subtraction getSubtraction_player_2() {
            return subtraction_player_2;
        }

        @NonNull
        public Players getActor() {
            return actor;
        }

        @Nullable
        public Skill getSkill() {
            return skill;
        }

        @Nullable
        public PlayCharacter.RoundStatus getStatus() {
            return status;
        }

        @Nullable
        public BodyPart.BodyPartNames getTarget() {
            return target;
        }

        public static Builder newStageBuilder(Subtraction pl1, Subtraction pl2, Players actor) {
            return new Builder(pl1, pl2, actor);
        }

        public static Builder newStageBuilder(Subtractor subtractor, Players actor) {
            return new Builder(subtractor, actor);
        }

        public JSONObject toJson() throws JSONException {
            return RoundResultsJsonParser.toJson(this);
        }

        private RoundStage(Builder builder) {
            subtraction_player_1 = builder.subtraction_player_1;
            subtraction_player_2 = builder.subtraction_player_2;
            actor = builder.actor;
            skill = builder.skill;
            status = builder.status;
            target = builder.target;
        }

        public static class Builder {
            private final Subtraction subtraction_player_1, subtraction_player_2;
            private final Players actor;
            @Nullable private Skill skill = null;
            @Nullable private PlayCharacter.RoundStatus status = null;
            @Nullable private BodyPart.BodyPartNames target = null;

            public Builder(Subtraction subtraction_player_1, Subtraction subtraction_player_2, Players actor) {
                this.subtraction_player_1 = subtraction_player_1;
                this.subtraction_player_2 = subtraction_player_2;
                this.actor = actor;
            }

            public Builder(Subtractor subtractor, Players actor) {
                subtraction_player_1 = subtractor.getActorSubtraction();
                subtraction_player_2 = subtractor.getEnemySubtraction();
                this.actor = actor;

                if (subtractor instanceof SkillAttack) {
                    setSkill(((SkillAttack) subtractor).getSkill());
                }
                if (subtractor instanceof NormalAttack) {
                    NormalAttack attack = (NormalAttack) subtractor;
                    
                    setRoundStatus(attack.getRoundStatus());
                    setTarget(attack.getTarget());
                }
            }

            public Builder setSkill(Skill skill) {
                this.skill = skill;
                return this;
            }

            public Builder setRoundStatus(PlayCharacter.RoundStatus status) {
                this.status = status;
                return this;
            }

            public Builder setTarget(BodyPart.BodyPartNames target) {
                this.target = target;
                return this;
            }

            public RoundStage build() {
                return new RoundStage(this);
            }
        }
    }


    enum Players {
        PLAYER_1(RoundResultsJsonParser.PLAYER_1),
        PLAYER_2(RoundResultsJsonParser.PLAYER_2);

        private final String name;

        Players(String name) {
            this.name = name;
        }

        public static Players find(String s) {
            return aliasMap.get(s);
        }

        private static Map<String, Players> aliasMap;
        static {
            aliasMap = new HashMap<>();
            for (Players p : Players.values()) {
                aliasMap.put(p.name, p);
            }
        }
    }
}
