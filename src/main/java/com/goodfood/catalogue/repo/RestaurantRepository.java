package com.goodfood.catalogue.repo;

import com.goodfood.catalogue.dto.RestaurantDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RestaurantRepository {
    private final JdbcTemplate jdbc;
    public RestaurantRepository(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    // Recherche simple (sans pagination)
    public List<RestaurantDto> search(String q, Boolean openNow) {
        StringBuilder sql = new StringBuilder(
                "SELECT id, name, city, is_open FROM restaurant WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (q != null && !q.isBlank()) {
            sql.append(" AND (name ILIKE ? OR city ILIKE ?)");
            params.add("%"+q+"%");
            params.add("%"+q+"%");
        }
        if (openNow != null && openNow) sql.append(" AND is_open = true");
        sql.append(" ORDER BY rating_avg DESC NULLS LAST, name");
        return jdbc.query(sql.toString(), params.toArray(), (rs,i) ->
                new RestaurantDto(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("city"),
                        rs.getBoolean("is_open")));
    }

    // Recherche paginée + tri
    public List<RestaurantDto> searchPaged(String q, Boolean openNow, int page, int size, String sort) {
        if (size <= 0 || size > 200) size = 20;
        int offset = Math.max(page, 0) * size;

        String orderBy = switch (sort) {
            case "name_asc"  -> "name ASC";
            case "name_desc" -> "name DESC";
            case "rating_asc"-> "rating_avg ASC NULLS LAST";
            default          -> "rating_avg DESC NULLS LAST, name";
        };

        StringBuilder sql = new StringBuilder(
                "SELECT id, name, city, is_open FROM restaurant WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (q != null && !q.isBlank()) {
            sql.append(" AND (name ILIKE ? OR city ILIKE ?)");
            params.add("%"+q+"%");
            params.add("%"+q+"%");
        }
        if (openNow != null && openNow) sql.append(" AND is_open = true");

        sql.append(" ORDER BY ").append(orderBy).append(" LIMIT ? OFFSET ?");
        params.add(size);
        params.add(offset);

        return jdbc.query(sql.toString(), params.toArray(), (rs,i) ->
                new RestaurantDto(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("city"),
                        rs.getBoolean("is_open")));
    }

    // Menu JSON (fonction SQL ou vue coté DB)
    public String menuJson(long restaurantId) {
        return jdbc.queryForObject("SELECT get_menu_json(?)::text", String.class, restaurantId);
    }

    // Ajustement de stock basique (utilisé par StockController)
    public int adjustStock(String sku, int delta) {
        return jdbc.update("UPDATE stock_item SET qty_available = qty_available + ? WHERE sku = ?",
                delta, sku);
    }
}
