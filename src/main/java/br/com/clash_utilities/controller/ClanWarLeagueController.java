package br.com.clash_utilities.controller;

import br.com.clash_utilities.model.ClanWarLeagueGroup;
import br.com.clash_utilities.service.ClanWarLeagueService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/clanwarleague")
public class ClanWarLeagueController {

    @Autowired
    private ClanWarLeagueService clanWarLeagueService;

    @Value("${clashofclans.endpoints.clan-war-league-group}")
    private String apiWarLeagueGroup;

    @Value("${clashofclans.endpoints.clan-wars-league}")
    private String apiClanWarsLeague;

    @Value("${clashofclans.api.bearer-token}")
    private String bearerToken;

    @GetMapping("/exportLeagueFile")
    public List<String> exportLeagueFile() {
        try {//, "%2320P292C8Y", "%232QCVR0P2R"
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
}
