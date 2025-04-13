package br.com.clash_utilities.model;

import java.util.List;

public record ClanCapital(
        int capitalHallLevel,
        List<District> districts
) {
}
