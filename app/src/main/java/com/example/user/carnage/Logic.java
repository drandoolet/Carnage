package com.example.user.carnage;

import android.widget.Toast;

import java.security.SecureRandom;
import java.util.Arrays;

import static java.security.AccessController.getContext;

class PlayCharacter {
    private int maxHP;
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

    private int neural_ok_atk_head, neural_ok_atk_body, neural_ok_atk_waist, neural_ok_atk_legs;
    private int neural_attacked_head, neural_attacked_body, neural_attacked_waist, neural_attacked_legs;

    private boolean isCritical, hasDodged;

    BodyPart bpHead = new BodyPart(BodyPartNames.HEAD);
    BodyPart bpBody = new BodyPart(BodyPartNames.BODY);
    BodyPart bpWaist = new BodyPart(BodyPartNames.WAIST);
    BodyPart bpLegs = new BodyPart(BodyPartNames.LEGS);
    private BodyPart attacked;
    private BodyPart target;

    private String roundStatus = "";

    private Chars playerChar;

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
        criticalDamage = stats[11];
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
    }

    public void setHP(int hp) {
        HP = hp;
        System.out.println(name+" HP is now: "+hp);
    }

    public String getName() {return name;}
    public int getHP() {return HP;}
    public String getStrPower() {return "("+power[0]+" - "+power[1]+")";}
    public String getStrCritDmg() {
        String formatted = String.format("%.2f", criticalDamage);
        return formatted;
    }
    public int getKick() {
        random = new SecureRandom();
        currKick = power[0] + random.nextInt(power[1]-power[0]);
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

    public boolean hasDodged(PlayCharacter en) {
        int rate = ((dodgeRate - en.antiDodgeRate) / dodgeRate);
        rate = rate *50 /2 +50;
        if (rate <10) rate = 10;
        return random.nextInt(100) < rate;
    }
    public boolean isCritical(PlayCharacter en) {
        int rate = ((critical - en.antiCritical) / critical);
        rate = rate *50 /2 +50;
        if (rate <10) rate = 10;
        return random.nextInt(100) < rate;
    }

    public void damageReceived(PlayCharacter en) {
        int dmg = 0;
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
                dmg = (int) (en.getKick()*attacked.getAdjustion()*incomingCriticalDmg);
                System.out.println("Critical damage!");
                en.finalKick = dmg;
                //CarnageSwing.addLogText(" и наносит критический удар!"+'\n'+name+" получает "+dmg+" ед. урона.");
            } else {
                dmg = (int) (en.getKick()*attacked.getAdjustion());
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
                dmg = (int) (en.getKick()*attacked.getAdjustion()*incomingCriticalDmg*1.5);
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
            en.roundStatus = "dodged";
        } else if (attacked.isAttackSuccessful()) {
            if (!isCritical) {
                en.roundStatus = "normal";
            } else if (isCritical) en.roundStatus = "critical";
        } else if (!attacked.isAttackSuccessful()) {
            if (isCritical && isBlockBreak) {
                en.roundStatus = "block break";
            } else en.roundStatus = "blocked";
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
        }
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
        }
        switch (plc.getAttack()) {
            case HEAD : target = bpHead; break;
            case BODY : target = bpBody; break;
            case WAIST : target = bpWaist; break;
            case LEGS : target = bpLegs; break;
        }
    }
    public void clearBodyPartsSelection() {
        BodyPart bps[] = new BodyPart[] {bpHead, bpBody, bpWaist, bpLegs};
        for (BodyPart x : bps) x.clear();
    }

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
    public String getRoundStatus() { return roundStatus; }

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

    public void setCustom(int str, int agi, int sta, int intuition) {
        playerChar.setCustom(str, agi, sta, intuition);
        maxHP = playerChar.getHP();
        HP = playerChar.getHP();
        power[0] = playerChar.getMinAttack();
        power[1] = playerChar.getMaxAttack();
        critical = playerChar.getCritChance();
        criticalDamage = playerChar.getCritDmg();
        dodgeRate = playerChar.getDodgeRate();
        antiDodgeRate = playerChar.getAntiDodgeRate();
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
}







enum  BodyPartNames {
    HEAD(1.2, "ГОЛОВУ"), BODY(0.8, "ТЕЛО"), WAIST(0.9, "ПОЯС"), LEGS(1.1, "НОГИ") ;

    private double adjustion;
    private String runame;

