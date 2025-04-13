package br.com.clash_utilities.controller;

import br.com.clash_utilities.service.MembersService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/members")
public class MemberController {
    Logger logger = Logger.getLogger(MemberController.class.getName());

    private MembersService membersService;

    public MemberController(MembersService membersService) {
        this.membersService = membersService;
    }

    @GetMapping("/exportExcel")
    public List<String> exportToExcel(){
        return List.of(membersService.exportMembersToExcel());
    }
}