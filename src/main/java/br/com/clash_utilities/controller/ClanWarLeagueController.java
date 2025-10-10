package br.com.clash_utilities.controller;

import br.com.clash_utilities.model.*;
import br.com.clash_utilities.model.enums.Clans;
import br.com.clash_utilities.service.ClanWarLeagueService;
import br.com.clash_utilities.service.ClanWarLeagueServiceV2;
import br.com.clash_utilities.utils.ExcelGenerator;
import br.com.clash_utilities.utils.HttpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@RestController
@RequestMapping("/api/clanwarleague")
public class ClanWarLeagueController {

    @Autowired
    private ClanWarLeagueService clanWarLeagueService;

    @Autowired
    private ClanWarLeagueServiceV2 clanWarLeagueServiceV2;

    @Value("${clashofclans.endpoints.clan-war-league-group}")
    private String apiWarLeagueGroup;

    @Value("${clashofclans.endpoints.clan-wars-league}")
    private String apiClanWarsLeague;

    @Value("${clashofclans.api.bearer-token}")
    private String bearerToken;

    @GetMapping("/v1/exportLeagueFile")
    public List<String> exportLeagueFile() {
        try {//, , "%232QCVR0P2R","%232YJRLYYCC",
            //2320P292C8Y infinitos
            //232YJRLYYCC natividade
            List<String> tags = Arrays.asList("%232YJRLYYCC");
            List<String> filePaths = new ArrayList<>();
            for (String tag : tags) {
                File excelFile = clanWarLeagueService.exportExcelFileAllSevenDaysOfLeague(tag);
                filePaths.add(excelFile.getAbsolutePath());
                System.out.println("Excel file exported to: " + excelFile.getAbsolutePath());
            }
            return filePaths;
        } catch (Exception e) {
            throw new RuntimeException("Error exporting Clan War League data", e);
        }
    }

    @GetMapping("/v3/exportLeagueFile")
    public void exportLeagueFileV3() {
        clanWarLeagueServiceV2.exportLeagueFile();
    }

