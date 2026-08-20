package com.naumoff.rnc.services.pages;

import com.naumoff.rnc.database.entities.pages.SeoPage;
import com.naumoff.rnc.database.repository.pages.SeoPageRepository;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Optional;

@Service
public class SeoPagesService {

    private final SeoPageRepository seoPageRepository;

    public SeoPagesService(
            SeoPageRepository seoPageRepository
    ) {
        this.seoPageRepository = seoPageRepository;
    }

    /**
     * Assign seo metainformation from database to data model for present in templates
     * @param slug this is a uri to page
     * @param model data model for templates
     */
    public void assignSeoMetainformationToModel(
            String slug,
            Model model
    ) {
        model.addAttribute("seo", getSeoMetainformation(slug));
    }

    /**
     * Get seo metainformation
     *
     * @param slug this is a uri to page
     * @return get seo metainformation
     */
    public SeoPage getSeoMetainformation(String slug) {
        Optional<SeoPage> result = seoPageRepository.findBySlug(slug);

        SeoPage resultSeo;

        if (result.isEmpty()) {
            // @todo default seo information
        }

        return result.orElse(null);
    }
}
