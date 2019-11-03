package com.example.user.carnage.logic.main.attack;

import com.example.user.carnage.logic.main.BodyPart;
import com.example.user.carnage.logic.main.PlayCharacter;
import com.example.user.carnage.logic.main.attack.effect.MainScalesSubtraction;
import com.example.user.carnage.logic.skills.Skill;

public final class AttackFactory {
    public static NormalAttack newNormalAttack(MainScalesSubtraction actorSubtraction,
                                               MainScalesSubtraction enemySubtraction,
                                               PlayCharacter.RoundStatus status,
                                               BodyPart.BodyPartNames target) {
        return new NormalAttack(actorSubtraction, enemySubtraction, status, target);
    }

    public static SkillAttack newSkillAttack(MainScalesSubtraction actorSub,
                                             MainScalesSubtraction enemySub,
                                             Skill skill) {
        return new SkillAttack(actorSub, enemySub, skill);
    }
}
