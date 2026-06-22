# The Last Dragon

This repository hosts our team's submission for the June 2026 HytaleModding mod jam. Meant for the release branch.

The Last Dragon is largely a map, but you'll also find:

- extensions for trigger volume effect and conditions, for features not found within vanilla trigger volumes (Release 0.5.6)

- a few assets to enhance the experience, either made by ourselves or sourced from third parties where the license is appropriate

- utilities related to instancing, to make everyone's lives easier


In the interest of transparency, as the Puzzle Experience theme is restricted to trigger volumes, we use Java for the following (besides instancing logic):

 - the Sokoban puzzle, to simplify complex state management and certain kinds of interactions, while still integrating with trigger volumes through effects,

 - the 'Red Light Green Light' puzzle, to check a player's historical positions which the vanilla trigger conditions cannot do,

 - to set a player's camera (unused in final submission - changing a player's camera inconsistently resulted in a client-side NPE),

 - a trigger effect that removes the player from the instance, used only at the end of the map.

Everything else was made with trigger volumes as-is, for release version 0.5.6.

# Team Members
@Cobrinthine
@Drakonkinst
@VegetalDev

## SFX Credits

* Mushroom Bounce
  * bubbling boing 0-H16j by Setuniman -- https://freesound.org/s/146266/ -- License: Attribution NonCommercial 4.0
  * Ruler long bounce by bolkmar -- https://freesound.org/s/489364/ -- License: Creative Commons 0
* Wind
  * windloop.wav by warwickallison -- https://freesound.org/s/115759/ -- License: Attribution 4.0
  * wind.wav by ERH -- https://freesound.org/s/34338/ -- License: Attribution NonCommercial 4.0
  * Wind Gust by crashoverride6 -- https://freesound.org/s/146932/ -- License: Creative Commons 0
  * Woosh by florianreichelt -- https://freesound.org/s/683096/ -- License: Creative Commons 0
* Geyser
  * geyser1.mp3 by mariethompson -- https://freesound.org/s/516934/ -- License: Creative Commons 0
  * steam sfx.wav by adr1911 -- https://freesound.org/s/542585/ -- License: Creative Commons 0