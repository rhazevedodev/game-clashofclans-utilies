package br.com.clash_utilities.service;

import br.com.clash_utilities.model.Clan;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class ClanConfigService {
    @Value("${clans.config.path:classpath:clans.yml}")
    private Resource clansConfigResource;

    private List<Clan> clans = Collections.emptyList();
    private double attackStarsWeight = 1.0;
    private double defenseStarsWeight = 0.5;

    @PostConstruct
    public void loadClans() {
        try (InputStream is = clansConfigResource.getInputStream()) {
            Yaml yaml = new Yaml();
            Map<String, Object> data = yaml.load(is);
            List<Map<String, String>> clanList = (List<Map<String, String>>) data.get("clans");
            clans = clanList.stream()
                    .map(m -> new Clan(m.get("nome"), m.get("tag")))
                    .toList();
            // Carregar pesos
            Object atkWeight = data.get("attackStarsWeight");
            Object defWeight = data.get("defenseStarsWeight");
            if (atkWeight != null) attackStarsWeight = Double.parseDouble(atkWeight.toString());
            if (defWeight != null) defenseStarsWeight = Double.parseDouble(defWeight.toString());
        } catch (Exception e) {
            e.printStackTrace();
            clans = Collections.emptyList();
        }
    }

    public List<Clan> getClans() {
        return clans;
    }

    public double getAttackStarsWeight() {
        return attackStarsWeight;
    }

    public double getDefenseStarsWeight() {
        return defenseStarsWeight;
    }
}
