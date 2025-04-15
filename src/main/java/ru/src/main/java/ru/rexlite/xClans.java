package ru.rexlite;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Location;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import cn.nukkit.utils.TextFormat;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class xClans extends PluginBase {
    public static xClans instance;
    private File cfg;
    public static Config config;
    private File clanData;
    public static Config clans;
    private File playerData;
    public static Config players;

    public xClans() {
    }

    public static xClans getPlugin() {
        return instance;
    }

    public static Boolean isClanMember(String player, String clan) {
        List<String> clanMembers = clans.getStringList("clans." + clan + ".members");
        if (clanMembers != null) {
            return clanMembers.contains(player) ? true : false;
        } else {
            return false;
        }
    }

    public String getTranslation(String section, String message, Boolean prefix) {
        config.reload();
        String pref = config.getString("prefix");
        String output = config.getString("TRANSLATION." + config.getString("language") + "." + section + "." + message);
        return prefix ? TextFormat.colorize(pref + " " + output) : TextFormat.colorize(output);
    }

    public static void setClanNameTag(Player player, String clan) {
        clans.reload();
        if (getPlayerClan(player.getName()) != null && (clans.getString("clans." + clan + ".tag") != null || clans.getString("clans." + clan + ".tag") != "")) {
            String tag = clans.getString("clans." + clan + ".tag");
            player.setNameTag(TextFormat.colorize("&f[" + tag + "&f]" + " " + player.getDisplayName()));
            player.setNameTagVisible(true);
        }

    }

    public static String getClanName(String clan) {
        return clans.getString("clans." + clan + ".name") == "" && clans.getString("clans." + clan + ".name") == null ? null : clans.getString("clans." + clan + ".name");
    }

    public static void renameClan(String clanName, String newName) {
        if (clans.getString("clans." + clanName) != "" || clans.getString("clans." + clanName) != null) {
            clans.set("clans." + clanName + ".name", newName);
        }

    }

    public static void setClanTag(String clan, String newClanTag) {
        if (clans.getString("clans." + clan + ".tag") != null || clans.getString("clans." + clan + ".tag") != "") {
            clans.set("clans." + clan + ".tag", newClanTag);
        }

    }

    public static void addClanMember(Player player, String clan) {
        if (clans.getString("clans." + clan) != null || clans.getString("clans." + clan) != "") {
            List<String> members = clans.getList("clans." + clan);
            if (!members.contains(player.getName())) {
                members.add(player.getName());
            }
        }

    }

    public static void addClanMember(String player, String clan) {
        if (clans.getString("clans." + clan) != null || clans.getString("clans." + clan) != "") {
            List<String> members = clans.getList("clans." + clan);
            if (!members.contains(player)) {
                members.add(player);
            }
        }

    }

    public void reloadAllConfigs() {
        players.save();
        clans.save();
        config.save();
        players.reload();
        clans.reload();
        config.reload();
    }

    public static void addClan(String clan, String clanName, String owner, Boolean friendly) {
        List<String> owners = new ArrayList();
        owners.add(owner);
        String clanPath = "clans." + clan;
        clans.set(clanPath + ".name", clanName);
        clans.set(clanPath + ".tag", clanName);
        clans.set(clanPath + ".friendly", friendly);
        clans.set(clanPath + ".managers", owners);
        players.set(owner + ".clan", clan);
        players.save();
        clans.save();
        players.reload();
        clans.reload();
    }

    public static List<String> getClanMembers(String clan) {
        List<String> members = clans.getList("clans." + clan + ".members");
        return members;
    }

    public static void setPlayerClan(Player player, String newClan) {
        if (players.getString(player.getName() + ".clan") != null || players.getString(player.getName() + ".clan") != "") {
            players.set(player.getName() + ".clan", newClan);
        }

    }

    public static void setPlayerClan(String player, String newClan) {
        if (players.getString(player + ".clan") != null || players.getString(player + ".clan") != "") {
            players.set(player + ".clan", newClan);
        }

    }

    public static void removePlayerFromClan(String player) {
        if (players.getString(player + ".clan") != null || players.getString(player + ".clan") != "") {
            players.set(player, (Object)null);
        }

    }

    public static void removePlayerFromClan(Player player) {
        if (players.getString(player.getName() + ".clan") != null || players.getString(player.getName() + ".clan") != "") {
            players.set(player.getName(), (Object)null);
        }

    }

    public static List<String> getClanManagers(String clan) {
        List<String> managers = clans.getList("clans." + clan + ".managers");
        return managers;
    }

    public static String getClanTag(String clan) {
        return clans.getString("clans." + clan + ".tag") == "" && clans.getString("clans." + clan + ".tag") == null ? TextFormat.colorize("&7[*]") : TextFormat.colorize(clans.getString("clans." + clan + ".tag"));
    }

    public static String getPlayerClan(Player player) {
        return players.getString(player.getName() + ".clan") != "" ? players.getString(player.getName() + ".clan") : null;
    }

    public static Boolean isFriendly(String clan) {
        if (clans.getString("clans." + clan + ".name") == "" && clans.getString("clans." + clan + ".name") == null) {
            return false;
        } else {
            return clans.getBoolean("clans." + clan + ".friendly") ? true : false;
        }
    }

    public static String getPlayerClan(String player) {
        return players.getString(player + ".clan") != "" ? players.getString(player + ".clan") : null;
    }

    public static Boolean isClanManager(String player) {
        if (players.getString(player + ".clan") == "" && players.getString(player + ".clan") == null) {
            return false;
        } else {
            List<String> owner = clans.getStringList("clans." + getPlayerClan(player) + ".managers");
            return owner.contains(player) ? true : false;
        }
    }

    public static List<String> getClanModerators(String clan) {
        List<String> owner = clans.getStringList("clans." + clan + ".moderators");
        return owner != null ? owner : null;
    }

    public static void removeClanManager(Player player, String clan) {
        if (players.getString(player + ".clan") != "" || players.getString(player + ".clan") != null) {
            List<String> owner = clans.getList("clans." + getPlayerClan(player) + ".managers");
            if (owner.contains(player.getName()) && owner != null) {
                owner.remove(player.getName());
            }
        }

    }

    public static void removeClanManager(String player, String clan) {
        if (players.getString(player + ".clan") != "" || players.getString(player + ".clan") != null) {
            List<String> owner = clans.getList("clans." + getPlayerClan(player) + ".managers");
            if (owner.contains(player) && owner != null) {
                owner.remove(player);
            }
        }

    }

    public static void removeClanModerator(Player player, String clan) {
        if (players.getString(player + ".clan") != "" || players.getString(player + ".clan") != null) {
            List<String> owner = clans.getList("clans." + getPlayerClan(player) + ".moderators");
            if (owner.contains(player.getName()) && owner != null) {
                owner.remove(player.getName());
            }
        }

    }

    public static void removeClanModerator(String player, String clan) {
        if (players.getString(player + ".clan") != "" || players.getString(player + ".clan") != null) {
            List<String> owner = clans.getList("clans." + getPlayerClan(player) + ".moderators");
            if (owner.contains(player) && owner != null) {
                owner.remove(player);
            }
        }

    }

    public static void addClanManager(Player player, String clan) {
        if (players.getString(player + ".clan") != "" || players.getString(player + ".clan") != null) {
            List<String> owner = clans.getList("clans." + getPlayerClan(player) + ".managers");
            if (!owner.contains(player.getName())) {
                if (owner != null) {
                    owner.add(player.getName());
                } else {
                    List<String> moderator = new ArrayList();
                    moderator.add(player.getName());
                    String clanName = getPlayerClan(player);
                    config.set("clans." + clanName + ".managers", moderator);
                }
            }
        }

    }

    public static void addClanManager(String player, String clan) {
        if (players.getString(player + ".clan") != "" || players.getString(player + ".clan") != null) {
            List<String> owner = clans.getList("clans." + getPlayerClan(player) + ".managers");
            if (!owner.contains(player)) {
                if (owner != null) {
                    owner.add(player);
                } else {
                    List<String> moderator = new ArrayList();
                    moderator.add(player);
                    String clanName = getPlayerClan(player);
                    config.set("clans." + clanName + ".managers", moderator);
                }
            }
        }

    }

    public static Boolean isClanHaveHome(String clan) {
        return clans.getString("clans." + clan + ".home") != null ? true : false;
    }

    public static String getClanHomeX(String clan) {
        return clans.getString("clans." + clan + ".home") != null ? clans.getString("clans." + clan + ".home.x") : null;
    }

    public static String getClanHomeY(String clan) {
        return clans.getString("clans." + clan + ".home") != null ? clans.getString("clans." + clan + ".home.y") : null;
    }

    public static String getClanHomeZ(String clan) {
        return clans.getString("clans." + clan + ".home") != null ? clans.getString("clans." + clan + ".home.z") : null;
    }

    public static List<String> getClanBannedMembers(String clan) {
        List<String> owner = clans.getStringList("clans." + clan + ".banned");
        return owner != null ? owner : null;
    }

    public static void addClanModerator(String player, String clan) {
        if (players.getString(player + ".clan") != "" || players.getString(player + ".clan") != null) {
            List<String> owner = clans.getList("clans." + getPlayerClan(player) + ".moderators");
            if (!owner.contains(player)) {
                if (owner != null) {
                    owner.add(player);
                } else {
                    List<String> moderator = new ArrayList();
                    moderator.add(player);
                    String clanName = getPlayerClan(player);
                    config.set("clans." + clanName + ".moderators", moderator);
                }
            }
        }

    }

    public static void addClanModerator(Player player, String clan) {
        if (players.getString(player + ".clan") != "" || players.getString(player + ".clan") != null) {
            List<String> owner = clans.getList("clans." + getPlayerClan(player) + ".moderators");
            if (!owner.contains(player.getName())) {
                if (owner != null) {
                    owner.add(player.getName());
                } else {
                    List<String> moderator = new ArrayList();
                    moderator.add(player.getName());
                    String clanName = getPlayerClan(player);
                    config.set("clans." + clanName + ".moderators", moderator);
                }
            }
        }

    }

    public static Boolean isClanModerator(String player) {
        if (players.getString(player + ".clan") == "" && players.getString(player + ".clan") == null) {
            return false;
        } else {
            List<String> owner = clans.getStringList("clans." + getPlayerClan(player) + ".moderators");
            return owner.contains(player) ? true : false;
        }
    }

    public String getMessage(String text, Boolean prefix) {
        if (!prefix) {
            return TextFormat.colorize(config.getString(text));
        } else {
            String prefixs = TextFormat.colorize("&e[&bxClans&e]");
            return TextFormat.colorize(prefixs + " " + config.getString(text));
        }
    }

    public void sendConsoleMessage(String text, Boolean prefix) {
        if (!prefix) {
            this.getServer().getConsoleSender().sendMessage(TextFormat.colorize(text));
        } else {
            String prefixs = TextFormat.colorize("&e[&bxClans&e]");
            this.getServer().getConsoleSender().sendMessage(prefixs + " " + TextFormat.colorize(text));
        }

    }

    public void startScheduler() {
        int secManager = config.getInt("cooldown");
        String compiler = String.valueOf(secManager) + 0;
        int sec = Integer.parseInt(compiler);
        this.getServer().getScheduler().scheduleRepeatingTask(new Runnable() {
            public void run() {
                for(Player player : xClans.this.getServer().getOnlinePlayers().values()) {
                    xClans.setClanNameTag(player, xClans.getPlayerClan(player));
                }

                if (xClans.config.getBoolean("debug")) {
                    xClans.this.sendConsoleMessage(xClans.this.getTranslation("DEBUG", "CLAN-TAG-UPDATED", false), true);
                }

            }
        }, sec);
    }

    public void onEnable() {
        this.getServer().getScheduler().cancelAllTasks();
        this.getDataFolder().mkdirs();
        this.saveDefaultConfig();
        this.getServer().getPluginManager().registerEvents(new xListener(this), this);
        this.saveResource("clans.yml", false);
        this.saveResource("players.yml", false);
        this.saveResource("config.yml", false);
        this.playerData = new File(this.getDataFolder(), "players.yml");
        players = new Config(this.playerData, 2);
        this.clanData = new File(this.getDataFolder(), "clans.yml");
        clans = new Config(this.clanData, 2);
        this.cfg = new File(this.getDataFolder(), "config.yml");
        config = new Config(this.cfg, 2);
        ConfigSection section = clans.getSections("clans");
        if (this.getServer().getLanguage().getLang().equalsIgnoreCase("rus")) {
            config.set("language", "russian");
            this.reloadAllConfigs();
        } else if (this.getServer().getLanguage().getLang().equalsIgnoreCase("eng")) {
            config.set("language", "english");
            this.reloadAllConfigs();
        }

        this.sendConsoleMessage(this.getTranslation("DEBUG", "LOADED2", false), true);
        this.sendConsoleMessage(this.getTranslation("DEBUG", "LOADED", false).replaceAll("<count>", String.valueOf(section.size())), true);
        this.startScheduler();
    }

    public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
        if (cmdLabel.equalsIgnoreCase("clan")) {
            if (args.length == 0) {
                if (getPlayerClan(sender.getName()) == null) {
                    sender.sendMessage(this.getTranslation("CLAN-MANAGEMENT", "NO-CLAN.DONT-HAVE-CLAN", true));
                    sender.sendMessage(this.getTranslation("CLAN-MANAGEMENT", "NO-CLAN.NO-CLAN-CREATE", true));
                } else {
                    sender.sendMessage(this.getTranslation("CLAN-MANAGEMENT", "CLAN.MYCLAN", true).replaceAll("<name>", getClanName(getPlayerClan(sender.getName()))));
                    if (isClanManager(sender.getName())) {
                        sender.sendMessage(this.getTranslation("CLAN-MANAGEMENT", "CLAN.RANK.MANAGER", true));
                    } else if (isClanMember(sender.getName(), getPlayerClan(sender.getName()))) {
                        sender.sendMessage(this.getTranslation("CLAN-MANAGEMENT", "CLAN.RANK.MEMBER", true));
                    }

                    if (isFriendly(getPlayerClan(sender.getName()))) {
                        sender.sendMessage(this.getTranslation("CLAN-MANAGEMENT", "CLAN.FRIENDLY", true));
                    } else {
                        sender.sendMessage(this.getTranslation("CLAN-MANAGEMENT", "CLAN.NOT-FRIENDLY", true));
                    }

                    if (getClanMembers(getPlayerClan(sender.getName())) != null) {
                        sender.sendMessage("\n");
                        sender.sendMessage(this.getTranslation("CLAN-MANAGEMENT", "CLAN.MEMBERS", true));
                        sender.sendMessage(getClanMembers(getPlayerClan(sender.getName())).toString());
                        sender.sendMessage("\n");
                    } else {
                        sender.sendMessage(this.getTranslation("CLAN-MANAGEMENT", "CLAN.NOMEMBERS", true));
                        sender.sendMessage("\n");
                    }

                    if (getClanManagers(getPlayerClan(sender.getName())) != null) {
                        sender.sendMessage(this.getTranslation("CLAN-MANAGEMENT", "CLAN.MANAGERS", true));
                        sender.sendMessage(getClanManagers(getPlayerClan(sender.getName())).toString());
                        sender.sendMessage("\n");
                    }
                }
            } else if (args[0].equalsIgnoreCase("create")) {
                if (args.length == 1) {
                    sender.sendMessage(this.getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-CREATE-USE", true));
                } else {
                    ConfigSection section = clans.getSections("clans");
                    Set<String> clansNames = section.getKeys(false);
                    if (!clansNames.contains(args[1].replaceAll("[^A-Za-z]+", ""))) {
                        if (players.getString(sender.getName() + ".clan") == "") {
                            addClan(args[1].replaceAll("[^A-Za-z]+", ""), args[1].replaceAll("[^A-Za-z]+", ""), sender.getName(), false);
                            players.save();
                            clans.save();
                            players.reload();
                            clans.reload();
                            sender.sendMessage(this.getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-CREATED", true).replaceAll("<clan>", args[1].replaceAll("[^A-Za-z]+", "")));
                        } else {
                            sender.sendMessage(this.getTranslation("CLAN-MANAGEMENT", "CLAN.ALREADY-MEMBER", true));
                        }
                    } else {
                        sender.sendMessage(this.getTranslation("CLAN-MANAGEMENT", "CLAN.ALREADY-CREATED", true));
                    }
                }
            } else if (args[0].equalsIgnoreCase("join")) {
                if (args.length == 1) {
                    sender.sendMessage(this.getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-JOIN-USE", true));
                } else {
                    if (getPlayerClan(sender.getName()) == null) {
                        ConfigSection section = clans.getSections("clans");
                        Set<String> clansNames = section.getKeys(false);
                        if (clansNames.contains(args[1])) {
                            List<String> claners = new ArrayList();
                            claners.add(sender.getName());
                            clans.set("clans." + args[1] + ".members", claners);
                            players.set(sender.getName() + ".clan", args[1]);
                            this.reloadAllConfigs();
                            sender.sendMessage(this.getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-JOIN", true).replaceAll("<clan>", args[1]));
                        } else {
                            sender.sendMessage(this.getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-NOT-FOUND", true));
                        }
                    } else {
                        sender.sendMessage(this.getTranslation("CLAN-MANAGEMENT", "CLAN.ALREADY-MEMBER", true));
                    }

                    this.reloadAllConfigs();
                }
            } else if (args[0].equalsIgnoreCase("leave")) {
                String myClan = getPlayerClan(sender.getName());
                if (getPlayerClan(sender.getName()) == "") {
                    sender.sendMessage(this.getTranslation("CLAN-MANAGEMENT", "NO-CLAN.NOT-IN-CLAN", true));
                } else {
                    if (getClanManagers(myClan).contains(sender.getName())) {
                        if (getClanManagers(myClan).size() == 1) {
                            sender.sendMessage(this.getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-CANT-LEAVE", true));
                            sender.sendMessage(this.getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-CANT-LEAVE2", true));
                        } else if (getClanManagers(myClan).size() <= 2) {
                            sender.sendMessage(myClan);
                            List string = clans.getList("clans." + myClan + ".managers");
                            string.remove(sender.getName());
                            removePlayerFromClan(sender.getName());
                            sender.sendMessage(this.getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-LEAVE", true));
                        }
                    } else if (getClanMembers(myClan).contains(sender.getName())) {
                        getClanMembers(myClan).remove(sender.getName());
                        removePlayerFromClan(sender.getName());
                        sender.sendMessage(this.getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-LEAVE", true));
                    }

                    this.reloadAllConfigs();
                }
            } else if (args[0].equalsIgnoreCase("disband")) {
                if (getPlayerClan(sender.getName()) == "") {
                    sender.sendMessage(this.getTranslation("CLAN-MANAGEMENT", "NO-CLAN.NOT-IN-CLAN", true));
                } else {
                    if (!isClanManager(sender.getName())) {
                        sender.sendMessage(this.getTranslation("CLAN-MANAGEMENT", "CLAN.RANK.NOT-A-MANAGER", true));
                    } else {
                        String myClan = getPlayerClan(sender.getName());
                        if (getClanManagers(myClan).size() == 1) {
                            String clanName = getPlayerClan(sender.getName());

                            try {
                                for(String toRemove : getClanMembers(clanName)) {
                                    removePlayerFromClan(sender.getName());
                                }
                            } catch (Exception var13) {
                            }

                            for(String toRemove : getClanManagers(clanName)) {
                                removePlayerFromClan(sender.getName());
                            }

                            clans.set("clans." + clanName, (Object)null);
                            clans.remove("clans." + clanName);
                            sender.sendMessage(this.getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-DISBAND", true));
                        } else {
                            sender.sendMessage(this.getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-CANT-DISBAND", true));
                            sender.sendMessage(this.getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-CANT-DISBAND2", true));
                        }
                    }

                    this.reloadAllConfigs();
                }
            } else if (args[0].equalsIgnoreCase("tag")) {
                if (args.length == 1) {
                    sender.sendMessage(this.getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-TAG-USE", true));
                } else {
                    if (getPlayerClan(sender.getName()) == "") {
                        sender.sendMessage(this.getTranslation("CLAN-MANAGEMENT", "NO-CLAN.NOT-IN-CLAN", true));
                    } else if (isClanManager(sender.getName())) {
                        if (!args[1].matches("[^A-Za-z&123456789]+")) {
                            setClanTag(getPlayerClan(sender.getName()), args[1].replaceAll("[^A-Za-z&123456789]+", ""));
                            sender.sendMessage(TextFormat.colorize(this.getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-TAG", true).replaceAll("<tag>", args[1].replaceAll("[^A-Za-z&123456789]+", ""))));
                        } else {
                            sender.sendMessage(this.getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-TAG-ERROR", true));
                        }
                    } else {
                        sender.sendMessage(this.getTranslation("CLAN-MANAGEMENT", "CLAN.RANK.NOT-A-MANAGER", true));
                    }

                    this.reloadAllConfigs();
                }
            } else if (args[0].equalsIgnoreCase("sethome")) {
                if (isClanManager(sender.getName())) {
                    Player p = (Player)sender;
                    int x = p.getLocation().getFloorX();
                    int y = p.getLocation().getFloorY();
                    int z = p.getLocation().getFloorZ();
                    clans.set("clans." + getPlayerClan(sender.getName()) + ".home.x", x);
                    clans.set("clans." + getPlayerClan(sender.getName()) + ".home.y", y);
                    clans.set("clans." + getPlayerClan(sender.getName()) + ".home.z", z);
                    sender.sendMessage(this.getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-HOME-SET", true));
                    clans.save();
                    clans.reload();
                } else {
                    sender.sendMessage(this.getTranslation("CLAN-MANAGEMENT", "CLAN.RANK.NOT-A-MANAGER", true));
                }
            } else if (args[0].equalsIgnoreCase("home")) {
                if (isClanHaveHome(getPlayerClan(sender.getName()))) {
                    clans.reload();
                    double x = (double)clans.getInt("clans." + getPlayerClan(sender.getName()) + ".home.x");
                    double y = (double)clans.getInt("clans." + getPlayerClan(sender.getName()) + ".home.y");
                    double z = (double)clans.getInt("clans." + getPlayerClan(sender.getName()) + ".home.z");
                    Player p = (Player)sender;
                    Location location = new Location(x, y, z);
                    p.teleport(location);
                    sender.sendMessage(this.getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-HOME-TELEPORT", true));
                }
            } else if (args[0].equalsIgnoreCase("kick")) {
                if (args.length == 1) {
                    sender.sendMessage(this.getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-KICK-USE", true));
                } else {
                    clans.reload();
                    if (isClanManager(sender.getName())) {
                        if (getClanMembers(getPlayerClan(sender.getName())) != null) {
                            if (args[1].contains(sender.getName())) {
                                sender.sendMessage(this.getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-KICK-ERROR", true).replaceAll("<player>", args[1]));
                            } else if (getClanMembers(getPlayerClan(sender.getName())) != null) {
                                if (this.getServer().getOnlinePlayers().containsValue(args[1])) {
                                    this.getServer().getPlayerExact(args[1]).sendMessage(this.getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-KICK-TARGET", true).replaceAll("<clan>", getPlayerClan(args[1])));
                                }

                                removePlayerFromClan(args[1]);
                                getClanMembers(getPlayerClan(sender.getName())).remove(args[1]);
                                this.reloadAllConfigs();
                                sender.sendMessage(this.getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-KICK", true).replaceAll("<player>", args[1]));
                            } else {
                                sender.sendMessage(this.getTranslation("CLAN-MANAGEMENT", "CLAN.NOMEMBERS", true).replaceAll("<player>", args[1]));
                            }
                        }
                    } else {
                        sender.sendMessage(this.getTranslation("CLAN-MANAGEMENT", "CLAN.RANK.NOT-A-MANAGER", true));
                    }
                }
            } else if (args[0].equalsIgnoreCase("ban")) {
                if (args.length == 1) {
                    sender.sendMessage(this.getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-BAN-USE", true));
                } else {
                    clans.reload();
                    if (isClanManager(sender.getName())) {
                        if (getClanBannedMembers(getPlayerClan(sender.getName())) != null) {
                            if (!getClanBannedMembers(getPlayerClan(sender.getName())).contains(args[1])) {
                                if (args[1].contains(sender.getName())) {
                                    sender.sendMessage(this.getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-BAN-ERROR", true).replaceAll("<player>", args[1]));
                                } else {
                                    if (this.getServer().getOnlinePlayers().containsKey(args[1])) {
                                        this.getServer().getPlayerExact(args[1]).sendMessage(this.getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-KICK-TARGET", true).replaceAll("<clan>", getPlayerClan(args[1])));
                                    }

                                    sender.sendMessage(this.getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-BAN", true).replaceAll("<player>", args[1]));
                                    removePlayerFromClan(args[1]);
                                    getClanBannedMembers(getPlayerClan(sender.getName())).add(args[1]);
                                    this.reloadAllConfigs();
                                }
                            } else {
                                getClanBannedMembers(getPlayerClan(sender.getName())).remove(args[1]);
                                sender.sendMessage(this.getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-BAN-UNBANNED", true).replaceAll("<player>", args[1]));
                                this.reloadAllConfigs();
                            }
                        } else {
                            List<String> jokey = new ArrayList();
                            jokey.add(args[1]);
                            clans.set("clans." + getPlayerClan(sender.getName()) + ".banned", jokey);
                            removePlayerFromClan(args[1]);
                            this.getServer().getPlayerExact(args[1]).sendMessage(this.getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-KICK-TARGET", true).replaceAll("<clan>", getPlayerClan(args[1])));
                            sender.sendMessage(this.getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-BAN", true).replaceAll("<player>", args[1]));
                            this.reloadAllConfigs();
                        }
                    } else {
                        sender.sendMessage(this.getTranslation("CLAN-MANAGEMENT", "CLAN.RANK.NOT-A-MANAGER", true));
                    }
                }
            } else if (args[0].equalsIgnoreCase("friendly")) {
                if (isClanManager(sender.getName())) {
                    if (isFriendly(getPlayerClan(sender.getName()))) {
                        clans.set("clans." + getPlayerClan(sender.getName()) + ".friendly", false);
                        sender.sendMessage(this.getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-FRIENDLY-OFF", true));
                        this.reloadAllConfigs();
                    } else {
                        clans.set("clans." + getPlayerClan(sender.getName()) + ".friendly", true);
                        sender.sendMessage(this.getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-FRIENDLY-ON", true));
                        this.reloadAllConfigs();
                    }
                } else {
                    sender.sendMessage(this.getTranslation("CLAN-MANAGEMENT", "CLAN.RANK.NOT-A-MANAGER", true));
                }
            }
        } else if (cmdLabel.equalsIgnoreCase("xclans") && sender.isOp()) {
            if (args.length == 0) {
                sender.sendMessage(this.getTranslation("CLAN-MANAGEMENT", "ADMIN-MSG.RELOAD-MESSAGE", true));
            } else if (args[0].equalsIgnoreCase("reload")) {
                this.reloadAllConfigs();
                sender.sendMessage(this.getTranslation("CLAN-MANAGEMENT", "ADMIN-MSG.RELOAD-SUCCESS", true));
            }
        }

        return true;
    }
}
