package br.com.clash_utilities.model;

public record ClanCapitalRaidSeasonMember(
        String tag,
        String name,
        int attacks,
        int attackLimit,
        int bonusAttackLimit,
        int capitalResourcesLooted
) {
}
