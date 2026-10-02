This repository contains a set of web applications that manage and request the omnisport database. Currently two instances of this database are available
as a Docker image: mwdf/omnisportdb and mwdf/omnisportdb-sqlserver.

**Build**

The repository has an instance of gradle-wrapper included. To create the war files you can run `.\gradlew war` from the repository's root. 
This will create the war-files under the `build\libs` directories of all modules that define a web application. To create a war file for a 
single web application you can navigate to its module directory and run `..\gradlew war` from there. The most common way of deployment is in a
Tomcat web server. There is a Docker image available that contains all war files in a Tomcat instance: mwdf/omnisportweb. 

**Applications**

**alcifosports**

Management application for 'alcifosports' which are sports like athletics and cycling that don't involve matches between players or teams.
Alcifo is an acronym for 'Altius, citius, fortius'. 

**alias**

Management application dedicated to the 'alias' entity. This defines time-dependent string representations of 'named' entities like people and teams.

**api**

API that serves output to external users. There is no endpoint definition publicly available yet, but the entry point is the parameterless sportlist:
`http://localhost:8080/api/json/sportlist/Marijn-Willem/M2U`. Here the last two parameters are the username and the password for the admin user defined in
the Docker images of the database. Note that every endpoint supports three content types: JSON, XML and YAML. 
You can replace the `json` parameter in the example URL by `xml` and `yaml` to get the response in the corresponding content type. 

**cyclingroad**

Management application for the cycling road sport specifically.

**darts**

Management application for the darts sport specifically.

**flush**

Application that allows one to manually flush data from the API cache.

**h2hsports**

Management application for 'h2hsports'. These are sports like tennis and badminton that involve matches between either players or doubles.

**management**

Management application for basic entities like people, teams and competitions.

**speedskating**

Management application for the speed skating sport specifically.

**teamsports**

Management application for team sports.
