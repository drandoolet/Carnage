package com.example.user.carnage.common.logic.main;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.example.user.carnage.client.MainActivity;
import com.example.user.carnage.common.logic.main.BodyPart.*;
import com.example.user.carnage.common.logic.main.PlayerChoice.*;
import com.example.user.carnage.common.logic.skills.Skill;

public class PlayCharacter {
    private int currentExp, level, availableStatPoints = 0;
    private int maxHP, maxMP, maxSP;
    private int HP;
    private int MP;
    private int SP;
    private int[] power = {1,100};
    private int critical;
    private int antiCritical;
    private double criticalDamage;
    private String name;
    private int dodgeRate;
    private int antiDodgeRate;
    private int defence = 0, magic_defence = 0;

    private int STR, STA, AGI, LUCK, INT;

    private int neural_ok_atk_head, neural_ok_atk_body, neural_ok_atk_waist, neural_ok_atk_legs;
    private int neural_attacked_head, neural_attacked_body, neural_attacked_waist, neural_attacked_legs;

    private BodyPart bpHead = new BodyPart(BodyPartNames.HEAD);
    private BodyPart bpBody = new BodyPart(BodyPartNames.BODY);
    private BodyPart bpWaist = new BodyPart(BodyPartNames.WAIST);
    private BodyPart bpLegs = new BodyPart(BodyPartNames.LEGS);

    public PlayCharacter(String profile, String name) {
        int[] stats = MainActivity.getInitialStats(profile);
        this.name = name;
        STR = stats[0];
        STA = stats[1];
        AGI = stats[2];
        LUCK = stats[3];
        INT = stats[4];
        level = stats[5];
        currentExp = stats[6];
        availableStatPoints = stats[7];
        setStatsFromMainStats();
    }

    public PlayCharacter(PlayCharacter playCharacter, String name) {
        this.name = name;
        int[] stats = playCharacter.getMainStats();
        int statSum = 0;
        for (int i=0; i<stats.length-3; i++) statSum += stats[i];

        STR = 1; STA = 1; AGI = 1; LUCK = 1; INT = 1;
        Random random = new Random();
        for (int i=1; i<=statSum-5; i++) {
            switch (1 + random.nextInt(4)) {
                case 1: STR++; break;
                case 2: STA++; break;
                case 3: AGI++; break;
                case 4: LUCK++; break;
                default: STA++; break;
            }
        }
        setStatsFromMainStats();

        neural_ok_atk_head = 0;
        neural_ok_atk_body = 0;
        neural_ok_atk_waist = 0;
        neural_ok_atk_legs = 0;
        neural_attacked_head = 0;
        neural_attacked_body = 0;
        neural_attacked_waist = 0;
        neural_attacked_legs = 0;
    }

    public void restoreHPby(int by) {
        HP = (HP + by) > maxHP ? maxHP : (HP + by);
    }

    public void restoreMPby(int by) {
        MP = (MP + by) > maxMP ? maxMP : (MP + by);
    }
    public void restoreSPby(int by) {
        SP = (SP + by) > maxSP ? maxSP : (SP + by);
    }

     boolean reduceHPby(int by) {
        if ((HP - by) > 0) {
            HP = HP - by;
            return true;
        } else {
            HP = 0;
            return false;
        }
    }
    public void reduceMPby(int by) {
            MP = MP - by;
    }
    boolean reduceSPby(int by) {
        if ((SP - by) > 0) {
            SP = SP - by;
            return true;
        } else {
            return false;
        }
    }
    public boolean checkMP(int mp) {
        return (MP - mp) >= 0;
    }

    public void receiveMagic(Skill skill) {
        if (skill.isEffectOnPlayer()) {
            reduceMPby(skill.getManaCost());
            restoreHPby(skill.getEffect());
        } else {
            reduceHPby(skill.getEffect());
        }
    }

    public int[] getPower() {
        if (power[0] == power[1]) return new int[] {power[0], power[1]+1};
        else return power;
    }

    public int getDefence() {
        return defence;
    }

    public int getLevel() { return level; }
    public int getCurrentExp() { return currentExp; }
    void setExp(int exp) { currentExp = exp; }

