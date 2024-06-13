package net.runelite.client.plugins.microbot.fighterGuild;

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

import net.runelite.api.coords.WorldPoint

import java.awt.event.KeyEvent.VK_SPACE
import net.runelite.api.{Client, GameObject, ItemID, Skill}
import net.runelite.client.plugins.microbot.{Microbot, Script}
import net.runelite.client.plugins.microbot.fighterGuild.fighterGuildPlugin.client
import net.runelite.client.plugins.microbot.util.gameobject.Rs2GameObject
import net.runelite.client.plugins.microbot.util.grounditem.Rs2GroundItem
import net.runelite.client.plugins.microbot.util.inventory.{Rs2Inventory, Rs2Item}
import net.runelite.client.plugins.microbot.util.math.Random
import net.runelite.client.plugins.microbot.util.npc.Rs2Npc
import net.runelite.client.plugins.microbot.util.player.Rs2Player

import java.util.concurrent.TimeUnit
import scala.collection.convert.ImplicitConversions.`collection AsScalaIterable`
import scala.language.postfixOps
import cats._
import cats.Monad
import cats.implicits._
import cats.effect.IO
import cats.Applicative
import cats.Comparison
import cats.data.AndThen.andThen
import cats.data.OptionT
import net.runelite.client.plugins.microbot.fighterGuild.fighterGuildConfig
import net.runelite.client.plugins.microbot.util.Global.sleep
import net.runelite.client.plugins.microbot.util.bank.Rs2Bank
import net.runelite.client.plugins.microbot.util.combat.Rs2Combat
import net.runelite.client.plugins.microbot.util.keyboard.Rs2Keyboard
import net.runelite.client.plugins.microbot.util.magic.Rs2Magic
import net.runelite.client.plugins.microbot.util.security.Login
import net.runelite.client.plugins.microbot.util.walker.Rs2Walker
import net.runelite.client.plugins.microbot.util.widget.Rs2Widget


class fighterGuildScript extends Script {


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

  /*
    implicit val inventoryMonad: Monad[_] = new Monad[Inventory] {
      def pure[A](x: A): Inventory = Inventory(List(x))

      def flatMap[A, B](fa: Inventory)(f: A => Inventory): Inventory = {
        fa.items.flatMap(item => f(item).items)
      }

    }
  */


  def fillList(itemId: Int, amount: Int): List[Int] = {
    (1 to amount).foldLeft(List.empty[Int]) { (acc, _) =>
      acc :+ itemId
    }
  }

  def getOurInv = {
    Inventory(Rs2Inventory.inventoryItems.map(z => z.id).toList)
  }

  def shouldBank(ourInv: Inventory, desiredInv: Inventory): Option[List[Boolean]] = {
    desiredInv.items.traverse { item =>
      if (Rs2Inventory.hasItem(item)) Some(true) else None
    }
  }

  def itemsToGetFromBank(ourInv: Inventory, inv: Inventory): Inventory = {
    Inventory(
      inv.items.filter(x => !ourInv.items.contains(x)))
  }

  def toBank: Boolean = {
    sleepUntil(() => Rs2Walker.walkTo(new WorldPoint(2843, 3543, 0), 0))
  }

  def deposit(ourInv: Inventory, desiredInv: Inventory): Inventory = {
    liftIO(Rs2Bank.openBank())
    sleep(2000)
    while (!Rs2Bank.isOpen) {
      sleep(1000)
      Rs2Bank.openBank
    }
    sleep(800, 1500)
    ourInv.items.foreach(x => if (!desiredInv.items.contains(x)) {
      Rs2Bank.depositOne(x)
      sleep(600, 1200)
    })
    Inventory(Rs2Inventory.inventoryItems.toList.map(x => x.id))
  }

  def withdraw(newInv: Inventory, desiredInv: Inventory): Inventory = {
    newInv |+| Inventory(
      itemsToGetFromBank(newInv, desiredInv).items.map(x => {
        Rs2Bank.withdrawItem(x)
        sleep(300, 1000)
        x
      }))
  }

  def bankingLogic(ourInv: Inventory, desiredInv: Inventory): IO[Unit] = {
    println(shouldBank(ourInv, desiredInv))
    toBank
    withdraw(deposit(ourInv, desiredInv), desiredInv)
    Rs2Bank.closeBank()
    IO(sleep(1500))
  }

  def walkToRoom = {
    Rs2Walker.walkMiniMap(new WorldPoint(2854, 3546, 0))
    sleepUntil(() => !Rs2Player.isWalking && !Rs2Player.isAnimating
      && (Rs2Player.getWorldLocation == new WorldPoint(2854, 3546, 0)))
    println("Opening door hopefully...")
    val result = Rs2GameObject.interact(24306, "open")
    sleep(1000, 2000)
    sleepUntil(() => !Rs2Player.isMoving && !Rs2Player.isAnimating)
    sleep(3000, 5000)
    result
  }

  def findPlatform: List[GameObject] = {
    Rs2GameObject.getGameObjects(23955).toList
  }

  def checkPlatform(platforms: List[GameObject]): GameObject = {
    if (Rs2Player.getPlayers.map(x => x.getWorldLocation)
      .map(z => Rs2Walker.isNear(z)).toList.contains(true)) {
      val world = Login.getRandomWorld(true)
      Microbot.hopToWorld(world)
      sleep(2000, 3500)
      checkPlatform(platforms)
    }
    println("here " + platforms.head)
    platforms.head
  }

  def clickPlatform(platform: GameObject): IO[Unit] = {
    for {
      result <- IO(Rs2GameObject.interact(platform))
      _      <- IO(println("click"))
      _      <- IO(sleepUntil(() => Rs2Player.isAnimating, 10000))
    } yield result
  }

