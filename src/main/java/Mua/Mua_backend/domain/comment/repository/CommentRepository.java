package Mua.Mua_backend.domain.comment.repository;

import Mua.Mua_backend.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByFeedIdOrderByCreatedAtAsc(Long feedId);

    List<Comment> findByParentIdOrderByCreatedAtAsc(Long parentId);
}