    void levelUp() {
        level++;
        availableStatPoints += 3;
    }

    public int getAvailableStatPoints() { return availableStatPoints; }

    public String getName() {return name;}
    public int getHP() {return HP;}
    public int getSP() { return SP; }
    public int getMP() {
        return MP;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public int getMaxSP() {
        return maxSP;
    }

    public int getMaxMP() {
        return maxMP;
    }

    public int getMPPercent(int percent) {
        return (int) (maxMP * percent)/100;
    }
    public int getHPPercent(int percent) {
        return (int) (maxHP * percent/100);
    }

    public int getCritical() {
        return critical;
    }
    public double getCriticalDmg() {return criticalDamage;}
    public int getAntiCritical() {return antiCritical;}

    public int getDodgeRate() {
        return dodgeRate;
    }

    public int getAntiDodgeRate() {
        return antiDodgeRate;
    }

    public void refresh() {
        HP = maxHP;
        SP = maxSP;
        MP = maxMP;
    }

    public int[] getStatsForNeuralNet() {
        int[] stats = new int[8];
        stats[0] = neural_ok_atk_head;
        stats[1] = neural_ok_atk_body;
        stats[2] = neural_ok_atk_waist;
        stats[3] = neural_ok_atk_legs;

        stats[4] = neural_attacked_head;
        stats[5] = neural_attacked_body;
        stats[6] = neural_attacked_waist;
        stats[7] = neural_attacked_legs;

        return stats;
    }

    public void setStatsFromMainStats() {
        Chars statHandler = Chars.CUSTOM;
        statHandler.setCustom(STA, STR, AGI, LUCK, INT);
        /*maxHP = statHandler.getHP();
        HP = statHandler.getHP();
        SP = statHandler.getSP();
        MP = statHandler.getMP();
        power[0] = statHandler.getMinAttack();
        power[1] = statHandler.getMaxAttack();
        critical = statHandler.getCritChance();
        antiCritical = statHandler.getAntiCrit();
        criticalDamage = statHandler.getCritDmg();
        dodgeRate = statHandler.getDodgeRate();
        antiDodgeRate = statHandler.getAntiDodgeRate();
        defence = statHandler.getDefence();
        magic_defence = statHandler.getMagicDefence(); */
        int[] stats = statHandler.getStats();
        maxHP = stats[3];
        HP = stats[3];
        maxSP = stats[4];
        SP = stats[4];
        maxMP = stats[5];
        MP = stats[5];
        power[0] = stats[0];
        power[1] = stats[1];
        critical = stats[7];
        antiCritical = stats[8];
        criticalDamage = statHandler.getCriticalDamage();
        dodgeRate = stats[9];
        antiDodgeRate = stats[10];
        defence = stats[2];
        magic_defence = stats[6];
    }

    public int[] getStats() {
        int[] stats = new int[11];
        stats[0] = HP;
        stats[1] = SP;
        stats[2] = MP;
        stats[3] = power[0];
        stats[4] = power[1];
        stats[5] = defence;
        stats[6] = magic_defence;
        stats[7] = dodgeRate;
        stats[8] = antiDodgeRate;
        stats[9] = critical;
        stats[10] = antiCritical;

        return stats;
    }

    public int[] getMainStats() {
        int[] stats = new int[8];
        stats[0] = STR;
        stats[1] = STA;
        stats[2] = AGI;
        stats[3] = LUCK;
        stats[4] = INT;
        stats[5] = level;
        stats[6] = currentExp;
        stats[7] = availableStatPoints;
        return stats;
    }

    public String getInfo() {
        String s = "Main stats: "+ Arrays.toString(getMainStats()) +". SubStats: "+Arrays.toString(getStats());
        return s;
    }

    public enum RoundStatus {
        NORMAL, BLOCK, CRITICAL, BLOCK_BREAK, DODGE
    }


//TODO: implement States in PlayCharacter, initialize in constructor. Then finish Subtractions


    // all code above - to refactor
    /**
     * Marker interface
     *
     * Used for marking values (e.g. HP or STR), that can be temporarily influenced during battles
     */
    public interface SubtractableValue {
        enum Value {
            MAX_VALUE, CURRENT_VALUE
        }
    }

    public enum MainScales implements SubtractableValue {
        HP, SP, MP

    }

    public enum Stats implements SubtractableValue {
        STAMINA,
        STRENGTH,
        AGILITY,
        LUCK,
        INTELLIGENCE
    }

    public enum Substats implements SubtractableValue {
        CRITICAL, ANTI_CRITICAL, CRITICAL_DAMAGE,
        DODGE, ANTI_DODGE,
        DEFENCE, MAGICAL_DEFENCE
    }

    private static Map<String, SubtractableValue> SUBTRACTABLE_VALUE_ALIAS_MAP = new HashMap<>();
    static {
        for (MainScales m : MainScales.values())
            SUBTRACTABLE_VALUE_ALIAS_MAP.put(m.toString(), m);
        for (Stats m : Stats.values())
            SUBTRACTABLE_VALUE_ALIAS_MAP.put(m.toString(), m);
        for (Substats m : Substats.values())
            SUBTRACTABLE_VALUE_ALIAS_MAP.put(m.toString(), m);
    }

    public static SubtractableValue findValue(String s) {
        return SUBTRACTABLE_VALUE_ALIAS_MAP.get(s);
    }

    public int getState(SubtractableValue type, SubtractableValue.Value value) { // i have no choice
        if (type instanceof MainScales)
            return getMainScaleState((MainScales) type, value);
        else if (type instanceof Stats)
            return getStatsState((Stats) type, value);
        else if (type instanceof Substats)
            return getSubtatsState((Substats) type, value);
        else throw new IllegalArgumentException();
    }

    private int getStatsState(Stats stat, SubtractableValue.Value value) {
        if (value == SubtractableValue.Value.MAX_VALUE) {
            switch (stat) {
                case STAMINA: return STA;
                case STRENGTH: return STR;
                case AGILITY: return AGI;
                case INTELLIGENCE: return INT;
                case LUCK: return LUCK;
            }
        } else if (value == SubtractableValue.Value.CURRENT_VALUE) {
            // TODO implement current values for Stats & Substats
            switch (stat) {
                case STAMINA: return STA;
                case STRENGTH: return STR;
                case AGILITY: return AGI;
                case INTELLIGENCE: return INT;
                case LUCK: return LUCK;
            }
        }
        throw new IllegalArgumentException();
    }

    private int getSubtatsState(Substats stat, SubtractableValue.Value value) {
        if (value == SubtractableValue.Value.MAX_VALUE) {
            switch (stat) {
                case DODGE: return dodgeRate;
                case ANTI_DODGE: return antiDodgeRate;
                case CRITICAL: return critical;
                case ANTI_CRITICAL: return antiCritical;
                case CRITICAL_DAMAGE: return (int) (criticalDamage * 100);
                case DEFENCE: return defence;
                case MAGICAL_DEFENCE: return magic_defence;
            }
        } else if (value == SubtractableValue.Value.CURRENT_VALUE) {
            // TODO implement current values for Stats & Substats
            switch (stat) {
                case DODGE: return dodgeRate;
                case ANTI_DODGE: return antiDodgeRate;
                case CRITICAL: return critical;
                case ANTI_CRITICAL: return antiCritical;
                case CRITICAL_DAMAGE: return (int) (criticalDamage * 100);
                case DEFENCE: return defence;
                case MAGICAL_DEFENCE: return magic_defence;
            }
        }
        throw new IllegalArgumentException();
    }

    private int getMainScaleState(MainScales type, SubtractableValue.Value value) {
        if (value == SubtractableValue.Value.MAX_VALUE) {
            switch (type) {
                case HP: return maxHP;
                case MP: return maxMP;
                case SP: return maxSP;
            }
        } else if (value == SubtractableValue.Value.CURRENT_VALUE) {
            switch (type) {
                case HP: return HP;
                case SP: return SP;
                case MP: return MP;
            }
        }
        throw new IllegalArgumentException();
    }
}
