package br.com.clash_utilities.model;

import java.util.List;

public record Clan(
        WarLeague warLeague,
        CapitalLeague capitalLeague,
        List<Member> memberList,
        String tag,
        int clanBuilderBasePoints,
        int clanCapitalPoints,
        int requiredTrophies,
        int requiredBuilderBaseTrophies,
        int requiredTownhallLevel,
        int warWinStreak,
        String warFrequency,
        boolean isFamilyFriendly,
        boolean isWarLogPublic,
        ChatLanguage chatLanguage,
        int warWins,
        int warTies,
        int warLosses,
        int clanPoints,
        int clanLevel,
        List<Label> labels,
        String name,
        Location location,
        String type,
        int members,
        String description,
        ClanCapital clanCapital,
        BadgeUrls badgeUrls
) {
}
