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
                .map(DependencyFilterService::formatDependencyStrings) //
                .map(strings -> DependencyIdentifier.create(strings.get(0), strings.get(1), strings.get(2))) //
                .collect(Collectors.toSet());
    }

    static List<String> formatDependencyStrings(List<String> strings) {
        List<String> result = new ArrayList<>(strings);
        if (result.size() > 3) {
            throw new IllegalStateException("Filtered dependency not valid: " + String.join(":", strings));
        }
        while (result.size() < 3) {
            result.add("*");
        }
        return result;
    }

    public boolean isIgnored(DependencyIdentifier dependencyIdentifier, Set<DependencyIdentifier> dependencyFilter) {
        return dependencyFilter.stream() //
                .anyMatch(ignore -> {
                    if (dependencyIdentifier.equals(ignore)) {
                        return true;
                    }
                    if (ignore.getVersion().equals("*") && ignore.getName().equals("*") && ignore.getGroup().equals(dependencyIdentifier.getGroup())) {
                        return true;
                    }
                    if (ignore.getVersion().equals("*") && ignore.getGroup().equals(dependencyIdentifier.getGroup()) && ignore.getName().equals(dependencyIdentifier.getName())) {
                        return true;
                    }
                    return false;
                });
    }
}
