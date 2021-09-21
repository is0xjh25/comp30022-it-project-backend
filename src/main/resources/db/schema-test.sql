-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema testdb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema testdb
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `testdb`;
CREATE SCHEMA `testdb` DEFAULT CHARACTER SET utf8 ;
USE `testdb` ;

-- -----------------------------------------------------
-- Table `testdb`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `testdb`.`user` (
                                               `id` INT NOT NULL AUTO_INCREMENT,
                                               `email` VARCHAR(255) NOT NULL,
                                               `password` VARCHAR(255) NOT NULL,
                                               `first_name` VARCHAR(45) NOT NULL,
                                               `middle_name` VARCHAR(45) NULL,
                                               `last_name` VARCHAR(45) NOT NULL,
                                               `phone` VARCHAR(45) NOT NULL,
                                               `website` VARCHAR(255) NULL,
                                               `description` LONGTEXT NULL,
                                               `status` ENUM('active', 'deleted', 'pending') NOT NULL DEFAULT 'active',
                                               PRIMARY KEY (`id`),
                                               UNIQUE INDEX `name_UNIQUE` (`id` ASC),
                                               UNIQUE INDEX `email_UNIQUE` (`email` ASC))
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `testdb`.`organization`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `testdb`.`organization` (
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
                                                               REFERENCES `testdb`.`user` (`id`)
                                                               ON DELETE NO ACTION
                                                               ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `testdb`.`department`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `testdb`.`department` (
                                                     `id` INT NOT NULL AUTO_INCREMENT,
                                                     `name` VARCHAR(255) NOT NULL,
                                                     `organization_id` INT NOT NULL,
                                                     `status` ENUM('active', 'deleted') NOT NULL DEFAULT 'active',
                                                     PRIMARY KEY (`id`),
                                                     UNIQUE INDEX `id_UNIQUE` (`id` ASC) ,
                                                     INDEX `fk_department_orgnization1_idx` (`organization_id` ASC) ,
                                                     CONSTRAINT `fk_department_orgnization1`
                                                         FOREIGN KEY (`organization_id`)
                                                             REFERENCES `testdb`.`organization` (`id`)
                                                             ON DELETE NO ACTION
                                                             ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `testdb`.`contact`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `testdb`.`contact` (
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
                                                          REFERENCES `testdb`.`department` (`id`)
                                                          ON DELETE NO ACTION
                                                          ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `testdb`.`event`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `testdb`.`event` (
                                                `id` INT NOT NULL AUTO_INCREMENT,
                                                `user_id` INT NOT NULL,
                                                `start_time` DATETIME NOT NULL,
                                                `finish_time` DATETIME NOT NULL,
                                                `description` LONGTEXT NULL,
                                                `status` ENUM('active', 'done', 'deleted') NOT NULL DEFAULT 'active',
                                                PRIMARY KEY (`id`),
                                                UNIQUE INDEX `id_UNIQUE` (`id` ASC) ,
                                                INDEX `fk_order_client1_idx` (`user_id` ASC) ,
                                                CONSTRAINT `fk_order_client10`
                                                    FOREIGN KEY (`user_id`)
                                                        REFERENCES `testdb`.`user` (`id`)
                                                        ON DELETE NO ACTION
                                                        ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `testdb`.`attend`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `testdb`.`attend` (
                                                 `id` INT NOT NULL AUTO_INCREMENT,
                                                 `event_id` INT NOT NULL,
                                                 `contact_id` INT NOT NULL,
                                                 PRIMARY KEY (`id`),
                                                 UNIQUE INDEX `id_UNIQUE` (`id` ASC) ,
                                                 INDEX `fk_attend_event1_idx` (`event_id` ASC) ,
                                                 INDEX `fk_attend_customer1_idx` (`contact_id` ASC) ,
                                                 CONSTRAINT `fk_attend_event1`
                                                     FOREIGN KEY (`event_id`)
                                                         REFERENCES `testdb`.`event` (`id`)
                                                         ON DELETE NO ACTION
                                                         ON UPDATE NO ACTION,
                                                 CONSTRAINT `fk_attend_customer1`
                                                     FOREIGN KEY (`contact_id`)
                                                         REFERENCES `testdb`.`contact` (`id`)
                                                         ON DELETE NO ACTION
                                                         ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `testdb`.`belong_to`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `testdb`.`belong_to` (
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
                                                            REFERENCES `testdb`.`user` (`id`)
                                                            ON DELETE NO ACTION
                                                            ON UPDATE NO ACTION,
                                                    CONSTRAINT `fk_belong_to_orgnization1`
                                                        FOREIGN KEY (`organization_id`)
                                                            REFERENCES `testdb`.`organization` (`id`)
                                                            ON DELETE NO ACTION
                                                            ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `testdb`.`permission`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `testdb`.`permission` (
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
                                                             REFERENCES `testdb`.`user` (`id`)
                                                             ON DELETE NO ACTION
                                                             ON UPDATE NO ACTION,
                                                     CONSTRAINT `fk_belong_to_copy1_department1`
                                                         FOREIGN KEY (`department_id`)
                                                             REFERENCES `testdb`.`department` (`id`)
                                                             ON DELETE NO ACTION
                                                             ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `testdb`.`to_do_list`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `testdb`.`to_do_list` (
                                                     `id` INT NOT NULL AUTO_INCREMENT,
                                                     `user_id` INT NOT NULL,
                                                     `date_time` DATETIME NOT NULL,
                                                     `description` LONGTEXT NULL,
                                                     `status` ENUM('active', 'done', 'deleted') NOT NULL DEFAULT 'active',
                                                     PRIMARY KEY (`id`),
                                                     UNIQUE INDEX `id_UNIQUE` (`id` ASC) ,
                                                     INDEX `fk_order_client1_idx` (`user_id` ASC) ,
                                                     CONSTRAINT `fk_order_client100`
                                                         FOREIGN KEY (`user_id`)
                                                             REFERENCES `testdb`.`user` (`id`)
                                                             ON DELETE NO ACTION
                                                             ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `testdb`.`recent_contact`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `testdb`.`recent_contact` (
                                                         `contact_id` INT NOT NULL,
                                                         `user_id` INT NOT NULL,
                                                         `last_contact` DATETIME NOT NULL,
                                                         INDEX `fk_order_customer1_idx` (`contact_id` ASC) ,
                                                         INDEX `fk_order_client1_idx` (`user_id` ASC) ,
                                                         PRIMARY KEY (`contact_id`, `user_id`),
                                                         CONSTRAINT `fk_order_customer10`
                                                             FOREIGN KEY (`contact_id`)
                                                                 REFERENCES `testdb`.`contact` (`id`)
                                                                 ON DELETE NO ACTION
                                                                 ON UPDATE NO ACTION,
                                                         CONSTRAINT `fk_order_client11`
                                                             FOREIGN KEY (`user_id`)
                                                                 REFERENCES `testdb`.`user` (`id`)
                                                                 ON DELETE NO ACTION
                                                                 ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `testdb`.`token_key`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `testdb`.`token_key` (
                                                    `id` INT NOT NULL AUTO_INCREMENT,
                                                    `user_id` INT NOT NULL,
                                                    `jwt_key` VARCHAR(255) NOT NULL,
                                                    `expired_time` DATETIME NOT NULL,
                                                    PRIMARY KEY (`id`),
                                                    UNIQUE INDEX `idtoken_UNIQUE` (`id` ASC) ,
                                                    INDEX `fk_token_user1_idx` (`user_id` ASC) ,
                                                    CONSTRAINT `fk_token_user1`
                                                        FOREIGN KEY (`user_id`)
                                                            REFERENCES `testdb`.`user` (`id`)
                                                            ON DELETE NO ACTION
                                                            ON UPDATE NO ACTION)
    ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;