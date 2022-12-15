package com.heypixel.germfashionaddon.dao;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "germ_skins_cfg")
public class GermSkinCfgDao {

    @Id
    private int id;
    @Column
    private String skin;
    @Column
    private String display_name;
    @Column
    private String skin_group;
    @Column
    private String path;
}
