package io.online.videosite.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.online.videosite.constant.Constants;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 抽象实体类
 *
 * @author 张维维
 */
@MappedSuperclass
@Getter
@Setter
@ToString
public abstract class BaseEntity {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = Constants.JSONFORMAT_DATETIME, timezone = Constants.JSONFORMAT_TIMEZONE)
    private LocalDateTime createdDate;
    /**
     * 创建人
     */
    private String creator;
    /**
     * 创建人昵称
     */
    @Transient
    private String creatorNick;
    /**
     * 修改时间
     */
    @JsonFormat(pattern = Constants.JSONFORMAT_DATETIME, timezone = Constants.JSONFORMAT_TIMEZONE)
    private LocalDateTime modifiedDate;
    /**
     * 修改人
     */
    private String modifier;
}
