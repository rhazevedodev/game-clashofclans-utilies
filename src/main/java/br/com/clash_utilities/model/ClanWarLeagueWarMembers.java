package br.com.clash_utilities.model;

import java.util.List;

public record ClanWarLeagueWarMembers(
        String tag,
        String name,
        int townhallLevel,
        int mapPosition,
        List<ClanWarLeagueWarAttacks> attacks,
        int opponentAttacks,
        ClanWarLeagueWarBestOpponentAttack bestOpponentAttack
) {
}
