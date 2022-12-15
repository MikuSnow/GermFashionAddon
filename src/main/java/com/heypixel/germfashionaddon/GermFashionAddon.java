package com.heypixel.germfashionaddon;

import com.avaje.ebean.EbeanServer;
import com.heypixel.germfashionaddon.commands.equipCommand;
import com.heypixel.germfashionaddon.commands.tryCommand;
import com.heypixel.germfashionaddon.commands.unlockCommand;
import com.heypixel.germfashionaddon.dao.GermSkinsDao;
import com.heypixel.germfashionaddon.dao.GermSkinCfgDao;
import com.mengcraft.simpleorm.DatabaseException;
import com.mengcraft.simpleorm.EbeanHandler;
import com.mengcraft.simpleorm.EbeanManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public final class GermFashionAddon extends JavaPlugin {

    private static GermFashionAddon instance;
    private static EbeanServer dataSource;
    public FileConfiguration config;

    /**
     * 玩家时装缓存，入服时增加，下线时删除。状态变化时更新。
     * {
     * player: [skin1,skin2,skin3]
     * }
     */
    public HashMap<Player, HashMap<String, GermSkinsDao>> playerSkins = new HashMap<>();

    public HashMap<String, GermSkinCfgDao> skinsInfo;

    public static EbeanServer getDataSource() {
        return dataSource;
    }

    public static GermFashionAddon getInstance() {
        return instance;
    }


    @Override
    public void onEnable() {
        instance = this;
        skinsInfo = new HashMap<>();
        PluginManager plugin = getServer().getPluginManager();
        EbeanManager manager = getServer().getServicesManager().getRegistration(EbeanManager.class).getProvider();
        EbeanHandler handler = manager.getHandler(this);

        loadCfg();
        plugin.registerEvents(new EventsListener(), this);

        if (handler.isNotInitialized()) {
            handler.add(GermSkinsDao.class);
            handler.add(GermSkinCfgDao.class);
            try {
                handler.initialize();
            } catch (DatabaseException e) {
                e.printStackTrace();
            }
        }
        handler.reflect();
        handler.install();
        dataSource = handler.getServer();

        if (plugin.getPlugin("PlaceholderAPI") != null) {
            new Placeholders().register();
        }

        // 读取数据库的配置
        loadOrmCfg();

    }

    private void loadOrmCfg() {

        List<GermSkinCfgDao> futureList = null;
        try {
            futureList = dataSource.find(GermSkinCfgDao.class).findFutureList().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        if (futureList == null) {
            return;
        }

        for (GermSkinCfgDao germSkinCfgDao : futureList) {
            skinsInfo.put(germSkinCfgDao.getSkin(), germSkinCfgDao);
        }

    }

    private void loadCfg() {
        File f = new File(getDataFolder() + "/config.yml");
        if (f.exists()) {
            getLogger().info("检测到配置文件，开始加载");
        } else {
            getLogger().info("未检测到配置，生成默认配置");
            saveDefaultConfig();
        }
        reloadConfig();
        getLogger().info("配置文件加载成功！");
        config = getConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
//        return super.onCommand(sender, command, label, args);

        //gfs try <player> <skin>
        //gfs unlock <player> <skin> <day>
        //gfs equip <player> <skin>
        if (args.length < 1) {
            send_help(sender);
            return false;
        }

        if (!sender.getName().equals("CONSOLE") && !sender.isOp()) {
            sender.sendMessage("你没有权限这样做");
        }
        switch (args[0]) {
            case "try":
                new tryCommand().run(sender, args);
                break;
            case "unlock":
                new unlockCommand().run(sender, args);
                break;
            case "equip":
                new equipCommand().run(sender, args);
                break;
            case "reload":
                loadCfg();
                loadOrmCfg();
                break;
            default:
                return false;
        }
        return false;
    }

    private void send_help(CommandSender sender) {
        for (String s : getConfig().getStringList("message.help")) {
            sender.sendMessage(s.replace("&", "§"));
        }
    }

}
