package com.example.user.carnage.logic.main.attack;

import com.example.user.carnage.logic.main.attack.effect.MainScalesSubtraction;
import com.example.user.carnage.logic.main.attack.effect.Subtraction;
import com.example.user.carnage.logic.main.attack.effect.Subtractor;

abstract class AbstractAttack implements Subtractor {
    private final Subtraction actorSubtraction, enemySubtraction;

    AbstractAttack(Subtraction actorSubtraction, Subtraction enemySubtraction) {
        this.actorSubtraction = actorSubtraction;
        this.enemySubtraction = enemySubtraction;
    }

    @Override
    public Subtraction getActorSubtraction() {
        return actorSubtraction;
    }

    @Override
    public Subtraction getEnemySubtraction() {
        return enemySubtraction;
    }
}
