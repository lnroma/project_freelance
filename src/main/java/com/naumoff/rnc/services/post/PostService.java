package com.naumoff.rnc.services.post;

import com.naumoff.rnc.database.entities.post.PostEntity;
import com.naumoff.rnc.database.entities.users.UserEntity;
import com.naumoff.rnc.database.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    /**
     * get user posts
     *
     * @param user author user
     * @return list of PostEntity
     */
    public Page<PostEntity> getUserPosts(
            UserEntity user,
            Pageable pageable
    ) {
        return postRepository.findByAuthorOrderByCreatedAtDesc(user, pageable);
    }

    /**
     * create post user
     *
     * @param user author user
     * @param content content
     * @return saved entity
     */
    public PostEntity createPost(UserEntity user, String content) {
        PostEntity post = new PostEntity();
        post.setAuthor(user);
        post.setContent(content);

        return postRepository.save(post);
    }
}