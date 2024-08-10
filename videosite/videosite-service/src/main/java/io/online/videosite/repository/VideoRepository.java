package io.online.videosite.repository;

import io.online.videosite.constant.AuditStatus;
import io.online.videosite.constant.UserType;
import io.online.videosite.domain.Authority;
import io.online.videosite.domain.User;
import io.online.videosite.domain.Video;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.ArrayList;
import java.util.List;

/**
 * 视频数据访问对象接口
 *
 * @author 张维维
 */
public interface VideoRepository extends JpaRepository<Video, Integer>, JpaSpecificationExecutor<Video> {
    /**
     * Find By Category Id And Audit Status
     * @param categoryId 视频类别主键
     * @param auditStatus 审核状态
     * @return List of Video
     */
    default List<Video> findByCategoryIdAndAuditStatus(Integer categoryId, AuditStatus... auditStatus) {
        return this.findAll((root, query, builder) -> {
            List<Predicate> list = new ArrayList<>();
            // 如果指定审核状态
            if (auditStatus.length != 0) {
                list.add(root.get("auditStatus").in(auditStatus));
            }
            // 如果指定类别
            if (categoryId != null) {
                list.add(builder.equal(root.get("categoryId"), categoryId));
            }
            return query.where(list.toArray(Predicate[]::new)).getRestriction();
        }, Sort.by(Sort.Direction.DESC, "videoHits"));
    }

    /**
     * Find By User
     * @param user {@link User}
     * @return List of Video
     */
    default List<Video> findByUser(User user) {
        return this.findAll((root, query, builder) -> {
            // 如果是管理员，查询所有视频
            if (user.getAuthorities()
                    .stream()
                    .map(Authority::getAuthority)
                    .anyMatch(p -> UserType.valueOf(p) == UserType.ROLE_ADMIN)) {
                return query.getRestriction();
            }
            return builder.equal(root.get("creator"), user.getUsername());
        }, Sort.by(Sort.Direction.DESC, "id"));
    }

    /**
     * Find By Audit Status And Video Name
     * @param keyword 关键字
     * @return List of Video
     */
    default List<Video> findByAuditStatusAndVideoName(String keyword) {
        return this.findAll((root, query, builder) ->
                        builder.and(builder.equal(root.get("auditStatus"), AuditStatus.PASSED),
                                builder.like(root.get("videoName"), "%" + keyword + "%")),
                Sort.by(Sort.Direction.DESC, "id"));
    }
}
