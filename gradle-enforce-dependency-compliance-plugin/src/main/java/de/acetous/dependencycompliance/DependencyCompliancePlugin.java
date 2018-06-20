package de.acetous.dependencycompliance;

import de.acetous.dependencycompliance.check.DependencyCheckTask;
import de.acetous.dependencycompliance.export.DependencyExportTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class DependencyCompliancePlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        DependencyComplianceExtension extension = project.getExtensions().create("dependencyCompliance", DependencyComplianceExtension.class, project);
        project.getTasks().create("dependencyComplianceCheck", DependencyCheckTask.class, (task) -> {
            task.setOutputFile(extension.getOutputFile());
        });
        project.getTasks().create("dependencyComplianceExport", DependencyExportTask.class, (task) -> {
            task.setOutputFile(extension.getOutputFile());
        });
        project.getTasks().create("dependencyComplianceList", DependencyListTask.class);
    }
}
