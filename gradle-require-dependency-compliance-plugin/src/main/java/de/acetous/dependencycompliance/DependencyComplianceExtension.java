package de.acetous.dependencycompliance;

import org.gradle.api.Project;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.ListProperty;

/**
 * Extension for the {@link DependencyCompliancePlugin}.
 */
public class DependencyComplianceExtension {

    /**
     * The file the report will be written to.
     */
    private final RegularFileProperty outputFile;

    private ListProperty<String> ignore;

    public DependencyComplianceExtension(Project project) {
        outputFile = project.getLayout().fileProperty(project.provider(() -> project.getLayout().getProjectDirectory().file("dependency-compliance-report.json")));
        ignore = project.getObjects().listProperty(String.class);
    }

    public void setOutputFile(RegularFileProperty outputFile) {
        this.outputFile.set(outputFile);
    }

    public void setIgnore(ListProperty<String> ignore) {
        this.ignore = ignore;
    }

    public RegularFileProperty getOutputFile() {
        return outputFile;
    }

    public ListProperty<String> getIgnore() {
        return ignore;
    }
}
