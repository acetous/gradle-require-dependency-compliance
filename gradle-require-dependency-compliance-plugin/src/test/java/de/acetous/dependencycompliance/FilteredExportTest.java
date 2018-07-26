package de.acetous.dependencycompliance;


import de.acetous.dependencycompliance.export.DependencyExport;
import de.acetous.dependencycompliance.export.DependencyIdentifier;
import de.acetous.dependencycompliance.export.RepositoryIdentifier;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.TaskOutcome;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

public class FilteredExportTest extends AbstractTest {

    private BuildResult result;

    @Before
    public void setup() throws IOException {
        copyFile("export-filtered/testcase.gradle", "build.gradle");

        result = createGradleRunner()
                .withArguments("dependencyComplianceExport")
                .build();
    }

    @Test
    public void taskShouldSucceed() {
        assertThat(result.task(":dependencyComplianceExport").getOutcome()).isEqualTo(TaskOutcome.SUCCESS);
    }

    @Test
    public void taskShouldLogIgnoredDependencies() {
        assertThat(result.getOutput()).contains("(DependencyCompliance) Ignoring these dependencies:");
        assertThat(result.getOutput()).contains("foo:bar:123");
        assertThat(result.getOutput()).contains("commons-io:commons-io:*");
        assertThat(result.getOutput()).contains("org.springframework:*:*");
    }

    @Test
    public void dependencyReportShouldBeWritten() {
        assertThat(readFile("dependency-compliance-report.json")).isNotBlank();
    }

    @Test
    public void dependenciesShouldBeResolved() {
        DependencyExport dependencyExport = parseDependencyExport();
        Collection<DependencyIdentifier> dependencies = dependencyExport.getDependencies();
        assertThat(dependencies).hasSize(0);
    }

    @Test
    public void repositoriesShouldBeResolved() {
        DependencyExport dependencyExport = parseDependencyExport();
        Collection<RepositoryIdentifier> repositories = dependencyExport.getRepositories();
        assertThat(repositories).hasSize(1);
        repositories.forEach(repositoryIdentifier -> {
            assertThat(repositoryIdentifier.getName()).isEqualTo("BintrayJCenter");
            assertThat(repositoryIdentifier.getUrl()).isEqualTo("https://jcenter.bintray.com/");
        });
    }

    @Test
    public void buildDependenciesShouldBeResolved() {
        DependencyExport dependencyExport = parseDependencyExport();
        Collection<DependencyIdentifier> dependencies = dependencyExport.getBuildDependencies();
        assertThat(dependencies).hasSize(0);
    }

    @Test
    public void buildRepositoriesShouldBeResolved() {
        DependencyExport dependencyExport = parseDependencyExport();
        Collection<RepositoryIdentifier> repositories = dependencyExport.getBuildRepositories();
        assertThat(repositories).hasSize(2);
        repositories.forEach(repositoryIdentifier -> {
            assertThat(repositoryIdentifier.getUrl()).isIn("https://repo.maven.apache.org/maven2/", "https://plugins.gradle.org/");
        });
    }

}
