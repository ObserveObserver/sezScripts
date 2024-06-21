package net.runelite.client.plugins.microbot.sezCooking.location

import net.runelite.api.coords.WorldPoint
import net.runelite.client.plugins.microbot.util.player.Rs2Player
import net.runelite.client.plugins.microbot.util.walker.Rs2Walker

object location {

  object thievesGuild {
    val nwCorner = new WorldPoint(3036, 4987, 1);
    val seCorner = new WorldPoint(3062, 4958, 1);
  }

  def getLocation(): String = {
    Rs2Player.getWorldLocation match {
      case loc if Rs2Walker.isInArea(thievesGuild.nwCorner, thievesGuild.seCorner) => "thieves guild"
      case _ => "somewhere unremarkable"
    }
  }

}
