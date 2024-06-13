package net.runelite.client.plugins.microbot.sezCooking.banking

import cats.effect.IO
import cats.effect.unsafe.IORuntime
import net.runelite.client.plugins.microbot.util.Global.{sleep, sleepUntil}
import net.runelite.client.plugins.microbot.util.bank.Rs2Bank
import net.runelite.client.plugins.microbot.util.npc.Rs2Npc
import net.runelite.client.plugins.microbot.sezCooking.location.location._


object banking {
  val version: Double = 1.0;

  def mainBanking(food: Int): IO[Unit] = {
    println("bank")
    val loc = getLocation()
    println(loc)
    loc match {
      case "thieves guild" => thievesMain(food).unsafeRunSync()(getRuntime)
      case _ => otherMain(food).unsafeRunSync()(getRuntime)
    }
    for {
     _ <- IO(manageBank(food)).unsafeRunSync()(getRuntime)
    } yield()
  }

  def getRuntime(): IORuntime = {
    val runtime: IORuntime = IORuntime.global
    runtime
  }

  def manageBank(food: Int): IO[Boolean] = {
    for {
      _ <- IO(sleep(1000,1300))
      _ <- IO(Rs2Bank.depositAll)
      _ <- IO(sleep(1000,1300))
      _ <- IO(Rs2Bank.withdrawAll(food))
      _ <- IO(sleep(300,700))
      res <- IO(Rs2Bank.closeBank)
    } yield(res)
  }

  def openThievesBank(): IO[Unit] = {
    for {
      _ <- IO(Rs2Npc.interact(3194, "bank"))
    } yield()
  }

  def thievesMain(food: Int): IO[Unit] = {
    for {
      _ <- IO(openThievesBank).unsafeRunSync()(getRuntime)
      _ <- IO(manageBank(food)).unsafeRunSync()(getRuntime)
    } yield()
  }

  def otherMain(food: Int): IO[Unit] = {
    for {
      _ <- IO(openBank).unsafeRunSync()(getRuntime)
      _ <- IO(manageBank(food)).unsafeRunSync()(getRuntime)
    } yield()
  }

  def openBank(): IO[Boolean] = {
    for {
      res <- IO(Rs2Bank.openBank)
      _ <- IO(sleepUntil(() => Rs2Bank.isOpen),35000)
    } yield(res)
  }
}
