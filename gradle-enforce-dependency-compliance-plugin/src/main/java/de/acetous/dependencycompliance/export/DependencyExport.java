package de.acetous.dependencycompliance.export;

import java.util.Set;

public class DependencyExport {

    private final Set<DependencyIdentifier> dependencies;
    private final Set<DependencyIdentifier> buildDependencies;
    private final Set<RepositoryIdentifier> repositories;
    private final Set<RepositoryIdentifier> buildRepositories;

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
