package br.com.clash_utilities.service;

import br.com.clash_utilities.model.*;
import br.com.clash_utilities.utils.ExcelGenerator;
import br.com.clash_utilities.utils.HttpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

@Service
public class ClanWarLeagueServiceV2 {

    @Value("${clashofclans.endpoints.clan-war-league-group}")
    private String apiWarLeagueGroup;

    @Value("${clashofclans.endpoints.clan-wars-league}")
    private String apiClanWarsLeague;

    @Value("${clashofclans.api.bearer-token}")
    private String bearerToken;

    @Autowired
    private ClanConfigService clanConfigService;

    private Set<String> uniqueMembersInfinitos = new HashSet<>();
    private Set<String> uniqueMembersNatividade = new HashSet<>();
    private Set<String> uniqueMembersJoy = new HashSet<>();

    public void exportLeagueFile() {
        System.out.println("Iniciando exportação do arquivo da liga de guerras...");
        try {
            LocalDate hoje = LocalDate.now();
            String nomeMes = hoje.getMonth().getDisplayName(TextStyle.FULL, new Locale("pt", "BR"));
            String ano = String.valueOf(hoje.getYear());

            Workbook workbook = new XSSFWorkbook();
            String filePath = "C:\\Users\\rafae\\Documents\\clash\\LigaMensal_" + ano + nomeMes + ".xlsx";

            for (Clan clan : clanConfigService.getClans()) {
                String tag = clan.getTag();
                String nomeCla = clan.getNome();
                System.out.println("Clã: " + nomeCla + " | Tag: " + tag);

                try {
                    gerarArquivosDeGuerra(tag);
                } catch (RuntimeException e) {
                    if (e.getMessage() != null && e.getMessage().contains("Não foi encontrado uma liga de guerras para o clã informado."))  {
                        System.out.println("[AVISO] " + e.getMessage() + " (Tag: " + tag + ")");
                        continue; // Pula para o próximo clã
                    } else {
                        throw e; // Relança outras exceções
                    }
                }
                List<ClanWarLeagueWarRegistry> registros = lerRegistrosDeGuerra();
                List<ClanWarLeagueWarClan> clans = lerClansDaLiga(nomeCla, registros);
                List<List<ClanWarLeagueWarMembers>> lerMembrosDaLiga = lerMembrosDaLiga(clans);
                Set<String> uniqueTags = obterTagsUnicas(lerMembrosDaLiga,nomeCla);
                System.out.println("Total de membros na liga: " + uniqueTags.size());
                System.out.println("Membros únicos na liga: " + uniqueTags);
                System.out.println("Nomes únicos na liga (Natividade): " + uniqueMembersNatividade);
                System.out.println("Nomes únicos na liga (Infinitos): " + uniqueMembersInfinitos);
                System.out.println("Nomes únicos na liga (Joy): " + uniqueMembersJoy);
                List<PlayerData> playerDataList = organizarDadosDosJogadores(uniqueTags, lerMembrosDaLiga);

                gerarExcelFile(playerDataList, workbook, nomeCla);
            }
            // Salva o arquivo Excel único
            try (java.io.FileOutputStream fileOut = new java.io.FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
            System.out.println("Excel mensal gerado com sucesso em: " + filePath);
        } catch (Exception ex) {
            throw new RuntimeException("Erro ao exportar arquivo da liga de guerras: " + ex.getMessage(), ex);
        }
    }

    private void gerarExcelFile(List<PlayerData> playerDataList, Workbook workbook, String sheetName) {
        System.out.println("Gerando aba Excel: " + sheetName);
        ExcelGenerator excelGenerator = new ExcelGenerator();
        excelGenerator.generatePlayerDataExcel(playerDataList, workbook, sheetName);
    }

    private List<PlayerData> organizarDadosDosJogadores(Set<String> uniqueTags, List<List<ClanWarLeagueWarMembers>> lerMembrosDaLiga) {
        System.out.println("Organizando dados dos jogadores...");
        List<PlayerData> playerDataList = new ArrayList<>();
        double attackStarsWeight = clanConfigService.getAttackStarsWeight();
        double defenseStarsWeight = clanConfigService.getDefenseStarsWeight();
        for (String tags : uniqueTags) {
            Map<Integer, DayData> warData = new HashMap<>();
            String name = null;

            for (int day = 0; day < lerMembrosDaLiga.size(); day++) {
                for (ClanWarLeagueWarMembers member : lerMembrosDaLiga.get(day)) {
                    if (member.tag().equals(tags)) {
                        name = member.name();
                        int attackStars = member.attacks() != null && !member.attacks().isEmpty()
                                ? (int)(member.attacks().get(0).stars() * attackStarsWeight)
                                : (int)(0 * attackStarsWeight);
                        double defenseStars = member.bestOpponentAttack() != null
                                ? ((3 - member.bestOpponentAttack().stars()) * defenseStarsWeight)
                                : defenseStarsWeight;
                        warData.put(day + 1, new DayData(attackStars, defenseStars));
                    }
                }
            }

            if (name != null) {
                playerDataList.add(new PlayerData(tags, name, warData));
            }
        }
        Collections.sort(
                playerDataList,
                Comparator.comparingDouble(PlayerData::getTotalStars).reversed()
                        .thenComparing(Comparator.comparingInt(PlayerData::getTotalAttackStars).reversed())
                        .thenComparing(Comparator.comparingDouble(PlayerData::getTotalDefenseStars).reversed())
        );
        return playerDataList;
    }

    private Set<String> obterTagsUnicas(List<List<ClanWarLeagueWarMembers>> lerMembrosDaLiga, String nomeCla){
        System.out.println("Obtendo tags únicas dos membros da liga...");
        Set<String> uniqueTags = new HashSet<>();
        for (List<ClanWarLeagueWarMembers> dayMembers : lerMembrosDaLiga) {
            for (ClanWarLeagueWarMembers member : dayMembers) {
                uniqueTags.add(member.tag());
                if(nomeCla.equalsIgnoreCase("NATIVIDADE_BR")) {
                    uniqueMembersNatividade.add(member.name());
                } else if (nomeCla.equalsIgnoreCase("INFINITOS")) {
                    uniqueMembersInfinitos.add(member.name());
                } else if (nomeCla.equalsIgnoreCase("Joy Division II")) {
                    uniqueMembersJoy.add(member.name());
                }
            }
        }
        return uniqueTags;
    }

    private List<List<ClanWarLeagueWarMembers>> lerMembrosDaLiga(List<ClanWarLeagueWarClan> clans) throws Exception {
        System.out.println("Lendo membros da liga...");
        List<List<ClanWarLeagueWarMembers>> membrosDaLiga = new ArrayList<>();
        for (ClanWarLeagueWarClan clan : clans) {
            if (clan != null) {
                membrosDaLiga.add(clan.members());
            }
        }
        return membrosDaLiga;
    }

    private List<ClanWarLeagueWarClan> lerClansDaLiga(String nomeCla, List<ClanWarLeagueWarRegistry> registros) throws Exception {
        System.out.println("Lendo clans da liga...");
        List<ClanWarLeagueWarClan> clans = new ArrayList<>();
        for (ClanWarLeagueWarRegistry registro : registros) {
            if (registro != null) {
                ClanWarLeagueWarClan clan = registro.clan().name().equals(nomeCla) ? registro.clan() : registro.opponent();
                clans.add(clan);
            }
        }
        return clans;
    }

    private List<ClanWarLeagueWarRegistry> lerRegistrosDeGuerra() throws IOException {
        System.out.println("Lendo registros de guerra...");
        ObjectMapper objectMapper = new ObjectMapper();
        List<ClanWarLeagueWarRegistry> warRegistries = new ArrayList<>();
        int arquivosEncontrados = 0;
        for (int i = 1; i <= 7; i++) {
            String filePath = "C:\\Users\\rafae\\AppData\\Local\\Temp\\generatedJsons\\warRegistry" + i + ".json";
            File file = new File(filePath);
            if (file.exists()) {
                ClanWarLeagueWarRegistry registry = objectMapper.readValue(file, ClanWarLeagueWarRegistry.class);
                warRegistries.add(registry);
                arquivosEncontrados++;
            } else {
                System.out.println("Arquivo não encontrado: " + filePath);
            }
        }
        if (arquivosEncontrados == 0) {
            throw new RuntimeException("Nenhum arquivo de registro de guerra foi encontrado.");
        }
        return warRegistries;
    }

    private void gerarArquivosDeGuerra(String tag) {
        System.out.println("Gerando arquivos de guerra para o clã com tag: " + tag);
        try {
            for(int i = 1; i <= 7; i++) {
                ClanWarLeagueWarRegistry warRegistry = buscarRegistroDeGuerraDaLigaDeClas(tag, i);
                exportarRegistroDeGuerraParaJson(warRegistry, "warRegistry" + i + ".json");
            }
        } catch (Exception ex){
            throw new RuntimeException("Erro ao gerar arquivos de guerra para o clã com tag " + tag + ": " + ex.getMessage(), ex);
        }
    }

    private ClanWarLeagueWarRegistry buscarRegistroDeGuerraDaLigaDeClas(String tag, int dia) throws Exception {
        System.out.println("Buscando registro de guerra do dia " + dia + " para o clã com tag: " + tag);
        ClanWarLeagueGroup clanWarLeagueGroup = buscarDadosDaLigaDeGuerrasDoCla(tag);
        List<String> tags = clanWarLeagueGroup.rounds().get(dia - 1).warTags();

        HttpClient client = HttpClient.newHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();

        for (String tagHere : tags) {
            String encodedTag = tagHere.replace("#", "%23");
            String url = apiClanWarsLeague.replace("tag", encodedTag);

            HttpRequest request = HttpUtil.createRequest(url, bearerToken);
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String responseBody = response.body();
                for (Clan clan : clanConfigService.getClans()) {
                    if (responseBody.contains(clan.getNome())) {
                        return objectMapper.readValue(responseBody, ClanWarLeagueWarRegistry.class);
                    }
                }
            } else {
                System.out.println("Erro: " + response.statusCode() + " - " + response.body());
            }
        }
        return null;
    }

    private File exportarRegistroDeGuerraParaJson(ClanWarLeagueWarRegistry warRegistry, String fileName) throws IOException {
        System.out.println("Exportando registro de guerra para JSON: " + fileName);
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

    private ClanWarLeagueGroup buscarDadosDaLigaDeGuerrasDoCla(String tag) throws Exception {
        System.out.println("Buscando dados da liga de guerras do clã com tag: " + tag);
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
