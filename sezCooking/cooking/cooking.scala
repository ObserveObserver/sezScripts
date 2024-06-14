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
import org.apache.commons.math3.distribution.NormalDistribution
import scala.math._


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
          _ <- IO(nSleep(500,2000,3500,0.1))
          _ <- IO(randomBreak)
        } yield IO.unit
      }

    def randomBreak(): IO[Unit] = {
      if (Random.between(1,11) == 10) {
        nSleep(5000,10000,30000,0.1)
      } else {
      }
      IO.unit
    }


    def getRuntime(): IORuntime = {
      val runtime: IORuntime = IORuntime.global
      runtime
    }

    def skewNormal(mean: Double, stdDev: Double, skew: Double): Double = {
      val normal = new NormalDistribution(0, 1)
      val u = normal.sample()
      val delta = skew / sqrt(1 + skew * skew)
      val t = sqrt(2 * 3.1415) * delta / sqrt(1 - 2 * delta * delta / 3.1415)

      val x = if (delta > 0) {
        stdDev * (t - t / (1 + pow(t * u / sqrt(2 - delta * delta), 2)) + u * sqrt(1 - delta * delta))
      } else {
        stdDev * (t + t / (1 - pow(t * u / sqrt(2 + delta * delta), 2)) - u * sqrt(1 - delta * delta))
      }

      mean + x
    }

    def nSleep(min: Int, avg: Int, max: Int, skew: Double): Unit = {
      val range = max - min
      val stdDev = range / 6.0 // Standard deviation approximation

      // Generate a skewed normal value
      val skewedValue = skewNormal(avg, stdDev, skew)

      // Bound the value between min and max
      val boundedSleepTime = math.max(min, math.min(skewedValue, max)).toInt
      println("Sleeing for: " + boundedSleepTime)
      sleep(boundedSleepTime)
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
