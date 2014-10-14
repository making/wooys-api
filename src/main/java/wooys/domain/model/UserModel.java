package wooys.domain.model;

import jqiita.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"items", "comments"})
public class UserModel implements Serializable {
    @Id
    @Size(min = 1, max = 30)
    private String id;
    @Column(nullable = true)
    @Size(max = 512)
    private String description;
    @Column(nullable = true)
    private String facebookId;
    @Column(nullable = true)
    private String githubLoginName;
    @Column(nullable = true)
    private String linkedinId;
    @Column(nullable = true)
    private String location;
    @Column(nullable = true)
    private String name;
    @Column(nullable = true)
    private String organization;
    @NotNull
    @URL
    private String profileImageUrl;
    @Column(nullable = true)
    private String twitterScreenName;
    @Column(nullable = true)
    @URL
    private String websiteUrl;

    // for JPA
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<CommentModel> comments;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ItemModel> items;
    @Version
    private Integer optimisticLock;


    // conversion
    public static UserModel newFromUser(User user) {
        UserModel userModel = new UserModel();
        userModel.setDescription(user.getDescription());
        userModel.setFacebookId(user.getFacebookId());
        userModel.setGithubLoginName(user.getGithubLoginName());
        userModel.setId(user.getId());
        userModel.setLinkedinId(user.getLinkedinId());
        userModel.setLocation(user.getLocation());
        userModel.setName(user.getName());
        userModel.setOrganization(user.getOrganization());
        userModel.setProfileImageUrl(user.getProfileImageUrl());
        userModel.setTwitterScreenName(user.getTwitterScreenName());
        userModel.setWebsiteUrl(user.getWebsiteUrl());
        return userModel;
    }

    @Transient
    public User toUser() {
        User user = new User();
        user.setDescription(this.getDescription());
        user.setFacebookId(this.getFacebookId());
        user.setGithubLoginName(this.getGithubLoginName());
        user.setId(this.getId());
        user.setLinkedinId(this.getLinkedinId());
        user.setLocation(this.getLocation());
        user.setName(this.getName());
        user.setOrganization(this.getOrganization());
        user.setProfileImageUrl(this.getProfileImageUrl());
        user.setTwitterScreenName(this.getTwitterScreenName());
        user.setWebsiteUrl(this.getWebsiteUrl());
        return user;
    }
}
