package com.example.user.carnage.logic.main.attack;

import com.example.user.carnage.logic.main.attack.effect.MainScalesSubtraction;
import com.example.user.carnage.logic.main.attack.effect.Subtractor;

abstract class AbstractAttack implements Subtractor {
    private final MainScalesSubtraction actorSubtraction, enemySubtraction;

    AbstractAttack(MainScalesSubtraction actorSubtraction, MainScalesSubtraction enemySubtraction) {
        this.actorSubtraction = actorSubtraction;
        this.enemySubtraction = enemySubtraction;
    }

    @Override
    public MainScalesSubtraction getActorSubtraction() {
        return actorSubtraction;
    }

    @Override
    public MainScalesSubtraction getEnemySubtraction() {
        return enemySubtraction;
    }
}
