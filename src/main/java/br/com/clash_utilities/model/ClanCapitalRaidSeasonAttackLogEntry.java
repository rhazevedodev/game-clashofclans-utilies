package br.com.clash_utilities.model;

public record ClanCapitalRaidSeasonAttackLogEntry(
        ClanCapitalRaidSeasonClanInfo defender,
        int attackCount,
        int disctrictCount,
        int districtsDestroyed,
        ClanCapitalRaidSeasonDistrictList districts
) {
}
