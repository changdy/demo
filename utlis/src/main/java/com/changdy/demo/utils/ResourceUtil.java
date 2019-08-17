package com.example.demo.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by Changdy on 2019/8/17.
 */
@Slf4j
public class ResourceUtil {
    private static final Class<ResourceUtil> CLASS = ResourceUtil.class;

    public static Path getPath(String fileName) throws URISyntaxException {
        URL resource = CLASS.getResource("/" + fileName);
        return Paths.get(resource.toURI());
    }

    public static String getString(String fileName) throws URISyntaxException, IOException {
        Path path = getPath(fileName);
        return new String(Files.readAllBytes(path));
    }

    public static List<String> getAllLines(String fileName) throws URISyntaxException, IOException {
        Path path = getPath(fileName);
        return Files.readAllLines(path);
    }
}