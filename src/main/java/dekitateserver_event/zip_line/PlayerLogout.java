package dekitateserver_event.zip_line;

import org.bukkit.event.Listener;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerLogout implements Listener {
    private JavaPlugin plugin;
    private Entity entity;
    private Player passenger;
    public PlayerLogout(JavaPlugin plugin, Entity entity, Player passenger) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
        this.entity = entity;
        this.passenger = passenger;
    }

    @EventHandler
    public void LogoutCheck(PlayerQuitEvent e){
        Player player = e.getPlayer();
        if (player == passenger) {
            entity.remove();
        }
        PlayerQuitEvent.getHandlerList().unregister(plugin);
    }

}
