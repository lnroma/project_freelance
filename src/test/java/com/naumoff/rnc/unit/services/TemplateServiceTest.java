package com.naumoff.rnc.unit.services;

import com.naumoff.rnc.services.TemplateService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TemplateServiceTest {

    @Mock
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Mock
    private Configuration configuration;

    @Mock
    private Template template;

    @InjectMocks
    private TemplateService templateService;

    private Map<String, Object> model;

    @BeforeEach
    void setUp() {
        model = new HashMap<>();
        model.put("title", "Привет, мир!");
        model.put("items", List.of("A", "B", "C"));

        when(freeMarkerConfigurer.getConfiguration()).thenReturn(configuration);
        try {
            when(configuration.getTemplate(any(String.class))).thenThrow(new RuntimeException()).thenReturn(template);
        } catch (Exception e) {}
    }

    @Test
    @DisplayName("render — корректно обрабатывает шаблон и возвращает строку")
    void render_success() throws IOException, TemplateException {
        // Arrange
        String expectedOutput = "<h1>Привет, мир!</h1><ul><li>A</li><li>B</li><li>C</li></ul>";
        doAnswer(invocation -> {
            var writer = (java.io.StringWriter) invocation.getArgument(1);
            writer.write(expectedOutput);
            return null;
        }).when(template).process(any(), any());

        // Act
        String result = templateService.render("page.ftl", model);

        // Assert
        assertThat(result).isEqualTo(expectedOutput);
        verify(configuration, times(1)).getTemplate("page.ftl");
        verify(template, times(1)).process(eq(model), any());
    }

    @Test
    @DisplayName("render — пробрасывает IOException от Template.process")
    void render_throwsIOException() throws IOException, TemplateException {
        // Arrange
        IOException ioException = new IOException("Ошибка ввода-вывода при рендере");
        doThrow(ioException).when(template).process(any(), any());

        // Act & Assert
        assertThatThrownBy(() -> templateService.render("page.ftl", model))
                .isInstanceOf(IOException.class)
                .hasMessage("Ошибка ввода-вывода при рендере");

        verify(template, times(1)).process(any(), any());
    }

    @Test
    @DisplayName("render — пробрасывает TemplateException от Template.process")
    void render_throwsTemplateException() throws IOException, TemplateException {
        // Arrange
        TemplateException templateException = new TemplateException("Ошибка шаблона", null);
        doThrow(templateException).when(template).process(any(), any());

        // Act & Assert
        assertThatThrownBy(() -> templateService.render("page.ftl", model))
                .isInstanceOf(TemplateException.class)
                .hasMessage("Ошибка шаблона");

        verify(template, times(1)).process(any(), any());
    }

    @Test
    @DisplayName("render — запрашивает шаблон по точному имени")
    void render_usesCorrectTemplateName() throws IOException, TemplateException {
        // Arrange
        doNothing().when(template).process(any(), any());

        // Act
        templateService.render("dashboard.ftl", model);

        // Assert
        verify(configuration, times(1)).getTemplate("dashboard.ftl");
    }
}
