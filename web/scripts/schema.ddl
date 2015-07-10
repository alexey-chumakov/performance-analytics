USE `performance_analytics`;

DROP TABLE IF EXISTS `REQUEST_DETAILS`;
DROP TABLE IF EXISTS `REQUEST`;
CREATE TABLE `REQUEST` (
  `id`          BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `TIMESTAMP`   BIGINT(20)   NOT NULL,
  `YEAR`        INT          NOT NULL,
  `MONTH`       INT          NOT NULL,
  `DAY`         INT          NOT NULL,
  `HOUR`        INT          NOT NULL,
  `MINUTE`      INT          NOT NULL,
  `APP_NAME`    VARCHAR(50)  NOT NULL,
  `SERVER_ID`   VARCHAR(50)  NOT NULL,
  `URL`         VARCHAR(500) NOT NULL,
  `COUNT`       BIGINT(20)   NOT NULL,
  `DURATION`    BIGINT(20)   NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `REQUEST_DAY_INDEX` (`YEAR`, `MONTH`, `DAY`)
)
  ENGINE =InnoDB
  AUTO_INCREMENT =1
  DEFAULT CHARSET =utf8;

CREATE TABLE `REQUEST_DETAILS` (
  `id`          BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `REQUEST_ID`  BIGINT(20)   NOT NULL,
  `SYSTEM_NAME` VARCHAR(50)  NOT NULL,
  `COUNT`       BIGINT(20)   NOT NULL,
  `DURATION`    BIGINT(20)   NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`REQUEST_ID`) REFERENCES `REQUEST` (`id`)
)
  ENGINE =InnoDB
  AUTO_INCREMENT =1
  DEFAULT CHARSET =utf8;