    BodyPartNames(double damageAdjust, String rusName) {
        adjustion = damageAdjust;
        runame = rusName;
    }
    double getAdjustion() {
        return adjustion;
    }
    String getRuName() {
        return runame;
    }
}



class BodyPart {
    private BodyPartNames name;
    private boolean isDefended = false, isAttacked = false;

    BodyPart(BodyPartNames part_name) {
        name = part_name;
    }

    public void setDefended(boolean set) {
        isDefended = set;
    }
    public void setAttacked(boolean set) {
        isAttacked = true;
    }
    public boolean isAttackSuccessful() {
        return !isDefended && isAttacked;
    }
    public double getAdjustion() {
        return name.getAdjustion();
    }
    public String getName() {
        return name.toString();
    }
    public String getRuName() {
        return name.getRuName();
    }
    public void clear() {
        isDefended = false;
        isAttacked = false;
    }
}


class PlayerChoice {
    String str_att, str_def;
    BodyPartNames attack;
    BodyPartNames defend_1;
    BodyPartNames defend_2;
    private SecureRandom random;

    private int neural_atk_head, neural_atk_body, neural_atk_waist, neural_atk_legs;
    private int neural_def_head, neural_def_body, neural_def_waist, neural_def_legs;
    private int[] atkStatistics = {neural_atk_head, neural_atk_body, neural_atk_waist, neural_atk_legs};
    private int[] defStatistics = {neural_def_head, neural_def_body, neural_def_waist, neural_def_legs};

    public BodyPartNames getAttack() { return attack; }
    public BodyPartNames getDefend_1() { return defend_1; }
    public BodyPartNames getDefend_2() { return defend_2; }

    PlayerChoice(String jcb_name_att, String jcb_name_def) {
        str_att = jcb_name_att;
        str_def = jcb_name_def;

        if(str_att.contains("HEAD")) attack = BodyPartNames.HEAD;
        else if(str_att.contains("BODY")) attack = BodyPartNames.BODY;
        else if(str_att.contains("WAIST")) attack = BodyPartNames.WAIST;
        else if(str_att.contains("LEGS")) attack = BodyPartNames.LEGS;

        if(str_def.contains("HEAD")) {
            defend_1 = BodyPartNames.HEAD;
            if(str_def.contains("BODY")) defend_2 = BodyPartNames.BODY;
            else if(str_def.contains("WAIST")) defend_2 = BodyPartNames.WAIST;
            else if(str_def.contains("LEGS")) defend_2 = BodyPartNames.LEGS;
        }
        else if(str_def.contains("BODY")) {
            defend_1 = BodyPartNames.BODY;
            if(str_def.contains("HEAD")) defend_2 = BodyPartNames.HEAD;
            else if(str_def.contains("WAIST")) defend_2 = BodyPartNames.WAIST;
            else if(str_def.contains("LEGS")) defend_2 = BodyPartNames.LEGS;
        }
        else if(str_def.contains("WAIST")) {
            defend_1 = BodyPartNames.WAIST;
            if(str_def.contains("BODY")) defend_2 = BodyPartNames.BODY;
            else if(str_def.contains("HEAD")) defend_2 = BodyPartNames.HEAD;
            else if(str_def.contains("LEGS")) defend_2 = BodyPartNames.LEGS;
        }
        else if(str_def.contains("LEGS")) {
            defend_1 = BodyPartNames.LEGS;
            if(str_def.contains("BODY")) defend_2 = BodyPartNames.BODY;
            else if(str_def.contains("WAIST")) defend_2 = BodyPartNames.WAIST;
            else if(str_def.contains("HEAD")) defend_2 = BodyPartNames.HEAD;
        }
        //System.out.println("ATT: "+attack+". DEF_1: "+defend_1+". DEF_2: "+defend_2);
    }


