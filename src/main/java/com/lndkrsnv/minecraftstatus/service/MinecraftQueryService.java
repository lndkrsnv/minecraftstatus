package com.lndkrsnv.minecraftstatus.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tekgator.queryminecraftserver.api.Protocol;
import com.tekgator.queryminecraftserver.api.QueryStatus;
import com.tekgator.queryminecraftserver.api.Status;
import org.springframework.stereotype.Service;

@Service
public class MinecraftQueryService {

    public String queryServer(String host, int port, String protocolName) throws Exception {
        Protocol protocol = switch (protocolName.toUpperCase()) {
            case "UDP_FULL" -> Protocol.UDP_FULL;
            case "UDP_BASIC" -> Protocol.UDP_BASIC;
            default -> Protocol.TCP;
        };

        QueryStatus queryStatus = new QueryStatus.Builder(host)
                .setPort(port)
                .setProtocol(protocol)
                .setTimeout(5000)
                .build();

        Status status = queryStatus.getStatus();
        String fullJson = status.toJson();

        JsonObject jsonObject = JsonParser.parseString(fullJson).getAsJsonObject();
        jsonObject.remove("favicon");

        return jsonObject.toString();
    }
}
