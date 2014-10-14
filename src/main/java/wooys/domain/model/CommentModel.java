package wooys.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentModel implements Serializable {
    @Id
    @GeneratedValue(generator = "wooysid")
    @GenericGenerator(name = "wooysid", strategy = "wooys.infra.id.WooysIdGenerator")
    @Size(min = 20, max = 20)
    private String id;
    @NotNull
    @Size(min = 1, max = 512)
    private String body;
    @ManyToOne
    private UserModel user;

    // for JPA
    @Version
    private Integer optimisticLock;
}