  def checkHp(): Float = {
    client.getBoostedSkillLevel(Skill.HITPOINTS).toFloat / client.getRealSkillLevel(Skill.HITPOINTS).toFloat
  }

  def pickUpItems(inv: Inventory): Unit = {
    Microbot.status = "Picking up armor + tokens..."
    if (Rs2Inventory.getInventoryFood.isEmpty) {
      println("NULL")
    }


    inv.items
      .filter(z => z != 379)
      .foreach(x => {
        Rs2GroundItem.pickup(x)
        println(x)
        sleep(1600, 2400)
      })
  }

  def fightEnemy(inv: Inventory): IO[Unit] = {
    for {
      _ <- IO(sleep(4000))
      npc <- IO(Rs2Npc.getNpcsAttackingPlayer(client.getLocalPlayer))
      _ <- IO {
        while (!npc.head.isDead) {
          if (checkHp <= 0.50) {
            Rs2Inventory.getInventoryFood.foreach { food =>
              if (Rs2Inventory.interact(food.name, "Eat")) {
                println("Eating...")
              }
            }
          }
          Thread.sleep(1000)
        }
      }
      _ <- IO(sleep(2800))
      _ <- liftIO(pickUpItems(inv))
    } yield ()
  }

  def checkIfFood(): IO[Unit] = {
    if (Rs2Inventory.getInventoryFood.isEmpty) {
      Rs2Walker.walkMiniMap(new WorldPoint(2855, 3544, 0), 0)
      sleep(1000)
      sleepUntil(() => !Rs2Player.isAnimating && !Rs2Player.isMoving)

      Rs2GameObject.interact(24306, "open")
      IO(sleep(1000))
    }
    else {
      IO.unit
    }
  }

  def fightingLogic(inv: Inventory): IO[Unit] = {
    if (!Rs2Combat.inCombat()) {
      val platforms = findPlatform
      val platform = checkPlatform(platforms)

      for {
        _ <- clickPlatform(platform)
        _ <- fightEnemy(inv)
        _ <- IO(checkIfFood)
      } yield ()
    } else {
      IO.unit
    }
  }

  def checkBanking(desiredInv: Inventory): IO[Unit] = {
    shouldBank(getOurInv, desiredInv) match {
      case None => bankingLogic(getOurInv, desiredInv)
      case Some(_) => IO.unit
    }
  }

  def checkIfInRoom(): Boolean = {
    val xLoc = Rs2Player.getWorldLocation.getX
    val yLoc = Rs2Player.getWorldLocation.getY

    println("X Loc: " + xLoc + " yLoc: " + yLoc)

    (2850 to 2860).contains(xLoc) && (3534 to 3545).contains(yLoc)
  }

  def walkToGiants: Option[Boolean] = {
    Rs2Walker.walkTo(new WorldPoint(2846, 3541, 2))
    Some(sleepUntil(() => Rs2Player.getWorldLocation == new WorldPoint(2846, 3541, 2), 15000))
  }

  def enterDoor: Option[Boolean] = {
    if (Rs2GameObject.interact(24309, "open") && sleepUntil(() => Rs2Widget.hasWidget("defender"))) {
      for {
        _ <- IO(Rs2Keyboard.keyPress(VK_SPACE))
        _ <- IO(sleep(2000))
      } yield ()
    }
    Some(Rs2Npc.hasLineOfSight(Rs2Npc.getAttackableNpcs.toList.head))
  }


// giants combat logic starts here

  def giantsCombatLogic: IO[Unit] = {
      val cyclops = Rs2Npc.getNpc("cyclops")
      if (cyclops != null) {
        for {
          _ <- IO(Rs2Npc.interact(cyclops))
          _ <- IO(sleep(2000))
        } yield ()
      } else {
        IO.unit
      }
  }

  def fightGiantsLogic(desiredInv: Inventory): IO[Unit] = {
    for {
      _ <- checkBanking(desiredInv)
      caseTest = (walkToGiants, enterDoor).tupled
      _ <- caseTest match {
        case Some(_) => giantsCombatLogic
        case None => IO.unit
      }
    } yield ()
  }

  def task(config: fighterGuildConfig, armor: Inventory): Runnable = new Runnable {
    def run(): Unit = {

      val foodList = Inventory(fillList(ItemID.LOBSTER, Random.random(18, 25)) ++ List(ItemID.WARRIOR_GUILD_TOKEN))
      var desiredInv = armor.combine(foodList)
      checkBanking(desiredInv)
      if (Rs2Inventory.get("token", false).quantity >= 1500) {
        val desiredInv = Inventory(fillList(ItemID.LOBSTER, Random.random(20, 25))) |+| Inventory(List(8851))
        fightGiantsLogic(desiredInv)
      } else {
        println(checkIfInRoom)
        if (!checkIfInRoom) {
          println("we arent in room")
          walkToRoom
        } else {
          println("we're in")
        }
        fightingLogic(desiredInv)
        println(desiredInv.items.toString)
      }
    }
  }


  def main(client: Client, config: fighterGuildConfig): Boolean = {
    if (!isLoggedIn()) {
      return true
    }
    val armor = config.armorToKill().toString match {
      case "black" => Inventory(List(ItemID.BLACK_FULL_HELM, ItemID.BLACK_PLATEBODY, ItemID.BLACK_PLATELEGS))
    }
    mainScheduledFuture = scheduledExecutorService.scheduleWithFixedDelay({
      task(config, armor)
    }, 0, 1000, TimeUnit.MILLISECONDS)
    true
  }

  def isLoggedIn(): Boolean = {
    Microbot.isLoggedIn()
  }
}

  object fighterGuildScript {
    val version: Double = 1.0;
    print("test")
  }