    PlayerChoice() { // for COMP only
        random = new SecureRandom();
        handleNeuralNetStatistics(MainActivity.getNeuralNetStatistics());

        int att = random.nextInt(100);
        int def_1 = random.nextInt(100);
        int def_2;

        System.out.println("att = "+att+". def_1 = "+def_1);

        if (att < atkStatistics[0]) {
            attack = BodyPartNames.HEAD;
        } else if (att < atkStatistics[1] && att >= atkStatistics[0]) {
            attack = BodyPartNames.BODY;
        } else if (att < atkStatistics[2] && att >= atkStatistics[1]) {
            attack = BodyPartNames.WAIST;
        } else if (att < atkStatistics[3] && att >= atkStatistics[2]) {
            attack = BodyPartNames.LEGS;
        } else {
            System.out.println("error making ATK choice");
        }

        if (def_1 < defStatistics[0]) {
            defend_1 = BodyPartNames.HEAD;
        } else if (def_1 < defStatistics[1] && def_1 >= defStatistics[0]) {
            defend_1 = BodyPartNames.BODY;
        } else if (def_1 < defStatistics[2] && def_1 >= defStatistics[1]) {
            defend_1 = BodyPartNames.WAIST;
        } else if (def_1 < defStatistics[3] && def_1 >= defStatistics[2]) {
            defend_1 = BodyPartNames.LEGS;
        } else {
            System.out.println("error making DEF_1 choice");
        }

        do {
            def_2 = random.nextInt(100);
            System.out.println("def_2 = "+def_2);
            if (def_2 < defStatistics[0]) {
                defend_2 = BodyPartNames.HEAD;
            } else if (def_2 < defStatistics[1] && def_2 >= defStatistics[0]) {
                defend_2 = BodyPartNames.BODY;
            } else if (def_2 < defStatistics[2] && def_2 >= defStatistics[1]) {
                defend_2 = BodyPartNames.WAIST;
            } else if (def_2 < defStatistics[3] && def_2 >= defStatistics[2]) {
                defend_2 = BodyPartNames.LEGS;
            } else {
                System.out.println("error making DEF_2 choice");
            }
            System.out.println("ATK: "+attack.getRuName()+". DEF_1: "+defend_1.getRuName()+". DEF_2: "+defend_2.getRuName());
        } while (defend_1 == defend_2);

        int num_att = 1 + random.nextInt(3);  // IT WORKS - don't delete (no neural net here)
        int num_def_1 = 1 + random.nextInt(3);
        int num_def_2 = 1 + random.nextInt(3);
        while (num_def_1 == num_def_2) num_def_2 = 1 + random.nextInt(3);

        switch (num_att) {
            case 1:
                attack = BodyPartNames.HEAD;
                break;
            case 2:
                attack = BodyPartNames.BODY;
                break;
            case 3:
                attack = BodyPartNames.WAIST;
                break;
            case 4:
                attack = BodyPartNames.LEGS;
                break;
            default:
                break;
        }

        switch(num_def_1) {
            case 1:
                defend_1 = BodyPartNames.HEAD;
                break;
            case 2:
                defend_1 = BodyPartNames.BODY;
                break;
            case 3:
                defend_1 = BodyPartNames.WAIST;
                break;
            case 4:
                defend_1 = BodyPartNames.LEGS;
                break;
        }
        switch(num_def_2) {
            case 1:
                defend_2 = BodyPartNames.HEAD;
                break;
            case 2:
                defend_2 = BodyPartNames.BODY;
                break;
            case 3:
                defend_2 = BodyPartNames.WAIST;
                break;
            case 4:
                defend_2 = BodyPartNames.LEGS;
                break;
        }
        //System.out.println("ATT: "+num_att+". DEF_1: "+num_def_1+". DEF_2: "+num_def_2);
        //System.out.println("ATT: "+attack+". DEF_1: "+defend_1+". DEF_2: "+defend_2);
    }

    private void handleNeuralNetStatistics(int[] stats) {
        for (int i=0; i<4; i++) atkStatistics[i] = stats[i];
        for (int i=4; i<8; i++) defStatistics[i-4] = stats[i];

        int atkSum = 0;
        int defSum = 0;
        for (int i=0; i<atkStatistics.length; i++) {
            atkSum += atkStatistics[i];
        }
        for (int i=0; i<defStatistics.length; i++) {
            defSum += defStatistics[i];
        }
        System.out.println("checking neural stats: atkSum = "+atkSum+". defSum = "+defSum);

        for (int i=0; i<atkStatistics.length; i++) atkStatistics[i] = (100*atkStatistics[i]/atkSum);
        for (int i=0; i<defStatistics.length; i++) defStatistics[i] = (100*defStatistics[i]/defSum);

        atkStatistics[1] = atkStatistics[0]+atkStatistics[1];
        atkStatistics[2] = atkStatistics[1]+atkStatistics[2];
        atkStatistics[3] = 100;
        defStatistics[1] = defStatistics[0]+defStatistics[1];
        defStatistics[2] = defStatistics[1]+defStatistics[2];
        defStatistics[3] = 100;


        //System.out.println("checking ATK stats: head = "+neural_atk_head+". body = "+neural_atk_body+". waist = "+neural_atk_waist+". legs = "+neural_atk_legs);
        System.out.println("checking stats : atkStats = "+Arrays.toString(atkStatistics)+". defStats = "+Arrays.toString(defStatistics));
    }


}


