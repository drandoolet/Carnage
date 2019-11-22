package com.example.user.carnage.server.roundprocessor.roundelement;

import android.os.Build;
import android.support.annotation.NonNull;

import com.example.user.carnage.common.logic.main.attack.effect.Subtractor;

import java.util.ArrayList;
import java.util.List;

public class RoundResults {
    private final List<RoundStage> stages;

    private RoundResults(Builder builder) {
        this.stages = builder.stages;
    }

    public static class Builder {
        private List<RoundStage> stages;

        private Builder() {
            stages = new ArrayList<>();
        }

        public static Builder newInstance() {
            return new Builder();
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        if (Build.VERSION.SDK_INT > 23)
            stages
                    .stream()
                    .map(stage -> stage.toString() + '\n')
                    .forEach(builder::append);
        else {
            for (RoundStage stage : getStages())
                builder.append(stage.toString()).append('\n');
        }

        return builder.toString();
    }

    /**
     * An entity that represents a single kick or magic usage.
     * i.e. "Player 1 aims at Head, dealing 100 points of damage."
     * <p>
     * Transferred to Server to make JSON for Clients
     */
    public static class RoundStage {
        @NonNull
        private final Subtractor subtractor;
        @NonNull
        private final Players actor; // or maybe a String? boolean?

        public static Builder newStageBuilder(Subtractor subtractor, Players actor) {
            return new Builder(subtractor, actor);
        }

        @NonNull
        public Subtractor getSubtractor() {
            return subtractor;
        }

        @NonNull
        public Players getActor() {
            return actor;
        }

        private RoundStage(Builder builder) {
            subtractor = builder.subtractor;
            actor = builder.actor;
        }

        @Override
        public String toString() {
            return String.format("  --- STAGE RESULTS ---\n\nFirst player: %s\nSubtractor:\n %s\n",
                    actor, subtractor.toString());
        }

        public static class Builder {
            private final Subtractor subtractor;
            private final Players actor;

            private Builder(Subtractor subtractor, Players actor) {
                this.subtractor = subtractor;
                this.actor = actor;
            }

            public RoundStage build() {
                return new RoundStage(this);
            }
        }
    }


    public enum Players {
        PLAYER_1, //("player1"),
        PLAYER_2 //("player2");
        /*
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
        } */
    }
}
