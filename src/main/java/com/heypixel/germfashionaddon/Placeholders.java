package com.heypixel.germfashionaddon;

import com.heypixel.germfashionaddon.dao.GermSkinsDao;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

import java.sql.Timestamp;
import java.util.ArrayList;

public class Placeholders extends PlaceholderExpansion {
    @Override
    public String getIdentifier() {
        return "gfs";
    }

    @Override
    public String getAuthor() {
        return "Miku_Snow";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public boolean isRegistered() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        //%gfs_isunlock_<skin>%
        //%gfs_equip_<group>%
        //%gfs_expire_<skin>%
        //%gfs_isexpire_<skin>%

        String[] args = params.split("_");
        switch (args[0]) {
            case "isunlock":
                if (GermFashionAddon.getInstance().playerSkins.containsKey(player)) {
                    if (GermFashionAddon.getInstance().playerSkins.get(player).containsKey(args[1])) {
                        return "true";
                    }
                    return "false";
                }
                return "false";
            case "equip":
                if (GermFashionAddon.getInstance().playerSkins.containsKey(player)) {
                    for (GermSkinsDao skinInfo : GermFashionAddon.getInstance().playerSkins.get(player).values()) {
                        if (skinInfo.getSkin_group().equals(args[1])) {
                            return skinInfo.getSkin();
                        }
                    }
                }
                return "null";
            case "expire":
                if (GermFashionAddon.getInstance().playerSkins.containsKey(player)) {
                    Timestamp expire_time = GermFashionAddon.getInstance().playerSkins.get(player).get(args[1]).getExpire_time();
                    return expire_time.toString();
                }
                return "null";
            case "isexpire":
                if (GermFashionAddon.getInstance().playerSkins.containsKey(player)) {
                     return String.valueOf(GermFashionAddon.getInstance().playerSkins.get(player).get(args[1]).isExpire());
                }
                return "true";

        }

        return null;
    }
}
