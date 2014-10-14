package wooys.domain.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import wooys.domain.model.UserModel;


public interface UserRepository extends JpaRepository<UserModel, String> {
}
