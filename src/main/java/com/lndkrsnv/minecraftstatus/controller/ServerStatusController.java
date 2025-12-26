package com.lndkrsnv.minecraftstatus.controller;

import com.lndkrsnv.minecraftstatus.service.MinecraftQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServerStatusController {

    @Autowired
    private MinecraftQueryService queryService;

    @GetMapping("/api/minecraft/status/{host}")
    public ResponseEntity<String> getServerStatus(
            @PathVariable String host,
            @RequestParam(required = false, defaultValue = "25565") int port,
            @RequestParam(required = false, defaultValue = "TCP") String protocol) {

        try {
            String statusJson = queryService.queryServer(host, port, protocol);
            return ResponseEntity.ok(statusJson);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}
