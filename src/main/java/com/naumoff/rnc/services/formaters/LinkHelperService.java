package com.naumoff.rnc.services.formaters;

import com.naumoff.rnc.dto.states.HttpParamsDto;
import org.springframework.stereotype.Service;
import org.apache.http.client.utils.URIBuilder;
import java.net.URI;
import java.util.List;

@Service
public class LinkHelperService {

    private HttpParamsDto httpParamsDto;

    public LinkHelperService() {}

    public void setHttpParamsDto(HttpParamsDto httpParamsDto) {
        this.httpParamsDto = httpParamsDto;
    }

    public String getUrlForNavigation(
            String resource,
            String query,
            List<Long> categoryIds,
            List<Long> cityIds,
            List<String> prices
    ) {
        if (query.equals("") &&
                (categoryIds == null || categoryIds.isEmpty()) &&
                (cityIds == null || cityIds.isEmpty()) &&
                (prices == null || prices.isEmpty())
        ) {
            return null;
        }

        try {
            URIBuilder builder = new URIBuilder()
                    .setScheme("https")
                    .setHost("api.example.com")
                    .setPath(resource);
//                .addParameter("param", "value")
//                .addParameter("param2", "value2");

            if (query != null && !query.equals("")) {
                builder.addParameter("query", query);
            }

            if (categoryIds != null && !categoryIds.isEmpty()) {
                for (Long categoryId : categoryIds) {
                    builder.addParameter("category_ids", String.valueOf(categoryId));
                }
            }

            if (cityIds != null && !cityIds.isEmpty()) {
                for (Long cityId : cityIds) {
                    builder.addParameter("city_ids", String.valueOf(cityId));
                }
            }

            if (prices != null && !prices.isEmpty()) {
                for (String price : prices) {
                    builder.addParameter("prices", price);
                }
            }

            URI uri = builder.build();
            String queryString = uri.getRawQuery();

            return "?" + queryString + "&";
        } catch (Exception e) {
            System.out.print(e.getMessage());
            e.printStackTrace();

            return "";
        }
    }
}
