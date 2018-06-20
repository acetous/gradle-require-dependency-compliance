package de.acetous.dependencycompliance.export;

import org.gradle.api.artifacts.repositories.ArtifactRepository;
import org.gradle.api.artifacts.repositories.IvyArtifactRepository;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;
import org.gradle.api.internal.artifacts.repositories.ArtifactRepositoryInternal;

import java.util.Objects;

public class RepositoryIdentifier {

    private final String name;
    private final String url;

    public RepositoryIdentifier(ArtifactRepository artifactRepository) {
        name = artifactRepository.getName();
        if (artifactRepository instanceof MavenArtifactRepository) {
            url = ((MavenArtifactRepository) artifactRepository).getUrl().normalize().toString();
        } else if (artifactRepository instanceof IvyArtifactRepository) {
            url = ((IvyArtifactRepository) artifactRepository).getUrl().normalize().toString();
        } else if (artifactRepository instanceof ArtifactRepositoryInternal) {
            url = "https://plugins.gradle.org/";
        } else {
            throw new IllegalStateException(String.format("ArtifactRepository '%s' of type %s is not supported for Export.", artifactRepository.getName(), artifactRepository.getClass().getName()));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RepositoryIdentifier that = (RepositoryIdentifier) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, url);
    }

    @Override
    public String toString() {
        return name + "(" + url + ")";
    }
}
