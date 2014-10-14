package wooys.infra.id;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.UUID;
import java.util.regex.Pattern;

public class WooysIdGenerator implements IdentifierGenerator {
    private static final Pattern HYPHEN_PATTERN = Pattern.compile(Pattern.quote("-"));

    @Override
    public Serializable generate(SessionImplementor sessionImplementor, Object o) throws HibernateException {
        // FIXME
        return HYPHEN_PATTERN.matcher(UUID.randomUUID().toString()).replaceAll("").substring(0, 20);
    }
}
