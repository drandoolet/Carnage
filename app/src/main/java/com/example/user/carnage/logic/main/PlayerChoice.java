package com.example.user.carnage.logic.main;

import com.example.user.carnage.MainActivity;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerChoice {
    String str_att, str_def;
    BodyPart.BodyPartNames attack;
    BodyPart.BodyPartNames defend_1;
    BodyPart.BodyPartNames defend_2;

    //private ArrayList<BodyPart.BodyPartNames> defended = new ArrayList<>(),
    //        attacked = new ArrayList<>();
    private ArrayList<BodyPart.BodyPartNames> defended, attacked;
    private SecureRandom random = new SecureRandom();

    private int neural_atk_head, neural_atk_body, neural_atk_waist, neural_atk_legs;
    private int neural_def_head, neural_def_body, neural_def_waist, neural_def_legs;
    private int[] atkStatistics = {neural_atk_head, neural_atk_body, neural_atk_waist, neural_atk_legs};
    private int[] defStatistics = {neural_def_head, neural_def_body, neural_def_waist, neural_def_legs};

    public BodyPart.BodyPartNames getAttack() { return attack; }
    public BodyPart.BodyPartNames getDefend_1() { return defend_1; }
    public BodyPart.BodyPartNames getDefend_2() { return defend_2; }

    public ArrayList<BodyPart.BodyPartNames> getDefended() {
        return defended;
    }

    public ArrayList<BodyPart.BodyPartNames> getAttacked() {
        return attacked;
    }

    public PlayerChoice(ArrayList<BodyPart.BodyPartNames> playerAttacked,
                        ArrayList<BodyPart.BodyPartNames> playerDefended) {
        defended = playerDefended;
        attacked = playerAttacked;
    }

    public PlayerChoice(String jcb_name_att, String jcb_name_def) {
        str_att = jcb_name_att;
        str_def = jcb_name_def;

        if (str_def.contains("HEAD")) defended.add(BodyPart.BodyPartNames.HEAD);
        if (str_def.contains("BODY")) defended.add(BodyPart.BodyPartNames.BODY);
        if (str_def.contains("WAIST")) defended.add(BodyPart.BodyPartNames.WAIST);
        if (str_def.contains("LEGS")) defended.add(BodyPart.BodyPartNames.LEGS);

        if (str_att.contains("HEAD")) attacked.add(BodyPart.BodyPartNames.HEAD);
        if (str_att.contains("BODY")) attacked.add(BodyPart.BodyPartNames.BODY);
        if (str_att.contains("WAIST")) attacked.add(BodyPart.BodyPartNames.WAIST);
        if (str_att.contains("LEGS")) attacked.add(BodyPart.BodyPartNames.LEGS);
        System.out.println("defended list: "+defended.toString());

        if(str_att.contains("HEAD")) attack = BodyPart.BodyPartNames.HEAD;
        else if(str_att.contains("BODY")) attack = BodyPart.BodyPartNames.BODY;
        else if(str_att.contains("WAIST")) attack = BodyPart.BodyPartNames.WAIST;
        else if(str_att.contains("LEGS")) attack = BodyPart.BodyPartNames.LEGS;

        if(str_def.contains("HEAD")) {
            defend_1 = BodyPart.BodyPartNames.HEAD;
            if(str_def.contains("BODY")) defend_2 = BodyPart.BodyPartNames.BODY;
            else if(str_def.contains("WAIST")) defend_2 = BodyPart.BodyPartNames.WAIST;
            else if(str_def.contains("LEGS")) defend_2 = BodyPart.BodyPartNames.LEGS;
        }
        else if(str_def.contains("BODY")) {
            defend_1 = BodyPart.BodyPartNames.BODY;
            if(str_def.contains("HEAD")) defend_2 = BodyPart.BodyPartNames.HEAD;
            else if(str_def.contains("WAIST")) defend_2 = BodyPart.BodyPartNames.WAIST;
            else if(str_def.contains("LEGS")) defend_2 = BodyPart.BodyPartNames.LEGS;
        }
        else if(str_def.contains("WAIST")) {
            defend_1 = BodyPart.BodyPartNames.WAIST;
            if(str_def.contains("BODY")) defend_2 = BodyPart.BodyPartNames.BODY;
            else if(str_def.contains("HEAD")) defend_2 = BodyPart.BodyPartNames.HEAD;
            else if(str_def.contains("LEGS")) defend_2 = BodyPart.BodyPartNames.LEGS;
        }
        else if(str_def.contains("LEGS")) {
            defend_1 = BodyPart.BodyPartNames.LEGS;
            if(str_def.contains("BODY")) defend_2 = BodyPart.BodyPartNames.BODY;
            else if(str_def.contains("WAIST")) defend_2 = BodyPart.BodyPartNames.WAIST;
            else if(str_def.contains("HEAD")) defend_2 = BodyPart.BodyPartNames.HEAD;
        }
        //System.out.println("ATT: "+attack+". DEF_1: "+defend_1+". DEF_2: "+defend_2);
    }

    public PlayerChoice(int i) {
        attacked = new ArrayList<>();
        defended = new ArrayList<>();
        ArrayList<BodyPart.BodyPartNames> availableAtk = new ArrayList<>(Arrays.asList(BodyPart.BodyPartNames.getAll()));
        ArrayList<BodyPart.BodyPartNames> availableDef = new ArrayList<>(Arrays.asList(BodyPart.BodyPartNames.getAll()));

        int attacks = 1; // randomize
        int defends = 2;

        for (int a=0; a<attacks; a++) {
            BodyPart.BodyPartNames part = availableAtk.get(random.nextInt(availableAtk.size()));
            attacked.add(part);
            availableAtk.remove(part);
        }
        for (int d=0; d<defends; d++) {
            BodyPart.BodyPartNames part = availableDef.get(random.nextInt(availableDef.size()));
            defended.add(part);
            availableDef.remove(part);
        }
    }

    public PlayerChoice() { // for COMP only
        random = new SecureRandom();
        handleNeuralNetStatistics(MainActivity.getNeuralNetStatistics(MainActivity.currentProfile));

        int att = random.nextInt(100);
        int def_1 = random.nextInt(100);
        int def_2;

        System.out.println("att = "+att+". def_1 = "+def_1);

        if (att < atkStatistics[0]) {
            attack = BodyPart.BodyPartNames.HEAD;
        } else if (att < atkStatistics[1] && att >= atkStatistics[0]) {
            attack = BodyPart.BodyPartNames.BODY;
        } else if (att < atkStatistics[2] && att >= atkStatistics[1]) {
            attack = BodyPart.BodyPartNames.WAIST;
        } else if (att < atkStatistics[3] && att >= atkStatistics[2]) {
            attack = BodyPart.BodyPartNames.LEGS;
        } else {
            System.out.println("error making ATK choice");
        }

        if (def_1 < defStatistics[0]) {
            defend_1 = BodyPart.BodyPartNames.HEAD;
        } else if (def_1 < defStatistics[1] && def_1 >= defStatistics[0]) {
            defend_1 = BodyPart.BodyPartNames.BODY;
        } else if (def_1 < defStatistics[2] && def_1 >= defStatistics[1]) {
            defend_1 = BodyPart.BodyPartNames.WAIST;
        } else if (def_1 < defStatistics[3] && def_1 >= defStatistics[2]) {
            defend_1 = BodyPart.BodyPartNames.LEGS;
        } else {
            System.out.println("error making DEF_1 choice");
        }

        do {
            def_2 = random.nextInt(100);
            System.out.println("def_2 = "+def_2);
            if (def_2 < defStatistics[0]) {
                defend_2 = BodyPart.BodyPartNames.HEAD;
            } else if (def_2 < defStatistics[1] && def_2 >= defStatistics[0]) {
                defend_2 = BodyPart.BodyPartNames.BODY;
            } else if (def_2 < defStatistics[2] && def_2 >= defStatistics[1]) {
                defend_2 = BodyPart.BodyPartNames.WAIST;
            } else if (def_2 < defStatistics[3] && def_2 >= defStatistics[2]) {
                defend_2 = BodyPart.BodyPartNames.LEGS;
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
                attack = BodyPart.BodyPartNames.HEAD;
                break;
            case 2:
                attack = BodyPart.BodyPartNames.BODY;
                break;
            case 3:
                attack = BodyPart.BodyPartNames.WAIST;
                break;
            case 4:
                attack = BodyPart.BodyPartNames.LEGS;
                break;
            default:
                break;
        }

        switch(num_def_1) {
            case 1:
                defend_1 = BodyPart.BodyPartNames.HEAD;
                break;
            case 2:
                defend_1 = BodyPart.BodyPartNames.BODY;
                break;
            case 3:
                defend_1 = BodyPart.BodyPartNames.WAIST;
                break;
            case 4:
                defend_1 = BodyPart.BodyPartNames.LEGS;
                break;
        }
        switch(num_def_2) {
            case 1:
                defend_2 = BodyPart.BodyPartNames.HEAD;
                break;
            case 2:
                defend_2 = BodyPart.BodyPartNames.BODY;
                break;
            case 3:
                defend_2 = BodyPart.BodyPartNames.WAIST;
                break;
            case 4:
                defend_2 = BodyPart.BodyPartNames.LEGS;
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

    public enum Chars {
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

        private int[][] increments = { // increments that [STR, STA, AGI, LUCK, INT] add to stats
                {3, 0, 0, 0, 0}, // 0 power from
                {3, 0, 2, 0, 0}, // 1 power to
                {0, 2, 0, 0, 0}, // 2 def
                {2, 10, 0, 0, 0}, // 3 HP
                {2, 10, 2, 0, 0}, // 4 SP
                {0, 0, 0, 0, 10}, // 5 MP
                {0, 1, 0, 0, 2}, // 6 MDEF
                {0, 0, 2, 5, 0}, // 7 crit
                {1, 2, 0, 7, 0}, // 8 a-crit
                {0, 0, 5, 0, 0}, // 9 dodge
                {1, 0, 7, 2, 0}, // 10 a-dodge
                {1, 0, 1, 0, 0}  // 11 crit dmg
        };


        int STR, STA, AGI, LUCK, INT;

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
            for (int i=0; i<=stats.length-1; i++) {
                int stat = 0;
                for (int b=0; b<=mainStats.length-1; b++) {
                    stat += mainStats[b] * increments[i][b];
                }
                stats[i] = stat;
            }
            System.out.println("getStats() stats: "+ Arrays.toString(stats));

            return stats;
        }

        public double getCriticalDamage() {
            int[] mainStats = new int[5];
            mainStats[0] = STR;
            mainStats[1] = STA;
            mainStats[2] = AGI;
            mainStats[3] = LUCK;
            mainStats[4] = INT;
            int critDmg = 0;
            for (int i=0; i<mainStats.length-1; i++) {
                critDmg += mainStats[i] * increments[11][i];
            }
            return 1.00 + ((double) critDmg)/1000.00;
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

        public void setCustom(int STA, int STR, int AGI, int LUCK, int INT) {
            this.STR = STR;
            this.AGI = AGI;
            this.STA = STA;
            this.LUCK = LUCK;
            this.INT = INT;
        }
        public int getAttack_from() {
            return (int) (increments[0][0] * STR);
        }

        public int getAttack_to() {
            return increments[1][0] * STR;
        }



    }

}



