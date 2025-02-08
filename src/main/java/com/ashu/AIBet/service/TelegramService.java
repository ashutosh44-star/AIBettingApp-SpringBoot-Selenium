package com.ashu.AIBet.service;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import com.ashu.AIBet.AiAviatorBettingAppApplication;

@Service
public class TelegramService extends TelegramLongPollingBot {

	private final String botToken = "7418311702:AAEGpjIyWncQ9_i3jT5KQsgoaKVR1kiMgew";
	private final String botUsername = "AshuAviatorBot";
	private final String groupOrChannelId = "-1002383575606";
	private static final long PERSONAL_USER_ID = 1107886337; // Replace with your Telegram user ID

	@Override
	public String getBotToken() {
		return botToken;
	}

	@Override
	public String getBotUsername() {
		return botUsername;
	}

	@Override
	public void onUpdateReceived(Update update) {

//	    	if (update.hasChannelPost() && update.getChannelPost().hasText()) {
//	            handleChannelPost(update.getChannelPost());
//	        }

		if (update.hasMessage() && update.getMessage().hasText()) {
			long senderId = update.getMessage().getFrom().getId();
			String messageText = update.getMessage().getText();

			// Process only messages from your personal account
			if (senderId == PERSONAL_USER_ID) {
				switch (messageText.toLowerCase()) {
				case "start":
					String isStarted = AiAviatorBettingAppApplication.startApplication();
					if (isStarted == "Started") {
						sendMessageToGroup("Application started from the channel.");
					} else if (isStarted == "Already Running") {
						sendMessageToGroup("Application is already running...!");
					}
					break;

				case "stop":
					AiAviatorBettingAppApplication.stopApplication();
					sendMessageToGroup("Application stopped from the channel.");
					break;
				
				case "check":
//					AiAviatorBettingAppApplication.stopApplication();
					sendMessageToGroup("There was some issue in Baccarat Bot....it might be network problem...!");
					break;

				default:
					sendMessageToGroup("Unknown command. Use start or stop.");
				}
			} else {
				sendMessageToGroup("Unauthorized access! This bot is private.");
			}
		}
	}

	public void sendMessageToGroup(String messageText) {
		SendMessage message = new SendMessage();
		message.setChatId(groupOrChannelId);
		message.setText(messageText);
		try {
			execute(message);
			System.out.println("Message sent to group/channel: " + messageText);
		} catch (TelegramApiException e) {
			System.out.println("There is some issue in Sending message....Might be network problem..!");
//			e.printStackTrace();
//			System.exit(0);
		}
	}
	public void sendPhotoToGroup(String filePath) {
	    SendPhoto sendPhotoRequest = new SendPhoto();
	    sendPhotoRequest.setChatId(groupOrChannelId);
	    sendPhotoRequest.setPhoto(new InputFile(new File(filePath)));  // Use InputFile for local file

	    try {
	        execute(sendPhotoRequest);
	        System.out.println("Photo sent to group/channel: " + filePath);
	    } catch (TelegramApiException e) {
//	        e.printStackTrace();
	    }
	}

	public void connect() {
		try {
			TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
			botsApi.registerBot(new TelegramService());
			System.out.println("Bot is up and running...");
			new TelegramService()
					.sendMessageToGroup("Baccarat Session Checker is starting...!");
		} catch (TelegramApiException e) {
//			e.printStackTrace();
		}
	}

}
