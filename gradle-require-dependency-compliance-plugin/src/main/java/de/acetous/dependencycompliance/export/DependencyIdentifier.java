package de.acetous.dependencycompliance.export;

import com.google.gson.annotations.SerializedName;
import org.gradle.api.artifacts.ModuleVersionIdentifier;

import java.util.Objects;

/**
 * Identifier for a dependency. JSON-serializeable.
 */
public class DependencyIdentifier {

    @SerializedName("group")
    private final String group;

    @SerializedName("name")
    private final String name;

    @SerializedName("version")
    private final String version;

    /**
     * Creates a {@code DependencyIdentifier} from a given {@link ModuleVersionIdentifier}.
     * @param moduleVersionIdentifier The {@code ModuleVersionIdentifier} to create this object from.
     */
    public DependencyIdentifier(ModuleVersionIdentifier moduleVersionIdentifier) {
        group = moduleVersionIdentifier.getGroup();
        name = moduleVersionIdentifier.getName();
        version = moduleVersionIdentifier.getVersion();
    }

    public String getGroup() {
        return group;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
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
