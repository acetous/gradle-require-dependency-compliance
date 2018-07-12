package de.acetous.dependencycompliance;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.TaskOutcome;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

public class CheckMavenLocalTest extends AbstractTest {

    @Test
    public void shouldNotIgnoreMavenLocal() throws IOException {
        copyFile("check-mavenlocal/not-ignored.gradle", "build.gradle");
        copyFile("check-mavenlocal/dependency-compliance-report.json", "dependency-compliance-report.json");

        BuildResult result = createGradleRunner().withArguments("dependencyComplianceCheck").buildAndFail();
        assertThat(result.task(":dependencyComplianceCheck").getOutcome()).isEqualTo(TaskOutcome.FAILED);
        assertThat(result.getOutput()).contains("Repository is not listed in dependency compliance export: 'MavenLocal (file:");
    }

    @Test
    public void shouldIgnoreMavenLocal() throws IOException {
        copyFile("check-mavenlocal/ignored.gradle", "build.gradle");
        copyFile("check-mavenlocal/dependency-compliance-report.json", "dependency-compliance-report.json");

        BuildResult result = createGradleRunner().withArguments("dependencyComplianceCheck").build();
        assertThat(result.task(":dependencyComplianceCheck").getOutcome()).isEqualTo(TaskOutcome.SUCCESS);
    }
}
