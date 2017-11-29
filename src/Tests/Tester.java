package Tests;

import Managers.FileManager;
import Managers.ZipManager;
import org.junit.Assert;
import org.junit.Test;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;

public class Tester {

    private final String currentDir = System.getProperty("user.dir");
    private final String exampleZip = currentDir + "/myGudFilesForTesting/hidden even more/zipFileTest.zip";
    private final String exampleJar = "/home/marcin/Downloads/idea-IU-163.7743.44/lib/junit.jar";
    private final String separator = System.getProperty("file.separator");

    @Test
    public void testFindingFilesInSelectedDirectory() {
        System.out.println("== Finding files in selected directory by name ==");
        List<Path> list = FileManager.findByName("java", currentDir);
        if(list != null)
            list.forEach(f -> System.out.println(f.getFileName()));

        List<Path> parallelList = FileManager.findByNameParallel("java", currentDir);

        String[] expectedFiles = {"JarManager.java", "ZipManager.java", "FileManager.java", "SpecialFunctions.java", "Tester.java"};
        List<String> actualFiles = Arrays.asList(expectedFiles);
        list.forEach(f -> actualFiles.contains(f.getFileName().toString()));
    }

    @Test
    public void testFindingFilesWhichContain() {
        System.out.println("== Finding files which contain ==");
        List<Path> list = FileManager.findByContent("Lorem ipsum dolor", currentDir);
        if(list != null)
            list.forEach(p -> System.out.println(p.getFileName()));

        List<Path> parallelList = FileManager.findByContentParallel("Lorem ipsum dolor", currentDir);

        String[] expectedFiles = {"someFile.txt", "Tester.java"};
        List<String> actualFiles = Arrays.asList(expectedFiles);
        list.forEach(f -> actualFiles.contains(f.getFileName().toString()));
    }

    @Test
    public void testFindingFilesInSelectedZip() {
        System.out.println("== Finding files in selected zip file by name ==");
        List<ZipEntry> list = ZipManager.findByName("File", exampleZip);
        if(list != null)
            list.forEach(f -> System.out.println(exampleZip + separator + f.getName()));

        List<ZipEntry> parallelList = ZipManager.findByNameParallel("File", exampleZip);
        String[] expectedFiles = {"someFile.txt","anotherNiceFileName","this File is inside directory", "directory for Files"};

        assert list != null;
        assert parallelList != null;
        for (ZipEntry entry : list) {
            boolean found = false;
            for(ZipEntry entryPal : parallelList) {
                if(entry.getName().equals(entryPal.getName())) {
                    found = true;
                    break;
                }
            }
            Assert.assertTrue(found);

            boolean found2 = false;
            for (String expectedFile : expectedFiles) {
                String fileName = entry.getName().split("/")[entry.getName().split("/").length - 1];

                if(fileName.equals(expectedFile)) {
                    found2 = true;
                    break;
                }
            }
            Assert.assertTrue(found2);
        }

    }

    @Test
    public void testFindingFilesInZipWhichContain() {
        System.out.println("== Finding files in zip file which contain ==");
        List<String> list = ZipManager.findByContent("Lorem", exampleZip);
        List<String> parallelList = ZipManager.findByContentParallel("Lorem", exampleZip);

        assert list != null;
        assert parallelList != null;
        for (String entry : list) {
            Assert.assertTrue(parallelList.contains(entry));
        }

        String[] expectedFiles = {"bacon salami", "someFile.txt", "directory for Files/this File is inside directory"};
        for (String expectedFile : expectedFiles) {
            Assert.assertTrue(list.contains(expectedFile));
        }
    }

    @Test
    public void testFindingFilesInJarWhichContain() {
        System.out.println("== Finding files in jar file which contain ==");
        List<String> list = ZipManager.findByContent("public", exampleJar);
        List<String> parallelList = ZipManager.findByContentParallel("public", exampleJar);

        assert list != null;
        assert parallelList != null;
        for (String entry : list) {
            Assert.assertTrue(parallelList.contains(entry));
        }

        String[] expectedFiles = {"junit/framework/TestSuite.class", "junit/framework/TestCase.class"};
        for (String expectedFile : expectedFiles) {
            Assert.assertTrue(list.contains(expectedFile));
        }
    }
}