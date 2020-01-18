package dekitateserver_event.zip_line;

import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class ZipLineRun extends BukkitRunnable {

    private final JavaPlugin plugin;

    private int counter;
    private Entity entity;

    private Vector v;

    public ZipLineRun(JavaPlugin plugin, int counter, Double mdx, Double mdy, Double mdz, Entity entity) {
        this.plugin = plugin;
        if (counter < 1) {
            throw new IllegalArgumentException("始点と終点が近すぎます");
        } else {
            this.counter = counter;

            this.entity = entity;

            v = new Vector(mdx, mdy, mdz);
        }
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
            PlayerQuitEvent.getHandlerList().unregister(plugin);
        }
    }
}
