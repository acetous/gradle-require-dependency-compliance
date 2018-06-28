package de.acetous.dependencycompliance;


import com.google.gson.Gson;
import de.acetous.dependencycompliance.export.DependencyExport;
import de.acetous.dependencycompliance.export.DependencyIdentifier;
import de.acetous.dependencycompliance.export.RepositoryIdentifier;
import org.apache.commons.io.FileUtils;
import org.assertj.core.api.SoftAssertions;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.gradle.testkit.runner.TaskOutcome;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

public class DependencyComplianceExportTest {

    @Rule
    public final TemporaryFolder testProjectDir = new TemporaryFolder();

    private File buildFile;
    private Gson gson;
    private BuildResult result;

    @Before
    public void setup() throws IOException {
        buildFile = testProjectDir.newFile("build.gradle");
        gson = new Gson();

        FileUtils.copyInputStreamToFile(createBuildFileInputStream(), buildFile);

        result = GradleRunner.create()
                .withProjectDir(testProjectDir.getRoot())
                .withArguments("dependencyComplianceExport")
                .withPluginClasspath()
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
        assertThatCode(this::parseDependencyExport).doesNotThrowAnyException();
    }

    @Test
    public void dependenciesShouldBeResolved() {
        DependencyExport dependencyExport = parseDependencyExport();
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
        DependencyExport dependencyExport = parseDependencyExport();
        Set<RepositoryIdentifier> repositories = dependencyExport.getRepositories();
        assertThat(repositories).hasSize(1);
        repositories.forEach(repositoryIdentifier -> {
            assertThat(repositoryIdentifier.getName()).isEqualTo("BintrayJCenter");
            assertThat(repositoryIdentifier.getUrl()).isEqualTo("https://jcenter.bintray.com/");
        });
    }

    @Test
    public void buildDependenciesShouldBeResolved() {
        DependencyExport dependencyExport = parseDependencyExport();
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
        DependencyExport dependencyExport = parseDependencyExport();
        Set<RepositoryIdentifier> repositories = dependencyExport.getBuildRepositories();
        assertThat(repositories).hasSize(2);
        repositories.forEach(repositoryIdentifier -> {
            assertThat(repositoryIdentifier.getUrl()).isIn("https://repo.maven.apache.org/maven2/", "https://plugins.gradle.org/");
        });
    }

    private DependencyExport parseDependencyExport() {
        return gson.fromJson(readFile("result.json"), DependencyExport.class);
    }

    private String readFile(String file) {
        try {
            return FileUtils.readFileToString(testProjectDir.getRoot().toPath().resolve(file).toFile(), Charset.forName("UTF-8"));
        } catch (IOException e) {
            fail("File '%s' could not be read.", file, e);
            return "";
        }
    }

    private InputStream createBuildFileInputStream() {
        return getClass().getResourceAsStream("testcase.gradle");
    }
}
