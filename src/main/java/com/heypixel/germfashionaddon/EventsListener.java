package com.heypixel.germfashionaddon;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;

public class EventsListener implements Listener {

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        // TODO
        // 玩家登陆时，获取该玩家所有未过期时装
        // 向所有在线玩家发送该玩家的时装信息

//        if (SkinsDataManager.getPlayerAllEquipSkins(event.getPlayer()) != null) {
//            for (String skin : SkinsDataManager.getPlayerAllEquipSkins(event.getPlayer())) {
//                SkinManager.equip(event.getPlayer(), skin);
//            }
//        }
        ArrayList<String> playerAllEquipSkins = SkinsDataManager.getPlayerAllEquipSkins(event.getPlayer());
        if (playerAllEquipSkins != null) {
            Bukkit.getScheduler().runTaskLater(GermFashionAddon.getInstance(), new Runnable() {
                @Override
                public void run() {
                    SkinManager.equip(event.getPlayer(), playerAllEquipSkins);
                }
            }, 40L);
        }



    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        GermFashionAddon.getInstance().playerSkins.remove(event.getPlayer());
    }

}
