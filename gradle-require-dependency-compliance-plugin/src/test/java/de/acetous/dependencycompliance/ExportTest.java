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

public class ExportTest extends AbstractTest {

    private BuildResult result;

    @Before
    public void setup() throws IOException {
        copyFile("export/testcase.gradle", "build.gradle");

        result = createGradleRunner()
                .withArguments("dependencyComplianceExport")
                .build();
    }

    @Test
    public void taskShouldSucceed() {
        assertThat(result.task(":dependencyComplianceExport").getOutcome()).isEqualTo(TaskOutcome.SUCCESS);
    }

    @Test
    public void dependencyReportShouldBeWritten() {
        assertThat(readFile("result.json")).isNotBlank();
    }

    @Test
    public void dependenReportShouldBeParsedAsJson() {
        assertThatCode(this::parseExport).doesNotThrowAnyException();
    }

    private DependencyExport parseExport() {
        return parseDependencyExport("result.json");
    }

    @Test
    public void dependenciesShouldBeResolved() {
        DependencyExport dependencyExport = parseDependencyExport("result.json");
        Set<DependencyIdentifier> dependencies = dependencyExport.getDependencies();
        assertThat(dependencies).hasSize(7);
        dependencies.forEach(dependencyIdentifier -> {
            assertThat(dependencyIdentifier.getGroup()).isEqualTo("org.springframework");
            assertThat(dependencyIdentifier.getName()).isIn("spring-core", "spring-context", "spring-jcl", "spring-expression", "spring-context-support", "spring-beans", "spring-aop");
            assertThat(dependencyIdentifier.getVersion()).isEqualTo("5.0.7.RELEASE");
        });
    }

    @Test
    public void repositoriesShouldBeResolved() {
        DependencyExport dependencyExport = parseDependencyExport("result.json");
        Set<RepositoryIdentifier> repositories = dependencyExport.getRepositories();
        assertThat(repositories).hasSize(1);
        repositories.forEach(repositoryIdentifier -> {
            assertThat(repositoryIdentifier.getName()).isEqualTo("BintrayJCenter");
            assertThat(repositoryIdentifier.getUrl()).isEqualTo("https://jcenter.bintray.com/");
        });
    }

    @Test
    public void buildDependenciesShouldBeResolved() {
        DependencyExport dependencyExport = parseDependencyExport("result.json");
        Set<DependencyIdentifier> dependencies = dependencyExport.getBuildDependencies();
        assertThat(dependencies).hasSize(1);
        dependencies.forEach(dependencyIdentifier -> {
            assertThat(dependencyIdentifier.getGroup()).isEqualTo("commons-io");
            assertThat(dependencyIdentifier.getName()).isEqualTo("commons-io");
            assertThat(dependencyIdentifier.getVersion()).isEqualTo("2.4");
        });
    }

    @Test
    public void buildRepositoriesShouldBeResolved() {
        DependencyExport dependencyExport = parseDependencyExport("result.json");
        Set<RepositoryIdentifier> repositories = dependencyExport.getBuildRepositories();
        assertThat(repositories).hasSize(2);
        repositories.forEach(repositoryIdentifier -> {
            assertThat(repositoryIdentifier.getUrl()).isIn("https://repo.maven.apache.org/maven2/", "https://plugins.gradle.org/");
        });
    }

}
