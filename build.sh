#!/bin/sh

sbt ';clean;++2.11.9;compile'
sbt ';clean;++2.11.10;compile'
sbt ';clean;++2.11.11;compile'
sbt ';clean;++2.12.0;compile'
sbt ';clean;++2.12.1;compile'
sbt ';clean;++2.12.2;compile'
sbt ';clean;++2.12.3;compile'
sbt ';clean;++2.13.0-M2;compile'
