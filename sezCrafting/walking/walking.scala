package net.runelite.client.plugins.microbot.sezCrafting.walking


import cats.effect.IO
import cats.effect.unsafe.IORuntime
import net.runelite.client.plugins.microbot.util.Global.{sleepUntil, sleepUntilTrue}
import net.runelite.client.plugins.microbot.util.player.Rs2Player
import net.runelite.client.plugins.microbot.util.bank.Rs2Bank
import net.runelite.client.plugins.microbot.util.gameobject.Rs2GameObject
import net.runelite.client.plugins.microbot.util.walker.Rs2Walker

object walking {

  def rt: IORuntime = {
    val runtime: IORuntime = IORuntime.global
    runtime
  }

  def walkBank: IO[Boolean] = {
    val res = for {
      bla <- IO(Rs2Bank.walkToBankAndUseBank())
    } yield(bla)
    res
  }

  def walkTo: IO[Unit] = {
    val furn = Rs2GameObject.get("furnace")
    for {
      _ <- IO(Rs2Walker.walkTo(furn.getWorldLocation))
      _ <- IO(Rs2Player.waitForWalking())
      _ <- IO(sleepUntil(() => !Rs2Player.isMoving, 15000))
      _ <- IO(!Rs2Player.isMoving)
     } yield(IO.unit)

  }

}
