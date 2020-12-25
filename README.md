# Dexecutor
[![Build Status](https://travis-ci.org/dexecutor/dexecutor-core.svg?branch=master)](https://travis-ci.org/dexecutor/dexecutor-core)
[![Coverage Status](https://coveralls.io/repos/github/dexecutor/dexecutor-core/badge.svg?branch=master)](https://coveralls.io/github/dexecutor/dexecutor-core?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.dexecutor/dexecutor-core/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.dexecutor/dexecutor-core)
[![Dependency Status](https://www.versioneye.com/user/projects/57cafb94939fc6004abe4b21/badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/57cafb94939fc6004abe4b21)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Gitter](https://badges.gitter.im/dexecutor/dependent-tasks-executor.svg)](https://gitter.im/dexecutor?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)


Executing dependent/Independent tasks in a reliable way, is made so easy that even grandma can do it.

Refer [wiki](https://github.com/dexecutor/dexecutor-core/wiki) or [Project Web site](https://dexecutor.github.io/) for more Details.


| Stable Release Version | JDK Version compatibility | Release Date |
| ------------- | ------------- | ------------|
| 2.1.2  | 1.8+ | 12/25/2020 |
| 1.1.2 | 1.7+ | 10/15/2016 |

## License

Dexecutor is licensed under **Apache Software License, Version 2.0**.

## News
* Version **2.1.2** released on 12/25/2020.
* Version **2.1.1** released on 01/17/2020.
* Version **2.1.0** released on 11/23/2018.
* Version **2.0.2** released on 01/27/2018.
* Version **2.0.1** released on 03/15/2017.
* Version **2.0.0** released on 11/01/2016.
* Version **1.1.2** released on 10/15/2016.
* Version **1.1.1** released on 10/08/2016.
* Version **1.1.0** released on 10/06/2016.
* Version **1.0.3** released on 09/16/2016.
* Version **1.0.2** released on 09/10/2016.
* Version **1.0.1** released on 09/05/2016.
* Version **1.0.0** released on 09/03/2016.
* Version **0.0.4** released on 08/25/2016.
* Version **0.0.3** released on 08/23/2016.
* Version **0.0.2** released on 08/21/2016.
* Version **0.0.1** released on 08/20/2016.

## 2.1.2

* Able to get any tasks results from within any other task `this.getResult(id)` you can call this from with in `Task.execute`

## Maven Repository

Dexecutor is deployed at sonatypes open source maven repository. You may use the following repository configuration (if you are interested in snapshots)

```xml
<repositories>
     <repository>
         <id>dexecutor-snapshots</id>
         <snapshots>
             <enabled>true</enabled>
         </snapshots>
         <url>https://oss.sonatype.org/content/groups/public/</url>
     </repository>
</repositories>
```
This repositories releases will be synched to maven central on a regular basis. Snapshots remain at sonatype.

Alternatively you can  pull Dexecutor from the central maven repository, just add these to your pom.xml file:
```xml
<dependency>
  <groupId>com.github.dexecutor</groupId>
  <artifactId>dexecutor-core</artifactId>
  <version>2.1.1</version>
</dependency>
```

## BUILDING from the sources

As it is maven project, buidling is just a matter of executing the following in your console:

	mvn package

This will produce the dexecutor-core-VERSION.jar file under the target directory.

## Support
If you need help using Dexecutor feel free to drop an email or create an issue in github.com (preferred)

## Contributions
To help Dexecutor development you are encouraged to  
* Provide suggestion/feedback/Issue
* pull requests for new features
* Star :star2: the project


[![View My profile on LinkedIn](https://static.licdn.com/scds/common/u/img/webpromo/btn_viewmy_160x33.png)](https://in.linkedin.com/pub/nadeem-mohammad/17/411/21)
	
	
