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
    if (e.getClickedInventory() == null)
    {
      e.getWhoClicked().closeInventory();
      return;
    }
    if (p.getOpenInventory().getTopInventory() == null) {
      return;
    }
    if (e.getClickedInventory() == null) {
      return;
    }
    if (e.getCurrentItem() == null) {
      return;
    }
    if (e.getCurrentItem().getItemMeta() == null) {
      return;
    }
    if (e.getClickedInventory().getName().equals("Secrets"))
    {
      e.setCancelled(true);
      return;
    }
  }
}

