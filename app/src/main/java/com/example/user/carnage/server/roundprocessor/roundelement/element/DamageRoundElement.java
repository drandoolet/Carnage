package com.example.user.carnage.server.roundprocessor.roundelement.element;

import com.example.user.carnage.logic.main.PlayCharacter;

abstract class DamageRoundElement extends RoundElement implements AttackPerformed {
    private PlayCharacter attackedPlayer;

    public PlayCharacter getAttackedPlayer() {
        return attackedPlayer;
    }

    @Override
    public int getAttackEffect() {
        return super.getEffect();
    }
}
