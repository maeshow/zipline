package dekitateserver_event.zip_line;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class ZipLineScheduler extends BukkitRunnable{

    private final JavaPlugin plugin;

    private int counter;
    private double mdx,mdy,mdz;

    private Entity entity;

    public ZipLineScheduler(JavaPlugin plugin, int counter, Double mdx, Double mdy, Double mdz, Entity entity) {
        this.plugin = plugin;
        if (counter < 1) {
            throw new IllegalArgumentException("始点と終点が近すぎます");
        } else {
            this.counter = counter;

            this.mdx = mdx;
            this.mdy = mdy;
            this.mdz = mdz;

            this.entity = entity;
        }
    }

    @Override
    public void run(){
        if (counter > 0) {
            entity.setVelocity(new Vector(mdx, mdy, mdz));
            counter--;
        }else{
            entity.setVelocity(new Vector(0f, 0f, 0f));
            entity.remove();
            this.cancel();
        }
    }
}
