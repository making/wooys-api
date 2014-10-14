package wooys.domain.model;

import jqiita.item.Item;
import jqiita.item.ItemRequest;
import jqiita.tag.Tag;
import jqiita.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemModel implements Serializable {
    @Id
    @GeneratedValue(generator = "wooysid")
    @GenericGenerator(name = "wooysid", strategy = "wooys.infra.id.WooysIdGenerator")
    @Size(min = 20, max = 20)
    private String id;
    @Size(min = 1, max = 5120)
    @NotNull
    private String body;
    private boolean coediting;
    @Type(type = "org.jadira.usertype.dateandtime.threeten.PersistentOffsetDateTime")
    private OffsetDateTime createdAt;
    @Accessors(prefix = "_")
    @Column(name = "private")
    private boolean _private;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<ItemTag> tags;
    @Size(min = 1, max = 512)
    @NotNull
    private String title;
    @Type(type = "org.jadira.usertype.dateandtime.threeten.PersistentOffsetDateTime")
    private OffsetDateTime updatedAt;
    @ManyToOne
    private UserModel user;

    @Version
    private Integer optimisticLock;

    // convert
    public static ItemModel newFromItemRequest(ItemRequest request, User user) {
        ItemModel model = new ItemModel();
        OffsetDateTime now = OffsetDateTime.now();
        model.setBody(request.getBody());
        model.setCoediting(request.isCoediting());
        model.setCreatedAt(now);
        model.setPrivate(request.isPrivate());
        model.setTags(request.getTags().stream()
                        .flatMap(tag -> ItemTag.newFromTagRequest(tag).stream())
                        .collect(Collectors.toList())
        );
        model.setTitle(request.getTitle());
        model.setUpdatedAt(now);
        model.setUser(UserModel.newFromUser(user));
        return model;
    }

    public Item toItem() {
        Item item = new Item();
        item.setBody(this.getBody());
        item.setCoediting(this.isCoediting());
        item.setCreatedAt(this.getCreatedAt());
        item.setId(this.getId());
        item.setPrivate(this.isPrivate());
        List<String> versions = this.getTags().stream()
                .map(ItemTag::getVersion)
                .collect(Collectors.toList());
        item.setTags(this.getTags().stream()
                .map(itemTag -> {
                    Tag tag = new Tag();
                    tag.setName(itemTag.getName().getId());
                    tag.setVersions(versions);
                    return tag;
                })
                .collect(Collectors.toList()));
        item.setTitle(this.getTitle());
        item.setUpdatedAt(this.getUpdatedAt());
        item.setUser(this.getUser().toUser());
        return item;
    }
}
