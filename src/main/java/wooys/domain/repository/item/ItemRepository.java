package wooys.domain.repository.item;

import org.springframework.data.jpa.repository.JpaRepository;
import wooys.domain.model.ItemModel;

public interface ItemRepository extends JpaRepository<ItemModel, String> {
}
