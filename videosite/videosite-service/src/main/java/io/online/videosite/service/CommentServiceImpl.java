package io.online.videosite.service;

import io.online.videosite.api.CommentService;
import io.online.videosite.domain.Comment;
import io.online.videosite.domain.User;
import io.online.videosite.repository.CommentRepository;
import io.online.videosite.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 视频评论服务接口实现类
 *
 * @author 张维维
 */
@Service
@AllArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Override
    public List<Comment> queryByVideoId(Integer videoId) {
        List<Comment> comments = this.commentRepository.findAll(
                (root, query, builder) -> builder.equal(root.get("videoId"), videoId),
                Sort.by(Sort.Direction.DESC, "id"));
        if (!comments.isEmpty()) {
            // 收集评论人的用户名
            List<String> usernames = comments.stream().map(Comment::getCreator).distinct().toList();
            // 按用户名查询一组用户
            List<User> users = this.userRepository.findAll((root, query, builder) -> root.get("username").in(usernames));
            // 组装Map，键是用户名，值是用户昵称
            Map<String, String> nameMap = users.stream().collect(Collectors.toMap(User::getUsername, User::getNickname, (a, b) -> b));
            // 填充评论人的昵称
            comments.forEach(f -> {
                // 如果取不到昵称，就设置为用户名
                f.setCreatorNick(nameMap.getOrDefault(f.getCreator(), f.getCreator()));
            });
        }
        return comments;
    }

    @Override
    public void save(Comment comment, User user) {
        log.info("save(): comment = {}, user = {}", comment, user);
        comment.setCreator(user.getUsername());
        comment.setModifier(user.getUsername());
        comment.setCreatedDate(LocalDateTime.now());
        comment.setModifiedDate(comment.getCreatedDate());
        this.commentRepository.save(comment);
    }
}
