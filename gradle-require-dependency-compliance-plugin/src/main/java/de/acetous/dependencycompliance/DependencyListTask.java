package de.acetous.dependencycompliance;

import de.acetous.dependencycompliance.export.RepositoryIdentifier;
import org.gradle.api.tasks.TaskAction;

import java.util.function.Consumer;

/**
 * Lists all dependencies and repositories.
 */
public class DependencyListTask extends DependencyTask {

    @TaskAction
    void listDependencies() {

        logHeading("Build dependencies:");
        resolveDependencies().stream().forEach(dependencyIdentifier -> getLogger().lifecycle(dependencyIdentifier.toString()));

        logHeading("Buildscript dependencies:");
        resolveBuildDependencies().stream().forEach(dependencyIdentifier -> getLogger().lifecycle(dependencyIdentifier.toString()));


        logHeading("Build repositories:");
        resolveRepositories().forEach(repository -> getLogger().lifecycle(repository.toString()));

        logHeading("Buildscript repositories:");
        resolveBuildRepositories().forEach(repository -> getLogger().lifecycle(repository.toString()));
    }

    /**
     * Write the heading to the log with additional formatting.
     *
     * @param heading The heading to log.
     */
    private void logHeading(String heading) {
        getLogger().lifecycle("-----------------------------------------");
        getLogger().lifecycle(heading);
        getLogger().lifecycle("-----------------------------------------");
    }

}
