package com.example.user.carnage.server.roundprocessor;

import com.example.user.carnage.common.logic.main.PlayerChoice;
import com.example.user.carnage.server.roundprocessor.roundelement.RoundResults;

public class Query {
    private final PlayerChoice playerChoice, enemyChoice;
    private final RoundResults.Players firstPlayer;

    public Query(PlayerChoice playerChoice, PlayerChoice enemyChoice, RoundResults.Players firstPlayer) {
        this.playerChoice = playerChoice;
        this.enemyChoice = enemyChoice;
        this.firstPlayer = firstPlayer;
    }

    public PlayerChoice getPlayerChoice() {
        return playerChoice;
    }

    public PlayerChoice getEnemyChoice() {
        return enemyChoice;
    }

    public RoundResults.Players getFirstPlayer() {
        return firstPlayer;
    }
}
