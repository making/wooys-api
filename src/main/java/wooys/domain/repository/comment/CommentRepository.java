package wooys.domain.repository.comment;


import org.springframework.data.jpa.repository.JpaRepository;
import wooys.domain.model.CommentModel;

public interface CommentRepository extends JpaRepository<CommentModel, String> {
}
