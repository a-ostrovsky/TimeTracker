package com.something.timetracker.system;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public final class Resources {
    public static Collection<String> getResourcesFlat(String folder) {
        ClassLoader classLoader = Resources.class.getClassLoader();
        String resourceUrl = Objects.requireNonNull(classLoader.getResource(folder)).getPath();
        List<String> filePaths = new ArrayList<>();
        for (File file : Objects.requireNonNull(new File(resourceUrl).listFiles())) {
            String fileName = Paths.get(folder, file.getName()).toString();
            filePaths.add(fileName);
        }
        return filePaths;
    }
}
