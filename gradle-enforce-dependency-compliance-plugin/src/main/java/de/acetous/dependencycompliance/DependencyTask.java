package de.acetous.dependencycompliance;

import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ModuleVersionIdentifier;
import org.gradle.api.artifacts.ResolvedArtifact;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.artifacts.repositories.ArtifactRepository;
import org.gradle.api.initialization.dsl.ScriptHandler;

import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class DependencyTask extends DefaultTask {

    public static final Charset CHARSET = Charset.forName("UTF-8");

    protected List<ModuleVersionIdentifier> resolveDependencies() {
        Set<ResolvedArtifact> allArtifacts = new HashSet<>();
        for (Project project : getProject().getAllprojects()) {
            String configurationName = "runtime";
            Configuration configuration = project.getConfigurations().findByName(configurationName);
            if (configuration == null) {
                project.getLogger().info(String.format("Configuration '%s' not found for project '%s'.", configurationName, project.getPath()));
                continue;
            }
            Set<ResolvedArtifact> artifacts = configuration.getResolvedConfiguration().getResolvedArtifacts();
            allArtifacts.addAll(artifacts);
        }
        return allArtifacts.stream().map(resolvedArtifact -> resolvedArtifact.getModuleVersion().getId()).collect(Collectors.toList());
    }

    protected List<ModuleVersionIdentifier> resolveBuildDependencies() {
        Set<ResolvedArtifact> allArtifacts = new HashSet<>();
        for (Project project : getProject().getAllprojects()) {
            Set<ResolvedArtifact> artifacts = project.getBuildscript().getConfigurations().getByName(ScriptHandler.CLASSPATH_CONFIGURATION).getResolvedConfiguration().getResolvedArtifacts();
            allArtifacts.addAll(artifacts);
        }
        return allArtifacts.stream().map(resolvedArtifact -> resolvedArtifact.getModuleVersion().getId()).collect(Collectors.toList());
    }

    protected Set<ArtifactRepository> resolveRepositories() {
        Set<ArtifactRepository> allRepositories = new HashSet<>();
        for (Project project : getProject().getAllprojects()) {
            RepositoryHandler repositories = project.getRepositories();
            allRepositories.addAll(repositories);
        }
        return allRepositories;
    }

    protected Set<ArtifactRepository> resolveBuildRepositories() {
        Set<ArtifactRepository> allRepositories = new HashSet<>();
        for (Project project : getProject().getAllprojects()) {
            RepositoryHandler repositories = project.getBuildscript().getRepositories();
            allRepositories.addAll(repositories);
        }
        return allRepositories;
    }
}
