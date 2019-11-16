package com.example.user.carnage.logic.main.attack;

import com.example.user.carnage.logic.main.attack.effect.Subtraction;
import com.example.user.carnage.logic.main.attack.effect.Subtractor;
import com.example.user.carnage.logic.skills.SkillNew;

public class SkillAttack extends AbstractAttack {
    private final SkillNew skill;
    private final SkillNew.SkillTypes skillType;

    SkillAttack(Subtraction actorSubtraction, Subtraction enemySubtraction, SkillNew skill) {
        super(actorSubtraction, enemySubtraction);
        this.skill = skill;
        skillType = skill.getType();
    }

    SkillAttack(Subtractor subtractor, SkillNew skill) {
        super(subtractor);
        this.skill = skill;
        skillType = skill.getType();
    }

    SkillAttack(Subtractor subtractor, SkillNew.SkillTypes type) {
        super(subtractor);
        skillType = type;
        skill = null;
    }

    public SkillNew getSkill() {
        return skill;
    }

    public SkillNew.SkillTypes getSkillType() {
        return skillType;
    }
}
