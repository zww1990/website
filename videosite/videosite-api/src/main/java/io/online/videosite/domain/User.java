package io.online.videosite.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.online.videosite.constant.UserType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

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
public class User extends BaseEntity implements UserDetails, CredentialsContainer {
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
    /**
     * 权限集合
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "t_user_authority",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "auth_id", referencedColumnName = "id"))
    private Set<Authority> authorities;
    /**
     * 帐户是否未过期
     */
    private boolean accountNonExpired;
    /**
     * 帐户是否未锁定
     */
    private boolean accountNonLocked;
    /**
     * 密码是否未过期
     */
    private boolean credentialsNonExpired;
    /**
     * 帐户是否已启用
     */
    private boolean enabled;

    @Override
    public void eraseCredentials() {
        this.password = null;
        this.password2 = null;
    }
}
