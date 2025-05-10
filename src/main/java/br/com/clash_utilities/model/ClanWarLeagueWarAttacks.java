package br.com.clash_utilities.model;

public record ClanWarLeagueWarAttacks(
        String attackerTag,
        String defenderTag,
        int stars,
        int destructionPercentage,
        int order,
        int duration
) {
}
