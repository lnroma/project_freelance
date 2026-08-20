package com.naumoff.rnc.database.repository.post;

import com.naumoff.rnc.database.entities.post.PostEntity;
import com.naumoff.rnc.database.entities.users.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<PostEntity, Long> {
    Page<PostEntity> findByAuthorOrderByCreatedAtDesc(UserEntity author, Pageable pageable);
}
