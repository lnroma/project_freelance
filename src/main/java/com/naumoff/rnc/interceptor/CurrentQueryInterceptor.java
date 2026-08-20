package com.naumoff.rnc.interceptor;

import com.naumoff.rnc.dto.states.HttpParamsDto;
import com.naumoff.rnc.dto.states.HttpParamsDtoConteiner;
import com.naumoff.rnc.services.formaters.LinkHelperService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigInteger;
import java.util.Map;

@Component
public class CurrentQueryInterceptor implements HandlerInterceptor {

    private final LinkHelperService linkHelperService;

    public CurrentQueryInterceptor(
            LinkHelperService linkHelperService
    ) {
        this.linkHelperService = linkHelperService;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {
        return true;
    }

    @Override
    public void postHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            @Nullable ModelAndView modelAndView
    ) {
        if (modelAndView == null) {
            return;
        }

        Map<String, String[]> requestParam = request.getParameterMap();
        HttpParamsDto httpParamsDto = HttpParamsDtoConteiner.getHttpParamsDto();

        if (requestParam.containsKey("size")) {
            httpParamsDto.setSize(Integer.valueOf(requestParam.get("size")[0]));
        }

        if (requestParam.containsKey("page")) {
            httpParamsDto.setNumber(Integer.valueOf(requestParam.get("page")[0]));
        }

        if (requestParam.containsKey("query")) {
            httpParamsDto.setQuery(requestParam.get("query")[0]);
        }

        if (requestParam.containsKey("priceFrom")) {
            httpParamsDto.setPriceFrom(new BigInteger(requestParam.get("priceFrom")[0]));
        }

        if (requestParam.containsKey("priceTo")) {
            httpParamsDto.setPriceTo(new BigInteger(requestParam.get("priceTo")[0]));
        }

        linkHelperService.setHttpParamsDto(httpParamsDto);

        modelAndView.addObject("linkHelper", linkHelperService);
        modelAndView.addObject("param", httpParamsDto);
    }
}
