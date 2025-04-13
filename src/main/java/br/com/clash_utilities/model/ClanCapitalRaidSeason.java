package br.com.clash_utilities.model;

import java.util.List;

public record ClanCapitalRaidSeason(
        String state,
        String startTime,
        String endTime,
        int capitalTotalLoot,
        int raidsCompleted,
        int totalAttacks,
        int enemyDistrictsDestroyed,
        int offensiveReward,
        int defensiveReward,
        List<ClanCapitalRaidSeasonMember> members,
        List<ClanCapitalRaidSeasonAttackLogList> attackLog,
        List<ClanCapitalRaidSeasonDefenseLogList> defenseLog
) {
}
