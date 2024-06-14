cooking

atm supports pretty much everywhere, including rogues den and nardah clay oven. food has to be changed in script because Rs2Inventory was being weird. ill fix it soon.

Features:

Rogues den, anywhere with a range I think. Bank has to be close for now or on screen. Once i figure out Rs2Camera, it won't matter.

gaussian sleeps based on a skewed normal distribution. skew will be adjusted off runtime in future to simulate fatigue.

TODO features:

1. Scripted lists in advanced settings, [Shark,Tuna] where if you're out of sharks, it'll do tunas next.

5. Sleep power ratio setting. If your ping is high, you can set a slider to give the sleeps a little more leeway.

6. Automatic detection of food in simple mode.

7. Break handling, I will make this into it's own library.

8. Making pies (inventory management)

9. refactor "food: Int" to use Inventory monoid
