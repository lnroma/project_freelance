package com.naumoff.rnc.services.breadcrumbs;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BreadcrumbsService {
    final public String BR_DASHBOARD = "dashboard";
    final public String BR_PROFILE = "profile";
    final public String BR_ORDER_CREATE = "order_create";
    final public String BR_CATALOG = "catalog";
    final public String BR_ORDER_PREVIEW = "order_preview";

    final public String URL_KEY = "url";
    final public String LABEL_KEY = "label";

    public void assignBreadcrumbsToModel(String uri, Model model) {
        List<Map<String, String>> breadcrumbs = getBreadCrumbsForPage(uri);
        model.addAttribute("breadcrumbs", breadcrumbs);
    }

    /**
     * get breadcrumbs for page
     *
     * @param uri uri for page
     * @return array of breadcrumbs
     */
    public List<Map<String, String>> getBreadCrumbsForPage(String uri) {
        if (uri.equals(BR_DASHBOARD)) {
            return getBreadCrumbsForDashboard();
        }

        if (uri.equals(BR_PROFILE)) {
            return getBreadCrumbsForProfile();
        }

        if (uri.equals(BR_ORDER_CREATE)) {
            return getBreadCrumbsForOrderCreate();
        }

        if (uri.equals(BR_CATALOG)) {
            return getBreadCrumbsForCatalog();
        }

        if (uri.equals(BR_ORDER_PREVIEW)) {
            return getBreadCrumbsForOrderPreview();
        }

        return null;
    }

    /**
     * Create breadcrumbs for order create
     *
     * @return list of breadcrumbs map
     */
    private List<Map<String, String>> getBreadCrumbsForOrderCreate() {
        List<Map<String, String>> result = new ArrayList<>();

        result.add(getLinkHome());

        Map<String, String> listOrder = new HashMap<>();

        listOrder.put(URL_KEY, "/catalog");
        listOrder.put(LABEL_KEY, "Список заказов");

        result.add(listOrder);

        Map<String, String> orderCreate = new HashMap<>();
        orderCreate.put(URL_KEY, "/catalog/order/create");
        orderCreate.put(LABEL_KEY, "Создать заказ");

        result.add(orderCreate);

        return result;
    }


    /**
     * Create breadcrumbs for order create
     *
     * @return list of breadcrumbs map
     */
    private List<Map<String, String>> getBreadCrumbsForOrderPreview() {
        List<Map<String, String>> result = new ArrayList<>();

        result.add(getLinkHome());

        Map<String, String> listOrder = new HashMap<>();

        listOrder.put(URL_KEY, "/catalog");
        listOrder.put(LABEL_KEY, "Список заказов");

        result.add(listOrder);

        Map<String, String> orderCreate = new HashMap<>();
        orderCreate.put(URL_KEY, "/catalog/order/preview");
        orderCreate.put(LABEL_KEY, "Подробнее о заказе");

        result.add(orderCreate);

        return result;
    }

    /**
     * get breadcrumbs for dashboard
     * @todo generate automaticaly
     *
     * @return list map string string for breadcrumbs
     */
    private List<Map<String, String>> getBreadCrumbsForDashboard() {
        List<Map<String, String>> result = new ArrayList<>();

        result.add(getLinkHome());

        Map<String, String> dash = new HashMap<>();
        dash.put("url", "/dashboard");
        dash.put("label", "Дашбоард");

        result.add(dash);

        return result;
    }

    private List<Map<String, String>> getBreadCrumbsForCatalog() {
        List<Map<String, String>> result = new ArrayList<>();

        result.add(getLinkHome());

        Map<String, String> catalog = new HashMap<>();
        catalog.put("url", "/catalog");
        catalog.put("label", "Каталог заказов");

        result.add(catalog);

        return result;
    }

    /**
     * Get breadcrumbs for profile
     *
     * @return array for bread crumbs for profile
     */
    private List<Map<String, String>> getBreadCrumbsForProfile() {
        List<Map<String, String>> result = getBreadCrumbsForDashboard();

        Map<String, String> profile = new HashMap<>();
        profile.put("url", "/profile");
        profile.put("label", "Профиль пользователя");

        result.add(profile);

        return result;
    }

    /**
     * Get link for first element in breadcrumbs
     *
     * @return Map of link home
     */
    private Map<String, String> getLinkHome() {
        Map<String, String> res = new HashMap<>();

        res.put("url", "/");
        res.put("label", "Главная");

        return res;
    }
}
