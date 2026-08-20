package com.naumoff.rnc.database.repository.pages;

import com.naumoff.rnc.database.entities.pages.SeoPage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface SeoPageRepository extends JpaRepository<SeoPage, Long> {

    Optional<SeoPage> findBySlug(String slug);

    List<SeoPage> findAllBySlugIn(Collection<String> slugs);
}