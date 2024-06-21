# sezScripts
scala scripts for Microbot, all WIP, all (mostly!) pure fp scala. when they're tidy ill release a jar and the pom.xml's required.

pom.xml is now included, it contains stuff like cats and scalaj for the LLM autotyper i'm making. feel free to remove what isn't needed if picking and choosing.

for future reference:

when compiling, lombok hates scala. but it's possible to integrate scala into your lombok'd java, esp with a java helper. in these, the plugin files serve as lombok helpers and allow us to pass getters. 

also sometimes things will just break for no reason, running Runelite lifecycle install and refreshing maven generally helps. rarely it doesn't and you're SOL and have to remerge. it's awesome.

your pom.xml needs scala cats (version which does NOT have scala 3 support or it will not compile; goes for all dependencies!), cats effect and scala 2.13.14. 

when i'm comfortable with how scripts are, i'll release my pom.xml i use to compile against. until then **probably dont use these** unless ur willing to go thru the headache.

Script ideas:

1. Barrows
2. Pickpocketing (high banrate activity, think I can figure out a way)
3. LLM-based auto-converse (tier-1 priority). this sounds cringe, but there's ways to generate real-sounding stuff with enough prompt hacking. leaning towards deepseek, as API access is free but there will be multiple options.
