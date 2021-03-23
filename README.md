# Project2-2

List of skills are stored in skills.txt

All skills are customizable.

### List of actions:
#### Fetch
Fetches items from a database according to limiters based on the item's attributes.
The items that can be fetched are lectures, events and notifications.
The lists of items are stored in .csv files.

A "lecture" item stores 4 attributes; the course, the date, the time and any extra text. 
The skill "lectures on <DAY>" fetches all of the lectures that fall on the next "<DAY>", the <DAY> depending on the user's input. So the input "lectures on Monday" returns all lectures that happen next Monday.
The skill "all <COURSE> lectures" fetches all of the lectures for that course.
Limiters can be doubled up, the skill "<COURSE> lectures on <DATE>" fetches all lectures in that course on that date.
For example if a user inputs "MM lectures next Monday", it will return all Mathematical Modelling lectures on the next Monday.

#### Add
The add action adds an item into the respective .csv.

#### Open
The user can open websites or folders on their computer.

#### Set
Sets a timer according to hh:mm or hh:mm:ss format.

### Skill editor
Usage of the skill editor differs for fetch.
##### Fetch
1. Select "Fetch" from the list of actions.
2. Select which item you want to fetch.
3. Enter in your command, if you want to add in user inputs select one from the drop down menu and click "Enter input".
4. Click "Parse" to parse your command for possible limiters.
5. Choose a limiter from the drop down menu and then click add. You can add as many limiters as you want.
6. Click generate.
##### All other actions
1. Select your chosen action
2. Enter in your command and add user inputs
