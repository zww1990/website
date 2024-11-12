package io.online.videosite.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

/**
 * 视频数据模型
 *
 * @author 张维维
 */
@Getter
@Setter
@ToString
public class VideoModel {
    /**
     * 主键
     */
    private Integer id;
    /**
     * 视频名称
     */
    private String videoName;
    /**
     * 视频类别主键
     */
    private Integer categoryId;
    /**
     * 视频链接
     */
    private MultipartFile videoLink;
    /**
     * 视频链接MD5
     */
    private String videoLinkMd5;
    /**
     * 视频封面
     */
    private MultipartFile videoLogo;
    /**
     * 视频链接路径
     */
    private String videoLinkPath;
    /**
     * 视频封面路径
     */
    private String videoLogoPath;
}
