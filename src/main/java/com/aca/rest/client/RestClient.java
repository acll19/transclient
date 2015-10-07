/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aca.rest.client;

import com.aca.translators.TranslationLimitException;
import com.aca.translators.Translator;
import com.aca.translators.TranslatorFactory;
import com.aca.translators.mymemory.MyMemoryTranslatorBuilder;
import com.aca.translators.yandex.Yandex;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 *
 * @author armando
 */
public class RestClient {

    private static Translator translator;
    private static TranslatorFactory translatorFactory;

    public static void main(String[] args) throws IOException {
        if (args.length == 0 || !"-t".equals(args[0])) {
            printHelp();
        } else {
            translatorFactory = new TranslatorFactory(args);
            String type = args[1];
            switch (type) {
                case "mymemory":
                    translator = translatorFactory.createMyMemory();
                    break;
                case "yandex":
                    translator = translatorFactory.createYandex();
                    break;
                default:
                    printHelp();
            }

            System.out.println("Enter file or directory path");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String pathString = br.readLine();
            Path path = Paths.get(pathString);
            boolean isDirectory = Files.isDirectory(path);
            if (isDirectory) {
                translateDirectory(path);
            } else {
                translateFile(path);
            }
            translator.setStartTime(getStartTime());
        }

    }

    private static void printHelp() {
        //TODO Make this more user friendly
        System.out.println(String.format("Usage example %s or %s", "-t mymemory -u YourUserName -p YourPassword -lp en|es", "-t yandex --apiKey YourApiKey -l en-es"));
    }

    private static LocalDateTime getStartTime() {
        return LocalDateTime.now(ZoneId.systemDefault());
    }

    private static void translateDirectory(Path path) {
        try (Stream<Path> pathStreams = Files.walk(path, FileVisitOption.FOLLOW_LINKS)) {
            pathStreams
                    .filter(p -> !p.toFile().isDirectory())
                    .filter(p -> !p.toFile().isHidden())
                    .filter(p -> p.toFile().canRead())
                    .forEach(p -> translateFile(p));
        } catch (IOException | SecurityException ex) {
            java.util.logging.Logger.getLogger(RestClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void translateFile(Path path) {
        Path translatedFilePath = getTranslatedFileName(path);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(translatedFilePath.toFile(), true)))) {
            try (Stream<String> linesStream = Files.lines(path, Charset.defaultCharset())) {
                linesStream.map(String::trim)
                        .filter(l -> !l.isEmpty())
                        .map(l -> wrappedTranslate(l))
                        .forEach(tl -> {
                            if (!translator.isIsLimitReached()) {
                                out.println(tl);
                                System.out.println(tl);
                            }
                        });
            } catch (IOException e) {
                java.util.logging.Logger.getLogger(RestClient.class.getName()).log(Level.SEVERE, null, e);
            }
        } catch (IOException e) {
            java.util.logging.Logger.getLogger(RestClient.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    private static Path getTranslatedFileName(Path path) {
        String translatedFileName = path
                .getFileName() + "_translated.txt";
        Path createdFilePath = path.getParent()
                .resolve(translatedFileName);
        return createdFilePath;
    }

    private static String wrappedTranslate(String l) {
        try {
            return translator.translate(l);
        } catch (TranslationLimitException | ClassCastException ex) {
            System.out.println(ex.getMessage());
            return "";
        }
    }
}
