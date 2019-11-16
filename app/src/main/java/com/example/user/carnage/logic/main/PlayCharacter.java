package com.example.user.carnage.logic.main;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.example.user.carnage.MainActivity;
import com.example.user.carnage.logic.main.BodyPart.*;
import com.example.user.carnage.logic.main.PlayerChoice.*;
import com.example.user.carnage.logic.skills.Skill;

public class PlayCharacter {
    private int currentExp, level, availableStatPoints = 0;
    private int maxHP, maxMP, maxSP;
    private int HP;
    private int MP;
    private int SP;
    private int[] power = {1,100};
    private int critical;
    private int antiCritical;
    private int incomingCritical;
    private double incomingCriticalDmg;
    private double criticalDamage;
    private String name;
    private int currKick, finalKick;
    private SecureRandom random;
    private String playerClass;
    private int dodgeRate;
    private int antiDodgeRate;
    private int defence = 0, magic_defence = 0;

    private int STR, STA, AGI, LUCK, INT;

    private int neural_ok_atk_head, neural_ok_atk_body, neural_ok_atk_waist, neural_ok_atk_legs;
    private int neural_attacked_head, neural_attacked_body, neural_attacked_waist, neural_attacked_legs;

    private boolean isCritical, hasDodged;

    BodyPart bpHead = new BodyPart(BodyPartNames.HEAD);
    BodyPart bpBody = new BodyPart(BodyPartNames.BODY);
    BodyPart bpWaist = new BodyPart(BodyPartNames.WAIST);
    BodyPart bpLegs = new BodyPart(BodyPartNames.LEGS);
    private BodyPart attacked;
    private BodyPart target;

    private RoundStatus roundStatus = null;

    private Chars playerChar;

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

