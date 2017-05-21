package pl.xcrafters.xcrtools.listeners;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import pl.xcrafters.xcrtools.ToolsPlugin;
import pl.xcrafters.xcrtools.ToolsPlugin.WeatherType;

public class SocketListener extends Thread {

    ToolsPlugin plugin;

    ServerSocket server;

    public SocketListener(ToolsPlugin plugin) {
        this.plugin = plugin;
        try {
            server = new ServerSocket((Bukkit.getPort() - 29080) + 55001);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                if (server != null && !server.isClosed()) {
                    Socket receive = server.accept();
                    DataInputStream dis = new DataInputStream(receive.getInputStream());
                    String channel = dis.readUTF();
                    plugin.getLogger().log(Level.INFO, channel);
                    if (channel.equals("SetTime")) {
                        final Long time = dis.readLong();
                        Bukkit.getScheduler().runTask(plugin, new Runnable() {
                            public void run() {
                                for (World world : Bukkit.getWorlds()) {
                                    world.setTime(time);
                                }
                            }
                        });
                    } else if (channel.equals("SetWeather")) {
                        final WeatherType type = WeatherType.valueOf(dis.readUTF());
                        Bukkit.getScheduler().runTask(plugin, new Runnable() {
                            public void run() {
                                if (type.equals(WeatherType.SKY)) {
                                    Bukkit.getWorld("world").setThundering(false);
                                    Bukkit.getWorld("world").setStorm(false);
                                } else if (type.equals(WeatherType.RAIN)) {
                                    Bukkit.getWorld("world").setStorm(true);
                                } else if (type.equals(WeatherType.THUNDER)) {
                                    Bukkit.getWorld("world").setThundering(true);
                                }
                            }
                        });
                    } else if (channel.equals("ExecuteCommand")) {
                        final String cmd = dis.readUTF();
                        Bukkit.getScheduler().runTask(plugin, new Runnable() {
                            public void run() {
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
                            }
                        });
                    } else if (channel.equals("ExecuteCommandAsPlayer")) {
                        final Player player = Bukkit.getPlayerExact(dis.readUTF());
                        if (player != null) {
                            final String cmd = dis.readUTF();
                            Bukkit.getScheduler().runTask(plugin, new Runnable() {
                                public void run() {
                                    Bukkit.dispatchCommand(player, cmd);
                                }
                            });
                        }
                    } else if (channel.equals("OpPlayer")) {
                        final String nick = dis.readUTF();
                        final boolean op = dis.readBoolean();
                        Bukkit.getScheduler().runTask(plugin, new Runnable() {
                            public void run() {
                                Bukkit.getOfflinePlayer(nick).setOp(op);
                            }
                        });
                    } else if (channel.equals("SetRespawn")) {
                        String position = dis.readUTF();
                        String[] location = position.split(" ");
                        final Location loc = new Location(Bukkit.getWorld(location[0]), Integer.parseInt(location[1]), Integer.parseInt(location[2]), Integer.parseInt(location[3]));
                        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                            public void run() {
                                loc.getWorld().setSpawnLocation(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
                            }
                        }, 1L);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public synchronized void start() {
        super.start();
    }

    public synchronized void close() throws IOException {
        server.close();
    }

}
