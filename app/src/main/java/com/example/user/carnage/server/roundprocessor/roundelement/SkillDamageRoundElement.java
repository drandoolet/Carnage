package com.example.user.carnage.server.roundprocessor.roundelement;

import com.example.user.carnage.logic.main.PlayCharacter;
import com.example.user.carnage.logic.skills.Skill;

public class SkillDamageRoundElement extends DamageRoundElement implements SkillUsed {
    private Skill skill;

    @Override
    public Skill getSkill() {
        return skill;
    }
}
