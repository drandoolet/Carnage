package com.example.user.carnage.logic.main.attack;

import com.example.user.carnage.logic.main.BodyPart;
import com.example.user.carnage.logic.main.PlayCharacter;
import com.example.user.carnage.logic.main.Subtraction;
import com.example.user.carnage.logic.skills.Skill;

public final class AttackFactory {
    public static NormalAttack newNormalAttack(Subtraction actorSubtraction,
                                         Subtraction enemySubtraction,
                                         PlayCharacter.RoundStatus status,
                                         BodyPart.BodyPartNames target) {
        return new NormalAttack(actorSubtraction, enemySubtraction, status, target);
    }

    public static SkillAttack newSkillAttack(Subtraction actorSub,
                                             Subtraction enemySub,
                                             Skill skill) {
        return new SkillAttack(actorSub, enemySub, skill);
    }
}
