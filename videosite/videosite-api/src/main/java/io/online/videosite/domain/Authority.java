package io.online.videosite.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

/**
 * 权限实体类
 *
 * @author 张维维
 * @since 2024-07-08 13:42:07
 */
@Table(name = "t_authority")
@Entity
@Getter
@Setter
@ToString(callSuper = true)
public class Authority extends BaseEntity implements GrantedAuthority {
    /**
     * 权限中文名称
     */
    private String authName;
    /**
     * 权限编码
     */
    private String authority;
}
