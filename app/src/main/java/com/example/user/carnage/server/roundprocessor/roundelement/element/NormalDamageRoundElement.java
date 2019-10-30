package com.example.user.carnage.server.roundprocessor.roundelement.element;

import com.example.user.carnage.logic.main.PlayCharacter;

public class NormalDamageRoundElement extends DamageRoundElement {
    private PlayCharacter.RoundStatus status;

    public PlayCharacter.RoundStatus getStatus() {
        return status;
    }
}
