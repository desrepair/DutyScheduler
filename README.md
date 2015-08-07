# DutyScheduler
## Scheduler designed to distribute days of various point values evenly.

Duty scheduler is a scheduler designed for fairly distributing duty days of varying point value amongst a set of
Residential Advisers. It is suitable for general duty assignment purposes, but for the purposes of this README
human resources will be referred to as RAs.

## Features:
    * Assigns duty within a set range of calendar dates.
    * Supports duty blocks of customizable length and assigned RAs.
    * Duty blocks can have varying point values based on day of week or custom - set.
    * Each RA can have "blackout dates" where they shall not be scheduled for duty.
    * The scheduler will attempt to assign duty to the RA with the least duty points accumulated.
    * If multiple RAs have the same number of duty points, the RA who had their last duty the furthest back is given duty.
    * Supports exporting of scheduled calendars to Google Calendar.

## Setup

### Dependencies:
    * Gradle (https://gradle.org)
    * Spark Framework (http://www.sparkjava.com)

## Important Notice: WIP

This is a work in progress and features may change without notice.

## Copyright

Duty Scheduler: A scheduler for point based duty.
Copyright (c) 2015 Khetthai Laksanakorn

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

For source code and contact information for Khetthai Laksanakorn,
see <http://www.github.com/desrepair/DutyScheduler>.