-- DROP DATABASE iam;
CREATE DATABASE iam_wrapper
  CHARACTER SET UTF8
  COLLATE UTF8_GENERAL_CI;

DROP USER 'iam_wrapper'@'localhost';
CREATE USER 'iam_wrapper'@'localhost'
  IDENTIFIED BY 'iam_wrapper';
GRANT ALL PRIVILEGES ON *.* TO 'iam_wrapper'@'localhost';
FLUSH PRIVILEGES;

USE iam_wrapper;

DROP TABLE IF EXISTS iam_wrapper.IAM;

CREATE TABLE IF NOT EXISTS iam_wrapper.IAM
(
  host         VARCHAR(255),
  port         INT(11),
  clientKey    VARCHAR(255),
  clientSecret VARCHAR(255),
  PRIMARY KEY (clientKey)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

INSERT INTO iam_wrapper.IAM (`host`, `port`, `clientKey`, `clientSecret`) VALUES ('localhost', '8080', '', '');

DROP TABLE IF EXISTS iam_wrapper.URIS;

CREATE TABLE IF NOT EXISTS iam_wrapper.URIS
(
  id        BIGINT NOT NULL AUTO_INCREMENT,
  uri       VARCHAR(255),
  runWith   VARCHAR(50),
  method    VARCHAR(255),
  wid       BIGINT,
  className VARCHAR(255),
  regDate   TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

DROP TABLE IF EXISTS iam_wrapper.WORKFLOW;

CREATE TABLE IF NOT EXISTS iam_wrapper.WORKFLOW
(
  id           BIGINT NOT NULL AUTO_INCREMENT,
  name         VARCHAR(255),
  designer_xml LONGTEXT,
  bpmn_xml     LONGTEXT,
  vars         LONGTEXT,
  regDate      TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
  steps        INT(11)         DEFAULT 2,
  status       VARCHAR(20),
  PRIMARY KEY (id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

DROP TABLE IF EXISTS iam_wrapper.WORKFLOW_HISTORY;

CREATE TABLE IF NOT EXISTS iam_wrapper.WORKFLOW_HISTORY (
  id                   BIGINT NOT NULL AUTO_INCREMENT,
  request              LONGTEXT,
  response             LONGTEXT,
  identifier           VARCHAR(255),
  wid                  BIGINT,
  name                 VARCHAR(255),
  vars                 LONGTEXT,
  startDate            TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
  endDate              TIMESTAMP,
  duration             INT(11),
  steps                INT(11)         DEFAULT 0,
  current              INT(11)         DEFAULT 0,
  task_id              VARCHAR(255),
  task_name            VARCHAR(255),
  status               VARCHAR(20),
  sf_parent_identifier VARCHAR(255),
  sf_root_identifier   VARCHAR(255),
  sf_depth             INT(11)         DEFAULT 0,
  sf_task_id           VARCHAR(255),
  PRIMARY KEY (id),
  UNIQUE KEY (identifier)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;


CREATE TABLE IF NOT EXISTS iam_wrapper.TASK_HISTORY (
  id         BIGINT NOT NULL AUTO_INCREMENT,
  identifier VARCHAR(255),
  wid        BIGINT,
  task_id    VARCHAR(255),
  name       VARCHAR(255),
  startDate  TIMESTAMP,
  endDate    TIMESTAMP,
  duration   INT(11),
  status     VARCHAR(20),
  stdout     LONGTEXT,
  stderr     LONGTEXT,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;


