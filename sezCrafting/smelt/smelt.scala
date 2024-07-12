package net.runelite.client.plugins.microbot.sezCrafting.smelt

import cats.effect.IO
import net.runelite.client.plugins.microbot.sezCrafting.walking.walking.rt
import net.runelite.client.plugins.microbot.util.Global.{sleep, sleepUntil, sleepUntilTrue}
import net.runelite.client.plugins.microbot.util.gameobject.Rs2GameObject
import net.runelite.client.plugins.microbot.util.inventory.Rs2Inventory
import net.runelite.client.plugins.microbot.util.math.Random
import net.runelite.client.plugins.microbot.util.widget.Rs2Widget
import scala.jdk.CollectionConverters._

object smelt {

  def getSmeltWidget(gem: String, kind: String): IO[Int] = {
    val baseAmulet = 29229093
    val baseNecklace = 29229079
    val baseRing = 29229064
    val baseKind = kind match {
      case "amulet" => baseAmulet
      case "necklace" => baseNecklace
      case "ring" => baseRing
    }
    gem match {
      case "sapphire" => IO(baseKind + 1)
      case "emerald" => IO(baseKind + 2)
      case "ruby" => IO(baseKind + 3)
      case "diamond" => IO(baseKind + 4)
    }
  }

  def smelt(gem: String, kind: String): IO[Unit] = {
    for {
      fur <- IO(Rs2GameObject.get("furnace",false))
      _ <- IO(Rs2GameObject.interact(fur,"smelt"))
      _ <- IO(sleepUntil(() => Rs2Widget.hasWidget("What would you like to make?")))
      _ <- IO(sleep(300,1500))
      _ <- IO(Rs2Widget.clickWidget(getSmeltWidget(gem, kind).unsafeRunSync()(rt)))
      _ <- IO(sleepUntilTrue(() => !Rs2Inventory.hasItem("gold bar"),300,60000))
      _ <- IO(sleep(500,3000))
      _ <- IO(randSleep)
    } yield(IO.unit)
  }

  def randSleep(): IO[Unit] = {
    if (Random.random(1,25) == 14) IO(sleep(5000,15000)) else IO.unit
  }
}
