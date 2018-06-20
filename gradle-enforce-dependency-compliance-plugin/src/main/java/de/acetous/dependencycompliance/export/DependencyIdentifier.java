package de.acetous.dependencycompliance.export;

import org.gradle.api.artifacts.ModuleVersionIdentifier;

import java.util.Objects;

public class DependencyIdentifier {

    private final String group;
    private final String name;
    private final String version;

    public DependencyIdentifier(ModuleVersionIdentifier moduleVersionIdentifier) {
        group = moduleVersionIdentifier.getGroup();
        name = moduleVersionIdentifier.getName();
        version = moduleVersionIdentifier.getVersion();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DependencyIdentifier that = (DependencyIdentifier) o;
        return Objects.equals(group, that.group) &&
                Objects.equals(name, that.name) &&
                Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(group, name, version);
    }

    @Override
    public String toString() {
        return group + ":" + name + ":" + version;
    }
}
