package com.example.user.carnage.logic.main.attack;


import com.example.user.carnage.logic.main.BodyPart;
import com.example.user.carnage.logic.main.PlayCharacter;
import com.example.user.carnage.logic.main.attack.effect.MainScalesSubtraction;

public class NormalAttack extends AbstractAttack {
    private final PlayCharacter.RoundStatus status;
    private final BodyPart.BodyPartNames target;

    NormalAttack(MainScalesSubtraction actorSubtraction,
                 MainScalesSubtraction enemySubtraction,
                 PlayCharacter.RoundStatus status,
                 BodyPart.BodyPartNames target)
    {
        super(actorSubtraction, enemySubtraction);
        this.status = status;
        this.target = target;
    }

    public PlayCharacter.RoundStatus getRoundStatus() {
        return status;
    }

    public BodyPart.BodyPartNames getTarget() {
        return target;
    }
}
