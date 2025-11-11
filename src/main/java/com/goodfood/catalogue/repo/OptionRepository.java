package com.goodfood.catalogue.repo;

import com.goodfood.catalogue.dto.OptionDtos.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class OptionRepository {
    private final JdbcTemplate jdbc;
    public OptionRepository(JdbcTemplate jdbc){ this.jdbc = jdbc; }

    // GROUPS
    public List<OptionGroupDto> listGroups(long restaurantId){
        return jdbc.query("""
      SELECT id, restaurant_id, name, min_select, max_select, required
      FROM option_group WHERE restaurant_id=? ORDER BY id
      """,(rs,i)-> new OptionGroupDto(
                rs.getLong("id"), rs.getLong("restaurant_id"), rs.getString("name"),
                rs.getInt("min_select"), rs.getInt("max_select"), rs.getBoolean("required")), restaurantId);
    }
    public Long createGroup(GroupUpsertReq r){
        return jdbc.queryForObject("""
      INSERT INTO option_group(restaurant_id,name,min_select,max_select,required)
      VALUES (?,?,?,?,COALESCE(?,false)) RETURNING id
      """, Long.class, r.restaurantId(), r.name(), r.minSelect(), r.maxSelect(), r.required());
    }
    public int patchGroup(long id, GroupUpsertReq r){
        return jdbc.update("""
      UPDATE option_group SET name=COALESCE(?,name),
        min_select=COALESCE(?,min_select), max_select=COALESCE(?,max_select),
        required=COALESCE(?,required) WHERE id=?
      """, r.name(), r.minSelect(), r.maxSelect(), r.required(), id);
    }
    public int deleteGroup(long id){ return jdbc.update("DELETE FROM option_group WHERE id=?", id); }

    // OPTIONS
    public List<OptionItemDto> listOptions(long groupId){
        return jdbc.query("""
      SELECT id, group_id, name, price_delta_cents, is_available
      FROM option_item WHERE group_id=? ORDER BY id
      """,(rs,i)-> new OptionItemDto(
                rs.getLong("id"), rs.getLong("group_id"), rs.getString("name"),
                rs.getInt("price_delta_cents"), rs.getBoolean("is_available")), groupId);
    }
    public Long createOption(OptionUpsertReq r){
        return jdbc.queryForObject("""
      INSERT INTO option_item(group_id,name,price_delta_cents,is_available)
      VALUES (?,?,COALESCE(?,0),COALESCE(?,true)) RETURNING id
      """, Long.class, r.groupId(), r.name(), r.priceDeltaCents(), r.isAvailable());
    }
    public int patchOption(long id, OptionUpsertReq r){
        return jdbc.update("""
      UPDATE option_item SET name=COALESCE(?,name),
        price_delta_cents=COALESCE(?,price_delta_cents),
        is_available=COALESCE(?,is_available) WHERE id=?
      """, r.name(), r.priceDeltaCents(), r.isAvailable(), id);
    }
    public int deleteOption(long id){ return jdbc.update("DELETE FROM option_item WHERE id=?", id); }
}
