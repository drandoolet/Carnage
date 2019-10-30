package com.example.user.carnage.server.roundprocessor.roundelement.element;

import com.example.user.carnage.logic.skills.Skill;

public class BuffRoundElement extends RoundElement implements SkillUsed {
    private Skill skill;

    @Override
    public Skill getSkill() {
        return skill;
    }
}
