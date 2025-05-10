package br.com.clash_utilities.model;

import java.util.List;

public record ClanWarLeagueGroup(
        String state,
        String season,
        List<ClanWarLeagueClan> clans,
        List<ClanWarLeagueRound> rounds
) {
}
