package com.heypixel.germfashionaddon;

import com.avaje.ebean.EbeanServer;
import com.heypixel.germfashionaddon.dao.GermSkinsDao;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SkinsDataManager {

    public static EbeanServer dataSource = GermFashionAddon.getDataSource();
    public static FileConfiguration config = GermFashionAddon.getInstance().getConfig();
    public static GermFashionAddon plugin = GermFashionAddon.getInstance();

    public static ArrayList<String> getPlayerAllEquipSkins(Player player) {
        ArrayList<String> equipSkins = new ArrayList<>();
        HashMap<String, GermSkinsDao> playerSkins = new HashMap<>();
        List<GermSkinsDao> playerAllData = getPlayerAllData(player);
        if (playerAllData == null) {
            return null;
        }
        for (GermSkinsDao germSkinsDao : playerAllData) {
            if (!germSkinsDao.isExpire()) {
                playerSkins.put(germSkinsDao.getSkin(), germSkinsDao);
                if (germSkinsDao.isEquip()) {
                    equipSkins.add(germSkinsDao.getSkin());
                }
            }
        }
        GermFashionAddon.getInstance().playerSkins.put(player, playerSkins);
        return equipSkins;
    }

    private static List<GermSkinsDao> getPlayerAllData(Player player) {
        String player_uuid = player.getUniqueId().toString();
        return dataSource.find(GermSkinsDao.class).where()
                .eq("player_uuid", player_uuid)
                .findList();
    }


    public static void equipSkin(Player player, String skin) {
        String player_uuid = player.getUniqueId().toString();
        List<GermSkinsDao> equipList = dataSource.find(GermSkinsDao.class).where().eq("player_uuid", player_uuid)
                .eq("skin_group", GermFashionAddon.getInstance().skinsInfo.get(skin).getSkin_group())
                .eq("equip", true)
                .ne("skin", skin)
                .findList();

        if (equipList.size() >= 1) {
            for (GermSkinsDao germSkinsDao : equipList) {
                System.out.println(germSkinsDao.toString());
                germSkinsDao.setEquip(false);
                SkinManager.unEquip(player, germSkinsDao.getSkin());
                dataSource.update(germSkinsDao);

                GermFashionAddon.getInstance().playerSkins.get(player).put(skin, germSkinsDao);
            }
        } else {
            GermSkinsDao unique = dataSource.find(GermSkinsDao.class).where().eq("player_uuid", player_uuid).eq("skin", skin).findUnique();

            if (unique != null) {
                if (!unique.isExpire()) {
                    if (!unique.isEquip()) {
                        SkinManager.equip(player, skin);
                    } else {
                        SkinManager.unEquip(player, skin);
                    }
                    unique.setEquip(!unique.isEquip());
                    dataSource.update(unique);

                    GermFashionAddon.getInstance().playerSkins.get(player).put(skin, unique);
                }
            }
        }

    }
}
