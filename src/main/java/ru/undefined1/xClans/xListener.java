package ru.undefined1.xClans;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.utils.TextFormat;

import javax.xml.soap.Text;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ryzhe on 20.05.2016.
 */
public class xListener implements Listener {

    xClans plugin;

    public xListener(xClans plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void chatTag(PlayerChatEvent e) {
        if(e.getMessage().startsWith(plugin.config.getString("clan-message-prefix"))) {
            if(plugin.getPlayerClan(e.getPlayer().getName()) != null) {
                List<String> members = plugin.getClanMembers(plugin.getPlayerClan(e.getPlayer()));
               List<String> managers = plugin.getClanManagers(plugin.getPlayerClan(e.getPlayer()));
                String clanTag = "&e[" + plugin.getClanTag(plugin.getPlayerClan(e.getPlayer().getName())) + "&e] ";
                plugin.clans.save();
                plugin.clans.reload();
                    e.setCancelled(true);
                    if(members != null) {
                        for (String clanMembers : members) {
                            plugin.getServer().getPlayerExact(clanMembers).sendMessage(TextFormat.colorize(clanTag + e.getPlayer().getDisplayName() + "&7:&e " + e.getMessage().replace("@", "")));
                        }
                    }
                    if(managers != null) {
                        for (String clanManagers : managers) {
                            plugin.getServer().getPlayerExact(clanManagers).sendMessage(TextFormat.colorize(clanTag + e.getPlayer().getDisplayName() + "&7:&e " + e.getMessage().replace("@", "")));
                        }
                    }
            }
        } else {
            e.setFormat(e.getFormat().replaceAll("<clan>", plugin.getClanTag(plugin.getPlayerClan(e.getPlayer()))));
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void playerDamageEvent(EntityDamageByEntityEvent e) {

        Player player = (Player) e.getEntity();
        Entity damager = ((EntityDamageByEntityEvent) e).getDamager();
        try {
            if(e instanceof EntityDamageByEntityEvent) {
            if (((EntityDamageByEntityEvent) e).getDamager() instanceof Player) {

                String playerDamagerClan = plugin.getPlayerClan(e.getDamager().getName());
                String playerClan = plugin.getPlayerClan((e.getEntity().getName()));

                if (((Player) e.getDamager()).isPlayer()) {
                    if (plugin.getPlayerClan(damager.getName()) != null) {
                        if (plugin.getPlayerClan(player.getName()) != null) {
                            if (playerClan.equals(playerDamagerClan)) {
                                player.sendMessage(plugin.getTranslation("PVP", "FRIENDLY-FIRE", true));
                                e.setCancelled(true);
                            } else {
                                if (plugin.isFriendly(playerClan)) {
                                    player.sendMessage(plugin.getTranslation("PVP", "FRIENDLY-CLAN", true));
                                    e.setCancelled(true);
                                } else {
                                    if (plugin.isFriendly(playerDamagerClan)) {
                                        player.sendMessage(plugin.getTranslation("PVP", "FRIENDLY", true));
                                        e.setCancelled(true);
                                    }
                                }
                            }
                        }
                    }
                }
            }


            }
        } catch (Exception ex) {
            if(plugin.config.getBoolean("debug")) {
                plugin.sendConsoleMessage("&c[WARNING] &7Cathed error: &e" + ex.getStackTrace(), true);
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void playerTagSetEvent(PlayerJoinEvent e) {
        final Player p = plugin.getServer().getPlayer(e.getPlayer().getName());
        plugin.setClanNameTag(p, plugin.getPlayerClan(p));
    }

}
