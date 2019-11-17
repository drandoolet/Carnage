package com.example.user.carnage.common.logic.main;

import com.example.user.carnage.common.logic.main.PlayCharacter.*;

import java.util.HashMap;
import java.util.Map;

public class State {
    private final Map<MainScales, Integer> mainScales;
    private final Map<Stats, Integer> stats;
    private final Map<Substats, Integer> substats;
    // TODO: add smth like List<Effect> effects (e.g. if "Strength is damaged by 10% for 3 rounds")

    public State(Map<MainScales, Integer> mainScales, Map<Stats, Integer> stats, Map<Substats, Integer> substats) {
        this.mainScales = mainScales;
        this.stats = stats;
        this.substats = substats;
    }

    /**
     *
     * @param value
     * @param v
     * @return true if update successful
     */
    public boolean update(SubtractableValue value, int v) {
        if (value instanceof MainScales)    return mainScales.put((MainScales) value, v) != null;
        if (value instanceof Stats)         return stats.put((Stats) value, v) != null;
        if (value instanceof Substats)      return substats.put((Substats) value, v) != null;

        throw new IllegalArgumentException("No such value: "+value.toString());
    }

    public int get(SubtractableValue value) {
        if (value instanceof MainScales)    return mainScales.get(value);
        if (value instanceof Stats)         return stats.get(value);
        if (value instanceof Substats)      return substats.get(value);

        throw new IllegalArgumentException("No such value: "+value.toString());
    }

    @Override
    public State clone() {
        final Map<MainScales, Integer> mainScales = new HashMap<>(this.mainScales);
        final Map<Stats, Integer> stats = new HashMap<>(this.stats);
        final Map<Substats, Integer> substats = new HashMap<>(this.substats);

        return new State(mainScales, stats, substats);
    }
}
