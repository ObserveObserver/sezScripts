cooking

atm supports pretty much everywhere, including rogues den. food has to be changed in script because Rs2Inventory was being weird. ill fix it soon.

Features:

Rogues den, anywhere with a range I think. Bank has to be close for now or on screen. Once i figure out Rs2Camera, it won't matter.

TODO features:

1. Scripted lists in advanced settings, [Shark,Tuna] where if you're out of sharks, it'll do tunas next.

2. Gaussian sleeps. normalSleep(min,avg,max) (banks idea, though I want to push it farther)
 It's there, I just have to test it more and tweak it (3 standard deviations seems too consistent lol)
2.5. Gaussian sleeps which indicate player fatigue (longer sleeps the longer the bot goes)
2.75. Lopsided, more flat distribution over time to indicate longer break cycles.
2.97. This will be included as a library function.

3. Sleep power ratio setting. If your ping is high, you can set a slider to give the sleeps a little more leeway.

4. Automatic detection of food in simple mode.

5. Break handling, I will make this into it's own library.

6. Making pies (inventory management)
