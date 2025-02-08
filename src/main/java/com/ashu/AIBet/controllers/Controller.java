package com.ashu.AIBet.controllers;



import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ashu.AIBet.entity.WebSocket;
import com.ashu.AIBet.service.AviatorService;
import com.ashu.AIBet.service.BaccaratService;

@RestController
@RequestMapping("/api/aviator")
public class Controller {

    @Autowired
    private AviatorService aviatorService;
    @Autowired
    private BaccaratService baccaratService;
    @PostMapping("/signal")
    public ResponseEntity<String> triggerAutomation(@RequestBody String signalMessage) {
        try {
        	System.out.println("calling from bot=="+signalMessage);
//            aviatorService.startAutomation();
        	baccaratService.startAutomation();
            return ResponseEntity.ok("Automation started successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error during automation: " + e.getMessage());
        }
    }
    
    @PostMapping("/bet")
    public ResponseEntity<String> automateBetting(@RequestBody String signalMessage) {
        try {
            aviatorService.startBetting(signalMessage);
            return ResponseEntity.ok("Automation started successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error during automation: " + e.getMessage());
        }
    }
    
    @GetMapping("/start-websocket")
    public String startWebSocketConnection() {
        try {
//        	URI serverUri = new URI("wss://cf.1win.direct/v4/socket.io/?Authorization=f4877bf1-3494-5b15-a844-3c9158d109b5&Language=en&xorigin=1win.pro&EIO=4&transport=websocket");
//            Map<String, String> headers = new HashMap<>();
//            headers.put("Authorization", "f4877bf1-3494-5b15-a844-3c9158d109b5");
//            headers.put("Language", "en");
//            headers.put("xorigin", "1win.pro");
//            headers.put("EIO", "4");
//            headers.put("transport", "websocket");
            URI serverUri1 = new URI("wss://eu-central-1-game6.spribegaming.com/BlueBox/websocket");
//            WebSocket client = new WebSocket(serverUri);
//            client.connect();  // Starts the WebSocket connection
            WebSocket client1 = new WebSocket(serverUri1);
            client1.connect();  // Starts the WebSocket connect
            return "WebSocket connection started.";
        } catch (Exception e) {
            return "Error starting WebSocket connection: " + e.getMessage();
        }
    }
}
