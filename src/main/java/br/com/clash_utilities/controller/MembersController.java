package br.com.clash_utilities.controller;

import br.com.clash_utilities.model.dto.ContaInfoDTO;
import br.com.clash_utilities.service.MembersService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/members")
public class MembersController {
    Logger logger = Logger.getLogger(MembersController.class.getName());

    @Autowired
    private MembersService membersService;

    @GetMapping("/exportMembersToExcel")
    public List<String> exportMembersToExcel() {
        return List.of(membersService.exportMembersToExcel());
    }

    @PostMapping("/adicionar")
    public ResponseEntity<String> adicionarMembro(@RequestBody ContaInfoDTO contaInfoDTO) {
            return membersService.adicionarMembro(contaInfoDTO);
    }

    @PostMapping("/adicionarVarios")
    public ResponseEntity<String> adicionarVariosMembros(@RequestBody List<ContaInfoDTO> contaInfoDTOList) {
        return membersService.adicionarVariosMembros(contaInfoDTOList);
    }

    @GetMapping("/obterJsonDoEnum")
    public ResponseEntity<String> obterJsonDoEnum() {
        return membersService.obterJsonDoEnum();
    }
}
