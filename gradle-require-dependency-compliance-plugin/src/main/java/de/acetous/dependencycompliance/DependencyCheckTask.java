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
import java.util.Collection;
import java.util.List;
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

        if (!violatingDependencies.isEmpty()) {
            getLogger().error("Dependencies are not listed in dependency compliance export.");
            printViolatingDependencies(dependencyExport.getDependencies(), violatingDependencies);
        }

        if (!violatingBuildDependencies.isEmpty()) {
            getLogger().error("Buildfile dependencies are not listed in dependency compliance export.");
            printViolatingDependencies(dependencyExport.getBuildDependencies(), violatingBuildDependencies);
        }

        violatingRepositories.forEach(repositoryIdentifier -> getLogger().error("Repository is not listed in dependency compliance export: '{}'", repositoryIdentifier));
        violatingBuildRepositories.forEach(repositoryIdentifier -> getLogger().error("Buildfile repository is not listed in dependency compliance export: '{}'", repositoryIdentifier));

        if (!violatingDependencies.isEmpty() || !violatingBuildDependencies.isEmpty() || !violatingRepositories.isEmpty() || !violatingBuildRepositories.isEmpty()) {
            throw new IllegalStateException("Build contains violating dependencies or repositories.");
        }
    }

    private void printViolatingDependencies(Collection<DependencyIdentifier> existingDependencies, Collection<DependencyIdentifier> violatingDependencies) {
        violatingDependencies.stream().map(dependencyIdentifier -> {
            StringBuilder result = new StringBuilder("    ");
            result.append(dependencyIdentifier.toString());
            List<String> existingVersions = getExistingVersions(dependencyIdentifier, existingDependencies);
            if (!existingVersions.isEmpty()) {
                result.append(" - existing versions: ");
                result.append(String.join(", ", existingVersions));
            }
            return result.toString();
        }).forEach(s -> getLogger().error(s));
    }

    List<String> getExistingVersions(DependencyIdentifier dependencyIdentifier, Collection<DependencyIdentifier> existingDependencies) {
        return existingDependencies.stream() //
                .filter(dependency -> dependencyIdentifier.getGroup().equals(dependency.getGroup()) && dependencyIdentifier.getName().equals(dependency.getName())) //
                .map(DependencyIdentifier::getVersion) //
                .collect(Collectors.toList());
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
