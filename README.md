# sezScripts
scala scripts for Microbot, all WIP, all (mostly!) pure fp scala. when they're tidy ill release a jar and the pom.xml's required.

works at least under java 17+

just add folders into net.runelite.client.plugins.microbot

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
2. GOTR (probably next up, since someone is working on barrows but ill probably still do barrows in future)

LLM discussion:

Why? isn't it expensive?: You can get away with not using many tokens for Runescape discussions. The script will speak 2-3 lines every hour, it ends up being like 300-600 tokens/h, most if it being the very long prompt (the prompt that will be included by default is long, but tries its best to make it very hard to prompt hack.)

Atm, stuff like "end the scenario" that works with 99% of chatbots doesn't break it, asking it random questions about history or math will not cause it to start spewing about the history of warlord-era china or something. Getting a prompt such that it types like a real player, doesn't break and is able to generate discussion has been more difficult than I thought; though, I think I've stumbled onto a good one tentatively.
