package com.example.user.carnage.common.logic.main;

public class BodyPart {
    private BodyPartNames name;

    BodyPart(BodyPartNames part_name) {
        name = part_name;
    }

    public String getName() {
        return name.toString();
    }
    public String getRuName() {
        return name.getRuName();
    }



    public enum BodyPartNames {
        HEAD(1.2, "ГОЛОВУ"), BODY(0.8, "ТЕЛО"), WAIST(0.9, "ПОЯС"), LEGS(1.1, "НОГИ") ;

        private double adjustion;
        private String runame;

        BodyPartNames(double damageAdjust, String rusName) {
            adjustion = damageAdjust;
            runame = rusName;
        }
        public double getAdjustion() {
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
