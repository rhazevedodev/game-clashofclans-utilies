package br.com.clash_utilities.model;

public record ClanWarLeagueWarRegistry(
        String state,
        int teamSize,
        String  preparationStartTime,
        String startTime,
        String endTime,
        ClanWarLeagueWarClan clan,
        ClanWarLeagueWarClan opponent,
        String warStartTime
) {

}
