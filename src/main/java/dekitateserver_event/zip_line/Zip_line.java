package dekitateserver_event.zip_line;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


import java.util.List;
import java.util.ArrayList;

import static org.bukkit.Bukkit.selectEntities;

public final class Zip_line extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("プラグインが有効になりました");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
        if (args.length != 0) {
            double sx = Double.parseDouble(args[2]);
            double sy = Double.parseDouble(args[3]);
            double sz = Double.parseDouble(args[4]);

            double ex = Double.parseDouble(args[5]);
            double ey = Double.parseDouble(args[6]);
            double ez = Double.parseDouble(args[7]);

            double dx = Math.abs(ex - sx);
            double dy = Math.abs(ey - sy);
            double dz = Math.abs(ez - sz);
            double max = dx;

            if (dy > max) max = dy;
            if (dz > max) max = dz;

            int counter = (int) max * 3;

            double mdx = (ex - sx) / counter;
            double mdy = (ey - sy) / counter;
            double mdz = (ez - sz) / counter;

            List<Entity> entity = selector(sender, args[1]);
            List<Location> passLoc = entityLoc(entity);

            Location playerLoc = passLoc.get(0);
            Entity firstPassenger = entity.get(0);

            if (args[0].equalsIgnoreCase("set")) {
                Location loc = new Location(playerLoc.getWorld(), sx, sy, sz);
                ArmorStand as = firstPassenger.getWorld().spawn(loc, ArmorStand.class);
                as.setInvulnerable(true);
                as.setSilent(true);
                as.setVisible(false);
                Entity passenger = null;

                for (int i = 0; i < entity.size(); i++) {
                    passenger = entity.get(i);
                    as.addPassenger(passenger);
                }
                if (passenger instanceof Player) {
                    Player player = (Player) passenger;
                    new PlayerLogout(this, as, player);
                }
                new ZipLineRun(this, counter, mdx, mdy, mdz, as).runTaskTimer(this, 0, 1);
                return true;

            } else if (args[0].equalsIgnoreCase("debug")) {
                Location loc = new Location(playerLoc.getWorld(), sx, sy, sz);
                ArmorStand as = firstPassenger.getWorld().spawn(loc, ArmorStand.class);
                as.setInvulnerable(true);
                as.setSilent(true);
                Entity passenger = null;

                for (int i = 0; i < entity.size(); i++) {
                    passenger = entity.get(i);
                    as.addPassenger(passenger);
                }
                if (passenger instanceof Player) {
                    Player player = (Player) passenger;
                    new PlayerLogout(this, as, player);
                }
                new ZipLineRun(this, counter, mdx, mdy, mdz, as).runTaskTimer(this, 0, 1);
                return true;

            } else if (args[0].equalsIgnoreCase("help")) {
                help(sender);
                return true;
            }
        }
        help(sender);
        return true;
    }

    private List<Entity> selector(CommandSender sender, String selector) {
        try {
            return selectEntities(sender, selector);
        } catch (IllegalArgumentException e) {
            sender.sendMessage("Entity not found");
            return null;
        }
    }

    private List<Location> entityLoc(List<Entity> entity){
        try {
            List<Location> passLoc = new ArrayList<>();
            entity.forEach(passenger ->
                    passLoc.add(passenger.getLocation())
            );
            return passLoc;
        } catch (IllegalArgumentException e){
            return null;
        }
    }

    private void help(CommandSender sender){
        sender.sendMessage(ChatColor.GREEN +"---------------ZipLine help---------------");
        sender.sendMessage(ChatColor.GREEN +"/zipline set [セレクタ] [始点座標] [終点座標] (乗り物:アーマスタンド, 状態:見えない)");
        sender.sendMessage(ChatColor.GREEN +"/zipline debug [セレクタ] [始点座標] [終点座標] (乗り物:アーマスタンド, 状態:見える)");
        sender.sendMessage(ChatColor.GREEN +"/zipline help (helpを表示)");
        sender.sendMessage(ChatColor.GREEN +"※始点と終点の間にブロックがあると埋まるので注意");
        sender.sendMessage(ChatColor.GREEN +"------------------------------------------");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("プラグインが無効になりました");
    }
}
