package io.online.videosite.repository;

import io.online.videosite.domain.Comment;
import io.online.videosite.domain.Video;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 视频评论数据访问对象接口
 *
 * @author 张维维
 */
public interface CommentRepository extends JpaRepository<Comment, Integer>, JpaSpecificationExecutor<Comment> {
    /**
     * Find By Video Id Sort By Id
     * @param videoId 视频主键
     * @return List of Comment
     */
    default List<Comment> findByVideoIdSortById(Integer videoId) {
        return this.findAll((root, query, builder) ->
                builder.equal(root.get("videoId"), videoId),
                Sort.by(Sort.Direction.DESC, "id"));
    }

    /**
     * Delete By Video Id
     * @param video {@link Video}
     * @return 影响行数
     */
    default long deleteByVideoId(Video video) {
        return this.delete((root, query, builder) ->
                builder.equal(root.get("videoId"), video.getId()));
    }
}
