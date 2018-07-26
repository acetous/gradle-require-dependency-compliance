package de.acetous.dependencycompliance;

import de.acetous.dependencycompliance.export.DependencyIdentifier;

import java.util.Comparator;

/**
 * Compares {@link DependencyIdentifier} by group, then name. Does not compare by version.
 */
public class DependencyIdentifierComparator implements Comparator<DependencyIdentifier> {
    @Override
    public int compare(DependencyIdentifier o1, DependencyIdentifier o2) {
        int comparedGroups = o1.getGroup().compareTo(o2.getGroup());
        if (comparedGroups == 0) {
            return o1.getName().compareTo(o2.getName());
        }
        return comparedGroups;
    }
}
