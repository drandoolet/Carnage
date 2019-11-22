package com.example.user.carnage.common.logic.main.attack;

import com.example.user.carnage.common.logic.main.attack.effect.Subtraction;
import com.example.user.carnage.common.logic.main.attack.effect.Subtractor;

abstract class AbstractAttack implements Subtractor {
    private final Subtraction actorSubtraction, enemySubtraction;

    AbstractAttack(Subtraction actorSubtraction, Subtraction enemySubtraction) {
        this.actorSubtraction = actorSubtraction;
        this.enemySubtraction = enemySubtraction;
    }

    AbstractAttack(Subtractor subtractor) {
        actorSubtraction = subtractor.getActorSubtraction();
        enemySubtraction = subtractor.getEnemySubtraction();
    }

    @Override
    public String toString() {
        return String.format("Player subtraction:\n%s\n" +
                "Enemy subtraction: \n%s",
                actorSubtraction.toString(),
                enemySubtraction.toString());
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
