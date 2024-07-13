package io.online.videosite.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户类型枚举类
 *
 * @author 张维维
 */
@AllArgsConstructor
@Getter
public enum UserType {
    /**
     * 系统管理员
     */
    ROLE_ADMIN("系统管理员"),
    /**
     * 普通用户
     */
    ROLE_NORMAL("普通用户");

    private final String userTypeName;
}
