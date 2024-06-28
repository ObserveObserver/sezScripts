sezCooking

atm supports pretty much everywhere, including rogues den and nardah clay oven. food has to be changed in script because Rs2Inventory was being weird. ill fix it soon.

Features:

Rogues den, anywhere with a range or clay oven, I think. Bank has to be close for now or on screen, i think. Once i figure out Rs2Camera, it won't matter.

gaussian sleeps based on a skewed normal distribution. skew will be adjusted off runtime in future to simulate fatigue.

Usage: 

**Create a list in the settings of your desired food, delimited by ",".**

**i.e.: raw shark,raw tuna**

**ALTERNATIVELY, for more simple startup, start with a raw food in your inv and it'll use that.**

--

**confirmed locations it works**:

rogues guild, cooks guild, nardah, hosidius kitchen

it should work everywhere the range is close, tho.

TODO features:

1. Sleep power ratio setting. If your ping is high, you can set a slider to give the sleeps a little more leeway.

2. Making pies (inventory management)

3. refactor "food: Int" to use Inventory monoid (important for pies in future)
