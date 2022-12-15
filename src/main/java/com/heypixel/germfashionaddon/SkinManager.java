package com.heypixel.germfashionaddon;

import com.germ.germplugin.api.GermSkinAPI;
import com.germ.germplugin.api.SkinType;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class SkinManager {
    public static void equip(Player player, String skin) {
        String skinPath = GermFashionAddon.getInstance().skinsInfo.get(skin).getPath();
        GermSkinAPI.addSkin(player, SkinType.ARMOR_SKIN, skinPath);

    }

    public static void equip(Player player, ArrayList<String> skins) {
        for (String skin : skins) {
            String skinPath = GermFashionAddon.getInstance().skinsInfo.get(skin).getPath();
            GermSkinAPI.addSkin(player, SkinType.ARMOR_SKIN, skinPath);
        }
    }

    public static void unEquip(Player player, String skin) {
        String skinPath = GermFashionAddon.getInstance().skinsInfo.get(skin).getPath();
        GermSkinAPI.removeSkin(player, SkinType.ARMOR_SKIN, skinPath);
    }
}
