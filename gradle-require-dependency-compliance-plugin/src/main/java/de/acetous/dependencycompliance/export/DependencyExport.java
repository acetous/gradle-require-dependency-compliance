package de.acetous.dependencycompliance.export;

import com.google.gson.annotations.SerializedName;

import java.util.Collection;

/**
 * Export of all dependencies, serializable as JSON.
 */
public class DependencyExport {

    @SerializedName("dependencies")
    private final Collection<DependencyIdentifier> dependencies;

    @SerializedName("buildDependencies")
    private final Collection<DependencyIdentifier> buildDependencies;

    @SerializedName("repositories")
    private final Collection<RepositoryIdentifier> repositories;

    @SerializedName("buildRepositories")
    private final Collection<RepositoryIdentifier> buildRepositories;

    /**
     * Constructor.
     *
     * @param dependencies      The dependencies.
     * @param buildDependencies The buildscript's dependencies.
     * @param repositories      The repositories.
     * @param buildRepositories The buildscript's repositories.
     */
    public DependencyExport(Collection<DependencyIdentifier> dependencies, Collection<DependencyIdentifier> buildDependencies, Collection<RepositoryIdentifier> repositories, Collection<RepositoryIdentifier> buildRepositories) {
        this.dependencies = dependencies;
        this.buildDependencies = buildDependencies;
        this.repositories = repositories;
        this.buildRepositories = buildRepositories;
    }

    public Collection<DependencyIdentifier> getDependencies() {
        return dependencies;
    }

    public Collection<DependencyIdentifier> getBuildDependencies() {
        return buildDependencies;
    }

    public Collection<RepositoryIdentifier> getRepositories() {
        return repositories;
    }

    public Collection<RepositoryIdentifier> getBuildRepositories() {
        return buildRepositories;
    }
}
