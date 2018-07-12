package de.acetous.dependencycompliance.export;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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

    @Test
    public void shouldFilterFullQualifiedDependencies() {
        assertThat(testSubject.isIgnored(DependencyIdentifier.create("foo", "bar", "123"), createDependencyFilter())).isTrue();
        assertThat(testSubject.isIgnored(DependencyIdentifier.create("foo", "bar", "42"), createDependencyFilter())).isFalse();
    }

    @Test
    public void shouldFilterWithVersionWildcard() {
        assertThat(testSubject.isIgnored(DependencyIdentifier.create("org", "baz", "123"), createDependencyFilter())).isTrue();
        assertThat(testSubject.isIgnored(DependencyIdentifier.create("org", "baz", "42"), createDependencyFilter())).isTrue();
        assertThat(testSubject.isIgnored(DependencyIdentifier.create("org", "bac", "4711"), createDependencyFilter())).isFalse();
    }

    @Test
    public void shouldFilterWithArtifactWildcard() {
        assertThat(testSubject.isIgnored(DependencyIdentifier.create("de", "foo", "123"), createDependencyFilter())).isTrue();
        assertThat(testSubject.isIgnored(DependencyIdentifier.create("de", "bar", "42"), createDependencyFilter())).isTrue();
        assertThat(testSubject.isIgnored(DependencyIdentifier.create("org", "bac", "4711"), createDependencyFilter())).isFalse();
    }

    @Test
    public void shouldNotFilterWithoutIgnored() {
        assertThat(testSubject.isIgnored(DependencyIdentifier.create("de", "foo", "123"), Collections.emptySet())).isFalse();
        assertThat(testSubject.isIgnored(DependencyIdentifier.create("de", "bar", "42"), Collections.emptySet())).isFalse();
        assertThat(testSubject.isIgnored(DependencyIdentifier.create("org", "bac", "4711"), Collections.emptySet())).isFalse();
    }

    private Set<DependencyIdentifier> createDependencyFilter() {
        return new HashSet<>(Arrays.asList(
                DependencyIdentifier.create("foo", "bar", "123"),
                DependencyIdentifier.create("org", "baz", "*"),
                DependencyIdentifier.create("de", "*", "*")
        ));
    }
}