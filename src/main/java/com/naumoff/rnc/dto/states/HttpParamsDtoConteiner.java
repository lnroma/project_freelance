package com.naumoff.rnc.dto.states;

public class HttpParamsDtoConteiner {
    private static HttpParamsDto httpParamsDto;

    /**
     * Get instance http params dto
     *
     * @return http params dto instance
     */
    public static HttpParamsDto getHttpParamsDto() {
        if (httpParamsDto == null) {
            httpParamsDto = new HttpParamsDto();
        }

        return httpParamsDto;
    }
}
