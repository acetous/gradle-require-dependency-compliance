package de.acetous.dependencycompliance;

import org.gradle.api.DefaultTask;
import org.gradle.api.artifacts.ResolvedArtifact;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.file.FileCollection;
import org.gradle.api.tasks.TaskAction;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DependencyWriterTask extends DefaultTask {

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
                    for (String s : getDependencies()) {
                        writer.write(s);
                        writer.newLine();
                    }
                }
            } catch (IOException e) {
                throw new IllegalStateException("Cannot write to file: " + path);
            }
        });
    }

    private List<String> getDependencies() {
        Set<ResolvedArtifact> artifacts = getProject().getConfigurations().getByName("runtime").getResolvedConfiguration().getResolvedArtifacts();
        return artifacts.stream().map(Object::toString).collect(Collectors.toList());
    }
}
