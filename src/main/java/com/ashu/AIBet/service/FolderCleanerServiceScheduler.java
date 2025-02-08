package com.ashu.AIBet.service;

import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;


@Component
public class FolderCleanerServiceScheduler {
	private static final String TEMP_FOLDER_PATH = "C:\\Users\\dell\\AppData\\Local\\Temp";

	@Scheduled(fixedRate = 14400000) // 4 hour in milliseconds
    public void cleanTempFolderAtInterval() {
        System.out.println("Starting temp folder cleanup...");
        cleanTempFolder(TEMP_FOLDER_PATH);
        System.out.println("Temp folder cleanup completed.");
    }

    public void cleanTempFolder(String folderPath) {
        File tempFolder = new File(folderPath);

        if (!tempFolder.exists() || !tempFolder.isDirectory()) {
            System.out.println("Temp folder not found or is not a directory.");
            return;
        }

        File[] files = tempFolder.listFiles();
        if (files != null) {
            for (File file : files) {
                try {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        if (file.delete()) {
                            System.out.println("Deleted file: " + file.getAbsolutePath());
                        } else {
//                            System.err.println("Could not delete file (in use or permission issue): " + file.getAbsolutePath());
                        }
                    }
                } catch (Exception e) {
//                    System.err.println("Error deleting: " + file.getAbsolutePath() + " - " + e.getMessage());
                }
            }
        }
    }

    private void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    if (file.delete()) {
                        System.out.println("Deleted file: " + file.getAbsolutePath());
                    } else {
//                        System.err.println("Could not delete file (in use or permission issue): " + file.getAbsolutePath());
                    }
                }
            }
        }
        if (directory.delete()) {
            System.out.println("Deleted directory: " + directory.getAbsolutePath());
        } else {
//            System.err.println("Could not delete directory (in use or permission issue): " + directory.getAbsolutePath());
        }
    }
}
