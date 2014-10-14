package wooys.domain.service.item;

import jqiita.item.Item;
import jqiita.item.ItemRequest;
import jqiita.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wooys.domain.model.ItemModel;
import wooys.domain.repository.item.ItemRepository;
import wooys.domain.repository.tag.TagRepository;
import wooys.domain.repository.user.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ItemService {
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TagRepository tagRepository;

    public List<Item> findAll() {
        return itemRepository.findAll()
                .stream()
                .map(ItemModel::toItem)
                .collect(Collectors.toList());
    }

    public Optional<Item> findOne(String id) {
        ItemModel itemModel = itemRepository.findOne(id);
        return Optional.ofNullable(itemModel).map(ItemModel::toItem);
    }

    public Item create(ItemRequest request, User user) {
        ItemModel model = ItemModel.newFromItemRequest(request, user);
        if (!userRepository.exists(model.getUser().getId())) {
            userRepository.saveAndFlush(model.getUser());
        }
        model.getTags().forEach(itemTag -> {
            if (!tagRepository.exists(itemTag.getName().getId())) {
                tagRepository.save(itemTag.getName());
            }
        });
        tagRepository.flush();
        ItemModel created = itemRepository.save(model);
        return created.toItem();
    }

    public void delete(String id) {
        itemRepository.delete(id);
    }
}
