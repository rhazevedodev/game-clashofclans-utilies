package br.com.clash_utilities.model;

public record ClanCapitalRaidSeasonDistrict(
        int stars,
        String name,
        int id,
        int destructionPrcent,
        int attackCount,
        int totalLooted,
        ClanCapitalRaidSeasonAttackList attacks,
        int districtHallLevel
) {
}
