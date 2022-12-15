package com.heypixel.germfashionaddon.commands;

import com.avaje.ebean.EbeanServer;
import com.heypixel.germfashionaddon.GermFashionAddon;
import com.heypixel.germfashionaddon.dao.GermSkinsDao;
import com.heypixel.germfashionaddon.utils.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.sql.Timestamp;

public class unlockCommand {

    public void run(CommandSender sender, String[] args) {
        Server server = Bukkit.getServer();
        //gfs uload <player> <skin> <day>
        //gfs args[0] args[1] args[2] args[3]
        Player player = server.getPlayerExact(args[1]);
        EbeanServer dataSource = GermFashionAddon.getDataSource();
        String skin = args[2];
        String player_uuid = player.getUniqueId().toString();
        int day = Integer.parseInt(args[3]);
        if (player.isOnline()){
            GermSkinsDao unique = dataSource.find(GermSkinsDao.class).where()
                    .eq("player_uuid", player_uuid)
                    .eq("skin", skin)
                    .findUnique();
            if (unique == null) {
                GermSkinsDao skinDao = new GermSkinsDao();
                skinDao.setSkin(skin);
                skinDao.setEquip(false);
                skinDao.setUnlock_time(new Timestamp(System.currentTimeMillis()));
                skinDao.setExpire_time(TimeUtil.getExpire_time(day));
                skinDao.setPlayer_name(args[1]);
                skinDao.setPlayer_uuid(player_uuid);
                skinDao.setSkin_group(GermFashionAddon.getInstance().skinsInfo.get(skin).getSkin_group());
                dataSource.save(skinDao);
            } else {
                if (unique.isExpire() && unique.isEquip()) {
                    unique.setEquip(false);
                }
                unique.setExpire_time(TimeUtil.getAdd_Expire_time(unique.getExpire_time(), day));
                dataSource.update(unique);
            }

            GermFashionAddon.getInstance().playerSkins.get(player).put(skin, unique);

            FileConfiguration config = GermFashionAddon.getInstance().getConfig();
            player.sendMessage(config.getString("message.unlock")
                    .replace("[display]", GermFashionAddon.getInstance().skinsInfo.get(skin).getDisplay_name())
                    .replace("[time]", day > 999 ? "§c永久": String.valueOf(day))
                    .replace("&", "§")
            );

        }

    }
}
