package br.com.clash_utilities.service;

import br.com.clash_utilities.model.ClanWarLeagueGroup;
import br.com.clash_utilities.model.ClanWarLeagueWarClan;
import br.com.clash_utilities.model.ClanWarLeagueWarRegistry;
import br.com.clash_utilities.model.enums.Clans;
import br.com.clash_utilities.utils.ExcelExporter2;
import br.com.clash_utilities.utils.HttpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
public class ClanWarLeagueService {

    @Value("${clashofclans.endpoints.clan-war-league-group}")
    private String apiWarLeagueGroup;

    @Value("${clashofclans.endpoints.clan-wars-league}")
    private String apiClanWarsLeague;

    @Value("${clashofclans.api.bearer-token}")
    private String bearerToken;

    public File exportExcelFileAllSevenDaysOfLeague(String tag) throws Exception {
        ClanWarLeagueWarRegistry warRegistry1 = fetchClanWarLeagueWarRegistry(tag, 1);
        exportWarRegistryToJson(warRegistry1, "warRegistry1.json");
        ClanWarLeagueWarClan meuClan1 = containsClanName(warRegistry1.clan().name()) ? warRegistry1.clan() : warRegistry1.opponent();

        ClanWarLeagueWarRegistry warRegistry2 = fetchClanWarLeagueWarRegistry(tag, 2);
        exportWarRegistryToJson(warRegistry2, "warRegistry2.json");
        ClanWarLeagueWarClan meuClan2 = (warRegistry2 != null && containsClanName(warRegistry2.clan().name())) ? warRegistry2.clan() : (warRegistry2 != null ? warRegistry2.opponent() : null);

        ClanWarLeagueWarRegistry warRegistry3 = fetchClanWarLeagueWarRegistry(tag, 3);
        exportWarRegistryToJson(warRegistry3, "warRegistry3.json");
        ClanWarLeagueWarClan meuClan3 = (warRegistry3 != null && containsClanName(warRegistry3.clan().name())) ? warRegistry3.clan() : (warRegistry3 != null ? warRegistry3.opponent() : null);

        ClanWarLeagueWarRegistry warRegistry4 = fetchClanWarLeagueWarRegistry(tag, 4);
        exportWarRegistryToJson(warRegistry4, "warRegistry4.json");
        ClanWarLeagueWarClan meuClan4 = (warRegistry4 != null && containsClanName(warRegistry4.clan().name())) ? warRegistry4.clan() : (warRegistry4 != null ? warRegistry4.opponent() : null);

        ClanWarLeagueWarRegistry warRegistry5 = fetchClanWarLeagueWarRegistry(tag, 5);
        exportWarRegistryToJson(warRegistry5, "warRegistry5.json");
        ClanWarLeagueWarClan meuClan5 = (warRegistry5 != null && containsClanName(warRegistry5.clan().name())) ? warRegistry5.clan() : (warRegistry5 != null ? warRegistry5.opponent() : null);

        ClanWarLeagueWarRegistry warRegistry6 = fetchClanWarLeagueWarRegistry(tag, 6);
        exportWarRegistryToJson(warRegistry6, "warRegistry6.json");
        ClanWarLeagueWarClan meuClan6 = (warRegistry6 != null && containsClanName(warRegistry6.clan().name())) ? warRegistry6.clan() : (warRegistry6 != null ? warRegistry6.opponent() : null);

        ClanWarLeagueWarRegistry warRegistry7 = fetchClanWarLeagueWarRegistry(tag, 7);
        exportWarRegistryToJson(warRegistry7, "warRegistry7.json");
        ClanWarLeagueWarClan meuClan7 = (warRegistry7 != null && containsClanName(warRegistry7.clan().name())) ? warRegistry7.clan() : (warRegistry7 != null ? warRegistry7.opponent() : null);

        String clanName = meuClan1.name();

        ExcelExporter2 excelExporter = new ExcelExporter2();

        int warInPreparation = 0;

        if(warRegistry2.state().equals("preparation")) {
            meuClan2 = meuClan1;
            warInPreparation = 2;
        }
        if(warRegistry3.state().equals("preparation")) {
            meuClan3 = meuClan1;
            warInPreparation = 3;
        }
        if(warRegistry4.state().equals("preparation")) {
            meuClan4 = meuClan1;
            warInPreparation = 4;
        }
        if(warRegistry5.state().equals("preparation")) {
            meuClan5 = meuClan1;
            warInPreparation = 5;
        }
        if(warRegistry6 == null) {
            meuClan6 = meuClan1;
            warInPreparation = 6;
        }
        if(warRegistry7 == null) {
            meuClan7 = meuClan1;
            warInPreparation = 7;
        }

        return excelExporter.exportToExcel(meuClan1.members(), meuClan2.members(), meuClan3.members(), meuClan4.members(),
                meuClan5.members(), meuClan6.members(), meuClan7.members(), warInPreparation,clanName);

    }

    private boolean containsClanName(String clanName) {
        for (Clans clan : Clans.values()) {
            if (clanName.contains(clan.name())) {
                return true;
            }
        }
        return false;
    }

    public ClanWarLeagueGroup fetchClanWarLeagueData(String tag) throws Exception {
        String encodedTag = tag.replace("#", "%23");
        String url = apiWarLeagueGroup.replace("tag", encodedTag);

        // Use HttpUtil to create the HttpRequest
        HttpRequest request = HttpUtil.createRequest(url, bearerToken);

        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response.body(), ClanWarLeagueGroup.class);
        }
        if (response.statusCode() == 404) {
            throw new RuntimeException("Não foi encontrado uma liga de guerras para o clã informado.");
        }
        return null;
    }

    public ClanWarLeagueWarRegistry fetchClanWarLeagueWarRegistry(String tag, int dia) throws Exception {
        // Busca os dados do grupo de guerras
        ClanWarLeagueGroup clanWarLeagueGroup = fetchClanWarLeagueData(tag);
        List<String> tags = clanWarLeagueGroup.rounds().get(dia-1).warTags();
//        System.out.println(tags);

        HttpClient client = HttpClient.newHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();

        for (String tagHere : tags) {
            String encodedTag = tagHere.replace("#", "%23");
            String url = apiClanWarsLeague.replace("tag", encodedTag);

            HttpRequest request = HttpUtil.createRequest(url, bearerToken);
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String responseBody = response.body();
                // Verifica se o conteúdo contém "NATIVIDADE"
                for (Clans clan : Clans.values()) {
                    if (responseBody.contains(clan.name())) {
                        return objectMapper.readValue(responseBody, ClanWarLeagueWarRegistry.class);
                    }
                }
            } else {
                System.out.println("Erro: " + response.statusCode() + " - " + response.body());
            }
        }

        // Caso não encontre o conteúdo desejado
        return null;
    }

    public File exportWarRegistryToJson(ClanWarLeagueWarRegistry warRegistry, String fileName) throws IOException {
        if (warRegistry != null) {
            File outputDir = new File(System.getProperty("java.io.tmpdir"), "generatedJsons");
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }

            File outputFile = new File(outputDir, fileName);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(outputFile, warRegistry);

            System.out.println("JSON exportado para: " + outputFile.getAbsolutePath());
            return outputFile;
        } else {
            System.out.println("WarRegistry é nulo, não será exportado: " + fileName);
            return null;
        }
    }


}
