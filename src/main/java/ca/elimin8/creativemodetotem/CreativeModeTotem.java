package ca.elimin8.creativemodetotem;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public final class CreativeModeTotem extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You need to be a player to do this.");
            return true;
        }
        if (label.equalsIgnoreCase("getcreativetotem")){
            Player p = (Player) sender;
            if (p.hasPermission("creativetotem.get")) {
                if (p.getInventory().firstEmpty() == -1) {
                    Location loc = p.getLocation();
                    World world = p.getWorld();
                    world.dropItemNaturally(loc, cmt());
                    p.sendMessage("§aThe Minecraft Legends dropped a gift near you.");
                    return true;
                }
                p.getInventory().addItem(cmt());
                p.sendMessage("§aThe Minecraft Legends gave you a gift.");
                return true;
            }else {p.sendMessage("§4You don't Have permission.");
            return true;}
        }

        return true;
    }

    public ItemStack cmt() {
        ItemStack cmt = new ItemStack(Material.TOTEM_OF_UNDYING);
        ItemMeta meta = cmt.getItemMeta();
        meta.setDisplayName("§6Totem Of Creative Mode");
        List<String> lore = new ArrayList<String>();
        lore.add("");
        lore.add("§7(Right Click) §2§oPuts you in creative for 30 seconds.");
        meta.setLore(lore);
        meta.addEnchant(Enchantment.ARROW_DAMAGE, 3142, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        cmt.setItemMeta(meta);
        return cmt;
    }
    @EventHandler
    public void onClick(PlayerInteractEvent e) throws InterruptedException {
        Player p = (Player) e.getPlayer();
        if (p.hasPermission("creativetotem.use")) {
            if (p.getInventory().getItemInMainHand().getType().equals(Material.TOTEM_OF_UNDYING))
                if (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals("§6Totem Of Creative Mode"))
                    if (e.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasEnchant(Enchantment.ARROW_DAMAGE)) {
                        p.sendMessage("§2You were set to creative mode.");
                        p.setGameMode(GameMode.CREATIVE);
                        p.getInventory().getItemInMainHand().removeEnchantment(Enchantment.ARROW_DAMAGE);
                        p.getInventory().getItemInMainHand().getItemMeta().setDisplayName("§6Broken Totem Of Creative Mode");
                        runnable(p);
                    }
            if (p.getInventory().getItemInOffHand().getType().equals(Material.TOTEM_OF_UNDYING))
                if (p.getInventory().getItemInOffHand().getItemMeta().getDisplayName().equals("§6Totem Of Creative Mode"))
                    if (e.getPlayer().getInventory().getItemInOffHand().getItemMeta().hasEnchant(Enchantment.ARROW_DAMAGE)) {
                        p.setGameMode(GameMode.CREATIVE);
                        p.sendMessage("§2You were set to creative mode.");
                        p.getInventory().getItemInOffHand().removeEnchantment(Enchantment.ARROW_DAMAGE);
                        p.getInventory().getItemInOffHand().getItemMeta().setDisplayName("§6Broken Totem Of Creative Mode");
                        runnable(p);
                        return;
                    }
        } else {p.sendMessage("§4You don't have permission.");
        return;}
    }
    public void runnable(Player p) {
        new BukkitRunnable() {
            @Override
            public void run() {
                p.setGameMode(GameMode.SURVIVAL);
                p.sendMessage("§4You are no longer in creative mode.");
                return;
            }
        }.runTaskLater(this, 600);
        return;
    }
}
