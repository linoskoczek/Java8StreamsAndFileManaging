package Managers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipManager {
    public static List<ZipEntry> findByName(String name, String location) {
        try {
            ZipFile file = new ZipFile(location);
            return file.stream()
                    .filter(f -> f.toString().contains(name))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("Cannot read this directory!");
            return null;
        }
    }

    public static List<ZipEntry> findByNameParallel(String name, String location) {
        try {
            ZipFile file = new ZipFile(location);
            return file.stream()
                    .parallel()
                    .filter(f -> f.toString().contains(name))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot read this directory!");
            return null;
        }
    }

    public static List<String> findByContent(String content, String location) {
        try {
            ZipFile file = new ZipFile(location);
            List<ZipEntry> fileList = file.stream()
                    .filter(f -> !f.isDirectory())
                    .collect(Collectors.toList());

            List<String> filesWithContent = new ArrayList<>();

            fileList.forEach(f -> {
                        try {
                            String fileAsString = SpecialFunctions.convertStreamToString(file.getInputStream(f));
                            if(fileAsString.contains(content)) filesWithContent.add(f.getName());
                        } catch (Exception ignored) {}
                    }
            );
            return filesWithContent;

        } catch (IOException e) {
            System.out.println("Cannot read this directory!");
            return null;
        }
    }

    public static List<String> findByContentParallel(String content, String location) {
        try {
            ZipFile file = new ZipFile(location);
            List<ZipEntry> fileList = file.stream().parallel()
                    .filter(f -> !f.isDirectory())
                    .collect(Collectors.toList());

            List<String> filesWithContent = new ArrayList<>();

            fileList.parallelStream().forEach(f -> {
                        try {
                            String fileAsString = SpecialFunctions.convertStreamToString(file.getInputStream(f));
                            if(fileAsString.contains(content)) filesWithContent.add(f.getName());
                        } catch (Exception ignored) {}
                    }
            );
            return filesWithContent;

        } catch (IOException e) {
            System.out.println("Cannot read this directory!");
            return null;
        }
    }
}
