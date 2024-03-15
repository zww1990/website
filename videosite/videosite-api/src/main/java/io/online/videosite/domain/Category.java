package io.online.videosite.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 视频类别实体类
 *
 * @author 张维维
 */
@Table(name = "t_category")
@Entity
@Getter
@Setter
@ToString(callSuper = true)
public class Category extends BaseEntity {
    /**
     * 类别名称
     */
    private String categoryName;
}
