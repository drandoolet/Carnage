package com.example.user.carnage.logic.main.attack;

import com.example.user.carnage.logic.main.BodyPart;
import com.example.user.carnage.logic.main.PlayCharacter;
import com.example.user.carnage.logic.main.attack.effect.AttackEffect;
import com.example.user.carnage.logic.main.attack.effect.MainScalesSubtraction;
import com.example.user.carnage.logic.main.attack.effect.Subtraction;
import com.example.user.carnage.logic.skills.Skill;

public final class AttackFactory {
    public static NormalAttack newNormalAttack(Subtraction actorSubtraction,
                                               Subtraction enemySubtraction,
                                               PlayCharacter.RoundStatus status,
                                               BodyPart.BodyPartNames target) {
        return new NormalAttack(actorSubtraction, enemySubtraction, status, target);
    }

    public static NormalAttack newNormalAttack(AttackEffect effect,
                                               PlayCharacter.RoundStatus status,
                                               BodyPart.BodyPartNames target) {
        return new NormalAttack(effect.getSubtraction_actor(), effect.getSubtraction_enemy(),
                status, target); // wrong
    }

    public static SkillAttack newSkillAttack(Subtraction actorSub,
                                             Subtraction enemySub,
                                             Skill skill) {
        return new SkillAttack(actorSub, enemySub, skill);
    }
}
