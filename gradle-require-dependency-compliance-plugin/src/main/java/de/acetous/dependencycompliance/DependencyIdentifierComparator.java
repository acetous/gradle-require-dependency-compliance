package de.acetous.dependencycompliance;

import de.acetous.dependencycompliance.export.DependencyIdentifier;
import org.gradle.api.internal.artifacts.ivyservice.ivyresolve.VersionInfo;
import org.gradle.api.internal.artifacts.ivyservice.ivyresolve.Versioned;
import org.gradle.api.internal.artifacts.ivyservice.ivyresolve.strategy.DefaultVersionComparator;
import org.gradle.api.internal.artifacts.ivyservice.ivyresolve.strategy.VersionParser;

import java.util.Comparator;

/**
 * Compares {@link DependencyIdentifier} by group, then name. Does not compare by version.
 */
public class DependencyIdentifierComparator implements Comparator<DependencyIdentifier> {

    private final DefaultVersionComparator defaultVersionComparator = new DefaultVersionComparator();
    private final VersionParser parser = new VersionParser();

    @Override
    public int compare(DependencyIdentifier o1, DependencyIdentifier o2) {
        int comparedGroups = o1.getGroup().compareTo(o2.getGroup());
        if (comparedGroups != 0) {
            return comparedGroups;
        }
        int compareNames = o1.getName().compareTo(o2.getName());
        if (compareNames != 0) {
            return compareNames;
        }

        Versioned v1 = new VersionInfo(parser.transform(o1.getVersion()));
        Versioned v2 = new VersionInfo(parser.transform(o2.getVersion()));
        return defaultVersionComparator.compare(v1, v2);
    }
}
