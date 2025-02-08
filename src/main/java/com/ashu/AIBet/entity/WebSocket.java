package com.ashu.AIBet.entity;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ServerHandshake;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.util.Base64;

public class WebSocket extends WebSocketClient {

    public WebSocket(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected to server");
//        byte[] decodedBytes = Base64.getDecoder().decode("gAA0EgADAAFjAgEAAWEDAA0AAXASAAMAAWMIAAxQSU5HX1JFUVVFU1QAAXIE/////wABcBIAAA==");
//        this.send(decodedBytes);
        File f=new File("C:/Users/dell/Downloads/download.drawio");
        Framedata fileBytes;
		try {
			byte[] a = Files.readAllBytes(f.toPath());
//			fileBytes.append(a);
//			sendFrame(fileBytes);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        System.out.println("message sent===");
    }

    @Override
    public void onMessage(String message) {
    	 byte[] decodedBytes = Base64.getDecoder().decode("gAA0EgADAAFjAgEAAWEDAA0AAXASAAMAAWMIAAxQSU5HX1JFUVVFU1QAAXIE/////wABcBIAAA==");
//       this.send(decodedBytes);
       send(decodedBytes);
        System.out.println("Message from server: " + message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Connection closed: " + reason+" code=="+ code);
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }
    public void sendBase64Message(String base64String) {
        byte[] decodedBytes = Base64.getDecoder().decode(base64String);
        this.send(decodedBytes);
    }

}
