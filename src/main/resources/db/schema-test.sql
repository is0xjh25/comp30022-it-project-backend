-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema testdb2
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema testdb2
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `testdb2`;
CREATE SCHEMA `testdb2` DEFAULT CHARACTER SET utf8 ;
USE `testdb2` ;

-- -----------------------------------------------------
-- Table `testdb2`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `testdb2`.`user` (
                                               `id` INT NOT NULL AUTO_INCREMENT,
                                               `email` VARCHAR(255) NOT NULL,
                                               `password` VARCHAR(255) NOT NULL,
                                               `first_name` VARCHAR(45) NOT NULL,
                                               `middle_name` VARCHAR(45) NULL,
                                               `last_name` VARCHAR(45) NOT NULL,
                                               `phone` VARCHAR(45) NOT NULL,
                                               `recent_activity` DATETIME,
                                               `website` VARCHAR(255) NULL,
                                               `description` LONGTEXT NULL,
                                               `status` ENUM('active', 'deleted', 'pending') NOT NULL DEFAULT 'active',
                                               PRIMARY KEY (`id`),
                                               UNIQUE INDEX `name_UNIQUE` (`id` ASC),
                                               UNIQUE INDEX `email_UNIQUE` (`email` ASC))
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `testdb2`.`organization`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `testdb2`.`organization` (
                                                       `id` INT NOT NULL AUTO_INCREMENT,
                                                       `name` VARCHAR(255) NOT NULL,
                                                       `owner` INT NOT NULL,
                                                       `status` ENUM('active', 'deleted') NOT NULL DEFAULT 'active',
                                                       PRIMARY KEY (`id`),
                                                       UNIQUE INDEX `name_UNIQUE` (`name` ASC) ,
                                                       UNIQUE INDEX `id_UNIQUE` (`id` ASC) ,
                                                       INDEX `fk_orgnization_client_idx` (`owner` ASC) ,
                                                       CONSTRAINT `fk_orgnization_client`
                                                           FOREIGN KEY (`owner`)
                                                               REFERENCES `testdb2`.`user` (`id`)
                                                               ON DELETE NO ACTION
                                                               ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `testdb2`.`department`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `testdb2`.`department` (
                                                     `id` INT NOT NULL AUTO_INCREMENT,
                                                     `name` VARCHAR(255) NOT NULL,
                                                     `organization_id` INT NOT NULL,
                                                     `status` ENUM('active', 'deleted') NOT NULL DEFAULT 'active',
                                                     PRIMARY KEY (`id`),
                                                     UNIQUE INDEX `id_UNIQUE` (`id` ASC) ,
                                                     INDEX `fk_department_orgnization1_idx` (`organization_id` ASC) ,
                                                     CONSTRAINT `fk_department_orgnization1`
                                                         FOREIGN KEY (`organization_id`)
                                                             REFERENCES `testdb2`.`organization` (`id`)
                                                             ON DELETE NO ACTION
                                                             ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `testdb2`.`contact`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `testdb2`.`contact` (
                                                  `id` INT NOT NULL AUTO_INCREMENT,
                                                  `department_id` INT NOT NULL,
                                                  `email` VARCHAR(255) NOT NULL,
                                                  `first_name` VARCHAR(45) NOT NULL,
                                                  `middle_name` VARCHAR(45) NULL,
                                                  `last_name` VARCHAR(45) NOT NULL,
                                                  `phone` VARCHAR(45) NULL,
                                                  `description` LONGTEXT NULL,
                                                  `gender` ENUM('male', 'female', 'not specified') NULL,
                                                  `birthday` DATE NULL,
                                                  `address` VARCHAR(255) NULL,
                                                  `organization` VARCHAR(255) NULL,
                                                  `customer_type` ENUM('company', 'personal') NULL,
                                                  `status` ENUM('active', 'deleted') NOT NULL DEFAULT 'active',
                                                  PRIMARY KEY (`id`),
                                                  UNIQUE INDEX `name_UNIQUE` (`id` ASC) ,
                                                  INDEX `fk_customer_department1_idx` (`department_id` ASC) ,
                                                  CONSTRAINT `fk_customer_department1`
                                                      FOREIGN KEY (`department_id`)
                                                          REFERENCES `testdb2`.`department` (`id`)
                                                          ON DELETE NO ACTION
                                                          ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `testdb2`.`event`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `testdb2`.`event` (
                                                `id` INT NOT NULL AUTO_INCREMENT,
                                                `user_id` INT NOT NULL,
                                                `start_time` DATETIME NOT NULL,
                                                `finish_time` DATETIME NOT NULL,
                                                `description` LONGTEXT NULL,
                                                `status` ENUM('to do', 'in progress', 'done') NOT NULL DEFAULT 'todo',
                                                PRIMARY KEY (`id`),
                                                UNIQUE INDEX `id_UNIQUE` (`id` ASC) ,
                                                INDEX `fk_order_client1_idx` (`user_id` ASC) ,
                                                CONSTRAINT `fk_order_client10`
                                                    FOREIGN KEY (`user_id`)
                                                        REFERENCES `testdb2`.`user` (`id`)
                                                        ON DELETE NO ACTION
                                                        ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `testdb2`.`attend`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `testdb2`.`attend` (
                                                 `id` INT NOT NULL AUTO_INCREMENT,
                                                 `event_id` INT NOT NULL,
                                                 `contact_id` INT NOT NULL,
                                                 PRIMARY KEY (`id`),
                                                 UNIQUE INDEX `id_UNIQUE` (`id` ASC) ,
                                                 INDEX `fk_attend_event1_idx` (`event_id` ASC) ,
                                                 INDEX `fk_attend_customer1_idx` (`contact_id` ASC) ,
                                                 CONSTRAINT `fk_attend_event1`
                                                     FOREIGN KEY (`event_id`)
                                                         REFERENCES `testdb2`.`event` (`id`)
                                                         ON DELETE NO ACTION
                                                         ON UPDATE NO ACTION,
                                                 CONSTRAINT `fk_attend_customer1`
                                                     FOREIGN KEY (`contact_id`)
                                                         REFERENCES `testdb2`.`contact` (`id`)
                                                         ON DELETE NO ACTION
                                                         ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `testdb2`.`belong_to`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `testdb2`.`belong_to` (
                                                    `id` INT NOT NULL AUTO_INCREMENT,
                                                    `user_id` INT NOT NULL,
                                                    `organization_id` INT NOT NULL,
                                                    `status` ENUM('active', 'deleted') NOT NULL DEFAULT 'active',
                                                    INDEX `fk_belong_to_client1_idx` (`user_id` ASC) ,
                                                    INDEX `fk_belong_to_orgnization1_idx` (`organization_id` ASC) ,
                                                    PRIMARY KEY (`id`),
                                                    UNIQUE INDEX `id_UNIQUE` (`id` ASC) ,
                                                    CONSTRAINT `fk_belong_to_client1`
                                                        FOREIGN KEY (`user_id`)
                                                            REFERENCES `testdb2`.`user` (`id`)
                                                            ON DELETE NO ACTION
                                                            ON UPDATE NO ACTION,
                                                    CONSTRAINT `fk_belong_to_orgnization1`
                                                        FOREIGN KEY (`organization_id`)
                                                            REFERENCES `testdb2`.`organization` (`id`)
                                                            ON DELETE NO ACTION
                                                            ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `testdb2`.`permission`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `testdb2`.`permission` (
                                                     `id` INT NOT NULL AUTO_INCREMENT,
                                                     `user_id` INT NOT NULL,
                                                     `department_id` INT NOT NULL,
                                                     `authority_level` INT NOT NULL DEFAULT 0,
                                                     `status` ENUM('active', 'deleted', 'pending') NOT NULL DEFAULT 'active',
                                                     INDEX `fk_belong_to_client1_idx` (`user_id` ASC) ,
                                                     PRIMARY KEY (`id`),
                                                     UNIQUE INDEX `id_UNIQUE` (`id` ASC) ,
                                                     INDEX `fk_belong_to_copy1_department1_idx` (`department_id` ASC) ,
                                                     CONSTRAINT `fk_belong_to_client10`
                                                         FOREIGN KEY (`user_id`)
                                                             REFERENCES `testdb2`.`user` (`id`)
                                                             ON DELETE NO ACTION
                                                             ON UPDATE NO ACTION,
                                                     CONSTRAINT `fk_belong_to_copy1_department1`
                                                         FOREIGN KEY (`department_id`)
                                                             REFERENCES `testdb2`.`department` (`id`)
                                                             ON DELETE NO ACTION
                                                             ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `testdb2`.`to_do_list`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `testdb2`.`to_do_list` (
                                                     `id` INT NOT NULL AUTO_INCREMENT,
                                                     `user_id` INT NOT NULL,
                                                     `date_time` DATETIME NOT NULL,
                                                     `description` LONGTEXT NULL,
                                                     `status` ENUM('to do', 'in progress', 'done') NOT NULL DEFAULT 'todo',
                                                     PRIMARY KEY (`id`),
                                                     UNIQUE INDEX `id_UNIQUE` (`id` ASC) ,
                                                     INDEX `fk_order_client1_idx` (`user_id` ASC) ,
                                                     CONSTRAINT `fk_order_client100`
                                                         FOREIGN KEY (`user_id`)
                                                             REFERENCES `testdb2`.`user` (`id`)
                                                             ON DELETE NO ACTION
                                                             ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `testdb2`.`recent_contact`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `testdb2`.`recent_contact` (
                                                         `contact_id` INT NOT NULL,
                                                         `user_id` INT NOT NULL,
                                                         `last_contact` DATETIME NOT NULL,
                                                         INDEX `fk_order_customer1_idx` (`contact_id` ASC) ,
                                                         INDEX `fk_order_client1_idx` (`user_id` ASC) ,
                                                         PRIMARY KEY (`contact_id`, `user_id`),
                                                         CONSTRAINT `fk_order_customer10`
                                                             FOREIGN KEY (`contact_id`)
                                                                 REFERENCES `testdb2`.`contact` (`id`)
                                                                 ON DELETE NO ACTION
                                                                 ON UPDATE NO ACTION,
                                                         CONSTRAINT `fk_order_client11`
                                                             FOREIGN KEY (`user_id`)
                                                                 REFERENCES `testdb2`.`user` (`id`)
                                                                 ON DELETE NO ACTION
                                                                 ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `testdb2`.`token_key`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `testdb2`.`token_key` (
                                                    `id` INT NOT NULL AUTO_INCREMENT,
                                                    `user_id` INT NOT NULL,
                                                    `jwt_key` VARCHAR(255) NOT NULL,
                                                    `expired_time` DATETIME NOT NULL,
                                                    PRIMARY KEY (`id`),
                                                    UNIQUE INDEX `idtoken_UNIQUE` (`id` ASC) ,
                                                    INDEX `fk_token_user1_idx` (`user_id` ASC) ,
                                                    CONSTRAINT `fk_token_user1`
                                                        FOREIGN KEY (`user_id`)
                                                            REFERENCES `testdb2`.`user` (`id`)
                                                            ON DELETE NO ACTION
                                                            ON UPDATE NO ACTION)
    ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;