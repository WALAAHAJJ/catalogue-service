package com.goodfood.catalogue.repo;

import com.goodfood.catalogue.dto.CategoryDto;
import com.goodfood.catalogue.dto.ItemDto;
import com.goodfood.catalogue.dto.ItemUpsertReq;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CatalogueRepository {
    private final JdbcTemplate jdbc;
    public CatalogueRepository(JdbcTemplate jdbc){ this.jdbc = jdbc; }

    // --------- CATEGORIES ----------
    public List<CategoryDto> listCategories(long restaurantId){
        return jdbc.query("""
        SELECT id, restaurant_id, name, sort_order, is_active
        FROM category WHERE restaurant_id=? ORDER BY sort_order, id
        """, (rs,i)-> new CategoryDto(
                rs.getLong("id"),
                rs.getLong("restaurant_id"),
                rs.getString("name"),
                rs.getInt("sort_order"),
                rs.getBoolean("is_active")
        ), restaurantId);
    }

    public Long createCategory(long restaurantId, String name, Integer sortOrder){
        return jdbc.queryForObject("""
        INSERT INTO category(restaurant_id,name,sort_order,is_active)
        VALUES (?,?,COALESCE(?,0),true) RETURNING id
        """, Long.class, restaurantId, name, sortOrder);
    }

    public int patchCategory(long id, String name, Integer sortOrder, Boolean isActive){
        return jdbc.update("""
        UPDATE category SET
          name = COALESCE(?, name),
          sort_order = COALESCE(?, sort_order),
          is_active = COALESCE(?, is_active)
        WHERE id = ?
        """, name, sortOrder, isActive, id);
    }

    public int deleteCategory(long id){
        return jdbc.update("DELETE FROM category WHERE id=?", id);
    }

    // --------- ITEMS ----------
    public List<ItemDto> listItems(long restaurantId, Long categoryId){
        String sql = """
        SELECT id, restaurant_id, category_id, sku, name, description,
               price_cents, currency, is_available, max_per_order
        FROM item WHERE restaurant_id=?""";
        var p = new ArrayList<Object>();
        p.add(restaurantId);
        if (categoryId != null){ sql += " AND category_id=?"; p.add(categoryId); }
        sql += " ORDER BY id";
        String s = sql;
        return jdbc.query(s, p.toArray(), (rs,i)-> new ItemDto(
                rs.getLong("id"),
                rs.getLong("restaurant_id"),
                rs.getLong("category_id"),
                rs.getString("sku"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getInt("price_cents"),
                rs.getString("currency"),
                rs.getBoolean("is_available"),
                rs.getObject("max_per_order")==null?null:rs.getInt("max_per_order")
        ));
    }

    public List<ItemDto> listItemsPaged(long restaurantId, Long categoryId, int page, int size){
        if (size<=0 || size>500) size=50;
        int offset = Math.max(page,0)*size;
        String sql = """
        SELECT id, restaurant_id, category_id, sku, name, description,
               price_cents, currency, is_available, max_per_order
        FROM item WHERE restaurant_id=?""";
        var p = new ArrayList<Object>(); p.add(restaurantId);
        if (categoryId!=null){ sql+=" AND category_id=?"; p.add(categoryId); }
        sql += " ORDER BY id LIMIT ? OFFSET ?"; p.add(size); p.add(offset);
        String s = sql;
        return jdbc.query(s, p.toArray(), (rs,i)-> new ItemDto(
                rs.getLong("id"),
                rs.getLong("restaurant_id"),
                rs.getLong("category_id"),
                rs.getString("sku"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getInt("price_cents"),
                rs.getString("currency"),
                rs.getBoolean("is_available"),
                rs.getObject("max_per_order")==null?null:rs.getInt("max_per_order")
        ));
    }

    public Long createItem(ItemUpsertReq r){
        return jdbc.queryForObject("""
        INSERT INTO item(restaurant_id, category_id, sku, name, description,
                         price_cents, currency, is_available, max_per_order)
        VALUES (?,?,?,?,?,?,?,?,?) RETURNING id
        """, Long.class,
                r.restaurantId(), r.categoryId(), r.sku(), r.name(), r.description(),
                r.priceCents(), r.currency()==null?"EUR":r.currency(),
                r.isAvailable()==null?Boolean.TRUE:r.isAvailable(), r.maxPerOrder());
    }

    public int patchItem(long id, ItemUpsertReq r){
        return jdbc.update("""
        UPDATE item SET
          restaurant_id = COALESCE(?, restaurant_id),
          category_id   = COALESCE(?, category_id),
          sku           = COALESCE(?, sku),
          name          = COALESCE(?, name),
          description   = COALESCE(?, description),
          price_cents   = COALESCE(?, price_cents),
          currency      = COALESCE(?, currency),
          is_available  = COALESCE(?, is_available),
          max_per_order = COALESCE(?, max_per_order),
          updated_at    = now()
        WHERE id = ?
        """,
                r.restaurantId(), r.categoryId(), r.sku(), r.name(), r.description(),
                r.priceCents(), r.currency(), r.isAvailable(), r.maxPerOrder(), id);
    }

    public int deleteItem(long id){
        return jdbc.update("DELETE FROM item WHERE id=?", id);
    }
}
