package unirio.pm.external_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import unirio.pm.external_service.services.AdminService;

@RestController
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/restaurarBanco")
    public ResponseEntity<Void> restaurarBanco() {
        adminService.restaurarBanco();
        return ResponseEntity.ok().build();
    }
}
