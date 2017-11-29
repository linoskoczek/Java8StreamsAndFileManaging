package Managers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public class JarManager {
    public static List<JarEntry> findByName(String name, String location) {
        try {
            JarFile file = new JarFile(location);
            return file.stream()
                    .filter(f -> f.toString().contains(name))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("Cannot read this directory!");
            return null;
        }
    }

    public static List<JarEntry> findByNameParallel(String name, String location) {
        try {
            JarFile file = new JarFile(location);
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
            JarFile file = new JarFile(location);
            List<JarEntry> fileList = file.stream()
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
            JarFile file = new JarFile(location);
            List<JarEntry> fileList = file.stream().parallel()
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
