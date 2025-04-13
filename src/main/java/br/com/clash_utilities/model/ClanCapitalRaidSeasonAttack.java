package br.com.clash_utilities.model;

public record ClanCapitalRaidSeasonAttack(
        ClanCapitalRaidSeasonAttacker attacker,
        int destructionPercent,
        int stars
) {
}
