package br.com.clash_utilities.model;

public record ClanWarLeagueWarBestOpponentAttack(
        String attackerTag,
        String defenderTag,
        int stars,
        int destructionPercentage,
        int order,
        int duration
) {
}
