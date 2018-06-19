package de.acetous.dependencycompliance;

import org.gradle.api.artifacts.ModuleVersionIdentifier;
import org.gradle.api.artifacts.repositories.ArtifactRepository;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.file.FileCollection;
import org.gradle.api.tasks.TaskAction;

import java.util.function.Consumer;

public class DependencyListTask extends DependencyTask {

    private final ConfigurableFileCollection outputFiles = getProject().getLayout().configurableFiles();

    public void setOutputFiles(FileCollection outputFiles) {
        this.outputFiles.setFrom(outputFiles);
    }

    @TaskAction
    void writeDependencies() {
        Consumer<ModuleVersionIdentifier> dependencyConsumer = moduleVersionIdentifier -> {
            getLogger().lifecycle(moduleVersionIdentifier.toString());
        };

        getLogger().lifecycle("-----------------------------------------");
        getLogger().lifecycle("Runtime dependencies:");
        getLogger().lifecycle("-----------------------------------------");
        resolveDependencies(dependencyConsumer);
        getLogger().lifecycle("-----------------------------------------\n");

        getLogger().lifecycle("-----------------------------------------");
        getLogger().lifecycle("Buildscript dependencies:");
        getLogger().lifecycle("-----------------------------------------");
        resolveBuildDependencies(dependencyConsumer);
        getLogger().lifecycle("-----------------------------------------\n");

        Consumer<ArtifactRepository> artifactRepositoryConsumer = artifactRepository -> {
            if (artifactRepository instanceof MavenArtifactRepository) {
                MavenArtifactRepository mavenArtifactRepository = (MavenArtifactRepository) artifactRepository;
                getLogger().lifecycle(artifactRepository.getName() + " (" + mavenArtifactRepository.getUrl() + ")");
            } else {
                getLogger().lifecycle(artifactRepository.getName());
            }

        };

        getLogger().lifecycle("-----------------------------------------");
        getLogger().lifecycle("Build repositories:");
        getLogger().lifecycle("-----------------------------------------");
        resolveRepositories(artifactRepositoryConsumer);
        getLogger().lifecycle("-----------------------------------------\n");

        getLogger().lifecycle("-----------------------------------------");
        getLogger().lifecycle("Buildscript repositories:");
        getLogger().lifecycle("-----------------------------------------");
        resolveBuildRepositories(artifactRepositoryConsumer);
        getLogger().lifecycle("-----------------------------------------\n");
    }

}
