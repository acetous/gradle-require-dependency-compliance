package de.acetous.dependencycompliance;

import de.acetous.dependencycompliance.export.DependencyExportTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * Plugin for checking a project's dependencies and repositories against an exported report.
 */
public class DependencyCompliancePlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        DependencyComplianceExtension extension = project.getExtensions().create("dependencyCompliance", DependencyComplianceExtension.class, project);

        project.getTasks().create("dependencyComplianceCheck", DependencyCheckTask.class, task -> setupTask(extension, task));
        project.getTasks().create("dependencyComplianceExport", DependencyExportTask.class, task -> setupTask(extension, task));
        project.getTasks().create("dependencyComplianceList", DependencyListTask.class);
    }

    private void setupTask(DependencyComplianceExtension extension, DependencyTask task) {
        task.setOutputFile(extension.getOutputFile());
        task.setIgnore(extension.getIgnore());
        task.setIgnoreMavenLocal(extension.getIgnoreMavenLocal());
    }
}
