package net.runelite.client.plugins.microbot.sezCrafting

import cats.data.State
import cats.effect.IO
import net.runelite.client.plugins.microbot.util.bank.Rs2Bank
import net.runelite.client.plugins.microbot.util.gameobject.Rs2GameObject
import net.runelite.client.plugins.microbot.util.inventory.{Rs2Inventory, Rs2Item}
import net.runelite.client.plugins.microbot.util.player.Rs2Player
import cats.effect.concurrent.Ref
import net.runelite.api.Client
import net.runelite.client.game.ItemManager
import net.runelite.client.plugins.microbot.Microbot
import net.runelite.client.plugins.microbot.sezCrafting.sezCraftingConfig


object state {
  def getState(gem: String): State[String, String] = State { inventory =>
    val state = {
      if (Rs2Inventory.contains(gem) && Rs2GameObject.get("furnace",false).getWorldLocation.distanceTo(Rs2Player.getWorldLocation) <= 6) {
        ("smelt", "smelt")
      } else if (Rs2Inventory.contains(gem) && Rs2Inventory.contains("gold bar") && !Rs2Player.isMoving) {
        ("walkTo","walkTo")
      } else if (Rs2Bank.isOpen) {
        ("bank","bank")
      } else if (!Rs2Inventory.contains(gem)) {
        ("walkBank","walkBank")
      } else {
        ("","")
      }
    }
    state
  }
  var jewelryCount: Int = 0

  var jewelryProfitSaved: Int = 0

  def jewelryProfit(count: Int, config: sezCraftingConfig, client: ItemManager): Unit = {
    val bar = client.search("gold bar").get(0).getPrice
    val gem = client.search(config.gemStones().toString).get(0).getPrice
    val jewelry = client.search(config.gemStones().toString ++ " " ++ config.jewelry().toString).get(0).getPrice
    jewelryProfitSaved = (jewelry * count) - ((gem * count) + (bar * count))
  }
}