    @GetMapping("/v2/exportLeagueFile")
    public void exportLeagueFileV2() {
        try {

            //TAGS DOS CLAS QUE TERAM O ARQUIVO DA LIGA GERADO E EXPORTADO
//            List<String> tags = Arrays.asList("%2320P292C8Y");
//
//            List<String> filePaths = new ArrayList<>();
//
//            for (String tag : tags) {
//                File excelFile = clanWarLeagueService.exportExcelFileAllSevenDaysOfLeague(tag);
//                filePaths.add(excelFile.getAbsolutePath());
//                System.out.println("Excel file exported to: " + excelFile.getAbsolutePath());
//            }
//            return filePaths;

//            String tag = "%2320P292C8Y"; //INFINITOS?
            String tag = "%232YJRLYYCC"; //NATIVIDADE
//            String meuCla = "INFINITOS";
            String meuCla = "NATIVIDADE_BR";

            ClanWarLeagueWarRegistry warRegistry1 = fetchClanWarLeagueWarRegistry(tag, 1);
            exportWarRegistryToJson(warRegistry1, "warRegistry1.json");

            ClanWarLeagueWarRegistry warRegistry2 = fetchClanWarLeagueWarRegistry(tag, 2);
            exportWarRegistryToJson(warRegistry2, "warRegistry2.json");

            ClanWarLeagueWarRegistry warRegistry3 = fetchClanWarLeagueWarRegistry(tag, 3);
            exportWarRegistryToJson(warRegistry3, "warRegistry3.json");

            ClanWarLeagueWarRegistry warRegistry4 = fetchClanWarLeagueWarRegistry(tag, 4);
            exportWarRegistryToJson(warRegistry4, "warRegistry4.json");

            ClanWarLeagueWarRegistry warRegistry5 = fetchClanWarLeagueWarRegistry(tag, 5);
            exportWarRegistryToJson(warRegistry5, "warRegistry5.json");

            ClanWarLeagueWarRegistry warRegistry6 = fetchClanWarLeagueWarRegistry(tag, 6);
            exportWarRegistryToJson(warRegistry6, "warRegistry6.json");

            ClanWarLeagueWarRegistry warRegistry7 = fetchClanWarLeagueWarRegistry(tag, 7);
            exportWarRegistryToJson(warRegistry7, "warRegistry7.json");

            ClanWarLeagueWarRegistry registroDia1 = lerRegistroDia1();
            ClanWarLeagueWarRegistry registroDia2 = lerRegistroDia2();
            ClanWarLeagueWarRegistry registroDia3 = lerRegistroDia3();
            ClanWarLeagueWarRegistry registroDia4 = lerRegistroDia4();
            ClanWarLeagueWarRegistry registroDia5 = lerRegistroDia5();
            ClanWarLeagueWarRegistry registroDia6 = lerRegistroDia6();
            ClanWarLeagueWarRegistry registroDia7 = lerRegistroDia7();

            ClanWarLeagueWarClan meuClan1 = registroDia1.clan().name().equals(meuCla) ? registroDia1.clan() : registroDia1.opponent();
            ClanWarLeagueWarClan meuClan2 = registroDia2.clan().name().equals(meuCla) ? registroDia2.clan() : registroDia2.opponent();
            ClanWarLeagueWarClan meuClan3 = registroDia3.clan().name().equals(meuCla) ? registroDia3.clan() : registroDia3.opponent();
            ClanWarLeagueWarClan meuClan4 = registroDia4.clan().name().equals(meuCla) ? registroDia4.clan() : registroDia4.opponent();
            ClanWarLeagueWarClan meuClan5 = registroDia5.clan().name().equals(meuCla) ? registroDia5.clan() : registroDia5.opponent();
            ClanWarLeagueWarClan meuClan6 = registroDia6.clan().name().equals(meuCla) ? registroDia6.clan() : registroDia6.opponent();
            ClanWarLeagueWarClan meuClan7 = registroDia7.clan().name().equals(meuCla) ? registroDia7.clan() : registroDia7.opponent();

            List<List<ClanWarLeagueWarMembers>> allDaysMembers = Arrays.asList(
                    meuClan1.members(),
                    meuClan2.members(),
                    meuClan3.members(),
                    meuClan4.members(),
                    meuClan5.members(),
                    meuClan6.members(),
                    meuClan7.members()
            );

            Set<String> uniqueTags = new HashSet<>();
            for (List<ClanWarLeagueWarMembers> dayMembers : allDaysMembers) {
                for (ClanWarLeagueWarMembers member : dayMembers) {
                    uniqueTags.add(member.tag());
                }
            }

            System.out.println("Total unique members: " + uniqueTags.size());
            System.out.println("Unique members: " + uniqueTags);

            List<PlayerData> playerDataList = new ArrayList<>();
            for (String tags : uniqueTags) {
                Map<Integer, DayData> warData = new HashMap<>();
                String name = null;

                for (int day = 0; day < allDaysMembers.size(); day++) {
                    for (ClanWarLeagueWarMembers member : allDaysMembers.get(day)) {
                        if (member.tag().equals(tags)) { // Corrigido para usar "tags"
                            name = member.name();
                            int attackStars = member.attacks() != null && !member.attacks().isEmpty()
                                    ? member.attacks().get(0).stars() // Pega os attackStars do primeiro ataque
                                    : 0;
                            double defenseStars = member.bestOpponentAttack() != null
                                    ? ((3 - member.bestOpponentAttack().stars()) * 0.5)
                                    : 0.5;
                            warData.put(day + 1, new DayData(attackStars, defenseStars));
                        }
                    }
                }

                if (name != null) {
                    playerDataList.add(new PlayerData(tags, name, warData));
                }
            }

            String filePath = "C:\\Users\\rafae\\Documents\\PlayerData.xlsx";

            Collections.sort(playerDataList, Comparator.comparingDouble(PlayerData::getTotalStars).reversed());

            ExcelGenerator excelGenerator = new ExcelGenerator();
            excelGenerator.generatePlayerDataExcel(playerDataList, filePath);

//            return lerRegistroDia1();
        } catch (Exception e) {
            throw new RuntimeException("Error exporting Clan War League data", e);
        }
    }