enum Chars {
    BALANCED(1000, 30, 70, 10, 1.3, 30, 30, "Сбалансированный"),
    BERSERKER(700, 10, 90, 40, 1.5, 10, 20, "Берсеркер"),
    TANK(1500, 10, 50, 5, 1.3, 0, 40, "Танк"),
    RANDOM(1, 1, 1, 1, 1, 1, 1, "RANDOM"),
    RANDOM2(1, 1, 1, 1, 1, 1, 1, "RANDOM"),
    SWORD(50, 40, 40, 30, 30),
    DAGGER(30, 20, 60, 60, 20),
    HAMMER(70, 60, 10, 40, 10),
    WIZARD(20, 30, 20, 20, 100),
    CUSTOM(1, 1, 1, 1, 1);
    private int attack_from, attack_to;
    private int HP, MP, SP;
    private int crit_chance;
    private double crit_dmg;
    private int dodgeRate, antiDodgeRate;
    private int critRate, antiCritRate;
    private int MDEF;
    private String playerClass;
    private String[] runames;
    private SecureRandom random;
//

    //private final int INCREMENT_PER_POINT_STRENGTH_POWER = 2;

    //private final int INCREMENT_PER_POINT_STRENGTH_DEF = 1;

    //private final int INCREMENT_PER_POINT_STRENGTH_A_CRIT = 1;

    //private final int INCREMENT_PER_POINT_STRENGTH_A_DODGE = 1;

    //private final int INCREMENT_PER_POINT_STRENGTH_HP = 1;

    //private final int INCREMENT_PER_POINT_STRENGTH_SP = 1;

    //private final int INCREMENT_PER_POINT_STRENGTH_CRIT_DMG = 2;

    //private final int INCREMENT_PER_POINT_AGILITY_CRIT = 1;

    //private final int INCREMENT_PER_POINT_AGILITY_CRIT_DMG = 2;

    //private final int INCREMENT_PER_POINT_AGILITY_DODGE = 2;

    //private final int INCREMENT_PER_POINT_AGILITY_A_DODGE = 2;

    //private final int INCREMENT_PER_POINT_AGILITY_SP = 1;

    //private final int INCREMENT_PER_POINT_STAMINA_HP = 2;

    //private final int INCREMENT_PER_POINT_STAMINA_DEF = 2;

    //private final int INCREMENT_PER_POINT_STAMINA_MDEF = 1;

    //private final int INCREMENT_PER_POINT_STAMINA_A_CRIT = 1;

    //private final int INCREMENT_PER_POINT_STAMINA_SP = 2;

    //private final int INCREMENT_PER_POINT_LUCK_CRIT = 2;

    //private final int INCREMENT_PER_POINT_LUCK_A_CRIT = 2;

    //private final int INCREMENT_PER_POINT_LUCK_A_DODGE = 1;

    //private final int INCREMENT_PER_POINT_INTELLIGENCE_MP = 1;

    //private final int INCREMENT_PER_POINT_INTELLIGENCE_MDEF = 1;

    private int[][] increments = { // increments that [STR, STA, AGI, LUCK, INT] add to stats
            {2, 0, 0, 0, 0}, // power from
            {2, 0, 0, 0, 0}, // power to
            {1, 2, 0, 0, 0}, // def
            {1, 2, 0, 0, 0}, // HP
            {1, 2, 1, 0, 0}, // SP
            {0, 0, 0, 0, 2}, // MP
            {0, 1, 0, 0, 2}, // MDEF
            {0, 0, 1, 2, 0}, // crit
            {1, 1, 0, 2, 0}, // a-crit
            {0, 0, 2, 0, 0}, // dodge
            {1, 0, 2, 1, 0}, // a-dodge
            {2, 0, 2, 0, 0}  // crit dmg
    };


    private int STR, STA, AGI, LUCK, INT;

    Chars(int health, int att1, int att2, int crit1, double crit2, int dodge, int antiDodge, String player_class) {
        HP = health;
        attack_from = att1;
        attack_to = att2;
        crit_chance = crit1;
        crit_dmg = crit2;
        dodgeRate = dodge;
        antiDodgeRate = antiDodge;
        playerClass = player_class;
    }

    Chars(int strength, int stamina, int agility, int luck, int intelligence) {
        STR = strength;
        STA = stamina;
        AGI = agility;
        LUCK = luck;
        INT = intelligence;
    }

