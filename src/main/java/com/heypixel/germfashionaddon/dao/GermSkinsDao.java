package com.heypixel.germfashionaddon.dao;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "germ_skins")
public class GermSkinsDao {
    @Id
    private int id;
    @Column
    private String player_name;
    @Column
    private String player_uuid;
    @Column
    private String skin;
    @Column
    private boolean equip;
    @Column
    private String skin_group;
    @Column
    private Timestamp unlock_time;
    @Column
    private Timestamp expire_time;

    public boolean isExpire() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return expire_time.before(timestamp);
    }

    public boolean isEquip() {
        return equip;
    }

}
