package de.acetous.dependencycompliance;

import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.file.FileCollection;
import org.gradle.api.tasks.TaskAction;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class DependencyWriterTask extends DependencyTask {

    private final ConfigurableFileCollection outputFiles = getProject().getLayout().configurableFiles();

    public void setOutputFiles(FileCollection outputFiles) {
        this.outputFiles.setFrom(outputFiles);
    }

    @TaskAction
    void writeDependencies() {
        outputFiles.forEach(file -> {
            Path path = file.toPath();
            Path parent = path.getParent();
            try {
                Files.createDirectories(parent);
                try (BufferedWriter writer = Files.newBufferedWriter(path, Charset.forName("UTF-8"), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                    resolveDependencies(moduleVersionIdentifier -> {
                        try {
                            writer.write(moduleVersionIdentifier.toString());
                            writer.newLine();
                        } catch (IOException e) {
                            throw new IllegalStateException("Cannot write to file: " + path);
                        }
                    });
                }
            } catch (IOException e) {
                throw new IllegalStateException("Cannot write to file: " + path);
            }
        });
    }
}
