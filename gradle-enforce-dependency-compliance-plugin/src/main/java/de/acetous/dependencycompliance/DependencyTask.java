package de.acetous.dependencycompliance;

import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ModuleVersionIdentifier;
import org.gradle.api.artifacts.ResolvedArtifact;
import org.gradle.api.artifacts.repositories.ArtifactRepository;
import org.gradle.api.initialization.dsl.ScriptHandler;

import java.util.Set;
import java.util.function.Consumer;

public abstract class DependencyTask extends DefaultTask {

    void resolveDependencies(Consumer<? super ModuleVersionIdentifier> dependencyConsumer) {
        for (Project project : getProject().getAllprojects()) {
            String configurationName = "runtime";
            Configuration configuration = project.getConfigurations().findByName(configurationName);
            if (configuration == null) {
                project.getLogger().warn(String.format("Configuration '%s' not found for project '%s'.", configurationName, project.getPath()));
                continue;
            }
            Set<ResolvedArtifact> artifacts = configuration.getResolvedConfiguration().getResolvedArtifacts();
            artifacts.stream().map(resolvedArtifact -> resolvedArtifact.getModuleVersion().getId()).forEach(dependencyConsumer);
        }
    }

    void resolveBuildDependencies(Consumer<? super ModuleVersionIdentifier> buildDependencyConsumer) {
        for (Project project : getProject().getAllprojects()) {
            Set<ResolvedArtifact> artifacts = project.getBuildscript().getConfigurations().getByName(ScriptHandler.CLASSPATH_CONFIGURATION).getResolvedConfiguration().getResolvedArtifacts();
            artifacts.stream().map(resolvedArtifact -> resolvedArtifact.getModuleVersion().getId()).forEach(buildDependencyConsumer);
        }
    }

    void resolveRepositories(Consumer<? super ArtifactRepository> repositoryConsumer) {
        for (Project project : getProject().getAllprojects()) {
            project.getRepositories().stream().forEach(repositoryConsumer);
        }
    }

    void resolveBuildRepositories(Consumer<? super ArtifactRepository> repositoryConsumer) {
        for (Project project : getProject().getAllprojects()) {
            project.getBuildscript().getRepositories().stream().forEach(repositoryConsumer);
        }
    }
}
