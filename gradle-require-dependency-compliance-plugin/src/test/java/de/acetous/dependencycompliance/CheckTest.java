package de.acetous.dependencycompliance;

import de.acetous.dependencycompliance.export.DependencyExport;
import de.acetous.dependencycompliance.export.DependencyIdentifier;
import org.gradle.internal.impldep.org.intellij.lang.annotations.MagicConstant;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.TaskOutcome;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Set;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

public class CheckTest extends AbstractTest {

    private BuildResult result;

    @Before
    public void setup() throws IOException {
        copyFile("check/testcase.gradle", "build.gradle");
    }

    @Test
    public void failsWithoutExport() {
        BuildResult result = createGradleRunner().withArguments("dependencyComplianceCheck").buildAndFail();
        assertThat(result.task(":dependencyComplianceCheck").getOutcome()).isEqualTo(TaskOutcome.FAILED);
        assertThat(result.getOutput()).matches(Pattern.compile(".*Dependency compliance export expectet at '.*' is not present\\..*", Pattern.DOTALL));
    }
}
