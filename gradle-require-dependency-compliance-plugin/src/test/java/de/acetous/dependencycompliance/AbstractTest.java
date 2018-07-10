package de.acetous.dependencycompliance;

import com.google.gson.Gson;
import de.acetous.dependencycompliance.export.DependencyExport;
import org.apache.commons.io.FileUtils;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import static org.assertj.core.api.Assertions.fail;

public abstract class AbstractTest {

    @Rule
    public final TemporaryFolder testProjectDir = new TemporaryFolder();

    @Before
    public void createGradleProperties() throws Exception {
        FileUtils.copyInputStreamToFile(getClass().getClassLoader().getResourceAsStream("testkit-gradle.properties"), testProjectDir.newFile("gradle.properties"));
    }

    protected Gson gson = new Gson();

    protected DependencyExport parseDependencyExport() {
        return parseDependencyExport("dependency-compliance-report.json");
    }

    protected DependencyExport parseDependencyExport(String file) {
        return gson.fromJson(readFile(file), DependencyExport.class);
    }

    protected String readFile(String file) {
        try {
            return FileUtils.readFileToString(testProjectDir.getRoot().toPath().resolve(file).toFile(), Charset.forName("UTF-8"));
        } catch (IOException e) {
            fail("File '%s' could not be read.", file, e);
            return "";
        }
    }

    protected InputStream createFileInputStream(String filename) {
        return getClass().getResourceAsStream(filename);
    }

    protected GradleRunner createGradleRunner() {
        return GradleRunner.create()
                .withProjectDir(testProjectDir.getRoot())
                .withPluginClasspath();
    }

    protected void copyFile(String source, String destination) {
        try {
            FileUtils.copyInputStreamToFile(createFileInputStream(source), testProjectDir.newFile(destination));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
