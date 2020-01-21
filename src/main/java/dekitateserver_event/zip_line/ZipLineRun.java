package dekitateserver_event.zip_line;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import static org.bukkit.Bukkit.getLogger;

public class ZipLineRun extends BukkitRunnable {

    private final JavaPlugin plugin;

    private int counter;
    private Entity entity;
    private Player player;

    private Vector v;

    public ZipLineRun(JavaPlugin plugin, int counter, Double mdx, Double mdy, Double mdz, Entity entity, Player passenger) {
        this.plugin = plugin;
        this.counter = counter;
        this.entity = entity;
        this.player = passenger;

        v = new Vector(mdx, mdy, mdz);
    }

    @Override
    public void run() {
        if (counter > 0) {
            entity.setVelocity(v);
            counter--;
        } else {
            entity.setVelocity(v.zero());
            entity.remove();
            this.cancel();
        }
    }
}
