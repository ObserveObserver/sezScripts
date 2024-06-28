package net.runelite.client.plugins.microbot.sezCooking.banking

import cats.effect.IO
import cats.effect.unsafe.IORuntime
import net.runelite.client.plugins.microbot.Microbot
import net.runelite.client.plugins.microbot.sezCooking.cooking.cooking.nSleep
import net.runelite.client.plugins.microbot.util.Global.{sleep, sleepUntil}
import net.runelite.client.plugins.microbot.util.bank.Rs2Bank
import net.runelite.client.plugins.microbot.util.npc.Rs2Npc
import net.runelite.client.plugins.microbot.sezCooking.location.location._
import net.runelite.client.plugins.microbot.util.player.Rs2Player



object banking {

  val version: Double = 1.0;

  def mainBanking(food: List[String]): IO[Unit] = {
    val loc = getLocation()
    loc match {
      case "thieves guild" => thievesMain(food).unsafeRunSync()(getRuntime)
      case _ => otherMain(food).unsafeRunSync()(getRuntime)
    }
    IO.unit
  }

  def getRuntime(): IORuntime = {
    val runtime: IORuntime = IORuntime.global
    runtime
  }

  def manageBank(food: Int): IO[Boolean] = {
    for {
      _ <- IO(nSleep(500,1000,2500,0.1))
      _ <- IO(Rs2Bank.depositAll)
      _ <- IO(nSleep(500,1100,2500,0.1))
      _ <- IO(Rs2Bank.withdrawAll(food))
      _ <- IO(nSleep(500,1000,2500,0.1))
      res <- IO(Rs2Bank.closeBank)
    } yield(res)
  }

  def checkIfOut(food: Int): IO[Unit] = {
    if (food == 0) {
      Rs2Bank.closeBank
      sleep(1000,1500)
      Rs2Player.logout()
      Microbot.pauseAllScripts = true
    }
    IO.unit
  }
  def openThievesBank(): IO[Unit] = {
    for {
      _ <- IO(Rs2Npc.interact(3194, "bank"))
      _ <- IO(sleepUntil(() => Rs2Bank.isOpen),35000)
    } yield()
  }

  def getOurFood(food: List[String] ): Int = {
    val ourFoodCheck = food.map(x => Rs2Bank.hasItem(x))
    if (!ourFoodCheck.contains(true)) return 0
    val ourFoodZip = food.zip(ourFoodCheck)
    val ourFood = Rs2Bank.findBankItem(ourFoodZip.collect { case (item,true) => item }.head).id
    if (ourFood != null) println(ourFood) else println ("no")
    ourFood
  }

  def thievesMain(food: List[String]): IO[Unit] = {
    for {
      _ <- IO(openThievesBank).unsafeRunSync()(getRuntime)
      _ <- checkIfOut(getOurFood(food))
      _ <- IO(manageBank(getOurFood(food))).unsafeRunSync()(getRuntime)
    } yield()
  }

  def otherMain(food: List[String]): IO[Unit] = {
    for {
      _ <- IO(openBank).unsafeRunSync()(getRuntime)
      _ <- checkIfOut(getOurFood(food))
      _ <- IO(manageBank(getOurFood(food))).unsafeRunSync()(getRuntime)
    } yield()
  }

  def openBank(): IO[Boolean] = {
    for {
      res <- IO(Rs2Bank.openBank)
      _ <- IO(sleepUntil(() => Rs2Bank.isOpen),35000)
    } yield(res)
  }
}
