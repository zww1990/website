package io.online.videosite.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.online.videosite.constant.UserType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户实体类
 *
 * @author 张维维
 */
@Table(name = "t_user")
@Entity
@Getter
@Setter
@ToString(callSuper = true)
public class User extends BaseEntity {
    /**
     * 用户名
     */
    private String username;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 密码
     */
    @ToString.Exclude
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    /**
     * 确认密码
     */
    @ToString.Exclude
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Transient
    private String password2;
    /**
     * 用户类型
     */
    @Enumerated(EnumType.STRING)
    private UserType userType;
}
