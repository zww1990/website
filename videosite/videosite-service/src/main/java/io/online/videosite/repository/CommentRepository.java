package io.online.videosite.repository;

import io.online.videosite.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 视频评论数据访问对象接口
 *
 * @author 张维维
 */
public interface CommentRepository extends JpaRepository<Comment, Integer>, JpaSpecificationExecutor<Comment> {
}
