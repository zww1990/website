package io.online.videosite.repository;

import io.online.videosite.domain.User;
import io.online.videosite.domain.Video;
import io.online.videosite.domain.VideoHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * 视频观看历史数据访问对象接口
 *
 * @author 张维维
 */
public interface VideoHistoryRepository extends JpaRepository<VideoHistory, Integer>,
        JpaSpecificationExecutor<VideoHistory> {
    /**
     * Find By Video Id And Creator
     * @param id 视频主键
     * @param user {@link User}
     * @return {@link VideoHistory}
     */
    default Optional<VideoHistory> findByVideoIdAndCreator(Integer id, User user) {
        return this.findOne((root, query, builder) -> builder.and(
                        builder.equal(root.get("videoId"), id),
                        builder.equal(root.get("creator"), user.getUsername())
                ));
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
