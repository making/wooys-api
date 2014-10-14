package wooys.infra.id;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class WooysUuidGeneratorTest {

    @Test
    public void testGenerate() throws Exception {
        WooysIdGenerator generator = new WooysIdGenerator();
        String id = generator.generate(null, null).toString();
        assertThat(id, is(notNullValue()));
        assertThat(id.length(), is(20));
        assertThat(id.matches("[0-9a-f]{20}"), is(true));
    }
}