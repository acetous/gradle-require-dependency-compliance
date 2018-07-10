package de.acetous.dependencycompliance;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.acetous.dependencycompliance.export.DependencyExport;
import de.acetous.dependencycompliance.export.DependencyIdentifier;
import de.acetous.dependencycompliance.export.RepositoryIdentifier;
import org.gradle.api.tasks.TaskAction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Checks the project's dependencies and repositories against the exported {@code outputFile}. The task fails if any additional dependencies / repositories are found.
 * Missing dependencies or repositories which are listed in the report file are ignored.
 */
public class DependencyCheckTask extends DependencyTask {

    private Gson gson = new GsonBuilder().create();

    @TaskAction
    void checkDependencies() {
        DependencyExport dependencyExport = readJson(readExportPath());

        Set<DependencyIdentifier> violatingDependencies = resolveDependencies().stream()
                .filter(dependencyIdentifier -> !dependencyExport.getDependencies().contains(dependencyIdentifier)) //
                .collect(Collectors.toSet());
        Set<DependencyIdentifier> violatingBuildDependencies = resolveBuildDependencies().stream() //
                .filter(dependencyIdentifier -> !dependencyExport.getBuildDependencies().contains(dependencyIdentifier)) //
                .collect(Collectors.toSet());
        Set<RepositoryIdentifier> violatingRepositories = resolveRepositories().stream() //
                .filter(repositoryIdentifier -> !dependencyExport.getRepositories().contains(repositoryIdentifier)) //
                .collect(Collectors.toSet());
        Set<RepositoryIdentifier> violatingBuildRepositories = resolveBuildRepositories().stream() //
                .filter(repositoryIdentifier -> !dependencyExport.getBuildRepositories().contains(repositoryIdentifier)) //
                .collect(Collectors.toSet());

        violatingDependencies.forEach(dependencyIdentifier -> getLogger().error("Dependency is not listed in dependency compliance export: '{}'", dependencyIdentifier));
        violatingBuildDependencies.forEach(dependencyIdentifier -> getLogger().error("Buildfile dependency is not listed in dependency compliance export: '{}'", dependencyIdentifier));
        violatingRepositories.forEach(repositoryIdentifier -> getLogger().error("Repository is not listed in dependency compliance export: '{}'", repositoryIdentifier));
        violatingBuildRepositories.forEach(repositoryIdentifier -> getLogger().error("Buildfile repository is not listed in dependency compliance export: '{}'", repositoryIdentifier));

        if (!violatingDependencies.isEmpty() || !violatingBuildDependencies.isEmpty() || !violatingRepositories.isEmpty() || !violatingBuildRepositories.isEmpty()) {
            throw new RuntimeException("Build contains violating dependencies or repositories.");
        }
    }

    /**
     * Deserialize a given report.
     *
     * @param json The report as JSON-String.
     * @return The deserialized report.
     */
    private DependencyExport readJson(String json) {
        DependencyExport dependencyExport = gson.fromJson(json, DependencyExport.class);
        if (dependencyExport == null) {
            throw new IllegalStateException("Cannot read existing export.");
        }
        return dependencyExport;
    }

    /**
     * Reads the report at the given path.
     *
     * @return The report as JSON-String.
     */
    private String readExportPath() {
        Path path = outputFile.getAsFile().get().toPath();
        if (!Files.isReadable(path)) {
            throw new IllegalStateException(String.format("Dependency compliance export expectet at '%s' is not present.", path.toString()));
        }
        try {
            byte[] bytes = Files.readAllBytes(path);
            return new String(bytes, CHARSET);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read file: " + path);
        }
    }
}
