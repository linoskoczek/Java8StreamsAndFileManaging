package Managers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileManager {
    public static List<Path> findByName(String name, String directory) {
        Path dir = Paths.get(directory);

        try {
            return Files.walk(dir)
                    .filter(f -> (Files.isRegularFile(f) || Files.isDirectory(f)))
                    .map(Path::getFileName)
                    .filter(f -> f.toString().contains(name))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("Cannot read this directory!");
            return null;
        }
    }

    public static List<Path> findByNameParallel(String name, String directory) {
        Path dir = Paths.get(directory);

        try {
            return Files.walk(dir)
                    .parallel()
                    .filter(f -> (Files.isRegularFile(f) || Files.isDirectory(f)))
                    .map(Path::getFileName)
                    .filter(f -> f.toString().contains(name))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("Cannot read this directory!");
            return null;
        }
    }

    public static List<Path> findByContent(String content, String directory) {
        Path dir = Paths.get(directory);

        try {
            List<Path> fileList = Files.walk(dir)
                    .filter(Files::isRegularFile)
                    .collect(Collectors.toList());

            List<Path> filesWithContent = new ArrayList<>();

            fileList.forEach(f -> {
                        try {
                            Files.lines(f)
                                    .filter(l -> l.contains(content))
                                    .findFirst()
                                    .ifPresent(p -> filesWithContent.add(f));
                        } catch (Exception ignored) {}
                    }
            );

            return filesWithContent;

        } catch (IOException e) {
            System.out.println("Cannot read this directory!");
            return null;
        }
    }

    public static List<Path> findByContentParallel(String content, String directory) {
        Path dir = Paths.get(directory);

        try {
            List<Path> fileList = Files.walk(dir)
                    .parallel()
                    .filter(f -> Files.isRegularFile(f))
                    .collect(Collectors.toList());

            List<Path> filesWithContent = new ArrayList<>();

            fileList.parallelStream().forEach(f -> {
                        try {
                            Files.lines(f)
                                    .parallel()
                                    .filter(l -> l.contains(content))
                                    .findFirst()
                                    .ifPresent(p -> filesWithContent.add(f));
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
