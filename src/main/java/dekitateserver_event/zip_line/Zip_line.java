package dekitateserver_event.zip_line;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
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
            double sx,sy,sz, ex,ey,ez;
            try {
                sx = Double.parseDouble(args[2]);
                sy = Double.parseDouble(args[3]);
                sz = Double.parseDouble(args[4]);

                ex = Double.parseDouble(args[5]);
                ey = Double.parseDouble(args[6]);
                ez = Double.parseDouble(args[7]);
            } catch (ArrayIndexOutOfBoundsException e){
                sender.sendMessage(ChatColor.GRAY + "[ZL]" + ChatColor.DARK_RED + "引数が足りません");
                return true;
            } catch (NumberFormatException n){
                sender.sendMessage(ChatColor.GRAY + "[ZL]" + ChatColor.DARK_RED + "座標を正しく入力してください");
                return true;
            }

            double dx = Math.abs(ex - sx);
            double dy = Math.abs(ey - sy);
            double dz = Math.abs(ez - sz);
            double max = dx;

            if (dy > max) max = dy;
            if (dz > max) max = dz;

            int counter = (int) max * 3;

            if (counter > 600){
                sender.sendMessage(ChatColor.GRAY + "[ZL]" + ChatColor.DARK_RED + "始点と終点の差が大きすぎます");
                return true;
            }
            if (counter < 1){
                sender.sendMessage(ChatColor.GRAY + "[ZL]" + ChatColor.DARK_RED + "始点と終点の差が小さすぎます");
                return true;
            }

            double mdx = (ex - sx) / counter;
            double mdy = (ey - sy) / counter;
            double mdz = (ez - sz) / counter;

            Location playerLoc;
            Entity firstPassenger;
            List<Entity> entity;
            ArmorStand as;

            try {
            entity = selector(sender, args[1]);
                if (entity == null){return true;}
            List<Location> passLoc = entityLoc(entity);
                if (passLoc == null){return true;}

            playerLoc = passLoc.get(0);
            firstPassenger = entity.get(0);

            } catch (IndexOutOfBoundsException e) {
                sender.sendMessage(ChatColor.GRAY + "[ZL]" + ChatColor.DARK_RED + "Entity not found");
                return true;
            }


            if (args[0].equalsIgnoreCase("set")) {
                as = setAs(sender, playerLoc, sx, sy, sz, firstPassenger);
                Entity passenger = null;
                Player player = null;

                if (as == null){return true;}
                as.setVisible(false);
                for (int i = 0; i < entity.size(); i++) {
                    passenger = entity.get(i);
                    as.addPassenger(passenger);
            }
                if (passenger instanceof Player) {
                    player = (Player) passenger;
                    new PlayerLogout(this, as, player);
                }
                new ZipLineRun(this, counter, mdx, mdy, mdz, as, player).runTaskTimer(this, 0, 1);
                return true;

            } else if (args[0].equalsIgnoreCase("debug")) {
                    as = setAs(sender, playerLoc, sx, sy, sz, firstPassenger);
                    Entity passenger = null;
                    Player player = null;

                    if (as == null){return true;}
                    for (int i = 0; i < entity.size(); i++) {
                        passenger = entity.get(i);
                        as.addPassenger(passenger);
                    }
                    if (passenger instanceof Player) {
                        player = (Player) passenger;
                        new PlayerLogout(this, as, player);
                    }
                    new ZipLineRun(this, counter, mdx, mdy, mdz, as, player).runTaskTimer(this, 0, 1);
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

    private ArmorStand setAs(CommandSender sender, Location playerLoc, double sx, double sy, double sz, Entity firstPassenger){
        try {
            Location loc = new Location(playerLoc.getWorld(), sx, sy, sz);
            ArmorStand as = firstPassenger.getWorld().spawn(loc, ArmorStand.class);
            as.setInvulnerable(true);
            as.setSilent(true);
            return as;
        } catch (NullPointerException e) {
            sender.sendMessage(ChatColor.GRAY + "[ZL]" + ChatColor.DARK_RED + "Entity not found");
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
        PlayerQuitEvent.getHandlerList().unregister(this);
    }
}
