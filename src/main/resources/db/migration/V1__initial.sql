CREATE TABLE IF NOT EXISTS `employee` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `first_name` VARCHAR(45) NOT NULL,
  `last_name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC) VISIBLE
);

CREATE TABLE IF NOT EXISTS `phone` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `phone_employee` (
  `phone_id` INT NOT NULL,
  `employee_id` INT NOT NULL,
  `booked_at` TIMESTAMP NOT NULL,
  `released_at` TIMESTAMP NULL DEFAULT NULL
);

ALTER TABLE `phone_employee`
ADD UNIQUE INDEX `phone_booked_unique` (`phone_id` ASC, `released_at` ASC);

ALTER TABLE `phone_employee`
ADD INDEX `FK_employee_idx` (`employee_id` ASC) VISIBLE;

ALTER TABLE `phone_employee`
ADD CONSTRAINT `FK_phone`
  FOREIGN KEY (`phone_id`)
  REFERENCES `phone` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `FK_employee`
  FOREIGN KEY (`employee_id`)
  REFERENCES `employee` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

INSERT INTO `phone` (`name`) VALUES ('Samsung Galaxy S9');
INSERT INTO `phone` (`name`) VALUES ('Samsung Galaxy S8');
INSERT INTO `phone` (`name`) VALUES ('Samsung Galaxy S8');
INSERT INTO `phone` (`name`) VALUES ('Motorola Nexus 6');
INSERT INTO `phone` (`name`) VALUES ('Oneplus 9');
INSERT INTO `phone` (`name`) VALUES ('Apple iPhone 13');
INSERT INTO `phone` (`name`) VALUES ('Apple iPhone 12');
INSERT INTO `phone` (`name`) VALUES ('Apple iPhone 11');
INSERT INTO `phone` (`name`) VALUES ('iPhone X');
INSERT INTO `phone` (`name`) VALUES ('Nokia 3310');