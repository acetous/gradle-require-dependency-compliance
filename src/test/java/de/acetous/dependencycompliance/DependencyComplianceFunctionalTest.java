package de.acetous.dependencycompliance;



import org.apache.commons.io.FileUtils;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.gradle.testkit.runner.TaskOutcome;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.*;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DependencyComplianceFunctionalTest {
    @Rule
    public final TemporaryFolder testProjectDir = new TemporaryFolder();

    private File buildFile;

    @Before
    public void setup() throws IOException {
        buildFile = testProjectDir.newFile("build.gradle");
    }

    @Test
    public void shouldWriteDependenciesToFile() throws IOException {
        FileUtils.copyInputStreamToFile(createBuildFileInputStream(), buildFile);

        BuildResult result = GradleRunner.create()
                .withProjectDir(testProjectDir.getRoot())
                .withArguments("dependencyComplianceWrite")
                .withPluginClasspath()
                .build();

        assertThat(result.task(":dependencyComplianceWrite").getOutcome()).isEqualTo(TaskOutcome.SUCCESS);

        List<String> lines = FileUtils.readLines(testProjectDir.getRoot().toPath().resolve("result.txt").toFile());
        assertThat(lines).hasSize(7);
        assertThat(lines).allMatch(s -> s.startsWith("spring-") && s.endsWith(":5.0.7.RELEASE)"));
    }

    private InputStream createBuildFileInputStream() throws IOException {
        return getClass().getResourceAsStream("testcase.gradle");
    }
}
