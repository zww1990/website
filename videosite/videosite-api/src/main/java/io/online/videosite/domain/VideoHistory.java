package io.online.videosite.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 视频观看历史实体类
 *
 * @author 张维维
 */
@Table(name = "t_video_history")
@Entity
@Getter
@Setter
@ToString(callSuper = true)
public class VideoHistory extends BaseEntity {
    /**
     * 视频主键
     */
    private Integer videoId;
    /**
     * 视频播放量
     */
    private Integer playCount;
}
