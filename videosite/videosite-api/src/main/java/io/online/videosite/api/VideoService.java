package io.online.videosite.api;

import io.online.videosite.constant.AuditStatus;
import io.online.videosite.domain.User;
import io.online.videosite.domain.Video;
import io.online.videosite.model.VideoModel;
import jakarta.persistence.FetchType;

import java.util.List;

/**
 * 视频服务接口
 *
 * @author 张维维
 */
public interface VideoService {
    /**
     * 按审核状态查询视频，并按播放量进行降序排序
     *
     * @param categoryId  类别主键
     * @param auditStatus {@link AuditStatus}
     * @return {@link List<Video>}
     */
    List<Video> query(Integer categoryId, AuditStatus... auditStatus);

    /**
     * 查询此用户所有的视频
     *
     * @param user {@link User}
     * @return {@link List<Video>}
     */
    List<Video> queryForUser(User user);

    /**
     * 按关键字查询审核通过的视频
     *
     * @param keyword 关键字
     * @return {@link List<Video>}
     */
    List<Video> queryForKeyword(String keyword);

    /**
     * 按主键查询
     *
     * @param id        主键
     * @param fetchType {@link FetchType}
     * @return {@link Video}
     */
    Video queryOne(Integer id, FetchType fetchType);

    /**
     * 增加播放量
     *
     * @param id   视频主键
     * @param user {@link User}
     */
    void addHits(Integer id, User user);

    /**
     * 视频审核
     *
     * @param video {@link Video}
     * @param user  {@link User}
     */
    void audit(Video video, User user);

    /**
     * 添加视频
     *
     * @param model {@link VideoModel}
     * @param user  {@link User}
     */
    void save(VideoModel model, User user);

    /**
     * 删除视频相关数据
     *
     * @param video {@link Video}
     */
    void delete(Video video);

    /**
     * 修改视频
     *
     * @param model {@link VideoModel}
     * @param user  {@link User}
     * @param video {@link Video}
     */
    void update(VideoModel model, User user, Video video);
}
