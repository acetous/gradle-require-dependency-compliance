package de.acetous.dependencycompliance;

import de.acetous.dependencycompliance.export.DependencyFilterService;
import de.acetous.dependencycompliance.export.DependencyIdentifier;
import de.acetous.dependencycompliance.export.RepositoryIdentifier;
import org.gradle.api.DefaultTask;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.initialization.dsl.ScriptHandler;
import org.gradle.api.internal.artifacts.DefaultProjectComponentIdentifier;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.OutputFile;

import java.nio.charset.Charset;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Parent-Class for all plugin tasks.
 */
public abstract class DependencyTask extends DefaultTask {

    protected static final Charset CHARSET = Charset.forName("UTF-8");

    @OutputFile
    final RegularFileProperty outputFile = getProject().getLayout().fileProperty();

    @Input
    private ListProperty<String> ignore = getProject().getObjects().listProperty(String.class);

    @Input
    private Property<Boolean> ignoreMavenLocal = getProject().getObjects().property(Boolean.class);

    private final DependencyFilterService dependencyFilterService = new DependencyFilterService();

    /**
     * Set if the MavenLocal repository should be ignored.
     *
     * @param ignoreMavenLocal The provider for this property.
     */
    public void setIgnoreMavenLocal(Property<Boolean> ignoreMavenLocal) {
        this.ignoreMavenLocal.set(ignoreMavenLocal);
    }

    /**
     * Set the {@code outputFile}.
     *
     * @param outputFile The new file.
     */
    public void setOutputFile(RegularFileProperty outputFile) {
        this.outputFile.set(outputFile);
    }

    /**
     * Set filtered dependencies.
     *
     * @param ignore A list of dependencies.
     */
    public void setIgnore(ListProperty<String> ignore) {
        this.ignore.addAll(ignore);
    }

    protected Set<DependencyIdentifier> getDependencyFilter() {
        return dependencyFilterService.getDependencyFilter(ignore.get());
    }

    private boolean filterIgnoredDependencies(DependencyIdentifier dependencyIdentifier) {
        return !dependencyFilterService.isIgnored(dependencyIdentifier, getDependencyFilter());
    }

    /**
     * Resolve all dependencies of all configurations of this project and it's subprojects.
     *
     * @return All resolved dependencies.
     */
    protected Set<DependencyIdentifier> resolveDependencies() {
        return getProject().getAllprojects().stream() // all projects
                .flatMap(project -> project.getConfigurations().stream()) // get all configurations
                .filter(Configuration::isCanBeResolved) // only if the configuration can be resolved
                .flatMap(configuration -> configuration.getResolvedConfiguration().getResolvedArtifacts().stream()) // get all artifacts
                .filter(resolvedArtifact -> !(resolvedArtifact.getId().getComponentIdentifier() instanceof DefaultProjectComponentIdentifier))
                .map(resolvedArtifact -> resolvedArtifact.getModuleVersion().getId()) // map to ModuleVersionIdentifier
                .map(DependencyIdentifier::new) //
                .distinct() //
                .filter(this::filterIgnoredDependencies) //
                .collect(Collectors.toSet()); // return as Set
    }

    /**
     * Resolve all buildscript dependencies of this project and it's subprojects.
     *
     * @return All resolved buildscript dependencies.
     */
    protected Set<DependencyIdentifier> resolveBuildDependencies() {
        return getProject().getAllprojects().stream() //
                .map(project -> project.getBuildscript().getConfigurations().getByName(ScriptHandler.CLASSPATH_CONFIGURATION).getResolvedConfiguration()) //
                .flatMap(confguration -> confguration.getResolvedArtifacts().stream()) //
                .map(resolvedArtifact -> resolvedArtifact.getModuleVersion().getId()) //
                .map(DependencyIdentifier::new) //
                .distinct() //
                .filter(this::filterIgnoredDependencies) //
                .collect(Collectors.toSet());
    }

    /**
     * Get all repositories of this project and it's subprojects.
     *
     * @return The repositories.
     */
    protected Set<RepositoryIdentifier> resolveRepositories() {
        return getProject().getAllprojects().stream() //
                .flatMap(project -> project.getRepositories().stream()) //
                .map(RepositoryIdentifier::new) //
                .filter(this::filterMavenLocal) //
                .collect(Collectors.toSet());
    }

    /**
     * Get all buildscript's repositories of this project and it's subprojects.
     *
     * @return The buildscript's repositories.
     */
    protected Set<RepositoryIdentifier> resolveBuildRepositories() {
        return getProject().getAllprojects().stream() //
                .flatMap(project -> project.getBuildscript().getRepositories().stream()) //
                .map(RepositoryIdentifier::new) //
                .filter(this::filterMavenLocal) //
                .collect(Collectors.toSet());
    }

    private boolean filterMavenLocal(RepositoryIdentifier repositoryIdentifier) {
        return !ignoreMavenLocal.get() || !"MavenLocal".equals(repositoryIdentifier.getName());
    }

    public void logDependencyFilter(Set<DependencyIdentifier> dependencyIdentifierList) {
        getLogger().lifecycle("(DependencyCompliance) Ignoring these dependencies:");
        dependencyIdentifierList.forEach(dependencyIdentifier -> getLogger().lifecycle(dependencyIdentifier.toString()));
    }
}
