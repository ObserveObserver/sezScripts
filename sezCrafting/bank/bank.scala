package net.runelite.client.plugins.microbot.sezCrafting.bank

import cats.effect.IO
import net.runelite.client.plugins.microbot.Microbot
import net.runelite.client.plugins.microbot.util.Global.{sleep, sleepUntilTrue}
import net.runelite.client.plugins.microbot.util.bank.Rs2Bank
import net.runelite.client.plugins.microbot.util.inventory.Rs2Inventory
import net.runelite.client.plugins.microbot.util.player.Rs2Player

object bank {

  def getMould(kind: String): String = {
    kind ++ " mould"
  }

  def checkInv(gem: String, mouldKind: String): IO[Unit] = {
    if (Rs2Inventory.hasItemAmount(gem, 13) &&
      Rs2Inventory.hasItemAmount("gold bar", 13) &&
      Rs2Inventory.hasItemAmount(mouldKind, 1)) {
      IO.unit
    } else {
      sleep(1000)
      Rs2Bank.closeBank
      sleep(1000)
      Rs2Player.logout
      println("Out of some item. Quitting...")
      Microbot.pauseAllScripts = true

    }
    IO.unit
  }

  def bankingLogic(gem: String, kind: String): IO[Boolean] = {
    val mouldKind = getMould(kind)
    for {
      _ <- IO(sleepUntilTrue(() => Rs2Bank.isOpen, 300, 10000))
      _ <- IO(Rs2Bank.depositAll)
      _ <- IO(sleep(300,1500))
      _ <- IO(Rs2Bank.withdrawX(true, gem, 13, true))
      _ <- IO(sleep(300,800))
      _ <- IO(Rs2Bank.withdrawX(true, "gold bar", 13, true))
      _ <- IO(sleep(300,1000))
      _ <- IO(Rs2Bank.withdrawOne(mouldKind))
      _ <- IO(sleep(300,1000))
      bo <- IO(Rs2Bank.closeBank)
      _ <- IO(checkInv(gem,mouldKind))
    } yield(bo)
  }
}
