# OCE IAM

## Installation

### Install CouchDB

```
$ sudo apt-get install software-properties-common -y

$ sudo add-apt-repository ppa:couchdb/stable -y

$ sudo apt-get update -y

# remove any existing couchdb binaries
$ sudo apt-get remove couchdb couchdb-bin couchdb-common -yf

$ sudo apt-get install -V couchdb

  Reading package lists...
  Done Building dependency tree
  Reading state information...
  Done
  The following extra packages will be installed:
  couchdb-bin (x.y.z0-0ubuntu2) couchdb-common (x.y.z-0ubuntu2) couchdb (x.y.z-0ubuntu2)

# manage via upstart for 14.04
$ sudo stop couchdb
  couchdb stop/waiting
  
# update /etc/couchdb/local.ini with 'bind_address=0.0.0.0' as needed
$ sudo start couchdb
  couchdb start/running, process 3541

# manage via upstart for 14.04
$ sudo stop couchdb
  couchdb stop/waiting
  
# update /etc/couchdb/local.ini with 'bind_address=0.0.0.0' as needed
$ sudo start couchdb
  couchdb start/running, process 3541

# manage via systemd for 15.10 and newer
$ sudo systemctl stop couchdb

# update /etc/couchdb/local.ini with 'bind_address=0.0.0.0' as needed
# or add 'level=debug' to the [log] section
$ sudo systemctl start couchdb

# systemd is not very chatty so lets get a status
$ sudo systemctl status couchdb
● couchdb.service - Apache CouchDB
   Loaded: loaded (/lib/systemd/system/couchdb.service; enabled; vendor preset: enabled)
   Active: active (running) since Sun 2016-01-31 23:50:50 UTC; 5s ago
 Main PID: 3106 (beam.smp)
   Memory: 20.3M
      CPU: 394ms
   CGroup: /system.slice/couchdb.service
           ├─3106 /usr/lib/erlang/erts-7.0/bin/beam.smp -Bd -K true -A 4 -- -root /usr/lib/erlang -progname erl -- -home /var/lib/couchdb -- -noshell -noin...
           └─3126 sh -s disksup

Jan 31 23:50:50 u1 systemd[1]: Started Apache CouchDB.
Jan 31 23:50:50 u1 couchdb[3106]: Apache CouchDB 1.6.1 (LogLevel=info) is starting.
Jan 31 23:50:51 u1 couchdb[3106]: Apache CouchDB has started. Time to relax.
Jan 31 23:50:51 u1 couchdb[3106]: [info] [<0.33.0>] Apache CouchDB has started on http://127.0.0.1:5984/

```

### Install Maven and Tomcat

#### Install Java8 and Maven

OCE Service Wrapper Use Java8

```
$ sudo add-apt-repository ppa:webupd8team/java && sudo apt-get update -y && sudo apt-get install oracle-java8-installer -y

$ sudo apt-get install maven
```

기존에 jdk7 이 시스템에 인스톨 되어있는 경우, jdk8 을 디폴트 jdk 로 설정을 하실 수 있습니다.

```
$ update-java-alternatives -l
  java-1.7.0-openjdk-amd64 1071 /usr/lib/jvm/java-1.7.0-openjdk-amd64
  java-8-oracle 1072 /usr/lib/jvm/java-8-oracle
  
$ sudo update-java-alternatives -s java-8-oracle
```

jdk7 으로 되돌릴 경우 다음과 같은 에러가 나지만, 무시하셔도 됩니다.(정삭적으로 바뀜)

```
$ sudo update-java-alternatives -s java-1.7.0-openjdk-amd64
update-java-alternatives: plugin alternative does not exist: /usr/lib/jvm/java-7-openjdk-amd64/jre/lib/amd64/IcedTeaPlugin.so
```

#### Install Tomcat8

tomcat 유저를 시스템에 추가합니다.
```
$ sudo groupadd tomcat
$ sudo useradd -s /bin/false -g tomcat -d /opt/tomcat tomcat
```

소스를 다운받습니다.

```
$ sudo wget http://mirror.navercorp.com/apache/tomcat/tomcat-8/v8.0.36/bin/apache-tomcat-8.0.36.tar.gz
```

다음 명령어로 압축을 풉니다.

```
$ sudo mkdir /opt/tomcat
$ sudo tar xvf apache-tomcat-8*tar.gz -C /opt/tomcat --strip-components=1
```

권한을 업데이트합니다.

