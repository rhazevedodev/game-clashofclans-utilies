package br.com.clash_utilities.model;

public record ClanCapitalRaidSeasonDefenseLogEntry(
        ClanCapitalRaidSeasonClanInfo attacker,
        int attackCount,
        int disctrictCount,
        int districtsDestroyed,
        ClanCapitalRaidSeasonDistrictList districts
) {
}
