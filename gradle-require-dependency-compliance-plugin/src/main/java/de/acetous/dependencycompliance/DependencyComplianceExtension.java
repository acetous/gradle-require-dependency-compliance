package de.acetous.dependencycompliance;

import org.gradle.api.Project;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;

/**
 * Extension for the {@link DependencyCompliancePlugin}.
 */
public class DependencyComplianceExtension {

    /**
     * The file the report will be written to.
     */
    private final RegularFileProperty outputFile;

    private ListProperty<String> ignore;

    private Property<Boolean> ignoreMavenLocal;

    public DependencyComplianceExtension(Project project) {
        outputFile = project.getObjects().fileProperty().value(project.getLayout().getProjectDirectory().file("dependency-compliance-report.json"));
        ignore = project.getObjects().listProperty(String.class).empty();
        ignoreMavenLocal = project.getObjects().property(Boolean.class).value(false);
    }

    public void setOutputFile(RegularFileProperty outputFile) {
        this.outputFile.set(outputFile);
    }

    public void setIgnore(ListProperty<String> ignore) {
        this.ignore = ignore;
    }

    public void setIgnoreRepositories(Property<Boolean> ignoreMavenLocal) {
        this.ignoreMavenLocal = ignoreMavenLocal;
    }

    public RegularFileProperty getOutputFile() {
        return outputFile;
    }

    public ListProperty<String> getIgnore() {
        return ignore;
    }

    public Property<Boolean> getIgnoreMavenLocal() {
        return ignoreMavenLocal;
    }
}
