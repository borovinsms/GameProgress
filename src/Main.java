import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {

    private static void saveGame(GameProgress gameProgress, String path) {
        try {
            File file = new File(path);
            file.getParentFile().mkdirs();
            file.createNewFile();
            try (FileOutputStream fileOutputStream = new FileOutputStream(file, true)) {
                try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
                    objectOutputStream.writeObject(gameProgress);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void zipFiles(String path, String[] files) {
        try {
            try (FileOutputStream fileOutputStream = new FileOutputStream(path)) {
                try (ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream)) {
                    for (String filePath : files) {
                        File file = new File(filePath);
                        try (FileInputStream fileInputStream = new FileInputStream(file)) {
                            ZipEntry zipEntry = new ZipEntry(file.getName());
                            zipOutputStream.putNextEntry(zipEntry);
                            int length;
                            byte[] buffer = new byte[1024];
                            while ((length = fileInputStream.read(buffer)) > 0) {
                                zipOutputStream.write(buffer, 0, length);
                            }
                            zipOutputStream.closeEntry();
                            file.deleteOnExit();
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String homeDir = System.getProperty("user.home");
        String[] files = {
                homeDir + "/Games/GunRunner/savegames/save1.dat",
                homeDir + "/Games/GunRunner/savegames/save2.dat",
                homeDir + "/Games/GunRunner/savegames/save3.dat"
        };
        GameProgress gameProgress1 = new GameProgress(100, 0, 1, 0);
        GameProgress gameProgress2 = new GameProgress(90, 1, 2, 2.5);
        GameProgress gameProgress3 = new GameProgress(70, 3, 3, 8.7);
        saveGame(gameProgress1, files[0]);
        saveGame(gameProgress2, files[1]);
        saveGame(gameProgress3, files[2]);
        zipFiles(homeDir + "/Games/GunRunner/savegames/saves.zip", files);
    }
}
