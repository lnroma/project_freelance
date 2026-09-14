package com.naumoff.rnc.unit.post;

import com.naumoff.rnc.database.entities.post.PostEntity;
import com.naumoff.rnc.database.entities.users.UserEntity;
import com.naumoff.rnc.database.repository.post.PostRepository;
import com.naumoff.rnc.services.post.PostService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @Test
    void getUserPosts_shouldCallRepositoryWithCorrectArgs() {
        UserEntity user = new UserEntity();
        user.setId(1L);

        Pageable pageable = PageRequest.of(0, 10);

        PostEntity post1 = new PostEntity();
        post1.setId(101L);
        post1.setContent("Первый пост");
        post1.setAuthor(user);

        PostEntity post2 = new PostEntity();
        post2.setId(102L);
        post2.setContent("Второй пост");
        post2.setAuthor(user);

        Page<PostEntity> expectedPage = new org.springframework.data.domain.PageImpl<>(
                List.of(post1, post2),
                pageable,
                2L
        );

        when(postRepository.findByAuthorOrderByCreatedAtDesc(eq(user), eq(pageable)))
                .thenReturn(expectedPage);

        Page<PostEntity> result = postService.getUserPosts(user, pageable);

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(2L);
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).getId()).isEqualTo(101L);
        assertThat(result.getContent().get(1).getId()).isEqualTo(102L);

        verify(postRepository).findByAuthorOrderByCreatedAtDesc(eq(user), eq(pageable));
    }

    @Test
    void getUserPosts_shouldReturnEmptyPageWhenNoPosts() {
        UserEntity user = new UserEntity();
        user.setId(2L);

        Pageable pageable = PageRequest.of(1, 5);

        Page<PostEntity> emptyPage = new org.springframework.data.domain.PageImpl<>(
                List.of(),
                pageable,
                0L
        );

        when(postRepository.findByAuthorOrderByCreatedAtDesc(eq(user), eq(pageable)))
                .thenReturn(emptyPage);

        Page<PostEntity> result = postService.getUserPosts(user, pageable);

        assertThat(result.getTotalElements()).isEqualTo(0L);
        assertThat(result.getContent()).isEmpty();

        verify(postRepository).findByAuthorOrderByCreatedAtDesc(eq(user), eq(pageable));
    }

    @Test
    void createPost_shouldSetAuthorAndContentAndSave() {
        UserEntity user = new UserEntity();
        user.setId(3L);

        String content = "Это тестовый пост от автора";

        PostEntity savedPost = new PostEntity();
        savedPost.setId(500L);
        savedPost.setAuthor(user);
        savedPost.setContent(content);

        when(postRepository.save(any(PostEntity.class)))
                .thenAnswer(invocation -> {
                    PostEntity post = invocation.getArgument(0);
                    // Эмуляция автогенерации ID
                    post.setId(500L);
                    return post;
                });

        PostEntity result = postService.createPost(user, content);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(500L);
        assertThat(result.getAuthor()).isEqualTo(user);
        assertThat(result.getContent()).isEqualTo(content);

        // Проверяем, что в save ушёл объект с корректными полями
        verify(postRepository).save(argThat(post ->
                post.getAuthor() != null &&
                post.getAuthor().getId().equals(3L) &&
                "Это тестовый пост от автора".equals(post.getContent())
        ));
    }

    @Test
    void createPost_shouldCallSaveExactlyOnce() {
        UserEntity user = new UserEntity();
        user.setId(4L);

        String content = "Ещё один пост";

        when(postRepository.save(any(PostEntity.class))).thenReturn(new PostEntity());

        postService.createPost(user, content);

        verify(postRepository, times(1)).save(any(PostEntity.class));
    }
}
