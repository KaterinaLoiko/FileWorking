import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {

  public static void main(String[] args) {
    StringBuilder log = new StringBuilder();
    File dir = new File("/Users/ekaterinalojko/Games/src");
    File dir1 = new File("/Users/ekaterinalojko/Games/res");
    File dir2 = new File("/Users/ekaterinalojko/Games/savegames");
    File dir3 = new File("/Users/ekaterinalojko/Games/temp");
    File dir4 = new File(dir.getAbsolutePath() + "/main");
    File dir5 = new File(dir.getAbsolutePath() + "/test");
    File dir6 = new File(dir.getAbsolutePath() + "/drawables");
    File dir7 = new File(dir.getAbsolutePath() + "/vectors");
    File dir8 = new File(dir.getAbsolutePath() + "/icons");

    createDir(dir, log);
    createDir(dir1, log);
    createDir(dir2, log);
    createDir(dir3, log);
    createDir(dir4, log);
    createDir(dir5, log);
    createDir(dir6, log);
    createDir(dir7, log);
    createDir(dir8, log);
    createFile(dir4, "Main.java", log);
    createFile(dir4, "Utils.java", log);
    createFile(dir3, "temp.txt", log);
    try (FileWriter fileWriter = new FileWriter(dir3 + "/temp.txt", true)) {
      fileWriter.write(log.toString());
      fileWriter.flush();
    } catch (IOException ex) {
      System.out.println(ex.getMessage());
    }

    GameProgress gameProgress = new GameProgress(30, 5, 3, 0.5);
    GameProgress gameProgress1 = new GameProgress(50, 3, 4, 0.5);
    GameProgress gameProgress2 = new GameProgress(20, 1, 6, 1.5);

    ArrayList<String> filePaths = new ArrayList<>();
    filePaths.add("/Users/ekaterinalojko/Games/savegames/save.dat");
    filePaths.add("/Users/ekaterinalojko/Games/savegames/save1.dat");
    filePaths.add("/Users/ekaterinalojko/Games/savegames/save2.dat");
    saveGame("/Users/ekaterinalojko/Games/savegames/save.dat", gameProgress);
    saveGame("/Users/ekaterinalojko/Games/savegames/save1.dat", gameProgress1);
    saveGame("/Users/ekaterinalojko/Games/savegames/save2.dat", gameProgress2);
    zipFiles("/Users/ekaterinalojko/Games/savegames/save.zip", filePaths);

    for (File item : Objects.requireNonNull(dir2.listFiles())) {
      if (!item.getAbsolutePath().contains(".zip")) {
        if (item.delete()) {
          log.append("Файл ").append(item.getAbsolutePath()).append(" удален\n");
        }
      }
    }

  }

  public static void createDir(File dir, StringBuilder log) {
    if (dir.mkdir()) {
      log.append("Директория ").append(dir.getAbsolutePath()).append(" создана\n");
    } else {
      log.append("Директория ").append(dir.getAbsolutePath()).append(" не создана\n");
    }
  }

  public static void createFile(File dir, String fileName, StringBuilder log) {
    if (dir.isDirectory()) {
      try {
        if (new File(dir, fileName).createNewFile()) {
          log.append("Файл ").append(fileName).append(" создан\n");
        }
      } catch (IOException ex) {
        log.append("Файл ").append(fileName).append(" не создан, потому что ")
            .append(ex.getMessage()).append("\n");
      }
    } else {
      log.append("Нельзя создать файл ").append(fileName).append(" не в директории\n");
    }
  }

  public static void saveGame(String filePath, GameProgress gameProgress) {
    try (FileOutputStream fos = new FileOutputStream(filePath);
        ObjectOutputStream oos = new ObjectOutputStream(fos)) {
      oos.writeObject(gameProgress);
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
    }
  }

  public static void zipFiles(String zipPath, ArrayList<String> filePaths) {
    try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(zipPath))) {
      ZipEntry entry = new ZipEntry("packed_notes.txt");
      for (String filePath : filePaths) {
        try (FileInputStream fis = new FileInputStream(filePath)) {
          zout.putNextEntry(entry);
          byte[] buffer = new byte[fis.available()];
          fis.read(buffer);
          zout.write(buffer);
        }
      }
      zout.closeEntry();
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
    }
  }
}
