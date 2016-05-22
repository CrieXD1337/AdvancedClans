package ru.undefined1.xClans;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.utils.TextFormat;

import javax.xml.soap.Text;

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
       e.setFormat(e.getFormat().replaceAll("<clan>", plugin.getClanTag(plugin.getPlayerClan(e.getPlayer()))));
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void playerDamageEvent(EntityDamageByEntityEvent e) {

        Player player = (Player) e.getEntity();
        Player damager = (Player) e.getDamager();
        try {
            if (e.getDamager() instanceof Player) {

                String playerDamagerClan = plugin.getPlayerClan(e.getDamager().getName());
                String playerClan = plugin.getPlayerClan((e.getEntity().getName()));

                if (((Player) e.getDamager()).isPlayer()) {
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
        } catch (Exception ex) {
            if(plugin.config.getBoolean("debug")) {
                plugin.sendConsoleMessage("&cOuch! I catch a error! \n&7" + ex.getLocalizedMessage(), true);
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void playerTagSetEvent(PlayerJoinEvent e) {
        final Player p = plugin.getServer().getPlayer(e.getPlayer().getName());
        plugin.setClanNameTag(p, plugin.getPlayerClan(p));
    }

}