    public int[] getStats() {
        int[] mainStats = new int[5];
        mainStats[0] = STR;
        mainStats[1] = STA;
        mainStats[2] = AGI;
        mainStats[3] = LUCK;
        mainStats[4] = INT;
        int[] stats = new int[12];
        for (int i=0; i<=stats.length; i++) {
            int stat = 0;
            for (int b=0; b<=mainStats.length; b++) {
                stat += mainStats[b] * increments[i][b];
            }
            stats[i] = stat;
        }
        System.out.println("getStats() stats: "+ Arrays.toString(stats));
        //
        //
        //stats[0] = STR * INCREMENT_PER_POINT_STRENGTH_POWER; // power from
        //stats[1] = STR * INCREMENT_PER_POINT_STRENGTH_POWER; // power to
        //stats[2] = STR * INCREMENT_PER_POINT_STRENGTH_DEF
        //+ STA * INCREMENT_PER_POINT_STAMINA_DEF; // def
        //stats[3] = STA * INCREMENT_PER_POINT_STAMINA_HP
        //+ STR * INCREMENT_PER_POINT_STRENGTH_HP; // HP
        //stats[4] = STA * INCREMENT_PER_POINT_STAMINA_SP
        //+ STR * INCREMENT_PER_POINT_STRENGTH_SP
        //+ AGI * INCREMENT_PER_POINT_AGILITY_SP; // SP (stamina)
        //stats[5] = INT * INCREMENT_PER_POINT_INTELLIGENCE_MP; // MP
        //stats[6] = INT * INCREMENT_PER_POINT_INTELLIGENCE_MDEF
        //+ STA * INCREMENT_PER_POINT_STAMINA_MDEF; // m-def
        //stats[7] = LUCK * INCREMENT_PER_POINT_LUCK_CRIT
        //+ AGI * INCREMENT_PER_POINT_AGILITY_CRIT; // crit
        //stats[8] = LUCK * INCREMENT_PER_POINT_LUCK_A_CRIT
        //+ STR * INCREMENT_PER_POINT_STRENGTH_A_CRIT
        //+ STA * INCREMENT_PER_POINT_STAMINA_A_CRIT; // a-crit
        //stats[9] = AGI * INCREMENT_PER_POINT_AGILITY_DODGE; // dodge
        //stats[10] = AGI * INCREMENT_PER_POINT_AGILITY_A_DODGE
        //+ LUCK * INCREMENT_PER_POINT_LUCK_A_DODGE
        //+ STR * INCREMENT_PER_POINT_STRENGTH_A_DODGE; // a-dodge
        //stats[11] = STR * INCREMENT_PER_POINT_STRENGTH_CRIT_DMG
        //+ AGI * INCREMENT_PER_POINT_AGILITY_CRIT_DMG; // crit dmg
        //
        return stats;
    }

    public int getHP() {return HP;}
    public int getMinAttack() {return attack_from;}
    public int getMaxAttack() {return attack_to;}
    public int getCritChance() {return crit_chance;}
    public double getCritDmg() {return crit_dmg;}
    public int getDodgeRate() { return dodgeRate; }
    public int getAntiDodgeRate() { return antiDodgeRate; }
    public String getPlayerClass() {return playerClass;}
    public String[] getAllRuNames() {
        runames = new String[] {Chars.BALANCED.getPlayerClass(), Chars.BERSERKER.getPlayerClass(),
                Chars.TANK.getPlayerClass(), Chars.RANDOM.getPlayerClass()};
        return runames;
    }
    synchronized public void refreshRandom(Chars c) {
        random = new SecureRandom();
        c.HP = 700 + random.nextInt(800);
        c.attack_from = random.nextInt(99);
        c.attack_to = c.attack_from + random.nextInt(100);
        if (c.attack_to > 100) c.attack_to = 100;
        c.crit_chance = random.nextInt(50);
        c.crit_dmg = 1.0 + random.nextDouble(); // probable mistake
        notify();
    }

    public void setCustom(int STR, int AGI, int STA, int INT) {
        if (playerClass == "CUSTOM") {
            this.STR = STR;
            this.AGI = AGI;
            this.STA = STA;
            this.INT = INT;
        } else {
            System.out.println("Attempt to set stats to non-CUSTOM Char denied.");
        }
    }
    public int getAttack_from() {
        return (int) (increments[0][0] * STR);
    }

    public int getAttack_to() {
        return increments[1][0] * STR;
    }



}

public class Logic {
}
