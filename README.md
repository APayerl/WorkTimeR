# WorkTimeR #
WorkTimeR is used to get access to your schedule through the terminal.

## Installation ##
The first time the program i started it automatically creates a default configuration.
The configuration is located in the users `$HOME/.worktimer/config.yaml`

## Commandline options ##
    --today         Outputs todays schedule 
    --yesterday     Outputs yesterdays schedule
    --tomorrow      Outputs tomorrows schedule
    --all           Outputs all defined schedules
    --week-n 27     Outputs the schedule for a specific week
    --this-week     Outputs this weeks schedule
    --next-week     Outputs next weeks schedule
    --last-week     Outputs last weeks schedule

## Configuration ##
The configuration is made in a YAML file located in the `$HOME/.worktimer/config.yaml`.
The `default` tag contains the default schedule while the `special` tag contains a list of all non standard times. 
The special entries will override the default based on the `validBetweenWeeks` attribute. 
A special entry with only `validBetweenWeeks` will be interpreted as a completely off week.
