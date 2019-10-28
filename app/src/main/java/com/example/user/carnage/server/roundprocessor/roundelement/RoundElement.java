package com.example.user.carnage.server.roundprocessor.roundelement;

import com.example.user.carnage.logic.main.PlayCharacter;

abstract class RoundElement {
    private PlayCharacter player;
    private int effect;

    public int getEffect() { return effect; }
    public String getPlayerName() { return  player.getName(); }

}
