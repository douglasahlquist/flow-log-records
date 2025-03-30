#!/bin/bash


JAR=target/Flow_Log_Records-0.0.1-SNAPSHOT-jar-with-dependencies.jar

java -jar $JAR com.ahlquist.app.WikiFileUtils main TagListFromWiki.csv tag_list.csv


