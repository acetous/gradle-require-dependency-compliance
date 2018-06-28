package de.acetous.dependencycompliance;

import org.gradle.api.DefaultTask;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ModuleVersionIdentifier;
import org.gradle.api.artifacts.repositories.ArtifactRepository;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.initialization.dsl.ScriptHandler;

import java.nio.charset.Charset;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Parent-Class for all plugin tasks.
 */
public abstract class DependencyTask extends DefaultTask {

    protected static final Charset CHARSET = Charset.forName("UTF-8");

    final RegularFileProperty outputFile = getProject().getLayout().fileProperty();

    /**
     * Set the {@code outputFile}.
     *
     * @param outputFile The new file.
     */
    public void setOutputFile(RegularFileProperty outputFile) {
        this.outputFile.set(outputFile);
    }

    /**
     * Resolve all dependencies of all configurations of this project and it's subprojects.
     *
     * @return All resolved dependencies.
     */
    protected Set<ModuleVersionIdentifier> resolveDependencies() {
        return getProject().getAllprojects().stream() // all projects
                .flatMap(project -> project.getConfigurations().stream()) // get all configurations
                .filter(Configuration::isCanBeResolved) // only if the configuration can be resolved
                .flatMap(configuration -> configuration.getResolvedConfiguration().getResolvedArtifacts().stream()) // get all artifacts
                .map(resolvedArtifact -> resolvedArtifact.getModuleVersion().getId()) // map to ModuleVersionIdentifier
                .collect(Collectors.toSet()); // return as Set
    }

    /**
     * Resolve all buildscript dependencies of this project and it's subprojects.
     *
     * @return All resolved buildscript dependencies.
     */
    protected Set<ModuleVersionIdentifier> resolveBuildDependencies() {
        return getProject().getAllprojects().stream() //
                .map(project -> project.getBuildscript().getConfigurations().getByName(ScriptHandler.CLASSPATH_CONFIGURATION).getResolvedConfiguration()) //
                .flatMap(confguration -> confguration.getResolvedArtifacts().stream()) //
                .map(resolvedArtifact -> resolvedArtifact.getModuleVersion().getId()) //
                .collect(Collectors.toSet());
    }

    /**
     * Get all repositories of this project and it's subprojects.
     *
     * @return The repositories.
     */
    protected Set<ArtifactRepository> resolveRepositories() {
        return getProject().getAllprojects().stream() //
                .flatMap(project -> project.getRepositories().stream()) //
                .collect(Collectors.toSet());
    }

    /**
     * Get all buildscript's repositories of this project and it's subprojects.
     *
     * @return The buildscript's repositories.
     */
    protected Set<ArtifactRepository> resolveBuildRepositories() {
        return getProject().getAllprojects().stream() //
                .flatMap(project -> project.getBuildscript().getRepositories().stream()) //
                .collect(Collectors.toSet());
    }
}
