package de.acetous.dependencycompliance.export;

import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;


public class DependencyFilterServiceTest {

    private DependencyFilterService testSubject = new DependencyFilterService();

    @Test
    public void shouldMapDependencies() {
        assertThat(testSubject.getDependencyFilter(Arrays.asList("foo:bar:123", "group:id"))).containsExactlyInAnyOrder(
                DependencyIdentifier.create("foo", "bar", "123"),
                DependencyIdentifier.create("group", "id", "*")
        );
    }

    @Test
    public void shouldFormatDependencies() {
        assertThat(DependencyFilterService.formatDependencyStrings(Arrays.asList("foo", "bar", "123"))).isEqualTo(Arrays.asList("foo", "bar", "123"));
        assertThat(DependencyFilterService.formatDependencyStrings(Arrays.asList("foo", "bar"))).isEqualTo(Arrays.asList("foo", "bar", "*"));
        assertThat(DependencyFilterService.formatDependencyStrings(Arrays.asList("foo"))).isEqualTo(Arrays.asList("foo", "*", "*"));
    }

    @Test
    public void shouldThrowException() {
        assertThatCode(() -> DependencyFilterService.formatDependencyStrings(Arrays.asList("1", "2", "3", "4"))).hasMessage("Filtered dependency not valid: 1:2:3:4");
    }
}