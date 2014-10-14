package wooys.domain.repository.tag;

import org.springframework.data.jpa.repository.JpaRepository;
import wooys.domain.model.TagModel;

public interface TagRepository extends JpaRepository<TagModel, String> {
}
