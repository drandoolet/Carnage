package com.example.user.carnage.common.logic.main.attack;


import com.example.user.carnage.common.logic.main.BodyPart;
import com.example.user.carnage.common.logic.main.PlayCharacter;
import com.example.user.carnage.common.logic.main.attack.effect.Subtraction;
import com.example.user.carnage.common.logic.main.attack.effect.Subtractor;

public class NormalAttack extends AbstractAttack {
    private final PlayCharacter.RoundStatus status;
    private final BodyPart.BodyPartNames target;

    NormalAttack(Subtraction actorSubtraction,
                 Subtraction enemySubtraction,
                 PlayCharacter.RoundStatus status,
                 BodyPart.BodyPartNames target)
    {
        super(actorSubtraction, enemySubtraction);
        this.status = status;
        this.target = target;
    }

    NormalAttack(Subtractor subtractor,
                 PlayCharacter.RoundStatus status,
                 BodyPart.BodyPartNames target) {
        super(subtractor);
        this.status = status;
        this.target = target;
    }

    @Override
    public String toString() {
        return super.toString() +
                String.format("\nRoundStatus: %s\nTarget: %s",
                        getRoundStatus().toString(),
                        target.toString());
    }

    public PlayCharacter.RoundStatus getRoundStatus() {
        return status;
    }

    public BodyPart.BodyPartNames getTarget() {
        return target;
    }
}
