package ru.rexlite;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.utils.TextFormat;
import java.util.List;

public class Listener implements cn.nukkit.event.Listener {
    xClans plugin;

    public Listener(xClans plugin) {
        this.plugin = plugin;
    }

    @EventHandler(
            ignoreCancelled = true,
            priority = EventPriority.HIGH
    )
    public void chatTag(PlayerChatEvent e) {
        String var10000 = e.getMessage();
        xClans var10001 = this.plugin;
        if (var10000.startsWith(xClans.config.getString("clan-message-prefix"))) {
            xClans var9 = this.plugin;
            if (xClans.getPlayerClan(e.getPlayer().getName()) != null) {
                var9 = this.plugin;
                var9 = this.plugin;
                List<String> members = xClans.getClanMembers(xClans.getPlayerClan(e.getPlayer()));
                var9 = this.plugin;
                var9 = this.plugin;
                List<String> managers = xClans.getClanManagers(xClans.getPlayerClan(e.getPlayer()));
                StringBuilder var14 = (new StringBuilder()).append("&e[");
                var10001 = this.plugin;
                var10001 = this.plugin;
                String clanTag = var14.append(xClans.getClanTag(xClans.getPlayerClan(e.getPlayer().getName()))).append("&e] ").toString();
                xClans var15 = this.plugin;
                xClans.clans.save();
                var15 = this.plugin;
                xClans.clans.reload();
                e.setCancelled(true);
                if (members != null) {
                    for(String clanMembers : members) {
                        this.plugin.getServer().getPlayerExact(clanMembers).sendMessage(TextFormat.colorize(clanTag + e.getPlayer().getDisplayName() + "&7:&e " + e.getMessage().replace("@", "")));
                    }
                }

                if (managers != null) {
                    for(String clanManagers : managers) {
                        this.plugin.getServer().getPlayerExact(clanManagers).sendMessage(TextFormat.colorize(clanTag + e.getPlayer().getDisplayName() + "&7:&e " + e.getMessage().replace("@", "")));
                    }
                }
            }
        } else {
            String var19 = e.getFormat();
            xClans var10003 = this.plugin;
            var10003 = this.plugin;
            e.setFormat(var19.replaceAll("<clan>", xClans.getClanTag(xClans.getPlayerClan(e.getPlayer()))));
        }

    }

    @EventHandler(
            ignoreCancelled = true,
            priority = EventPriority.HIGH
    )
    public void playerDamageEvent(EntityDamageByEntityEvent e) {
        Player player = (Player)e.getEntity();
        Entity damager = e.getDamager();

        try {
            if (e instanceof EntityDamageByEntityEvent && e.getDamager() instanceof Player) {
                xClans var7 = this.plugin;
                String playerDamagerClan = xClans.getPlayerClan(e.getDamager().getName());
                var7 = this.plugin;
                String playerClan = xClans.getPlayerClan(e.getEntity().getName());
                if (((Player)e.getDamager()).isPlayer()) {
                    var7 = this.plugin;
                    if (xClans.getPlayerClan(damager.getName()) != null) {
                        var7 = this.plugin;
                        if (xClans.getPlayerClan(player.getName()) != null) {
                            if (playerClan.equals(playerDamagerClan)) {
                                player.sendMessage(this.plugin.getTranslation("PVP", "FRIENDLY-FIRE", true));
                                e.setCancelled(true);
                            } else {
                                var7 = this.plugin;
                                if (xClans.isFriendly(playerClan)) {
                                    player.sendMessage(this.plugin.getTranslation("PVP", "FRIENDLY-CLAN", true));
                                    e.setCancelled(true);
                                } else {
                                    var7 = this.plugin;
                                    if (xClans.isFriendly(playerDamagerClan)) {
                                        player.sendMessage(this.plugin.getTranslation("PVP", "FRIENDLY", true));
                                        e.setCancelled(true);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            xClans var10000 = this.plugin;
            if (xClans.config.getBoolean("debug")) {
                this.plugin.sendConsoleMessage("&7[&cWARNING&7] Cathed error: &e" + ex.getStackTrace(), true);
            }
        }

    }

    @EventHandler(
            ignoreCancelled = true,
            priority = EventPriority.HIGH
    )
    public void playerTagSetEvent(PlayerJoinEvent e) {
        Player p = this.plugin.getServer().getPlayer(e.getPlayer().getName());
        xClans var10000 = this.plugin;
        xClans var10001 = this.plugin;
        xClans.setClanNameTag(p, xClans.getPlayerClan(p));
    }
}
