package de.acetous.dependencycompliance;

import de.acetous.dependencycompliance.export.DependencyIdentifier;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DependencyIdentifierComparatorTest {

    DependencyIdentifierComparator sut = new DependencyIdentifierComparator();


    @Test
    public void compareVersionSmaller() {
        DependencyIdentifier d1 = DependencyIdentifier.create("a", "a", "1.0");
        DependencyIdentifier d2 = DependencyIdentifier.create("a", "a", "1.1");

        int compare = sut.compare(d1, d2);

        assertThat(compare).isEqualTo(-1);
    }

    @Test
    public void compareGroupSmaller() {
        DependencyIdentifier d1 = DependencyIdentifier.create("a", "a", "1.0");
        DependencyIdentifier d2 = DependencyIdentifier.create("b", "a", "1.0");

        int compare = sut.compare(d1, d2);

        assertThat(compare).isEqualTo(-1);
    }

    @Test
    public void compareNameSmaller() {
        DependencyIdentifier d1 = DependencyIdentifier.create("a", "a", "1.0");
        DependencyIdentifier d2 = DependencyIdentifier.create("a", "b", "1.0");

        int compare = sut.compare(d1, d2);

        assertThat(compare).isEqualTo(-1);
    }

    @Test
    public void compareVersionDiffer() {
        DependencyIdentifier d1 = DependencyIdentifier.create("a", "a", "a.b-SNAPSHOT");
        DependencyIdentifier d2 = DependencyIdentifier.create("a", "a", "1.1");

        int compare = sut.compare(d2, d1);

        assertThat(compare).isEqualTo(1);
    }
}
