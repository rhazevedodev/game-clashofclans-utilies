package br.com.clash_utilities.model;

import java.util.List;

public record ClanWarLeagueClan(
        String tag,
        int clanLevel,
        String name,
        BadgeUrls badgeUrls,
        List<ClanWarLeagueClanMember> members
) {
}
