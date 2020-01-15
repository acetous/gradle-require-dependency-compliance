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
        project.getTasks().create("dependencyComplianceList", DependencyListTask.class, task -> setupTask(extension, task));
    }

    private void setupTask(DependencyComplianceExtension extension, DependencyTask task) {
        task.getOutputFile().set(extension.getOutputFile());
        task.getIgnore().set(extension.getIgnore());
        task.getIgnoreMavenLocal().set(extension.getIgnoreMavenLocal());
    }
}
