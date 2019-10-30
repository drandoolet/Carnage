package com.example.user.carnage.logic.main.attack;

import com.example.user.carnage.logic.main.Subtraction;
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
