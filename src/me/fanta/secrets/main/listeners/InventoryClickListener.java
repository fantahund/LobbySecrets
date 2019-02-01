package me.fanta.secrets.main.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener
  implements Listener
{
  @EventHandler
  public void onClick(InventoryClickEvent e)
  {
    Player p = (Player)e.getWhoClicked();
    if (p.getOpenInventory().getTitle().equals("Secrets"))
    {
      e.setCancelled(true);
    }
  }
}

