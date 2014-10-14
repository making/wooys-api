package wooys.domain.model;

import jqiita.tag.TagRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagModel implements Serializable {
    @Id
    @NotNull
    private String id;
    @URL
    @Column(nullable = true)
    private String iconUrl;

    // for JPA
    @Version
    private Integer optimisticLock;

    public static TagModel newFromTagRequest(TagRequest tagRequest) {
        return new TagModel(tagRequest.getName(), null, null);
    }
}
