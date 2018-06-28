package de.acetous.dependencycompliance.export;

import com.google.gson.annotations.SerializedName;

import java.util.Set;

/**
 * Export of all dependencies, serializable as JSON.
 */
public class DependencyExport {

    @SerializedName("dependencies")
    private final Set<DependencyIdentifier> dependencies;

    @SerializedName("buildDependencies")
    private final Set<DependencyIdentifier> buildDependencies;

    @SerializedName("repositories")
    private final Set<RepositoryIdentifier> repositories;

    @SerializedName("buildRepositories")
    private final Set<RepositoryIdentifier> buildRepositories;

    /**
     * Constructor.
     * @param dependencies The dependencies.
     * @param buildDependencies The buildscript's dependencies.
     * @param repositories The repositories.
     * @param buildRepositories The buildscript's repositories.
     */
    public DependencyExport(Set<DependencyIdentifier> dependencies, Set<DependencyIdentifier> buildDependencies, Set<RepositoryIdentifier> repositories, Set<RepositoryIdentifier> buildRepositories) {
        this.dependencies = dependencies;
        this.buildDependencies = buildDependencies;
        this.repositories = repositories;
        this.buildRepositories = buildRepositories;
    }

    public Set<DependencyIdentifier> getDependencies() {
        return dependencies;
    }

    public Set<DependencyIdentifier> getBuildDependencies() {
        return buildDependencies;
    }

    public Set<RepositoryIdentifier> getRepositories() {
        return repositories;
    }

    public Set<RepositoryIdentifier> getBuildRepositories() {
        return buildRepositories;
    }
}