    private ClanWarLeagueWarRegistry lerRegistroDia1() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        // Substitua pelo caminho completo do arquivo JSON
        String filePath = "C:\\Users\\rafae\\AppData\\Local\\Temp\\generatedJsons\\warRegistry1.json";
        File file = new File(filePath);
        if (file.exists()) {
            return objectMapper.readValue(file, ClanWarLeagueWarRegistry.class);
        } else {
            throw new RuntimeException("Arquivo não encontrado: " + filePath);
        }
    }

    private ClanWarLeagueWarRegistry lerRegistroDia2() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        // Substitua pelo caminho completo do arquivo JSON
        String filePath = "C:\\Users\\rafae\\AppData\\Local\\Temp\\generatedJsons\\warRegistry2.json";
        File file = new File(filePath);
        if (file.exists()) {
            return objectMapper.readValue(file, ClanWarLeagueWarRegistry.class);
        } else {
            throw new RuntimeException("Arquivo não encontrado: " + filePath);
        }
    }

    private ClanWarLeagueWarRegistry lerRegistroDia3() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        // Substitua pelo caminho completo do arquivo JSON
        String filePath = "C:\\Users\\rafae\\AppData\\Local\\Temp\\generatedJsons\\warRegistry3.json";
        File file = new File(filePath);
        if (file.exists()) {
            return objectMapper.readValue(file, ClanWarLeagueWarRegistry.class);
        } else {
            throw new RuntimeException("Arquivo não encontrado: " + filePath);
        }
    }

    private ClanWarLeagueWarRegistry lerRegistroDia4() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        // Substitua pelo caminho completo do arquivo JSON
        String filePath = "C:\\Users\\rafae\\AppData\\Local\\Temp\\generatedJsons\\warRegistry4.json";
        File file = new File(filePath);
        if (file.exists()) {
            return objectMapper.readValue(file, ClanWarLeagueWarRegistry.class);
        } else {
            throw new RuntimeException("Arquivo não encontrado: " + filePath);
        }
    }

    private ClanWarLeagueWarRegistry lerRegistroDia5() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        // Substitua pelo caminho completo do arquivo JSON
        String filePath = "C:\\Users\\rafae\\AppData\\Local\\Temp\\generatedJsons\\warRegistry5.json";
        File file = new File(filePath);
        if (file.exists()) {
            return objectMapper.readValue(file, ClanWarLeagueWarRegistry.class);
        } else {
            throw new RuntimeException("Arquivo não encontrado: " + filePath);
        }
    }

    private ClanWarLeagueWarRegistry lerRegistroDia6() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        // Substitua pelo caminho completo do arquivo JSON
        String filePath = "C:\\Users\\rafae\\AppData\\Local\\Temp\\generatedJsons\\warRegistry6.json";
        File file = new File(filePath);
        if (file.exists()) {
            return objectMapper.readValue(file, ClanWarLeagueWarRegistry.class);
        } else {
            throw new RuntimeException("Arquivo não encontrado: " + filePath);
        }
    }

    private ClanWarLeagueWarRegistry lerRegistroDia7() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        // Substitua pelo caminho completo do arquivo JSON
        String filePath = "C:\\Users\\rafae\\AppData\\Local\\Temp\\generatedJsons\\warRegistry7.json";
        File file = new File(filePath);
        if (file.exists()) {
            return objectMapper.readValue(file, ClanWarLeagueWarRegistry.class);
        } else {
            throw new RuntimeException("Arquivo não encontrado: " + filePath);
        }
    }

    private File exportWarRegistryToJson(ClanWarLeagueWarRegistry warRegistry, String fileName) throws IOException {
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

    private ClanWarLeagueWarRegistry fetchClanWarLeagueWarRegistry(String tag, int dia) throws Exception {
        // Busca os dados do grupo de guerras
        ClanWarLeagueGroup clanWarLeagueGroup = fetchClanWarLeagueData(tag);
        List<String> tags = clanWarLeagueGroup.rounds().get(dia - 1).warTags();
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

    private ClanWarLeagueGroup fetchClanWarLeagueData(String tag) throws Exception {
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

}
