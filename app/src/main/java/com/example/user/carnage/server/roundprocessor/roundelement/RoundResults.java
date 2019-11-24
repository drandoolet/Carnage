package com.example.user.carnage.server.roundprocessor.roundelement;

import android.os.Build;
import android.support.annotation.NonNull;

import com.example.user.carnage.common.logic.main.State;
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
        private final State.MainScalesState currentPlayerState, currentEnemyState;

        public static RoundStage of(Subtractor subtractor,
                                    Players actor,
                                    State.MainScalesState currentPlayerState,
                                    State.MainScalesState currentEnemyState) {
            return new RoundStage(subtractor, actor, currentPlayerState, currentEnemyState);
        }

        @NonNull
        public Subtractor getSubtractor() {
            return subtractor;
        }

        @NonNull
        public Players getActor() {
            return actor;
        }

        public State.MainScalesState getCurrentState(Players player) {
            switch (player) {
                case PLAYER_1: return currentPlayerState;
                case PLAYER_2: return currentEnemyState;
                default: throw new IllegalArgumentException();
            }
        }

        private RoundStage(Subtractor subtractor,
                           Players actor,
                           State.MainScalesState currentPlayerState,
                           State.MainScalesState currentEnemyState) {
            this.subtractor = subtractor;
            this.actor = actor;
            this.currentPlayerState = currentPlayerState;
            this.currentEnemyState = currentEnemyState;
        }

        @Override
        public String toString() {
            return String.format("  --- STAGE RESULTS ---\n\nFirst player: %s\nSubtractor:\n %s\n",
                    actor, subtractor.toString());
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
