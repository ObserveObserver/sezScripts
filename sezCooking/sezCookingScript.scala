package net.runelite.client.plugins.microbot.sezCooking;

/*
   Hunting Script
   Sezen

  Hunts shit, so far we have:

  Butterflies
  Salamanders

  With future support for chins coming.

  Sleeps must be changed based off ping, in future a sleep modifier
  will be included in the settings. Ping detection is probably better
  so I may just do that instead :)

 */

import cats._
import cats.data.State
import cats.effect.IO
import cats.effect.unsafe.IORuntime
import cats.implicits._
import net.runelite.api.coords.WorldPoint
import net.runelite.api.{Client, GameObject, ItemID, Skill}
import net.runelite.client.plugins.microbot.util.bank.Rs2Bank
import net.runelite.client.plugins.microbot.util.combat.Rs2Combat
import net.runelite.client.plugins.microbot.util.gameobject.Rs2GameObject
import net.runelite.client.plugins.microbot.util.grounditem.Rs2GroundItem
import net.runelite.client.plugins.microbot.util.inventory.Rs2Inventory
import net.runelite.client.plugins.microbot.util.keyboard.Rs2Keyboard
import net.runelite.client.plugins.microbot.util.math.Random
import net.runelite.client.plugins.microbot.util.npc.Rs2Npc
import net.runelite.client.plugins.microbot.util.player.Rs2Player
import net.runelite.client.plugins.microbot.util.security.Login
import net.runelite.client.plugins.microbot.util.walker.Rs2Walker
import net.runelite.client.plugins.microbot.util.widget.Rs2Widget
import net.runelite.client.plugins.microbot.{Microbot, Script}

import java.awt.event.KeyEvent.VK_SPACE
import java.util.concurrent.TimeUnit
import scala.collection.convert.ImplicitConversions.`collection AsScalaIterable`
import scala.language.postfixOps
import net.runelite.client.plugins.microbot.sezCooking.cooking.cooking._
import net.runelite.client.plugins.microbot.sezCooking.banking.banking._


class sezCookingScript extends Script {


  case class Inventory(items: List[Int])


  implicit val inventorySemigroup: Semigroup[Inventory] = new Semigroup[Inventory] {
    def combine(x: Inventory, y: Inventory): Inventory = {
      Inventory(x.items ++ y.items)
    }
  }


  implicit val inventoryM: Monoid[Inventory] = new Monoid[Inventory] {
    def empty: Inventory = Inventory(List.empty[Int])


    def combine(x: Inventory, y: Inventory): Inventory = {
      inventorySemigroup.combine(x, y)
    }
  }


  def liftIO(action: => Unit): IO[Unit] = IO(action)



  def getState(food: Int): State[Boolean, Boolean] = State { inventory =>
    val hasFood = Rs2Inventory.hasItem(food)
    (hasFood, hasFood)
  }


  def task(config: sezCookingConfig, food: Int): Runnable = new Runnable {
    def run(): Unit = {

      getState(food).run(true).value match {
        case (true,_)  => mainCooking(food)
        case (false,_) => mainBanking(food)
      }
      println(normalDistributionSleep(100,500,1500))
      println(normalDistributionSleep(100,500,1500))
      println(normalDistributionSleep(100,500,1500))

    }
  }



  def main(client: Client, config: sezCookingConfig): Boolean = {
    val foodList = ItemID.RAW_SHARK
    mainScheduledFuture = scheduledExecutorService.scheduleWithFixedDelay({
      task(config, foodList)
    }, 0, 1000, TimeUnit.MILLISECONDS)
    true
  }

  def isLoggedIn(): Boolean = {
    Microbot.isLoggedIn()
  }
}

  object sezCookingScript {
    val version: Double = 1.0;
    print("test")
  }
