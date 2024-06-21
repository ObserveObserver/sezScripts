# sezScripts
scala scripts for Microbot, all WIP, all (mostly!) pure fp scala. when they're tidy ill release a jar and the pom.xml's required.

probably requires compilation under java 22. if using 17, i think it should work since it's scala 2.13.14, but change the lombok version in pom.xml if it doesn't compile.

pom.xml is now included, it contains stuff like cats and scalaj for the LLM autotyper i'm making. feel free to remove what isn't needed if picking and choosing.

ill probably put classes in here sometime so ppl don't have to compile haha....

for future reference:

when compiling, lombok hates scala. but it's possible to integrate scala into your lombok'd java, esp with a java helper. in these, the plugin files serve as lombok helpers and allow us to pass getters. 

also sometimes things will just break for no reason, running Runelite lifecycle install and refreshing maven generally helps. rarely it doesn't and you're SOL and have to remerge. it's awesome.

your pom.xml needs scala cats (version which does NOT have scala 3 support or it will not compile; goes for all dependencies!), cats effect and scala 2.13.14. 

In progress:

1. LLM-based autotyper/converser. Works, just need to implement more API's than deepseek (though I have no keys so deepseek may be it for now, at least it's cheap as shit)

Script ideas:

1. Barrows
2. Pickpocketing (high banrate activity, think I can figure out a way)
