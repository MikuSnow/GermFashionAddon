package com.heypixel.germfashionaddon.commands;

import com.heypixel.germfashionaddon.GermFashionAddon;
import com.heypixel.germfashionaddon.SkinsDataManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class equipCommand {
    public void run(CommandSender sender, String[] args) {

        FileConfiguration config = GermFashionAddon.getInstance().getConfig();
        //gf equip <player> <skin>

//        Player player = null;


        if (args.length != 3) {
            return;
        }
        Player player = Bukkit.getServer().getPlayerExact(args[1]);
        String skin = args[2];

        if (GermFashionAddon.getInstance().skinsInfo.containsKey(skin)) {
            SkinsDataManager.equipSkin(player, skin);
        } else {
            player.sendMessage(config.getString("message.equip.none_skin"));
        }

    }
}
