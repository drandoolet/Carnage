package com.example.user.carnage.common.logic.main.attack;

import com.example.user.carnage.common.logic.main.BodyPart;
import com.example.user.carnage.common.logic.main.PlayCharacter;
import com.example.user.carnage.common.logic.main.attack.effect.Subtraction;
import com.example.user.carnage.common.logic.main.attack.effect.Subtractor;
import com.example.user.carnage.common.logic.skills.SkillNew;

public final class AttackFactory {
    public static NormalAttack newNormalAttack(Subtraction actorSubtraction,
                                               Subtraction enemySubtraction,
                                               PlayCharacter.RoundStatus status,
                                               BodyPart.BodyPartNames target) {
        return new NormalAttack(actorSubtraction, enemySubtraction, status, target);
    }

    public static NormalAttack newNormalAttack(Subtractor subtractor,
                                               PlayCharacter.RoundStatus status,
                                               BodyPart.BodyPartNames target) {
        return new NormalAttack(subtractor, status, target);
    }

    public static SkillAttack newSkillAttack(Subtraction actorSub,
                                             Subtraction enemySub,
                                             SkillNew skill) {
        return new SkillAttack(actorSub, enemySub, skill);
    }

    public static SkillAttack newSkillAttack(Subtractor subtractor, SkillNew skill) {
        return new SkillAttack(subtractor, skill);
    }
}
