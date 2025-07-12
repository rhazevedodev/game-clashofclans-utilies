package br.com.clash_utilities.model;

import java.util.Map;

public class PlayerData {
    private String tag;
    private String name;
    private int mapPosition;
    private DayData war1;
    private DayData war2;
    private DayData war3;
    private DayData war4;
    private DayData war5;
    private DayData war6;
    private DayData war7;
    private int totalAttackStars;
    private int totalDefenseStars;
    private int totalStars;

    public PlayerData() {
    }

    public PlayerData(String tag, String name, int mapPosition, Map<Integer, DayData> warData) {
        this.tag = tag;
        this.name = name;
        this.mapPosition = mapPosition;
        this.war1 = warData.getOrDefault(1, new DayData(0, 0));
        this.war2 = warData.getOrDefault(2, new DayData(0, 0));
        this.war3 = warData.getOrDefault(3, new DayData(0, 0));
        this.war4 = warData.getOrDefault(4, new DayData(0, 0));
        this.war5 = warData.getOrDefault(5, new DayData(0, 0));
        this.war6 = warData.getOrDefault(6, new DayData(0, 0));
        this.war7 = warData.getOrDefault(7, new DayData(0, 0));
        this.totalAttackStars = war1.getAttackStars() + war2.getAttackStars() + war3.getAttackStars() +
                                war4.getAttackStars() + war5.getAttackStars() + war6.getAttackStars() +
                                war7.getAttackStars();
        this.totalDefenseStars = war1.getDefenseStars() + war2.getDefenseStars() + war3.getDefenseStars() +
                                 war4.getDefenseStars() + war5.getDefenseStars() + war6.getDefenseStars() +
                                 war7.getDefenseStars();
        this.totalStars = this.totalAttackStars + this.totalDefenseStars;

    }

    public int getTotalAttackStars() {
        return totalAttackStars;
    }

    public void setTotalAttackStars(int totalAttackStars) {
        this.totalAttackStars = totalAttackStars;
    }

    public int getTotalDefenseStars() {
        return totalDefenseStars;
    }

    public void setTotalDefenseStars(int totalDefenseStars) {
        this.totalDefenseStars = totalDefenseStars;
    }

    public int getTotalStars() {
        return totalStars;
    }

    public void setTotalStars(int totalStars) {
        this.totalStars = totalStars;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMapPosition() {
        return mapPosition;
    }

    public void setMapPosition(int mapPosition) {
        this.mapPosition = mapPosition;
    }

    public DayData getWar1() {
        return war1;
    }

    public void setWar1(DayData war1) {
        this.war1 = war1;
    }

    public DayData getWar2() {
        return war2;
    }

    public void setWar2(DayData war2) {
        this.war2 = war2;
    }

    public DayData getWar3() {
        return war3;
    }

    public void setWar3(DayData war3) {
        this.war3 = war3;
    }

    public DayData getWar4() {
        return war4;
    }

    public void setWar4(DayData war4) {
        this.war4 = war4;
    }

    public DayData getWar5() {
        return war5;
    }

    public void setWar5(DayData war5) {
        this.war5 = war5;
    }

    public DayData getWar6() {
        return war6;
    }

    public void setWar6(DayData war6) {
        this.war6 = war6;
    }

    public DayData getWar7() {
        return war7;
    }

    public void setWar7(DayData war7) {
        this.war7 = war7;
    }
}
