-- phpMyAdmin SQL Dump
-- version 4.0.10.7
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Feb 11, 2015 at 07:47 PM
-- Server version: 5.6.22-log
-- PHP Version: 5.4.23

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `rbees_scouting`
--

-- --------------------------------------------------------

--
-- Table structure for table `configuration_lu`
--

CREATE TABLE IF NOT EXISTS `configuration_lu` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `configuration_desc` text COLLATE latin1_general_cs NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `invalid` tinyint(1) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `timestamp` (`timestamp`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs AUTO_INCREMENT=8 ;

--
-- Dumping data for table `configuration_lu`
--

INSERT INTO `configuration_lu` (`id`, `configuration_desc`, `timestamp`, `invalid`) VALUES
(1, 'Wide', '2014-01-25 15:18:38', 0),
(2, 'Long', '2014-01-25 15:18:38', 0),
(3, 'Square', '2014-01-25 15:18:38', 0),
(4, 'Round', '2014-01-25 15:18:38', 0),
(5, 'Hex', '2014-01-25 15:18:38', 0),
(6, 'Triangle', '2014-01-25 15:18:38', 0),
(7, 'Other', '2014-01-25 15:18:38', 0);

-- --------------------------------------------------------

--
-- Table structure for table `event_lu`
--

CREATE TABLE IF NOT EXISTS `event_lu` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `event_name` varchar(70) COLLATE latin1_general_cs NOT NULL,
  `event_code` varchar(10) COLLATE latin1_general_cs NOT NULL,
  `date_start` datetime NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `invalid` tinyint(1) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `event_name` (`event_name`),
  UNIQUE KEY `event_code_2` (`event_code`),
  KEY `timestamp` (`timestamp`),
  KEY `event_code` (`event_code`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs AUTO_INCREMENT=2 ;

--
-- Dumping data for table `event_lu`
--

INSERT INTO `event_lu` (`id`, `event_name`, `event_code`, `date_start`, `timestamp`, `invalid`) VALUES
(1, 'FIRST Championship', 'CMP', '2015-04-22 00:00:00', '2015-01-31 16:09:31', 0);

-- --------------------------------------------------------

--
-- Table structure for table `fact_match_data_2015`
--

CREATE TABLE IF NOT EXISTS `fact_match_data_2015` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `event_id` int(4) unsigned NOT NULL,
  `team_id` int(5) unsigned NOT NULL,
  `match_id` int(4) unsigned NOT NULL,
  `practice_match` tinyint(1) NOT NULL DEFAULT '0',
  `position_id` int(3) unsigned NOT NULL,
  `auto_move` tinyint(1) NOT NULL,
  `auto_totes` int(2) unsigned NOT NULL,
  `auto_stack_2` tinyint(1) NOT NULL,
  `auto_stack_3` tinyint(1) NOT NULL,
  `auto_bin` int(2) unsigned NOT NULL,
  `auto_step_bin` int(2) unsigned NOT NULL,
  `totes_1` int(3) unsigned NOT NULL,
  `totes_2` int(3) unsigned NOT NULL,
  `totes_3` int(3) unsigned NOT NULL,
  `totes_4` int(3) unsigned NOT NULL,
  `totes_5` int(3) unsigned NOT NULL,
  `totes_6` int(3) unsigned NOT NULL,
  `coop_1` int(2) unsigned NOT NULL,
  `coop_2` int(2) unsigned NOT NULL,
  `coop_3` int(2) unsigned NOT NULL,
  `coop_4` int(2) unsigned NOT NULL,
  `bin_1` int(3) unsigned NOT NULL,
  `bin_2` int(3) unsigned NOT NULL,
  `bin_3` int(3) unsigned NOT NULL,
  `bin_4` int(3) unsigned NOT NULL,
  `bin_5` int(3) unsigned NOT NULL,
  `bin_6` int(3) unsigned NOT NULL,
  `bin_litter` int(3) unsigned NOT NULL,
  `landfill_litter` int(3) unsigned NOT NULL,
  `tipped_stack` tinyint(1) NOT NULL,
  `foul` tinyint(1) NOT NULL,
  `yellow_card` tinyint(1) NOT NULL,
  `red_card` tinyint(1) NOT NULL,
  `tip_over` tinyint(1) NOT NULL,
  `notes` text COLLATE latin1_general_cs NOT NULL,
  `invalid` tinyint(1) NOT NULL DEFAULT '0',
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `event_id` (`event_id`,`team_id`,`match_id`,`invalid`,`timestamp`),
  KEY `position_id` (`position_id`),
  KEY `invalid` (`invalid`),
  KEY `timestamp` (`timestamp`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs AUTO_INCREMENT=6 ;

-- --------------------------------------------------------

--
-- Table structure for table `notes_options`
--

CREATE TABLE IF NOT EXISTS `notes_options` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `option_text` text COLLATE latin1_general_cs NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `invalid` tinyint(1) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `timestamp` (`timestamp`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs AUTO_INCREMENT=8 ;

--
-- Dumping data for table `notes_options`
--

INSERT INTO `notes_options` (`id`, `option_text`, `timestamp`, `invalid`) VALUES
(1, 'No Show', '2014-01-25 15:21:36', 0),
(2, 'Non-functional', '2014-01-25 15:21:36', 0),
(4, 'Turned Bin upright', '2015-01-23 00:43:18', 0),
(3, 'Turned Tote upright', '2015-01-23 00:43:18', 0);

-- --------------------------------------------------------

--
-- Table structure for table `position_lu`
--

CREATE TABLE IF NOT EXISTS `position_lu` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `position` text COLLATE latin1_general_cs NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `invalid` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `timestamp` (`timestamp`,`invalid`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs AUTO_INCREMENT=7 ;

--
-- Dumping data for table `position_lu`
--

INSERT INTO `position_lu` (`id`, `position`, `timestamp`, `invalid`) VALUES
(1, 'Red 1', '2015-02-12 00:30:10', 0),
(2, 'Red 2', '2015-02-12 00:30:10', 0),
(3, 'Red 3', '2015-02-12 00:30:36', 0),
(4, 'Blue 1', '2015-02-12 00:30:36', 0),
(5, 'Blue 2', '2015-02-12 00:30:50', 0),
(6, 'Blue 3', '2015-02-12 00:30:50', 0);

-- --------------------------------------------------------

--
-- Table structure for table `robot_lu`
--

CREATE TABLE IF NOT EXISTS `robot_lu` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `team_id` int(5) unsigned NOT NULL,
  `robot_photo` text COLLATE latin1_general_cs NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `invalid` tinyint(1) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `team_id` (`team_id`),
  KEY `timestamp` (`timestamp`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `scout_pit_data_2015`
--

CREATE TABLE IF NOT EXISTS `scout_pit_data_2015` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `team_id` int(5) unsigned NOT NULL,
  `push_tote` tinyint(1) NOT NULL,
  `push_bin` tinyint(1) NOT NULL,
  `lift_tote` tinyint(1) NOT NULL,
  `lift_bin` tinyint(1) NOT NULL,
  `push_litter` tinyint(1) NOT NULL,
  `load_litter` tinyint(1) NOT NULL,
  `stack_tote_height` int(2) unsigned NOT NULL,
  `stack_bin_height` int(2) unsigned NOT NULL,
  `coop_totes` tinyint(1) unsigned NOT NULL,
  `coop_stack_height` int(2) unsigned NOT NULL,
  `move_auto` tinyint(1) NOT NULL,
  `auto_bin_score` int(2) unsigned NOT NULL,
  `auto_tote_score` int(2) unsigned NOT NULL,
  `auto_tote_stack_height` int(2) unsigned NOT NULL,
  `auto_step_bins` int(2) unsigned NOT NULL,
  `config_id` int(3) unsigned NOT NULL,
  `wheel_base_id` int(3) unsigned NOT NULL,
  `wheel_type_id` int(3) unsigned NOT NULL,
  `manip_style` text COLLATE latin1_general_cs NOT NULL,
  `need_upright_tote` tinyint(1) NOT NULL,
  `need_upright_bin` tinyint(1) NOT NULL,
  `can_upright_tote` tinyint(1) NOT NULL,
  `can_upright_bin` tinyint(1) NOT NULL,
  `notes` text COLLATE latin1_general_cs NOT NULL,
  `invalid` tinyint(1) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `team_id` (`team_id`),
  KEY `invalid` (`invalid`,`timestamp`),
  KEY `invalid_2` (`invalid`),
  KEY `timestamp` (`timestamp`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs AUTO_INCREMENT=3 ;

-- --------------------------------------------------------

--
-- Table structure for table `wheel_base_lu`
--

CREATE TABLE IF NOT EXISTS `wheel_base_lu` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `wheel_base_desc` text COLLATE latin1_general_cs NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `invalid` tinyint(1) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `timestamp` (`timestamp`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs AUTO_INCREMENT=9 ;

--
-- Dumping data for table `wheel_base_lu`
--

INSERT INTO `wheel_base_lu` (`id`, `wheel_base_desc`, `timestamp`, `invalid`) VALUES
(1, '2 Wheel Drive', '2014-01-25 15:23:47', 0),
(2, '4 Wheel Drive', '2014-01-25 15:23:47', 0),
(3, '6 Wheel Drive', '2014-01-25 15:23:47', 0),
(5, 'Crab Drive', '2014-01-25 15:23:47', 0),
(6, 'Swerve Drive', '2014-01-25 15:23:47', 0),
(7, 'Tank Drive', '2014-01-25 15:23:47', 0),
(8, 'Other', '2014-01-25 15:23:47', 0),
(4, '8 Wheel Drive (or more)', '2014-01-25 15:23:47', 0);

-- --------------------------------------------------------

--
-- Table structure for table `wheel_type_lu`
--

CREATE TABLE IF NOT EXISTS `wheel_type_lu` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `wheel_type_desc` text COLLATE latin1_general_cs NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `invalid` tinyint(1) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `timestamp` (`timestamp`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs AUTO_INCREMENT=11 ;

--
-- Dumping data for table `wheel_type_lu`
--

INSERT INTO `wheel_type_lu` (`id`, `wheel_type_desc`, `timestamp`, `invalid`) VALUES
(1, 'Kit Wheels', '2014-01-25 15:25:39', 0),
(2, 'IFI', '2014-01-25 15:25:39', 0),
(3, 'Omni', '2014-01-25 15:25:39', 0),
(4, 'Mecanum', '2014-01-25 15:25:39', 0),
(5, 'Nylon', '2014-01-25 15:25:39', 0),
(6, 'Rubber', '2014-01-25 15:25:39', 0),
(7, 'Tank Tread', '2014-01-25 15:25:39', 0),
(8, 'Swerve', '2014-01-25 15:25:39', 0),
(9, 'Custom', '2014-01-25 15:25:39', 0),
(10, 'Pneumatic', '2014-01-25 15:25:39', 0);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
