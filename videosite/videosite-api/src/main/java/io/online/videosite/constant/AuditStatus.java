package io.online.videosite.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 审核状态枚举类
 *
 * @author 张维维
 */
@Getter
@AllArgsConstructor
public enum AuditStatus {
    /**
     * 待审核
     */
    PENDING("待审核"),
    /**
     * 审核不通过
     */
    UNPASSED("审核不通过"),
    /**
     * 审核通过
     */
    PASSED("审核通过");

    private final String desc;
}
