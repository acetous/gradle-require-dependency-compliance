package de.acetous.dependencycompliance;

import org.gradle.api.artifacts.ModuleVersionIdentifier;
import org.gradle.api.artifacts.repositories.ArtifactRepository;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.file.FileCollection;
import org.gradle.api.tasks.TaskAction;

import java.util.function.Consumer;

public class DependencyListTask extends DependencyTask {

    @TaskAction
    void listDependencies() {

        logHeading("Runtime dependencies:");
        resolveDependencies().stream().forEach(moduleVersionIdentifier -> getLogger().lifecycle(moduleVersionIdentifier.toString()));

        logHeading("Buildscript dependencies:");
        resolveBuildDependencies().stream().forEach(moduleVersionIdentifier -> getLogger().lifecycle(moduleVersionIdentifier.toString()));


        Consumer<ArtifactRepository> artifactRepositoryConsumer = artifactRepository -> {
            if (artifactRepository instanceof MavenArtifactRepository) {
                MavenArtifactRepository mavenArtifactRepository = (MavenArtifactRepository) artifactRepository;
                getLogger().lifecycle(artifactRepository.getName() + " (" + mavenArtifactRepository.getUrl() + ")");
            } else {
                getLogger().lifecycle(artifactRepository.getName());
            }

        };

        logHeading("Build repositories:");
        resolveRepositories().stream().forEach(artifactRepositoryConsumer);

        logHeading("Buildscript repositories:");
        resolveBuildRepositories().stream().forEach(artifactRepositoryConsumer);
    }

    private void logHeading(String heading) {
        getLogger().lifecycle("-----------------------------------------");
        getLogger().lifecycle(heading);
        getLogger().lifecycle("-----------------------------------------");
    }

}
