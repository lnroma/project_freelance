package com.naumoff.rnc.unit.pages;

import com.naumoff.rnc.database.entities.pages.SeoPage;
import com.naumoff.rnc.database.repository.pages.SeoPageRepository;
import com.naumoff.rnc.services.pages.SeoPagesService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SeoPagesServiceTest {

    @Mock
    private SeoPageRepository seoPageRepository;

    @Mock
    private Model model;

    @InjectMocks
    private SeoPagesService seoPagesService;

    // ── getSeoMetainformation ────────────────────────────────────

    @Test
    void getSeoMetainformation_shouldReturnSeoPage_whenFound() {
        String slug = "about";
        SeoPage seoPage = new SeoPage();
        seoPage.setSlug(slug);
        seoPage.setTitle("О компании");
        seoPage.setDescription("Информация о компании");

        when(seoPageRepository.findBySlug(slug)).thenReturn(Optional.of(seoPage));

        SeoPage result = seoPagesService.getSeoMetainformation(slug);

        assertThat(result).isNotNull();
        assertThat(result.getSlug()).isEqualTo(slug);
        assertThat(result.getTitle()).isEqualTo("О компании");
        verify(seoPageRepository).findBySlug(slug);
    }

    @Test
    void getSeoMetainformation_shouldReturnNull_whenNotFound() {
        String slug = "nonexistent";

        when(seoPageRepository.findBySlug(slug)).thenReturn(Optional.empty());

        SeoPage result = seoPagesService.getSeoMetainformation(slug);

        assertThat(result).isNull();
        verify(seoPageRepository).findBySlug(slug);
    }

    @Test
    void getSeoMetainformation_shouldReturnNull_whenSlugIsNull() {
        when(seoPageRepository.findBySlug(null)).thenReturn(Optional.empty());

        SeoPage result = seoPagesService.getSeoMetainformation(null);

        assertThat(result).isNull();
        verify(seoPageRepository).findBySlug(null);
    }

    // ── assignSeoMetainformationToModel ─────────────────────────

    @Test
    void assignSeoMetainformationToModel_shouldAddSeoAttribute_whenFound() {
        String slug = "catalog";
        SeoPage seoPage = new SeoPage();
        seoPage.setSlug(slug);
        seoPage.setTitle("Каталог");

        when(seoPageRepository.findBySlug(slug)).thenReturn(Optional.of(seoPage));

        seoPagesService.assignSeoMetainformationToModel(slug, model);

        verify(model).addAttribute(eq("seo"), eq(seoPage));
    }

    @Test
    void assignSeoMetainformationToModel_shouldAddNullSeoAttribute_whenNotFound() {
        String slug = "missing";

        when(seoPageRepository.findBySlug(slug)).thenReturn(Optional.empty());

        seoPagesService.assignSeoMetainformationToModel(slug, model);

        verify(model).addAttribute(eq("seo"), isNull());
    }

    @Test
    void assignSeoMetainformationToModel_shouldCaptureCorrectAttribute() {
        String slug = "home";
        SeoPage seoPage = new SeoPage();
        seoPage.setSlug(slug);
        seoPage.setTitle("Главная");

        when(seoPageRepository.findBySlug(slug)).thenReturn(Optional.of(seoPage));

        ArgumentCaptor<SeoPage> captor = ArgumentCaptor.forClass(SeoPage.class);
        when(model.addAttribute(eq("seo"), captor.capture())).thenReturn(model);

        seoPagesService.assignSeoMetainformationToModel(slug, model);

        SeoPage captured = captor.getValue();
        assertThat(captured.getSlug()).isEqualTo(slug);
        assertThat(captured.getTitle()).isEqualTo("Главная");
    }
}
