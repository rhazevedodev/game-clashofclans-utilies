package br.com.clash_utilities.model;

import java.util.List;

public record ClanWarLeagueWarClan(
        String tag,
        String name,
        BadgeUrls badgeUrls,
        int clanLevel,
        int attacks,
        int stars,
        float destructionPercentage,
        List<ClanWarLeagueWarMembers> members
) {
}
