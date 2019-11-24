package com.example.user.carnage.common.logic.main;

import android.os.Build;

import com.example.user.carnage.common.logic.main.PlayCharacter.*;
import com.example.user.carnage.common.logic.main.attack.effect.Subtraction;
import com.example.user.carnage.server.roundprocessor.roundelement.GameOverException;

import java.util.HashMap;
import java.util.Map;

public class State {
    private final Map<MainScales, Integer> maxMainScales;
    private final Map<Stats, Integer> maxStats;
    private final Map<Substats, Integer> maxSubstats;
    private final Map<MainScales, Integer> mainScales;
    private final Map<Stats, Integer> stats;
    private final Map<Substats, Integer> substats;
    // TODO: add smth like List<Effect> effects (e.g. if "Strength is damaged by 10% for 3 rounds")

    State(Map<MainScales, Integer> mainScales, Map<Stats, Integer> stats, Map<Substats, Integer> substats) {
        this.mainScales = mainScales;
        this.stats = stats;
        this.substats = substats;
        this.maxMainScales = new HashMap<>(mainScales);
        this.maxStats = new HashMap<>(stats);
        this.maxSubstats = new HashMap<>(substats);
    }

    /**
     *
     * @param subtraction
     * @throws GameOverException
     */
    public void update(Subtraction subtraction) throws GameOverException {
        for (SubtractableValue value : subtraction.getEntries().keySet())
            update(value, subtraction.getEntries().get(value));
    }

    private void update(SubtractableValue value, Subtraction.Entry entry) throws GameOverException {
        if (value instanceof MainScales)    {
            mainScales.put((MainScales) value, getNewValue(value, entry));
            return;
        }
        if (value instanceof Stats)         {
            stats.put((Stats) value, getNewValue(value, entry));
            return;
        }
        if (value instanceof Substats)      {
            substats.put((Substats) value, getNewValue(value, entry));
            return;
        }

        throw new IllegalArgumentException("No such value: "+value.toString());
    }

    private int getNewValue(SubtractableValue value, Subtraction.Entry entry) throws GameOverException {
        int newValue = getCurrent(value) - getSubtracted(value, entry);

        if (value.equals(MainScales.HP) && newValue <= 0) throw new GameOverException("Game over!");
        return newValue;
    }

    private int getSubtracted(SubtractableValue value, Subtraction.Entry entry) {
        switch (entry.getType()) {
            case ABSOLUTE: return entry.getEffect();
            case RELATIVE_MAX: return (entry.getEffect() * getMax(value)) / 100;
            case RELATIVE_CURRENT: return (entry.getEffect() * getCurrent(value)) / 100;
            default: throw new IllegalArgumentException();
        }
    }

    int getMax(SubtractableValue value) {
        if (value instanceof MainScales)    return maxMainScales.get(value);
        if (value instanceof Stats)         return maxStats.get(value);
        if (value instanceof Substats)      return maxSubstats.get(value);

        throw new IllegalArgumentException("No such value: "+value.toString());
    }

    int getCurrent(SubtractableValue value) {
        if (value instanceof MainScales)    return mainScales.get(value);
        if (value instanceof Stats)         return stats.get(value);
        if (value instanceof Substats)      return substats.get(value);

        throw new IllegalArgumentException("No such value: "+value.toString());
    }

    MainScalesState getMainScalesState() {
        return new MainScalesState(mainScales);
    }

    @Override
    public String toString() {
        return String.format("Main:\n%s\nStats:\n%s\nSubstats:\n%s\n",
                getStringMainScales(),
                getStringStats(),
                getStringSubstats());
    }

    private String getStringStats() {
        StringBuilder builder = new StringBuilder();
        for (Stats stat : stats.keySet())
            builder
                    .append(stat.toString())
                    .append(": ")
                    .append(stats.get(stat))
                    .append(". ");
        return builder.toString();
    }

    private String getStringSubstats() {
        StringBuilder builder = new StringBuilder();
        for (Substats substat : substats.keySet())
            builder
                    .append(substat.toString())
                    .append(": ")
                    .append(substats.get(substat))
                    .append(". ");
        return builder.toString();
    }

    private String getStringMainScales() {
        StringBuilder builder = new StringBuilder();
        for (MainScales scale : mainScales.keySet())
            builder
                    .append(scale.toString())
                    .append(": ")
                    .append(mainScales.get(scale))
                    .append(". ");
        return builder.toString();
    }

    @Override
    public State clone() {
        final Map<MainScales, Integer> mainScales = new HashMap<>(this.mainScales);
        final Map<Stats, Integer> stats = new HashMap<>(this.stats);
        final Map<Substats, Integer> substats = new HashMap<>(this.substats);

        return new State(mainScales, stats, substats);
    }

    public static class MainScalesState {
        private final Map<MainScales, Integer> map;

        public MainScalesState(Map<MainScales, Integer> map) {
            this.map = map;
        }

        public int getState(MainScales scale) {
            return map.get(scale);
        }
    }
}
