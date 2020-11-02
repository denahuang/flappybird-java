Bugs:
- Sometimes, if you die, during the next (few) round(s),
  the bird dies when going between the pipes. This could be
  either because of the pipes' measurements not being
  transferred properly, or because there is lag when 
  the bird jumps. HOWEVER, if you change the variable
  SPACE to a larger number (originally 120; example 300), 
  then this error does not occur as frequently.
- There is a slight lag sometimes when the bird jumps
  (could be the reasoning for the previous bug)
- Scoreboard does not display "NEW" if you get a new high
  score on the first try. Works if you get a new high score
  afterwards.
- Sometimes, there is some flickering with the graphics
  (but it could also be the lag).

Special Instructions:
- Bird is square for copyright issues
- Before playing, make sure that bestScore.txt has a single
  line of numbers (should be 0 when first played)
- When you die, make sure that you do not press the 
  space bar, or else the scoreboard will show for a 
  very brief moment and then the game will start 
  again (and you will not be able to see your score).
  I tried to disable the keyboard through setEnabled(false).
  It worked to disable the keyboard, but it still saved
  the fact that the key was pressed.

What I Did Not Accomplish:
- different difficulty levels, bird colors, and background 
  colors
- using the mouse to play as well (I decided that my game
  would be a "keyboard-only" game)
- more than one player mode

What I Added:
- beginning menu and scoreboard
- a live score counter
- a "high score" record book
- sound effects
- other graphics (eg. instructional signs)
- 