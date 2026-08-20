package com.naumoff.rnc.services;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TemplateService {

    private final FreeMarkerConfigurer freeMarkerConfigurer;

    public String render(String templateName, Map<String, Object> model) throws IOException, TemplateException {
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        Template template = configuration.getTemplate(templateName);
        StringWriter writer = new StringWriter();
        template.process(model, writer);

        return writer.toString();
    }
}