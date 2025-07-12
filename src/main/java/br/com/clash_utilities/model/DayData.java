package br.com.clash_utilities.model;

public class DayData {
    private int attackStars;
    private int defenseStars;

    public DayData(int attackStars, int defenseStars) {
        this.attackStars = attackStars;
        this.defenseStars = defenseStars;
    }

    // Getters e setters
    public int getAttackStars() {
        return attackStars;
    }

    public void setAttackStars(int attackStars) {
        this.attackStars = attackStars;
    }

    public int getDefenseStars() {
        return defenseStars;
    }

    public void setDefenseStars(int defenseStars) {
        this.defenseStars = defenseStars;
    }
}