        random = new SecureRandom();
    }

    public PlayCharacter(PlayCharacter playCharacter, String name) {
        this.name = name;
        int[] stats = playCharacter.getMainStats();
        int statSum = 0;
        for (int i=0; i<stats.length-3; i++) statSum += stats[i];

        STR = 1; STA = 1; AGI = 1; LUCK = 1; INT = 1;
        random = new SecureRandom();
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

    @Deprecated
    PlayCharacter(Chars ch, String pl_name) {
        maxHP = ch.getHP();
        HP = ch.getHP();
        power[0] = ch.getMinAttack();
        power[1] = ch.getMaxAttack();
        critical = ch.getCritChance();
        criticalDamage = ch.getCritDmg();
        dodgeRate = ch.getDodgeRate();
        antiDodgeRate = ch.getAntiDodgeRate();
        name = pl_name;
        playerClass = ch.getPlayerClass();
        playerChar = ch;

        random = new SecureRandom();

        neural_ok_atk_head = 0;
        neural_ok_atk_body = 0;
        neural_ok_atk_waist = 0;
        neural_ok_atk_legs = 0;
        neural_attacked_head = 0;
        neural_attacked_body = 0;
        neural_attacked_waist = 0;
        neural_attacked_legs = 0;
    }

    @Deprecated
    PlayCharacter(Chars ch, String pl_name, boolean RPGStyle) {
        int[] stats = ch.getStats();
        maxHP = stats[3];
        HP = stats[3];
        SP = stats[4];
        MP = stats[5];
        power[0] = stats[0];
        power[1] = stats[1];
        critical = stats[7];
        antiCritical = stats[8];
        criticalDamage = 1 + stats[11]/100;
        dodgeRate = stats[9];
        antiDodgeRate = stats[10];
        name = pl_name;
        playerClass = ch.getPlayerClass();
        playerChar = ch;

        random = new SecureRandom();

        neural_ok_atk_head = 0;
        neural_ok_atk_body = 0;
        neural_ok_atk_waist = 0;
        neural_ok_atk_legs = 0;
        neural_attacked_head = 0;
        neural_attacked_body = 0;
        neural_attacked_waist = 0;
        neural_attacked_legs = 0;

        STR = ch.STR;
        STA = ch.STA;
        AGI = ch.AGI;
        LUCK = ch.LUCK;
        INT = ch.INT;
    }

    public void setHP(int hp) {
        HP = hp;
        System.out.println(name+" HP is now: "+hp);
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
    public void setExp(int exp) { currentExp = exp; }

    public void levelUp() {
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
        return (int) (maxMP * percent/100);
    }
    public int getHPPercent(int percent) {
        return (int) (maxHP * percent/100);
    }

    public String getStrPower() {return "("+power[0]+" - "+power[1]+")";}
    public String getStrCritDmg() {
        String formatted = String.format("%.2f", criticalDamage);
        return formatted;
    }
    public int getKick() {
        if (power[0] == power[1]) currKick = power[0];
        else currKick = power[0] + random.nextInt(power[1]-power[0]);
        System.out.println('\n'+name+" aims at "+target.getName());
        //CarnageSwing.addLogText("\n ");
        //CarnageSwing.addLogText('\n'+name+" бьёт в "+target.getPlayerClass());

        System.out.println("kick: "+currKick);
        return currKick;
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

    public boolean hasDodged(PlayCharacter en) {
        int rate;
        if (dodgeRate == en.antiDodgeRate) {
            rate = 10;
        } else {
            rate = ((dodgeRate - en.antiDodgeRate) / dodgeRate);
            rate = rate *50 /2 +30;
            if (rate <10) rate = 10;
        }
        return random.nextInt(100) < rate;
    }
    public boolean isCritical(PlayCharacter en) {
        int rate;
        if (critical == en.antiCritical) {
            rate = 10;
        } else {
            rate = ((critical - en.antiCritical) / critical);
            rate = rate *50 /2 +40;
            if (rate <10) rate = 10;
        }
        return random.nextInt(100) < rate;
    }

    @Deprecated
    public void damageReceived(PlayCharacter en) {
        int dmg = 0;
        finalKick = 0;
        incomingCritical = en.getCritical();
        incomingCriticalDmg = en.getCriticalDmg();
        isCritical = isCritical(en);
        hasDodged = hasDodged(en);
        boolean isBlockBreak = false;

        if (hasDodged) {
            System.out.println("Dodge!");
        } else if(attacked.isAttackSuccessful()) {
            handleSuccessfulAttack(attacked, en);
            if(isCritical) {
                dmg = (int) (en.getKick()*attacked.getAdjustion()*incomingCriticalDmg)-defence;
                if (dmg <0) dmg = 0;
                System.out.println("Critical damage!");
                en.finalKick = dmg;
                //CarnageSwing.addLogText(" и наносит критический удар!"+'\n'+name+" получает "+dmg+" ед. урона.");
            } else {
                dmg = (int) (en.getKick()*attacked.getAdjustion())-defence;
                if (dmg <0) dmg = 0;
                System.out.println(name+" receives damage: "+dmg);
                en.finalKick = dmg;
                //CarnageSwing.addLogText(", и "+name+" получает ");
                //CarnageSwing.addDmgToLog(CarnageSwingGUI.jtaLog, dmg);
                //CarnageSwing.addLogText(" ед. урона.");

            }
            setHP(getHP()-dmg);
        } else if(!attacked.isAttackSuccessful()) {
            isBlockBreak = random.nextInt(100)>50;
            if(isCritical && isBlockBreak) {
                dmg = (int) (en.getKick()*attacked.getAdjustion()*incomingCriticalDmg*1.5)-defence;
                if (dmg <0) dmg = 0;
                System.out.println("Block break!");
                //CarnageSwing.addLogText(" и пробивает блок!");
                System.out.println(name+" receives damage: "+dmg);
                //CarnageSwing.addLogText('\n'+name+" получает "+dmg+" ед. урона.");
                setHP(getHP()-dmg);
                en.finalKick = dmg;
            } else {
                //roundStatus = "blocked";
                en.getKick();
                System.out.println(name+" receives damage: "+dmg);
                en.finalKick = dmg;
                //CarnageSwing.addLogText(", но "+name+" блокирует удар.");
            }
        }

        System.out.println(" attacked.isAttackSuccessful: "+attacked.isAttackSuccessful());
        System.out.println(" isCritical: "+isCritical);

        if (hasDodged) {
            en.roundStatus = RoundStatus.DODGE;
        } else if (attacked.isAttackSuccessful()) {
            if (!isCritical) {
                en.roundStatus = RoundStatus.NORMAL;
            } else if (isCritical) en.roundStatus = RoundStatus.CRITICAL;
        } else if (!attacked.isAttackSuccessful()) {
            if (isCritical && isBlockBreak) {
                en.roundStatus = RoundStatus.BLOCK_BREAK;
            } else en.roundStatus = RoundStatus.BLOCK;
        }
    }

    boolean isCritical() {
        random = new SecureRandom();
        boolean isCrit = random.nextInt(100) <= incomingCritical;
        if (isCrit) {
            isCritical = true;
        } else isCritical = false;
        return isCrit;
    }

    @Deprecated
    public void getChoices(PlayerChoice plc, PlayerChoice plc1) { // plc - player, plc1 - enemy
        switch (plc1.getAttack()) {
            case HEAD :
                bpHead.setAttacked(true);
                attacked = bpHead;
                neural_attacked_head++;
                break;
            case BODY :
                bpBody.setAttacked(true);
                attacked = bpBody;
                neural_attacked_body++;
                break;
            case WAIST :
                bpWaist.setAttacked(true);
                attacked = bpWaist;
                neural_attacked_waist++;
                break;
            case LEGS :
                bpLegs.setAttacked(true);
                attacked = bpLegs;
                neural_attacked_legs++;
                break;
        } /*
        switch (plc.getDefend_1()) {
            case HEAD : bpHead.setDefended(true); break;
            case BODY : bpBody.setDefended(true); break;
            case WAIST : bpWaist.setDefended(true); break;
            case LEGS : bpLegs.setDefended(true); break;
        }
        switch (plc.getDefend_2()) {
            case HEAD : bpHead.setDefended(true); break;
            case BODY : bpBody.setDefended(true); break;
            case WAIST : bpWaist.setDefended(true); break;
            case LEGS : bpLegs.setDefended(true); break;
        }*/
        switch (plc.getAttack()) {
            case HEAD : target = bpHead; break;
            case BODY : target = bpBody; break;
            case WAIST : target = bpWaist; break;
            case LEGS : target = bpLegs; break;
        }

        System.out.println("plc getdef: "+plc.getDefended().toString());
        for (BodyPartNames part : plc.getDefended()) {
            switch (part) {
                case HEAD : bpHead.setDefended(true); break;
                case BODY : bpBody.setDefended(true); break;
                case WAIST : bpWaist.setDefended(true); break;
                case LEGS : bpLegs.setDefended(true); break;
            }
        }
        for (BodyPartNames part : plc1.getAttacked()) {
            switch (part) {
                case HEAD : bpHead.setAttacked(true); break;
                case BODY : bpBody.setAttacked(true); break;
                case WAIST : bpWaist.setAttacked(true); break;
                case LEGS : bpLegs.setAttacked(true); break;
            }
        }
    }
    public void clearBodyPartsSelection() {
        BodyPart bps[] = new BodyPart[] {bpHead, bpBody, bpWaist, bpLegs};
        for (BodyPart x : bps) x.clear();
    }

    @Deprecated
    public String getInfo(PlayCharacter enemy) {
        String s1 = "*** Начинается бой между: " +name+ " и "+enemy.getName()+". *** \n";
        String s2 = "\nХарактеристики игрока: \nЗдоровье: "+HP +" ед.\nСила удара: "
                +getStrPower()+". \nШанс крит.удара: "+critical+"%. \nМножитель крит.удара: "
                +getStrCritDmg();
        String s3 = "\n "+"\nХарактеристики противника: \nЗдоровье: "+enemy.getHP() +" ед.\nСила удара: "
                +enemy.getStrPower()+". \nШанс крит.удара: "+enemy.getCritical()
                +"%. \nМножитель крит.удара: "+enemy.getStrCritDmg();
        String s = s1+s2+s3;
        return s;
    }

    public void refresh() {
        HP = maxHP;
        clearBodyPartsSelection();
    }


    public int getCurrentKick() { return finalKick; }
    public String getPlayerClass() { return playerClass; }
    public String getTarget() { return target.getName(); }
    public RoundStatus getRoundStatus() { return roundStatus; }

    @Deprecated
    private void handleSuccessfulAttack(BodyPart bodyPart, PlayCharacter enemy) {
        switch (bodyPart.getName()) {
            case "HEAD" : enemy.neural_ok_atk_head++; break;
            case "BODY" : enemy.neural_ok_atk_body++; break;
            case "WAIST" : enemy.neural_ok_atk_waist++; break;
            case "LEGS" : enemy.neural_ok_atk_legs++; break;
            default: System.out.println(" ERROR in handleSuccessfulAttack "+name);
        }
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

    public int valueOf(Stats stat) {
        switch (stat) {
            case STAMINA: return STA;
            case STRENGTH: return STR;
            case AGILITY: return AGI;
            case LUCK: return LUCK;
            case INTELLIGENCE: return INT;
        }
        return 0;
    }

    public double valueOf(Substats stat) {
        if (stat == null) return 0;
        switch (stat) {
            case CRITICAL: return critical;
            case ANTI_CRITICAL: return antiCritical;
            case CRITICAL_DAMAGE: return criticalDamage;
            case DODGE: return dodgeRate;
            case ANTI_DODGE: return antiDodgeRate;
            case DEFENCE: return defence;
            case MAGICAL_DEFENCE: return magic_defence;
        }
        return 0;
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
