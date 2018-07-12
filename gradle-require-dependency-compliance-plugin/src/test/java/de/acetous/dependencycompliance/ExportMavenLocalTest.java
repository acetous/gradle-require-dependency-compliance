package de.acetous.dependencycompliance;


import de.acetous.dependencycompliance.export.DependencyExport;
import de.acetous.dependencycompliance.export.DependencyIdentifier;
import de.acetous.dependencycompliance.export.RepositoryIdentifier;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.TaskOutcome;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

public class ExportMavenLocalTest extends AbstractTest {

    @Test
    public void mavenLocalShouldNotBeIgnored() throws IOException {
        copyFile("export-mavenlocal/not-ignored.gradle", "build.gradle");

        BuildResult result = createGradleRunner()
                .withArguments("dependencyComplianceExport")
                .build();

        DependencyExport dependencyExport = parseDependencyExport();

        assertThat(dependencyExport.getBuildRepositories()).hasSize(3);
        assertThat(dependencyExport.getRepositories()).hasSize(2);

        assertThat(dependencyExport.getBuildRepositories()).extracting(RepositoryIdentifier::getName).contains("MavenLocal");
        assertThat(dependencyExport.getRepositories()).extracting(RepositoryIdentifier::getName).contains("MavenLocal");
    }

    @Test
    public void mavenLocalShouldBeIgnored() throws IOException {
        copyFile("export-mavenlocal/ignored.gradle", "build.gradle");

        BuildResult result = createGradleRunner()
                .withArguments("dependencyComplianceExport")
                .build();

        DependencyExport dependencyExport = parseDependencyExport();

        assertThat(dependencyExport.getBuildRepositories()).hasSize(2);
        assertThat(dependencyExport.getRepositories()).hasSize(1);

        assertThat(dependencyExport.getBuildRepositories()).extracting(RepositoryIdentifier::getName).doesNotContain("MavenLocal");
        assertThat(dependencyExport.getRepositories()).extracting(RepositoryIdentifier::getName).doesNotContain("MavenLocal");
    }

}
