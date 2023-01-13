package de.acetous.dependencycompliance.export;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A service to handle filters for dependencies.
 */
public class DependencyFilterService {

    /**
     * Transforms a list of formatted strings (e.g: "foo:bar:123") to a list of {@link DependencyIdentifier}. Omitted
     * elements will be replaced by an asterix ("*"), e.g. "foo:bar" will be "foo:bar:*".
     *
     * @param dependenciesList The list of formatted strings.
     * @return The list of {@link DependencyIdentifier}.
     * @throws IllegalStateException When a dependency string is not properly formatted.
     */
    public Set<DependencyIdentifier> getDependencyFilter(List<String> dependenciesList) {
        return dependenciesList.stream() //
                .map(s -> s.split(":")) //
                .map(Arrays::asList) //
                .map(DependencyFilterService::validateDependencyFormat) //
                .map(strings -> DependencyIdentifier.create(strings.get(0), strings.get(1), strings.get(2))) //
                .collect(Collectors.toSet());
    }

    static List<String> validateDependencyFormat(List<String> strings) {
        if (strings.size() != 3) {
            throw new IllegalStateException("Filtered dependency not valid: " + String.join(":", strings));
        }
        return new ArrayList<>(strings);
    }

    public boolean isIgnored(DependencyIdentifier dependencyIdentifier, Set<DependencyIdentifier> dependencyFilter) {
        return dependencyFilter.stream() //
                .anyMatch(ignore -> ignoreMatchesComplete(dependencyIdentifier, ignore) || ignoreWithArtifactWildcardMatchesGroup(dependencyIdentifier, ignore) || ignoreWithVersionWildcardMatchesArtifact(dependencyIdentifier, ignore) || ignoreWithPartialGroup(dependencyIdentifier, ignore));
    }

    private boolean ignoreWithVersionWildcardMatchesArtifact(DependencyIdentifier dependencyIdentifier, DependencyIdentifier ignore) {
        return ignore.getVersion().equals("*") && ignore.getGroup().equals(dependencyIdentifier.getGroup()) && ignore.getName().equals(dependencyIdentifier.getName());
    }

    private boolean ignoreWithArtifactWildcardMatchesGroup(DependencyIdentifier dependencyIdentifier, DependencyIdentifier ignore) {
        return ignore.getVersion().equals("*") && ignore.getName().equals("*") && ignore.getGroup().equals(dependencyIdentifier.getGroup());
    }

    private boolean ignoreMatchesComplete(DependencyIdentifier dependencyIdentifier, DependencyIdentifier ignore) {
        return dependencyIdentifier.equals(ignore);
    }

    private boolean ignoreWithPartialGroup(DependencyIdentifier dependencyIdentifier, DependencyIdentifier ignore) {
        return ignore.getVersion().equals("*") && ignore.getName().equals("*") && ignore.getGroup().endsWith("*") && dependencyIdentifier.getGroup().startsWith(ignore.getGroup().substring(0, ignore.getGroup().length()-1));
    }
}
