package de.acetous.dependencycompliance.check;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.acetous.dependencycompliance.DependencyTask;
import de.acetous.dependencycompliance.export.DependencyExport;
import de.acetous.dependencycompliance.export.DependencyIdentifier;
import de.acetous.dependencycompliance.export.RepositoryIdentifier;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.tasks.TaskAction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;

public class DependencyCheckTask extends DependencyTask {

    private final RegularFileProperty outputFile = getProject().getLayout().fileProperty();

    private Gson gson = new GsonBuilder().create();

    public void setOutputFile(RegularFileProperty outputFile) {
        this.outputFile.set(outputFile);
    }

    @TaskAction
    void checkDependencies() {
        String json = readExportPath();
        DependencyExport dependencyExport = readJson(json);

        Set<DependencyIdentifier> violatingDependencies = resolveDependencies().stream().map(DependencyIdentifier::new).filter(dependencyIdentifier -> !dependencyExport.getDependencies().contains(dependencyIdentifier)).collect(Collectors.toSet());
        Set<DependencyIdentifier> violatingBuildDependencies = resolveBuildDependencies().stream().map(DependencyIdentifier::new).filter(dependencyIdentifier -> !dependencyExport.getBuildDependencies().contains(dependencyIdentifier)).collect(Collectors.toSet());
        Set<RepositoryIdentifier> violatingRepositories = resolveRepositories().stream().map(RepositoryIdentifier::new).filter(repositoryIdentifier -> !dependencyExport.getRepositories().contains(repositoryIdentifier)).collect(Collectors.toSet());
        Set<RepositoryIdentifier> violatingBuildRepositories = resolveBuildRepositories().stream().map(RepositoryIdentifier::new).filter(repositoryIdentifier -> !dependencyExport.getBuildRepositories().contains(repositoryIdentifier)).collect(Collectors.toSet());

        violatingDependencies.forEach(dependencyIdentifier -> getLogger().error("Dependency '{}' is not listed in dependecy compliance export.", dependencyIdentifier));
        violatingBuildDependencies.forEach(dependencyIdentifier -> getLogger().error("Buildfile dependency '{}' is not listed in dependecy compliance export.", dependencyIdentifier));
        violatingRepositories.forEach(repositoryIdentifier -> getLogger().error("Repository '{}' is not listed in dependecy compliance export.", repositoryIdentifier));
        violatingBuildRepositories.forEach(repositoryIdentifier -> getLogger().error("Buildfile repository '{}' is not listed in dependecy compliance export.", repositoryIdentifier));

        if (!violatingDependencies.isEmpty() || !violatingBuildDependencies.isEmpty() || !violatingRepositories.isEmpty() || !violatingBuildRepositories.isEmpty()) {
            throw new IllegalStateException("Build contains violating dependencies or repositories.");
        }
    }

    private DependencyExport readJson(String json) {
        DependencyExport dependencyExport = gson.fromJson(json, DependencyExport.class);
        if (dependencyExport == null) {
            throw new IllegalStateException("Cannot read existing export.");
        }
        return dependencyExport;
    }

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
