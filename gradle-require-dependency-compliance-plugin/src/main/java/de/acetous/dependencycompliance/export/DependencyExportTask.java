package de.acetous.dependencycompliance.export;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.acetous.dependencycompliance.DependencyTask;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.tasks.TaskAction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Task to export all dependencies and repositories to the {@code outputFile}.
 */
public class DependencyExportTask extends DependencyTask {

    private final RegularFileProperty outputFile = getProject().getLayout().fileProperty();

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public void setOutputFile(RegularFileProperty outputFile) {
        this.outputFile.set(outputFile);
    }

    @TaskAction
    void writeDependencies() {

        Set<DependencyIdentifier> dependencies = resolveDependencies().stream().map(DependencyIdentifier::new).collect(Collectors.toSet());
        Set<DependencyIdentifier> buildDependencies = resolveBuildDependencies().stream().map(DependencyIdentifier::new).collect(Collectors.toSet());
        Set<RepositoryIdentifier> repositories = resolveRepositories().stream().map(RepositoryIdentifier::new).collect(Collectors.toSet());
        Set<RepositoryIdentifier> buildRepositories = resolveBuildRepositories().stream().map(RepositoryIdentifier::new).collect(Collectors.toSet());

        DependencyExport dependencyExport = new DependencyExport(dependencies, buildDependencies, repositories, buildRepositories);

        String exportJson = gson.toJson(dependencyExport);

            Path path = outputFile.getAsFile().get().toPath();
            createFile(path);
            try {
                Files.write(path, exportJson.getBytes(CHARSET), StandardOpenOption.TRUNCATE_EXISTING);
            } catch (IOException e) {
                throw new IllegalStateException("Cannot write file: " + path);
            }
    }

    /**
     * Create a file if not present. Created intermediate directories.
     * @param path The path to the file.
     */
    private void createFile(Path path) {
        Path parent = path.getParent();
        try {
            Files.createDirectories(parent);
            if (!Files.isRegularFile(path)) {
                Files.createFile(path);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Cannot create file: " + path);
        }
    }
}
