package com.example.user.carnage.logic.main.attack;

import com.example.user.carnage.logic.main.attack.effect.MainScalesSubtraction;
import com.example.user.carnage.logic.main.attack.effect.Subtraction;
import com.example.user.carnage.logic.skills.Skill;

public class SkillAttack extends AbstractAttack {
    private final Skill skill;

    SkillAttack(Subtraction actorSubtraction, Subtraction enemySubtraction, Skill skill) {
        super(actorSubtraction, enemySubtraction);
        this.skill = skill;
    }

    public Skill getSkill() {
        return skill;
    }
}
