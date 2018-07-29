# VideoConverter
A Video Converter that is open source and being developed with a backend rooted in ffmpeg
# TODO
* Switch UI to Java FX
  * This will fix many issues that the interface currently has
* Have active monitoring for overwrite, and prompt user if they want to overwrite
* Add Option to, instead of making a new file for every encode, to overwrite the old file
* Fix Issue where instead of making the new file be the same name with -c it concatenates the same name onto itself with -c
  * Split doesnt like Ed Sheeran _ Eraser (Live) [Extended F64 Version] - #SBTV10-pb2fwx4O_Ks.webm
  * So absolutepath doesnt strip it off
