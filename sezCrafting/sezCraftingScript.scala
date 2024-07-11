package net.runelite.client.plugins.microbot.sezCrafting;

import cats._
import cats.effect.IO
import cats.effect.unsafe.IORuntime
import net.runelite.api.Client
import net.runelite.client.plugins.microbot.sezCrafting.bank.bank.bankingLogic
import net.runelite.client.plugins.microbot.sezCrafting.smelt.smelt.smelt
import net.runelite.client.plugins.microbot.{Microbot, Script}

import java.util.concurrent.TimeUnit
import scala.language.postfixOps
import net.runelite.client.plugins.microbot.sezCrafting.walking.walking._
import net.runelite.client.plugins.microbot.sezCrafting.state._
import net.runelite.client.plugins.microbot.util.camera.Rs2Camera


class sezCraftingScript extends Script {


  case class Inventory(items: List[Int])

  // inventory monoid will be placed in later when i do pies or something

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

  def task(config: sezCraftingConfig): Runnable = new Runnable {
    def run(): Unit = {
      val gem = config.gemStones().toString
      val kind = config.jewelry().toString
      if (!Microbot.isLoggedIn) {
        return
      }
      getState(gem).run("bank").value._1 match {
        case "walkBank" => walkBank.unsafeRunSync()(rt)
        case "bank" => bankingLogic(gem,kind).unsafeRunSync()(rt)
        case "walkTo" => walkTo.unsafeRunSync()(rt)
        case "smelt" => smelt(gem,kind).unsafeRunSync()(rt)
        case _ => IO(println("uh oh")).unsafeRunSync()(rt)
      }
    }
  }

  def rt: IORuntime = {
    val runtime: IORuntime = IORuntime.global
    runtime
  }

  def main(client: Client, config: sezCraftingConfig): Boolean = {
    mainScheduledFuture = scheduledExecutorService.scheduleWithFixedDelay({
      task(config)
    }, 0, 1000, TimeUnit.MILLISECONDS)
    true
  }
}