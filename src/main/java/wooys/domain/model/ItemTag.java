package wooys.domain.model;

import jqiita.tag.TagRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemTag {
    @ManyToOne
    private TagModel name;
    private String version;

    public static List<ItemTag> newFromTagRequest(TagRequest tagRequest) {
        String name = tagRequest.getName();
        List<String> versions = tagRequest.getVersions();
        if (versions == null || !versions.isEmpty()) {
            return Collections.singletonList(new ItemTag(new TagModel(name, null, null), null));
        }
        return versions.stream()
                .map(v -> new ItemTag(new TagModel(name, null, null), v))
                .collect(Collectors.toList());
    }
}
