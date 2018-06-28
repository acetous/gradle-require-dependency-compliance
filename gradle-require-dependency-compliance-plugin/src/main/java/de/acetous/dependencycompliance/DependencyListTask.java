package de.acetous.dependencycompliance;

import org.gradle.api.artifacts.repositories.ArtifactRepository;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;
import org.gradle.api.tasks.TaskAction;

import java.util.function.Consumer;

/**
 * Lists all dependencies and repositories.
 */
public class DependencyListTask extends DependencyTask {

    @TaskAction
    void listDependencies() {

        logHeading("Build dependencies:");
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

    /**
     * Write the heading to the log with additional formatting.
     * @param heading The heading to log.
     */
    private void logHeading(String heading) {
        getLogger().lifecycle("-----------------------------------------");
        getLogger().lifecycle(heading);
        getLogger().lifecycle("-----------------------------------------");
    }

}
