package io.online.videosite.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.online.videosite.constant.AuditStatus;
import io.online.videosite.constant.Constants;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 视频实体类
 *
 * @author 张维维
 */
@Table(name = "t_video")
@Entity
@Getter
@Setter
@ToString(callSuper = true)
public class Video extends BaseEntity {
    /**
     * 视频名称
     */
    private String videoName;
    /**
     * 视频链接
     */
    private String videoLink;
    /**
     * 视频链接MD5
     */
    private String videoLinkMd5;
    /**
     * 视频封面
     */
    private String videoLogo;
    /**
     * 视频播放量
     */
    private Integer videoHits;
    /**
     * 视频类别主键
     */
    private Integer categoryId;
    /**
     * 视频类别名称
     */
    @Transient
    private String categoryName;
    /**
     * 审核状态
     */
    @Enumerated(EnumType.STRING)
    private AuditStatus auditStatus;

    /**
     * 审核状态描述
     */
    @Transient
    public String getAuditStatusDesc() {
        return this.auditStatus.getDesc();
    }

    /**
     * 审核时间
     */
    @JsonFormat(pattern = Constants.JSONFORMAT_DATETIME, timezone = Constants.JSONFORMAT_TIMEZONE)
    private LocalDateTime auditedDate;
    /**
     * 审核人
     */
    private String auditor;
    /**
     * 审核人昵称
     */
    @Transient
    private String auditorNick;
    /**
     * 审核不通过原因
     */
    private String auditReason;
}
