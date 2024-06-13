package net.runelite.client.plugins.microbot.sezCooking.cooking

import cats.data.State
import cats.effect.IO
import cats.effect.unsafe.IORuntime
import net.runelite.api.GameObject
import net.runelite.client.plugins.microbot.util.Global.{sleep, sleepUntil}
import net.runelite.client.plugins.microbot.util.gameobject.Rs2GameObject
import net.runelite.client.plugins.microbot.util.inventory.Rs2Inventory
import net.runelite.client.plugins.microbot.util.keyboard.Rs2Keyboard
import net.runelite.client.plugins.microbot.util.widget.Rs2Widget

import java.awt.event.KeyEvent.VK_SPACE
import scala.util.Random
import net.runelite.client.plugins.microbot.sezCooking.location.location._


  object cooking {
    val version: Double = 1.0;

    def getRange: Option[GameObject] = {
      Option(Rs2GameObject.get("Range"))
    }

    def isInRange(range: Option[GameObject]): Boolean = {
      range match {
        case Some(gameObject) => Rs2GameObject.hasLineOfSight(gameObject)
        case None => false
      }
    }

    def clickRange(): IO[Unit] = {
      if (Rs2GameObject.get("range") != null) {
        Rs2GameObject.interact("range", "cook")
      }
      else if (Rs2GameObject.get("clay oven") != null) {
        Rs2GameObject.interact("clay oven", "cook")
      }
      IO.unit
    }

    def cookFood(food: Int): IO[Unit] = {
        for {
          _ <- IO(println("lets see HERE"))
          _ <- IO(sleepUntil(() => Rs2Widget.hasWidget("how many"),90000))
          _ <- IO(Rs2Keyboard.keyPress(VK_SPACE))
          _ <- IO(sleep(800, 2050))
          _ <- IO(sleepUntil(() => !Rs2Inventory.contains(food), 90000))
          _ <- IO(sleep(100,2500))
        } yield IO.unit
      }


    def getRuntime(): IORuntime = {
      val runtime: IORuntime = IORuntime.global
      runtime
    }

    def normalDistributionSleep(min: Int, avg: Int, max: Int): Int = {
      val random = new Random()
      val stdDev = (max - min) / 6.0
      val sleepTime = random.nextGaussian() * stdDev + avg
      val boundedSleepTime = Math.max(min, Math.min(sleepTime, max)).toInt
      return(boundedSleepTime)
    }

    def thievesGuildCook(food: Int): IO[Unit] = {
      for {
        _ <- IO(Rs2GameObject.interact(43475, "cook"))
        _ <- IO(cookFood(food)).unsafeRunSync() (getRuntime)
      } yield()
    }

    def startCooking(food: Int): IO[Unit] = {
      for {
        _ <- clickRange()
        _ <- cookFood(food)
        } yield ()
      }


    def mainCooking(food: Int): Boolean = {
      println("cooking " + food)
      val ourLocation = getLocation()
      println(ourLocation)
      ourLocation match {
        case "thieves guild" => thievesGuildCook(food).unsafeRunSync()(getRuntime)
        case _ =>
          startCooking(food).unsafeRunSync()(getRuntime)

      }
      true
    }
  }
