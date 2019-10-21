package com.example.user.carnage.logic.main;

public class BodyPart {
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



    public enum BodyPartNames {
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

        public static BodyPartNames[] getAll() {
            return new BodyPartNames[] {HEAD, BODY, WAIST, LEGS};
        }
    }
}
