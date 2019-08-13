package me.fanta.secrets.main;

import java.io.File;
import java.util.List;

import me.fanta.secrets.main.listeners.InventoryClickListener;
import me.fanta.secrets.main.listeners.InventoryItem;
import me.fanta.secrets.main.listeners.SignClickListener;
import me.fanta.secrets.main.listeners.SignCreateListener;
import me.fanta.secrets.main.methods.ItemUtil;
import me.fanta.secrets.main.methods.VaultUtils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Secrets extends JavaPlugin implements Listener {
	
	private static Plugin instance;
	
	public void onEnable() {
		instance = this;
		System.out.println(
				"------------------------- Secrets " + getDescription().getVersion() + " -------------------------");
		System.out.println("Secrets " + getDescription().getVersion() + " is loading... [" + Bukkit.getVersion() + "]");
		System.out.println("Plugin by " + getDescription().getAuthors());
		System.out.println("Date Modified: 28 Jan 2019");
		if(hasTTA()) {
			System.out.println("TTA detected!");
		} else {
            System.out.println("TTA not detected!");
        }
		if(hasVault()) {
			System.out.println("Vault detected!");
			VaultUtils.setupEconomy(this);
		}
        else {
            System.out.println("Vault not detected!");
        }
		getServer().getPluginManager().registerEvents(new SignClickListener(this), this);
		getServer().getPluginManager().registerEvents(new SignCreateListener(this), this);
		getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
		getServer().getPluginManager().registerEvents(new InventoryItem(this), this);
		System.out.println("Version " + getDescription().getVersion() + " Was loaded successfully!");
		System.out.println("-----------------------------------------------------------------");
		loadConfig();
		saveConfig();


	}

	public void onDisable() {
		System.out.println("[SECRETS] Plugin disabled");
	}

	public void loadConfig() {
		FileConfiguration cfg = getConfig();

		cfg.options().copyDefaults(true);
	}

	public String Linie1 = getConfig().getString("Sign.line1").replace("&", "§");
	public String Linie3 = getConfig().getString("Sign.line3").replace("&", "§");
	public String Linie4 = getConfig().getString("Sign.line4").replace("&", "§");

	public boolean onCommand(CommandSender sender, Command cmd, String cmdlable, String[] args) {
		if (args.length == 0) {
			Player p = (Player) sender;
			if (cmd.getName().equalsIgnoreCase("secrets")) {
				File Cmd = new File("plugins/Secrets", "Secrets.yml");
				FileConfiguration cmdfile = YamlConfiguration.loadConfiguration(Cmd);
				File Data = new File("plugins/Secrets", "Users.yml");
				FileConfiguration userfile = YamlConfiguration.loadConfiguration(Data);
				String NothingFoundLore = getConfig().getString("Items.ItemNoSecretFound.Lore").replace("&", "§");
				String NothingFound = getConfig().getString("Items.ItemNoSecretFound.Name").replace("&", "§");
				Material NothingFoundItem = Material.getMaterial(getConfig().getString("Items.ItemNoSecretFound.Type"));
				String SecretColor = getConfig().getString("Items.ItemSecretFound.SecretDisplayColor").replace("&", "§");
				String Found = getConfig().getString("Items.ItemSecretFound.Lore").replace("&", "§");
				String total = getConfig().getString("Items.ItemTotalFoundSecrets.Name").replace("&", "§");
				String totalcolor = getConfig().getString("Items.ItemTotalFoundSecrets.TotalFoundSecretsLoreColor").replace("&", "§");
				List<String> userlist = userfile.getStringList(p.getUniqueId().toString());
				List<String> list = cmdfile.getStringList("Secrets");
				Inventory inv = Bukkit.createInventory(null, 54, "Secrets");
				if (!userfile.contains(p.getUniqueId().toString())) {
					Inventory invs = Bukkit.createInventory(null, 27, "Secrets");
					ItemStack none = ItemUtil.Stack(NothingFound, NothingFoundItem, NothingFoundLore, 1, (short) 1);
					@SuppressWarnings("unused")
					ItemMeta Itemmeta = none.getItemMeta();
					invs.setItem(13, none);
					p.openInventory(invs);
					return false;
				}
				int i = -1;
				for (String all : userlist) {
					i++;
					Material SercetITEM = Material.getMaterial(getConfig().getString("Items.ItemSecretFound.Type"));
					ItemStack Secret = ItemUtil.Stack(SecretColor + all, SercetITEM, Found, 1, (short) 10);
					if (getConfig().getBoolean("Items.ItemSecretFound.Enchanted")) {
						ItemMeta SecretMeta = Secret.getItemMeta();
						Secret.setItemMeta(SecretMeta);
					}
					inv.setItem(i, Secret);
					if (i == 53) {
						break;
					}
				}
				int max = list.size();
				int amoun = userlist.size();
				Material TotalFoundItem = Material
						.getMaterial(getConfig().getString("Items.ItemTotalFoundSecrets.Type"));
				ItemStack amount = ItemUtil.Stack(total, TotalFoundItem, totalcolor + amoun + " / " + max, 1,
						(short) 0);
				if (getConfig().getBoolean("Items.ItemSecretFound.Enchanted")) {
					ItemMeta amountMeta = amount.getItemMeta();
					amount.setItemMeta(amountMeta);
				}
				inv.setItem(53, amount);
				p.openInventory(inv);
			}
			return false;
		} else if (args[0].equalsIgnoreCase("reload")) {
			if(sender.hasPermission("secrets.reload")) {
			String Reload = getConfig().getString("Messages.Reload").replace("&", "§");
			reloadConfig();
			sender.sendMessage(Reload);
			return true;
			} else {
				String NoPermReload = getConfig().getString("Messages.NoPermReload").replace("&", "§");
				sender.sendMessage(NoPermReload);	
			}
		} else if (args[0].equalsIgnoreCase("info")) {
			if(sender.hasPermission("secrets.info")) {
				sender.sendMessage("§a----------§9Secrets§a----------\n"
						+ "§9Commands:\n"
						+ "§a/secrets §c- §aShow the Secrets GUI\n"
						+ "§a/secrets reload §c- §aReload the Config\n"
						+ "§a/secrets info §c- §aShow this Page\n"
						+ "§aVersion: §c" + getDescription().getVersion() + " §aAuthors: §c" + getDescription().getAuthors() + "\n"
						+ "§a----------§9Secrets§a----------");
			return true;
			} else {
				sender.sendMessage("§a----------§9Secrets§a----------\n"
						+ "§9Commands:\n"
						+ "§a/secrets §c- §aShow the Secrets GUI\n"
						+ "§a/secrets info §c- Â§aShow this Page\n"
						+ "§aVersion: §c" + getDescription().getVersion() + " §aAuthors: §c" + getDescription().getAuthors() + "\n"
						+ "§a----------§9Secrets§a----------");
						
			}
		} else {
			String SubCommandnotFound = getConfig().getString("Messages.SubCommandnotFound").replace("&", "§");
			sender.sendMessage(SubCommandnotFound);
			return true;
		}
		return false;
	}
	
	public static boolean hasVault(){
        try {
            Class.forName("net.milkbowl.vault.Vault");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
	public static boolean hasTTA(){
		try {
			Class.forName("de.Herbystar.TTA.TTA_Methods");
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static Plugin getInstance() {
		return instance;
	}
	
}
	
