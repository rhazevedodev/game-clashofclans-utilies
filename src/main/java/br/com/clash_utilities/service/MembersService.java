package br.com.clash_utilities.service;

import br.com.clash_utilities.model.ClanMemberList;
import br.com.clash_utilities.model.dto.ContaInfoDTO;
import br.com.clash_utilities.model.dto.MemberDTO;
import br.com.clash_utilities.model.dto.MemberListDTO;
import br.com.clash_utilities.model.enums.ContaEnum;
import br.com.clash_utilities.utils.HttpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class MembersService {

    @Value("${clashofclans.endpoints.clan-members}")
    private String apiMembersUrl;

    @Value("${clashofclans.endpoints.clan}")
    private String apiUrl;

    @Value("${clashofclans.api.bearer-token}")
    private String bearerToken;

    @Autowired
    private HttpClient httpClient;
    @Autowired
    private ObjectMapper objectMapper;

    @SneakyThrows
    public String exportMembersToExcel()  {
        String tagNatividade = "%232YJRLYYCC";

        //1. Carregar lista dos membros do clã /tag+nome
        MemberListDTO list = obterDadosClan(tagNatividade);
        try {
            System.out.println(objectMapper.writeValueAsString(list));
        } catch (Exception e){
            throw new RuntimeException(e);
        }

        // 2. Carregar o JSON com as contas conhecidas
        List<ContaInfoDTO> contasConhecidas = carregarContas(); // método abaixo
        Map<String, ContaInfoDTO> mapaContas = contasConhecidas.stream()
                .collect(Collectors.toMap(c -> c.getTag().toUpperCase(), Function.identity()));

        // 3. Separar conhecidos e desconhecidos
        List<MemberDTO> conhecidos = new ArrayList<>();
        List<MemberDTO> desconhecidos = new ArrayList<>();
        List<MemberDTO> secundarias = new ArrayList<>();
//                conhecidos.stream()
//                .filter(membro -> !"true".equalsIgnoreCase(mapaContas.get(membro.getTag().toUpperCase()).getContaPrincipal()))
//                .toList();

        for (MemberDTO membro : list.getMembers()) {
            String tag = membro.getTag().toUpperCase();
            if (mapaContas.containsKey(tag)) {
                conhecidos.add(membro);
            } else {
                desconhecidos.add(membro);
            }
        }

        // 4. Debug temporário (você pode depois salvar ou exportar isso)
        try {
            System.out.println("Conhecidos:");
            System.out.println(objectMapper.writeValueAsString(conhecidos));
            System.out.println("Secundárias:");
            System.out.println(objectMapper.writeValueAsString(secundarias));
            System.out.println("Desconhecidos:");
            System.out.println(objectMapper.writeValueAsString(desconhecidos));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return gerarPlanilhaExcel("Natividade_BR", conhecidos, secundarias, desconhecidos);
    }

    private MemberListDTO obterDadosClan(String tag) {
        try {
            String encodedTag = tag.replace("#", "%23");
            String url = apiMembersUrl.replace("tag", encodedTag);

            HttpRequest request = HttpUtil.createRequest(url, bearerToken);
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            ClanMemberList clanMemberList = objectMapper.readValue(response.body(), ClanMemberList.class);

            MemberListDTO memberListDTO = new MemberListDTO();
            memberListDTO.setMembers(
                    clanMemberList.items().stream()
                            .map(member -> new MemberDTO(
                                    member.tag(),
                                    member.name()
                            ))
                            .toList()
            );
            return memberListDTO;
        } catch(Exception e){
            throw new RuntimeException (e);
        }
    }

    private List<ContaInfoDTO> carregarContas() {
        ObjectMapper mapper = new ObjectMapper();
        Path externalPath = Paths.get("data/membersList.json");

        try {
            if (Files.exists(externalPath)) {
                return new ArrayList<>(Arrays.asList(mapper.readValue(Files.newBufferedReader(externalPath), ContaInfoDTO[].class)));
            } else {
                try (InputStream is = getClass().getClassLoader().getResourceAsStream("membersList.json")) {
                    if (is == null) {
                        throw new FileNotFoundException("Arquivo membersList.json não encontrado");
                    }
                    return new ArrayList<>(Arrays.asList(mapper.readValue(is, ContaInfoDTO[].class)));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar membersList.json", e);
        }
    }

    @SneakyThrows
    public ResponseEntity<String> adicionarMembro(ContaInfoDTO membro) {
            List<ContaInfoDTO> contas = carregarContas();

        boolean jaExiste = contas.stream()
                .anyMatch(c -> c.getTag().equalsIgnoreCase(membro.getTag()));

        if (jaExiste) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Essa conta já está cadastrada.");
        }

            contas.add(membro);
            salvarContas(contas);
            return ResponseEntity.ok("Membro adicionado com sucesso!");
    }

    @SneakyThrows
    private void salvarContas(List<ContaInfoDTO> contas) {
        ObjectMapper mapper = new ObjectMapper();
        Path externalPath = Paths.get("data/membersList.json");

        try {
            // Cria o diretório se não existir
            Files.createDirectories(externalPath.getParent());

            // Salva com indentação (bonitinho)
            mapper.writerWithDefaultPrettyPrinter().writeValue(externalPath.toFile(), contas);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar membersList.json", e);
        }
    }

    public ResponseEntity<String> adicionarVariosMembros(List<ContaInfoDTO> contaInfoDTOList) {
        try {
            List<ContaInfoDTO> contasExistentes = carregarContas();

            // Criar um conjunto com as tags já existentes para evitar duplicatas
            Set<String> tagsExistentes = contasExistentes.stream()
                    .map(c -> c.getTag().toUpperCase())
                    .collect(Collectors.toSet());

            // Filtrar apenas as contas que ainda não existem
            List<ContaInfoDTO> contasParaAdicionar = contaInfoDTOList.stream()
                    .filter(c -> !tagsExistentes.contains(c.getTag().toUpperCase()))
                    .toList();

            // Adicionar as novas contas
            contasExistentes.addAll(contasParaAdicionar);

            // Salvar no arquivo
            salvarContas(contasExistentes);

            return ResponseEntity.ok(contasParaAdicionar.size() + " contas adicionadas com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao adicionar múltiplas contas: " + e.getMessage());
        }
    }
    public ResponseEntity<String> obterJsonDoEnum() {
        try {
            // Percorre todos os valores do enum ContaEnum e mapeia para ContaInfoDTO
            List<ContaInfoDTO> contaInfoDTOList = Arrays.stream(ContaEnum.values())
                    .map(contaEnum -> new ContaInfoDTO(
                            contaEnum.name(), // Nome do enum
                            contaEnum.getTag(),
                            contaEnum.getContato(),
                            contaEnum.isContaPrincipal(),
                            contaEnum.getTagContaPrincipal()
                    ))
                    .toList();

            // Converte a lista para JSON
            String json = objectMapper.writeValueAsString(contaInfoDTOList);

            return ResponseEntity.ok(json);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao gerar JSON do enum: " + e.getMessage());
        }

    }

    @SneakyThrows
    private String gerarPlanilhaExcel(String nomeClan, List<MemberDTO> principais, List<MemberDTO> secundarias, List<MemberDTO> desconhecidos) {
        String nomeSanitizado = nomeClan.replaceAll("[^a-zA-Z0-9]", "_");
        String dataAtual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String nomeArquivo = nomeSanitizado + "_" + dataAtual + ".xlsx";

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Membros");

            // Cabeçalho
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Tag");
            header.createCell(1).setCellValue("Membro");
            header.createCell(2).setCellValue("Tag");
            header.createCell(3).setCellValue("Membro");
            header.createCell(4).setCellValue("Tag");
            header.createCell(5).setCellValue("Membro");

            int max = Math.max(principais.size(), Math.max(secundarias.size(), desconhecidos.size()));
            for (int i = 0; i < max; i++) {
                Row row = sheet.createRow(i + 1);
                if (i < principais.size()) {
                    MemberDTO m = principais.get(i);
                    row.createCell(0).setCellValue(m.getTag());
                    row.createCell(1).setCellValue(m.getName());
                }
                if (i < secundarias.size()) {
                    MemberDTO m = secundarias.get(i);
                    row.createCell(2).setCellValue(m.getTag());
                    row.createCell(3).setCellValue(m.getName());
                }
                if (i < desconhecidos.size()) {
                    MemberDTO m = desconhecidos.get(i);
                    row.createCell(4).setCellValue(m.getTag());
                    row.createCell(5).setCellValue(m.getName());
                }
            }

            // Autoajuste de colunas
            for (int i = 0; i <= 5; i++) {
                sheet.autoSizeColumn(i);
            }

            // Salvar arquivo
            File outputDir = new File(System.getProperty("java.io.tmpdir"), "generatedExcels");
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }

            File outputFile = new File(outputDir, nomeArquivo+".xlsx");
            try (FileOutputStream fileOut = new FileOutputStream(outputFile)) {
                workbook.write(fileOut);
            }

            return "Arquivo gerado: " + outputFile.getAbsolutePath();
        }
    }
}
