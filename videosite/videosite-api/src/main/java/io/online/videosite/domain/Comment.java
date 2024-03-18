package io.online.videosite.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 视频评论实体类
 *
 * @author 张维维
 */
@Table(name = "t_comment")
@Entity
@Getter
@Setter
@ToString(callSuper = true)
public class Comment extends BaseEntity {
    /**
     * 评论内容
     */
    private String content;
    /**
     * 视频主键
     */
    private Integer videoId;
}
