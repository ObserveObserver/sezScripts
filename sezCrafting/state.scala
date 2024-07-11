package net.runelite.client.plugins.microbot.sezCrafting

import cats.data.State
import net.runelite.client.plugins.microbot.util.bank.Rs2Bank
import net.runelite.client.plugins.microbot.util.gameobject.Rs2GameObject
import net.runelite.client.plugins.microbot.util.inventory.{Rs2Inventory, Rs2Item}
import net.runelite.client.plugins.microbot.util.player.Rs2Player

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
}
