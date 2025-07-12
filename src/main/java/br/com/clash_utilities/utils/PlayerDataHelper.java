//package br.com.clash_utilities.utils;
//
//import br.com.clash_utilities.model.ClanWarLeagueWarAttacks;
//import br.com.clash_utilities.model.ClanWarLeagueWarMembers;
//import br.com.clash_utilities.model.PlayerData;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class PlayerDataHelper {
//
//    public Map<String, List<PlayerData>> criarMapaPorTag(List<ClanWarLeagueWarMembers> membros) {
//        Map<String, List<PlayerData>> mapaAuxiliar = new HashMap<>();
//
//        for (ClanWarLeagueWarMembers membro : membros) {
//            String tag = membro.tag();
//            int attackStars = 0;
//            int defenseStars = 0;
//
//            // Calcula attackStars
//            if (membro.attacks() != null) {
//                for (ClanWarLeagueWarAttacks attack : membro.attacks()) {
//                    attackStars += attack.stars();
//                }
//            } else {
//                // Se não houver ataques, assume 0 stars
//                attackStars = 0;
//            }
//
//            // Calcula defenseStars
//            if (membro.bestOpponentAttack() != null) {
//                defenseStars = 3 - membro.bestOpponentAttack().stars();
//            } else {
//                // Se não houver ataque do melhor oponente, assume 0 stars
//                defenseStars = 1;
//            }
//
//            // Cria o objeto PlayerData
//            PlayerData playerData = new PlayerData(
//                    membro.name(),
//                    membro.mapPosition(),
//                    attackStars,
//                    defenseStars
//            );
//
//            // Adiciona ao mapa
//            mapaAuxiliar.computeIfAbsent(tag, k -> new ArrayList<>()).add(playerData);
//        }
//
//        return mapaAuxiliar;
//    }
//}
