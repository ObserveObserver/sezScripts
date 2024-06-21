package net.runelite.client.plugins.microbot.sezCooking;

import cats._
import cats.data.State
import cats.effect.IO
import net.runelite.api.{Client}
import net.runelite.client.plugins.microbot.util.inventory.{Rs2Inventory, Rs2Item}
import net.runelite.client.plugins.microbot.{Microbot, Script}
import java.util.concurrent.TimeUnit
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
    }
  }



  def main(client: Client, config: sezCookingConfig): Boolean = {
    val foodList = Rs2Inventory.get("raw",false).id
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
