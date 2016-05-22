package ru.undefined1.xClans;

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
import java.util.regex.Pattern;

/**
 * Created by ryzhe on 20.05.2016.
 */
public class xClans extends PluginBase {

    public static xClans instance;

    public static xClans getPlugin() {
        return instance;
    }

    private File cfg;
    public static Config config;

    private File clanData;
    public static Config clans;

    private File playerData;
    public static Config players;

    //public void getClanHome(String clan) {
    //    int x = clans.getInt("clans." + clan + ".home.x");
    //    int y = clans.getInt("clans." + clan + ".home.y");
    //    int z = clans.getInt("clans." + clan + ".home.z");
    //    return String.valueOf(x) + String.valueOf(y) + String.valueOf(z);
    //}

    public static Boolean isClanMember(String player, String clan) {
        List<String> clanMembers = clans.getStringList("clans." + clan + ".members");
        if (clanMembers != null) {
            if (clanMembers.contains(player)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    public String getTranslation(String section, String message, Boolean prefix) {
        config.reload();

        String pref = config.getString("prefix");
        String  output = config.getString("TRANSLATION." + config.getString("language") + "." + section + "." + message);
            if (prefix) {
                return (TextFormat.colorize(pref + " " + output));
            } else {
                return (TextFormat.colorize(output));
            }
    }

    public static void setClanNameTag(Player player, String clan) {
        clans.reload();
        if(getPlayerClan(player.getName()) != null) {
            if (clans.getString("clans." + clan + ".tag") != null || clans.getString("clans." + clan + ".tag") != "") {
                String tag = clans.getString("clans." + clan + ".tag");
                player.setNameTag(TextFormat.colorize("&f[" + tag + "&f]" + " " + player.getDisplayName()));
                player.setNameTagVisible(true);
            }
        }
    }

    public static String getClanName(String clan) {
        if(clans.getString("clans." + clan + ".name" ) != "" || clans.getString("clans." + clan + ".name" ) != null) {
            return clans.getString("clans." + clan + ".name");
        } else {
            return null;
        }
    }

    public static void renameClan(String clanName, String newName) {
        if(clans.getString("clans." + clanName ) != "" || clans.getString("clans." + clanName) != null) {
            clans.set("clans." + clanName + ".name", newName);
        }
    }

    public static void setClanTag(String clan, String newClanTag) {
        if(clans.getString("clans." + clan + ".tag") != null || clans.getString("clans." + clan + ".tag") != "") {
            clans.set("clans." + clan + ".tag", newClanTag);
        }
    }

    public static void addClanMember(Player player, String clan) {
        if(clans.getString("clans." + clan) != null || clans.getString("clans." + clan) != "") {
            List<String> members = clans.getList("clans." + clan);
            if(!members.contains(player.getName())) {
                members.add(player.getName());
            }
        }
    }

    public static void addClanMember(String player, String clan) {
        if(clans.getString("clans." + clan) != null || clans.getString("clans." + clan) != "") {
            List<String> members = clans.getList("clans." + clan);
            if(!members.contains(player)) {
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
            List<String> owners = new ArrayList<String>();
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
        if(players.getString(player.getName() + ".clan") != null || players.getString(player.getName() + ".clan") != "") {
            players.set(player.getName() + ".clan", newClan);
        }
    }

    public static void setPlayerClan(String player, String newClan) {
        if(players.getString(player + ".clan") != null || players.getString(player + ".clan") != "") {
            players.set(player + ".clan", newClan);
        }
    }

    public static void removePlayerFromClan(String player) {
        if(players.getString(player + ".clan") != null || players.getString(player + ".clan") != "") {
            players.set(player, null);
        }
    }

    public static void removePlayerFromClan(Player player) {
        if(players.getString(player.getName() + ".clan") != null || players.getString(player.getName() + ".clan") != "") {
            players.set(player.getName(), null);
        }
    }

    public static List<String> getClanManagers(String clan) {
        List<String> managers = clans.getList("clans." + clan + ".managers");
        return managers;
    }

    public static String getClanTag(String clan) {
        if(clans.getString("clans." + clan + ".tag") != "" || clans.getString("clans." + clan + ".tag") != null ) {
            return TextFormat.colorize(clans.getString("clans." + clan + ".tag"));
        } else {
            return TextFormat.colorize("&7[*]");
        }
    }

    public static String getPlayerClan(Player player) {
        if(players.getString(player.getName() + ".clan") != "") {
            return (players.getString(player.getName() + ".clan"));
        } else {
            return null;
        }

    }

    public static Boolean isFriendly(String clan) {
        if(clans.getString("clans." + clan + ".name") != "" || clans.getString("clans." + clan + ".name") !=  null) {
            if (clans.getBoolean("clans." + clan + ".friendly")) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static String getPlayerClan(String player) {
        if(players.getString(player + ".clan") != "") {
            return (players.getString(player + ".clan"));
        } else {
            return null;
        }
    }

    public static Boolean isClanManager(String player) {
        if(players.getString(player + ".clan") != "" || players.getString(player + ".clan") != null) {
            List<String> owner = clans.getStringList("clans." + getPlayerClan(player) + ".managers");
            if(owner.contains(player)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static List<String> getClanModerators(String clan) {
            List<String> owner = clans.getStringList("clans." + clan + ".moderators");
            if(owner != null) {
                return owner;
            } else {
                return null;
            }
    }

    public static void removeClanManager(Player player, String clan) {
        if(players.getString(player + ".clan") != "" || players.getString(player + ".clan") != null) {
            List<String> owner = clans.getList("clans." + getPlayerClan(player) + ".managers");
            if(owner.contains(player.getName())) {
                if (owner != null) {
                    owner.remove(player.getName());
                }
            }
        }
    }
    public static void removeClanManager(String player, String clan) {
        if(players.getString(player + ".clan") != "" || players.getString(player + ".clan") != null) {
            List<String> owner = clans.getList("clans." + getPlayerClan(player) + ".managers");
            if(owner.contains(player)) {
                if (owner != null) {
                    owner.remove(player);
                }
            }
        }
    }

    public static void removeClanModerator(Player player, String clan) {
        if(players.getString(player + ".clan") != "" || players.getString(player + ".clan") != null) {
            List<String> owner = clans.getList("clans." + getPlayerClan(player) + ".moderators");
            if(owner.contains(player.getName())) {
                if (owner != null) {
                    owner.remove(player.getName());
                }
            }
        }
    }

    public static void removeClanModerator(String player, String clan) {
        if(players.getString(player + ".clan") != "" || players.getString(player + ".clan") != null) {
            List<String> owner = clans.getList("clans." + getPlayerClan(player) + ".moderators");
            if(owner.contains(player)) {
                if (owner != null) {
                    owner.remove(player);
                }
            }
        }
    }


    public static void addClanManager(Player player, String clan) {
        if(players.getString(player + ".clan") != "" || players.getString(player + ".clan") != null) {
            List<String> owner = clans.getList("clans." + getPlayerClan(player) + ".managers");
            if(!owner.contains(player.getName())) {
                if (owner != null) {
                    owner.add(player.getName());
                } else {
                    List<String> moderator = new ArrayList<String>();
                    moderator.add(player.getName());
                    String clanName = getPlayerClan(player);
                    config.set("clans." + clanName + ".managers", moderator);
                }
            }
        }
    }

    public static void addClanManager(String player, String clan) {
        if(players.getString(player + ".clan") != "" || players.getString(player + ".clan") != null) {
            List<String> owner = clans.getList("clans." + getPlayerClan(player) + ".managers");
            if(!owner.contains(player)) {
                if (owner != null) {
                    owner.add(player);
                } else {
                    List<String> moderator = new ArrayList<String>();
                    moderator.add(player);
                    String clanName = getPlayerClan(player);
                    config.set("clans." + clanName + ".managers", moderator);
                }
            }
        }
    }

    public static Boolean isClanHaveHome(String clan) {
        if(clans.getString("clans." + clan + ".home") != null) {
            return true;
        } else {
            return false;
        }
    }

    public static String getClanHomeX(String clan) {
        if(clans.getString("clans." + clan + ".home") != null) {
            return clans.getString("clans." + clan + ".home.x");
        } else {
            return null;
        }
    }

    public static String getClanHomeY(String clan) {
        if(clans.getString("clans." + clan + ".home") != null) {
            return clans.getString("clans." + clan + ".home.y");
        } else {
            return null;
        }
    }

    public static String getClanHomeZ(String clan) {
        if(clans.getString("clans." + clan + ".home") != null) {
            return clans.getString("clans." + clan + ".home.z");
        } else {
            return null;
        }
    }

    public static List<String> getClanBannedMembers(String clan) {
        List<String> owner = clans.getStringList("clans." + clan + ".banned");
        if(owner != null) {
            return owner;
        } else {
            return null;
        }
    }

    public static void addClanModerator(String player, String clan) {
        if(players.getString(player + ".clan") != "" || players.getString(player + ".clan") != null) {
            List<String> owner = clans.getList("clans." + getPlayerClan(player) + ".moderators");
            if(!owner.contains(player)) {
                if (owner != null) {
                    owner.add(player);
                } else {
                    List<String> moderator = new ArrayList<String>();
                    moderator.add(player);
                    String clanName = getPlayerClan(player);
                    config.set("clans." + clanName + ".moderators", moderator);
                }
            }
        }
    }

    public static void addClanModerator(Player player, String clan) {
        if(players.getString(player + ".clan") != "" || players.getString(player + ".clan") != null) {
            List<String> owner = clans.getList("clans." + getPlayerClan(player) + ".moderators");
            if(!owner.contains(player.getName())) {
                if (owner != null) {
                    owner.add(player.getName());
                } else {
                    List<String> moderator = new ArrayList<String>();
                    moderator.add(player.getName());
                    String clanName = getPlayerClan(player);
                    config.set("clans." + clanName + ".moderators", moderator);
                }
            }
        }
    }

    public static Boolean isClanModerator(String player) {
        if(players.getString(player + ".clan") != "" || players.getString(player + ".clan") != null) {
            List<String> owner = clans.getStringList("clans." + getPlayerClan(player) + ".moderators");
            if(owner.contains(player)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public String getMessage(String text, Boolean prefix) {
        if(!prefix) {
            return TextFormat.colorize(config.getString(text));
        } else {
            String prefixs = TextFormat.colorize("&e[&bxClans&e]");
            return TextFormat.colorize(prefixs + " " + config.getString(text));
        }
    }

    public void sendConsoleMessage(String text, Boolean prefix) {
        if(!prefix) {
            getServer().getConsoleSender().sendMessage(TextFormat.colorize(text));
        } else {
            String prefixs = TextFormat.colorize("&e[&bxClans&e]");
            getServer().getConsoleSender().sendMessage(prefixs + " " + TextFormat.colorize(text));
        }
    }

    public void startScheduler() {

        int secManager = config.getInt("cooldown");
        String compiler = (String.valueOf(secManager) + 0);
        int sec = Integer.parseInt(compiler);

        getServer().getScheduler()
                .scheduleRepeatingTask(new Runnable() {

                    public void run() {


                        for(Player player : getServer().getOnlinePlayers().values()) {
                            setClanNameTag(player, getPlayerClan(player));
                        }
                        if(config.getBoolean("debug")) {
                            sendConsoleMessage((getTranslation("DEBUG", "CLAN-TAG-UPDATED",false)), true);
                        }

                    }

                }, sec);
    }

    public void onEnable() {
        this.getServer().getScheduler().cancelAllTasks();
        // cn.nukkit.lang.BaseLang@238b4697
        // cn.nukkit.lang.BaseLang@2b76ff4e


        this.getDataFolder().mkdirs();
        this.saveDefaultConfig();

        this.getServer().getPluginManager().registerEvents(new xListener(this), this);

        this.saveResource("clans.yml", false);
        this.saveResource("players.yml", false);
        this.saveResource("config.yml", false);
        this.saveResource("THANKS.txt", true);

        this.playerData = new File(this.getDataFolder(), "players.yml");
        players = new Config(this.playerData, 2);

        this.clanData = new File(this.getDataFolder(), "clans.yml");
        clans = new Config(this.clanData, 2);

        this.cfg = new File(this.getDataFolder(), "config.yml");
        config = new Config(this.cfg, 2);

        ConfigSection section = this.clans.getSections("clans");

        if(getServer().getLanguage().getLang().equalsIgnoreCase("rus")) {
            config.set("language", "russian");
            reloadAllConfigs();
        } else if(getServer().getLanguage().getLang().equalsIgnoreCase("eng")) {
            config.set("language", "english");
            reloadAllConfigs();
        }

        sendConsoleMessage(getTranslation("DEBUG","LOADED2",false), true);
        sendConsoleMessage(getTranslation("DEBUG","LOADED",false).replaceAll("<count>", String.valueOf(section.size())), true);

        startScheduler();
    }



    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
        if (cmdLabel.equalsIgnoreCase("clan")) {
            if(args.length == 0) {

                if(getPlayerClan(sender.getName()) == null) {
                        sender.sendMessage(getTranslation("CLAN-MANAGEMENT", "NO-CLAN.DONT-HAVE-CLAN",true));
                    sender.sendMessage(getTranslation("CLAN-MANAGEMENT", "NO-CLAN.NO-CLAN-CREATE",true));
                } else {
                    sender.sendMessage(getTranslation("CLAN-MANAGEMENT", "CLAN.MYCLAN",true).replaceAll("<name>", getClanName(getPlayerClan(sender.getName()))));

                    if(isClanManager(sender.getName())) {
                        sender.sendMessage(getTranslation("CLAN-MANAGEMENT", "CLAN.RANK.MANAGER",true));
                    } else if(isClanMember(sender.getName(), getPlayerClan(sender.getName()))) {
                        sender.sendMessage(getTranslation("CLAN-MANAGEMENT", "CLAN.RANK.MEMBER",true));
                    }
                    if(isFriendly(getPlayerClan(sender.getName()))) {
                        sender.sendMessage(getTranslation("CLAN-MANAGEMENT", "CLAN.FRIENDLY", true));
                    } else {
                        sender.sendMessage(getTranslation("CLAN-MANAGEMENT", "CLAN.NOT-FRIENDLY",true));
                    }

    if (getClanMembers(getPlayerClan(sender.getName())) != null) {
        sender.sendMessage("\n");
        sender.sendMessage(getTranslation("CLAN-MANAGEMENT", "CLAN.MEMBERS",true));
        sender.sendMessage(getClanMembers(getPlayerClan(sender.getName())).toString());

        sender.sendMessage("\n");
    } else {
        sender.sendMessage(getTranslation("CLAN-MANAGEMENT", "CLAN.NOMEMBERS",true));
        sender.sendMessage("\n");
    }

    if (getClanManagers(getPlayerClan(sender.getName())) != null) {

        sender.sendMessage(getTranslation("CLAN-MANAGEMENT", "CLAN.MANAGERS",true));
        sender.sendMessage(getClanManagers(getPlayerClan(sender.getName())).toString());
        sender.sendMessage("\n");
    }

                }

            } else if (args[0].equalsIgnoreCase("create")) {
                if(args.length == 1) {
                    sender.sendMessage(getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-CREATE-USE",true));
                } else {
                    ConfigSection section = this.clans.getSections("clans");
                    Set<String> clansNames = section.getKeys(false);
                    if (!clansNames.contains(args[1].replaceAll("[^A-Za-z]+", ""))) {
                        if (players.getString(sender.getName() + ".clan") == "") {
                            this.addClan(args[1].replaceAll("[^A-Za-z]+", ""), args[1].replaceAll("[^A-Za-z]+", ""), sender.getName(), false);
                            players.save();
                            clans.save();
                            players.reload();
                            clans.reload();
                            sender.sendMessage(getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-CREATED",true).replaceAll("<clan>", args[1].replaceAll("[^A-Za-z]+", "")));

                        } else {
                            sender.sendMessage(getTranslation("CLAN-MANAGEMENT", "CLAN.ALREADY-MEMBER",true));
                        }
                    } else {
                        sender.sendMessage(getTranslation("CLAN-MANAGEMENT", "CLAN.ALREADY-CREATED",true));
                    }
                }

            } else if (args[0].equalsIgnoreCase("join")) {
                if(args.length == 1) {
                    sender.sendMessage(getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-JOIN-USE",true));
                } else {
                    if(getPlayerClan(sender.getName()) == null) {
                        ConfigSection section = this.clans.getSections("clans");
                        Set<String> clansNames = section.getKeys(false);

                        if (clansNames.contains(args[1])) {
                            List<String> claners = new ArrayList<String>();
                            claners.add(sender.getName());
                            clans.set("clans." + args[1] + ".members", claners);
                            players.set(sender.getName() + ".clan", args[1]);
                            reloadAllConfigs();
                            sender.sendMessage(getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-JOIN",true).replaceAll("<clan>",args[1]));
                        } else {
                            sender.sendMessage(getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-NOT-FOUND",true));
                        }

                    } else {
                        sender.sendMessage(getTranslation("CLAN-MANAGEMENT", "CLAN.ALREADY-MEMBER",true));
                    }
                    reloadAllConfigs();
                }
            } else if (args[0].equalsIgnoreCase("leave")) {
                String myClan = getPlayerClan(sender.getName());
                    if(getPlayerClan(sender.getName()) == "") {
                        sender.sendMessage(getTranslation("CLAN-MANAGEMENT", "NO-CLAN.NOT-IN-CLAN",true));
                    } else {

                        if(getClanManagers(myClan).contains(sender.getName())) {

                            if (getClanManagers(myClan).size() == 1) {
                                sender.sendMessage(getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-CANT-LEAVE",true));
                                sender.sendMessage(getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-CANT-LEAVE2",true));
                            } else if(getClanManagers(myClan).size() <= 2) {
                                sender.sendMessage(myClan);
                                List string = clans.getList("clans." + myClan + ".managers");
                                string.remove(sender.getName());
                                removePlayerFromClan(sender.getName());
                                sender.sendMessage(getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-LEAVE",true));
                            }

                        } else if(getClanMembers(myClan).contains(sender.getName())) {

                            getClanMembers(myClan).remove(sender.getName());
                            removePlayerFromClan(sender.getName());
                            sender.sendMessage(getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-LEAVE",true));

                        }

                        reloadAllConfigs();
                    }


            } else if (args[0].equalsIgnoreCase("disband")) {

                    if(getPlayerClan(sender.getName()) == "") {
                        sender.sendMessage(getTranslation("CLAN-MANAGEMENT", "NO-CLAN.NOT-IN-CLAN",true));
                    } else {
                        if (isClanManager(sender.getName())) {
                            String myClan = getPlayerClan(sender.getName());
                            if (getClanManagers(myClan).size() == 1) {
                                String clanName = getPlayerClan(sender.getName());
                                try {
                                List<String> getMembers = getClanMembers(clanName);
                                for (String toRemove : getMembers) {
                                    removePlayerFromClan(sender.getName());
                                }

                                } catch (Exception e) {

                                }

                                List<String> getManagers = getClanManagers(clanName);
                                for (String toRemove : getManagers) {
                                    removePlayerFromClan(sender.getName());
                                }

                                clans.set("clans." + clanName, null);
                                clans.remove("clans." + clanName);
                                sender.sendMessage(getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-DISBAND",true));
                            } else {
                                sender.sendMessage(getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-CANT-DISBAND",true));
                                sender.sendMessage(getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-CANT-DISBAND2",true));
                            }

                        } else {
                            sender.sendMessage(getTranslation("CLAN-MANAGEMENT", "CLAN.RANK.NOT-A-MANAGER",true));
                        }
                        reloadAllConfigs();
                    }

            } else if (args[0].equalsIgnoreCase("tag")) {
                if(args.length == 1) {
                    sender.sendMessage(getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-TAG-USE",true));
                } else {
                    if(getPlayerClan(sender.getName()) == "") {
                        sender.sendMessage(getTranslation("CLAN-MANAGEMENT", "NO-CLAN.NOT-IN-CLAN",true));
                    } else {
                       if(isClanManager(sender.getName())) {

                           if(!args[1].matches("[^A-Za-z&123456789]+")) {
                               setClanTag(getPlayerClan(sender.getName()), args[1].replaceAll("[^A-Za-z&123456789]+", ""));
                               sender.sendMessage(TextFormat.colorize(getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-TAG",true).replaceAll("<tag>", args[1].replaceAll("[^A-Za-z&123456789]+", ""))));
                           } else {
                               sender.sendMessage(getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-TAG-ERROR",true));
                           }


                       } else {
                           sender.sendMessage(getTranslation("CLAN-MANAGEMENT", "CLAN.RANK.NOT-A-MANAGER",true));
                       }
                    }
                    reloadAllConfigs();
                }
            } else if(args[0].equalsIgnoreCase("sethome")) {
                    if(isClanManager(sender.getName())) {
                        Player p = (Player) sender;
                        int x = p.getLocation().getFloorX();
                        int y = p.getLocation().getFloorY();
                        int z = p.getLocation().getFloorZ();

                        clans.set("clans." + getPlayerClan(sender.getName()) + ".home.x", x);
                        clans.set("clans." + getPlayerClan(sender.getName()) + ".home.y", y);
                        clans.set("clans." + getPlayerClan(sender.getName()) + ".home.z", z);

                        sender.sendMessage(getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-HOME-SET",true));

                        clans.save();
                        clans.reload();

                    } else {
                        sender.sendMessage(getTranslation("CLAN-MANAGEMENT", "CLAN.RANK.NOT-A-MANAGER",true));
                    }

            } else if(args[0].equalsIgnoreCase("home")) {
                if(isClanHaveHome(getPlayerClan(sender.getName()))) {
                    clans.reload();
                    double x = clans.getInt("clans." + getPlayerClan(sender.getName()) + ".home.x");
                    double y = clans.getInt("clans." + getPlayerClan(sender.getName()) + ".home.y");
                    double z = clans.getInt("clans." + getPlayerClan(sender.getName()) + ".home.z");

                    Player p = (Player) sender;
                    Location location = new Location(x, y, z);
                    p.teleport(location);
                    sender.sendMessage(getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-HOME-TELEPORT",true));
                }
            } else if(args[0].equalsIgnoreCase("kick")) {
                if(args.length == 1) {
                    sender.sendMessage(getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-KICK-USE",true));
                } else {
                    clans.reload();
                    if(isClanManager(sender.getName())) {
                        if(getClanMembers(getPlayerClan(sender.getName())) != null) {
                            if(args[1].contains(sender.getName())) {
                                sender.sendMessage(getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-KICK-ERROR", true).replaceAll("<player>", args[1]));
                            } else if(getClanMembers(getPlayerClan(sender.getName())) != null) {
                                if(getServer().getOnlinePlayers().containsValue(args[1])) {
                                    getServer().getPlayerExact(args[1]).sendMessage(getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-KICK-TARGET", true).replaceAll("<clan>", getPlayerClan(args[1])));
                                }
                                removePlayerFromClan(args[1]);
                                getClanMembers(getPlayerClan(sender.getName())).remove(args[1]);
                                reloadAllConfigs();
                                sender.sendMessage(getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-KICK", true).replaceAll("<player>", args[1]));
                            } else {
                                sender.sendMessage(getTranslation("CLAN-MANAGEMENT", "CLAN.NOMEMBERS", true).replaceAll("<player>", args[1]));
                            }
                        } else {

                        }
                    } else {
                        sender.sendMessage(getTranslation("CLAN-MANAGEMENT", "CLAN.RANK.NOT-A-MANAGER",true));
                    }
                }
            } else if(args[0].equalsIgnoreCase("ban")) {
                if(args.length == 1) {
                    sender.sendMessage(getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-BAN-USE",true));
                } else {
                    clans.reload();
                    if(isClanManager(sender.getName())) {
                        if(getClanBannedMembers(getPlayerClan(sender.getName())) != null) {
                            if(!getClanBannedMembers(getPlayerClan(sender.getName())).contains(args[1])) {
                                if(args[1].contains(sender.getName())) {
                                    sender.sendMessage(getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-BAN-ERROR", true).replaceAll("<player>", args[1]));
                                } else {
                                    if(getServer().getOnlinePlayers().containsKey(args[1])) {
                                        getServer().getPlayerExact(args[1]).sendMessage(getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-KICK-TARGET", true).replaceAll("<clan>", getPlayerClan(args[1])));
                                    }
                                    sender.sendMessage(getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-BAN", true).replaceAll("<player>", args[1]));
                                    removePlayerFromClan(args[1]);
                                    getClanBannedMembers(getPlayerClan(sender.getName())).add(args[1]);
                                    reloadAllConfigs();
                                }
                            } else {
                                getClanBannedMembers(getPlayerClan(sender.getName())).remove(args[1]);
                                sender.sendMessage(getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-BAN-UNBANNED",true).replaceAll("<player>", args[1]));
                                reloadAllConfigs();
                            }
                        } else {

                            List<String> jokey = new ArrayList<String>();
                            jokey.add(args[1]);
                            clans.set("clans." + getPlayerClan(sender.getName()) + ".banned", jokey);
                            removePlayerFromClan(args[1]);
                            getServer().getPlayerExact(args[1]).sendMessage(getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-KICK-TARGET",true).replaceAll("<clan>", getPlayerClan(args[1])));
                            sender.sendMessage(getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-BAN",true).replaceAll("<player>", args[1]));
                           reloadAllConfigs();
                        }
                    } else {
                        sender.sendMessage(getTranslation("CLAN-MANAGEMENT", "CLAN.RANK.NOT-A-MANAGER",true));
                    }

                }
            } else if(args[0].equalsIgnoreCase("friendly")) {
                if (isClanManager(sender.getName())) {
                    if (isFriendly(getPlayerClan(sender.getName()))) {
                        clans.set("clans." + getPlayerClan(sender.getName()) + ".friendly", false);
                        sender.sendMessage(getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-FRIENDLY-OFF", true));
                        reloadAllConfigs();
                    } else {
                        clans.set("clans." + getPlayerClan(sender.getName()) + ".friendly", true);
                        sender.sendMessage(getTranslation("CLAN-MANAGEMENT", "COMMANDS.CLAN-FRIENDLY-ON", true));
                        reloadAllConfigs();
                    }
                } else {
                    sender.sendMessage(getTranslation("CLAN-MANAGEMENT", "CLAN.RANK.NOT-A-MANAGER", true));
                }
            }

        } else if(cmdLabel.equalsIgnoreCase("xclans")) {
            if(sender.isOp()) {
                if(args.length == 0) {
                    sender.sendMessage(TextFormat.colorize("&7Use &e/xclan reload &7for all config reloading."));
                } else if(args[0].equalsIgnoreCase("reload")) {
                    reloadAllConfigs();
                    sender.sendMessage(TextFormat.colorize("&aConfigurations reloaded!"));
                }
            }
        }

        return true;
    }

}
