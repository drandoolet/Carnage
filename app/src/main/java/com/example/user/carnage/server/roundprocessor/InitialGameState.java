package com.example.user.carnage.server.roundprocessor;

import com.example.user.carnage.common.logic.main.State;
import com.example.user.carnage.server.roundprocessor.roundelement.RoundResults;

public class InitialGameState {
    private final String player1_name, player2_name;
    private final State player1_state, player2_state;

    public InitialGameState(String player1_name, String player2_name, State player1_state, State player2_state) {
        this.player1_name = player1_name;
        this.player2_name = player2_name;
        this.player1_state = player1_state;
        this.player2_state = player2_state;
    }

    public String getName(RoundResults.Players player) {
        switch (player) {
            case PLAYER_1: return player1_name;
            case PLAYER_2: return player2_name;
            default: throw new IllegalArgumentException();
        }
    }

    public State getState(RoundResults.Players player) {
        switch (player) {
            case PLAYER_1: return player1_state;
            case PLAYER_2: return player2_state;
            default: throw new IllegalArgumentException();
        }
    }
}