```
$ cd /opt/tomcat
$ sudo chgrp -R tomcat conf
$ sudo chmod g+rwx conf
$ sudo chmod g+r conf/*
$ sudo chown -R tomcat work/ temp/ logs/
```

#### Install Upstart Script

Default JDK 를 8 버젼으로 설정합니다.

```
$ sudo update-alternatives --config java

There are 2 choices for the alternative java (providing /usr/bin/java).

  Selection    Path                                            Priority   Status
------------------------------------------------------------
* 0            /usr/lib/jvm/java-8-oracle/jre/bin/java          1072      auto mode
  1            /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java   1071      manual mode
  2            /usr/lib/jvm/java-8-oracle/jre/bin/java          1072      manual mode
  
Press enter to keep the current choice[*], or type selection number: 
```

스타트 스크립트를 등록합니다.

```
$ sudo vi /etc/init/tomcat.conf

description "Tomcat Server"

  start on runlevel [2345]
  stop on runlevel [!2345]
  respawn
  respawn limit 10 5

  setuid tomcat
  setgid tomcat

  env JAVA_HOME=/usr/lib/jvm/java-8-oracle/jre
  env CATALINA_HOME=/opt/tomcat

  # Modify these options as needed
  env JAVA_OPTS="-Djava.awt.headless=true -Djava.security.egd=file:/dev/./urandom"
  env CATALINA_OPTS="-Xms512M -Xmx1024M -server -XX:+UseParallelGC"

  exec $CATALINA_HOME/bin/catalina.sh run

  # cleanup temp directory after stop
  post-stop script
    rm -rf $CATALINA_HOME/temp/*
  end script
```

톰캣을 시작합니다

```
$ sudo initctl reload-configuration
$ sudo initctl start tomcat
```

톰캣 종료

```
$ sudo initctl stop tomcat
```


### Download

```
$ git clone https://github.com/SeungpilPark/OCEIAM-SERVICEWARRPER
```

### Database Configuration

서비스 래퍼의 데이터베이스는 별도의 테넌트 구분을 하지 않고, 하나의 시스템이 한개의 CouchDB 데이터베이스를 사용합니다.

따라서 다수의 서비스 래퍼 시스템을 운영 할 경우 CouchDB 내에 각기 다른 데이터베이스를 준비하시기 바랍니다.

예)forcs-wrapper1,forcs-wrapper2,forcs-wrapper3...

다운받은 소스 코드의 oceIAM/iam-web/src/main/webapp/WEB-INF/config.properties 경로의 파일을 수정합니다.

```
$ sudo vi oceIAM/iam-web/src/main/webapp/WEB-INF/config.properties

.
.
###########################################
## DataSource Configuration
###########################################

couch.db.url=http://192.168.135.181:5984
couch.db.username=
couch.db.password=
couch.db.database=forcs_wrapper
couch.db.autoview=true
```

 - couch.db.url : 카우치db 호스트
 
 - couch.db.username : 카우치db 사용자
 
 - couch.db.password : 패스워드
 
 - couch.db.database : 데이터 베이스 이름
 
 - couch.db.autoview : 뷰 오토 제너레이션
 
카우치 db에 별도의 인증을 설정하지 않았다면 username 과 password 는 공란입니다. 

### Admin Configuration

```
###########################################
## System Administrator Configuration
###########################################

system.admin.username=support@iam.co.kr
system.admin.password=admin
```

서비스 래퍼는 스탠드어론 시스템으로서 하나의 관리자 계정만 제공합니다.
UI 에서 로그인 할때 쓰이는 계정입니다.

 - system.admin.username : 관리자 아이디
 
 - system.admin.password : 관리자 이름


### Build and Launch

```
$ sudo initctl stop tomcat

$ sudo chown -R tomcat:tomcat /opt/tomcat/webapps
$ sudo rm -rf /opt/tomcat/webapps/ROOT
$ sudo rm -rf /opt/tomcat/webapps/ROOT.war 

$ cd OCEIAM-SERVICEWARRPER

$ mvn clean install

$ sudo cp iam-wrapper/target/iam-wrapper-1.0.0-SNAPSHOT.war /opt/tomcat/webapps/ROOT.war

$ sudo chmod +x /opt/tomcat/webapps/ROOT.war

$ sudo initctl start tomcat
```

실행된 서버의 /service-console/index 주소로 접근해봅니다.

초기 로그인은 support@iam.co.kr  /   admin  입니다.





