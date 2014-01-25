-- phpMyAdmin SQL Dump
-- version 4.0.8
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jan 25, 2014 at 10:26 AM
-- Server version: 5.1.72-cll
-- PHP Version: 5.3.17

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
  PRIMARY KEY (`id`),
  KEY `timestamp` (`timestamp`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs AUTO_INCREMENT=8 ;

--
-- Dumping data for table `configuration_lu`
--

INSERT INTO `configuration_lu` (`id`, `configuration_desc`, `timestamp`) VALUES
(1, 'Wide', '2014-01-25 15:18:38'),
(2, 'Long', '2014-01-25 15:18:38'),
(3, 'Square', '2014-01-25 15:18:38'),
(4, 'Round', '2014-01-25 15:18:38'),
(5, 'Hex', '2014-01-25 15:18:38'),
(6, 'Triangle', '2014-01-25 15:18:38'),
(7, 'Other', '2014-01-25 15:18:38');

-- --------------------------------------------------------

--
-- Table structure for table `event_lu`
--

CREATE TABLE IF NOT EXISTS `event_lu` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `event_name` varchar(70) COLLATE latin1_general_cs NOT NULL,
  `match_url` text COLLATE latin1_general_cs NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `event_name` (`event_name`),
  KEY `timestamp` (`timestamp`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs AUTO_INCREMENT=103 ;

--
-- Dumping data for table `event_lu`
--

INSERT INTO `event_lu` (`id`, `event_name`, `match_url`, `timestamp`) VALUES
(1, 'Central Illinois Regional', 'http://www2.usfirst.org/2014comp/Events/ILIL/ScheduleQual.html', '2014-01-25 15:19:51'),
(2, 'Palmetto Regional', 'http://www2.usfirst.org/2014comp/Events/SCMB/ScheduleQual.html', '2014-01-25 15:19:51'),
(3, 'Alamo Regional', 'http://www2.usfirst.org/2014comp/Events/TXSA/ScheduleQual.html', '2014-01-25 15:19:51'),
(4, 'Greater Toronto West Regional', 'http://www2.usfirst.org/2014comp/Events/ONTO2/ScheduleQual.html', '2014-01-25 15:19:51'),
(5, 'Inland Empire Regional', 'http://www2.usfirst.org/2014comp/Events/CASB/ScheduleQual.html', '2014-01-25 15:19:51'),
(6, 'Israel Regional', 'http://www2.usfirst.org/2014comp/Events/ISTA/ScheduleQual.html', '2014-01-25 15:19:51'),
(7, 'Greater Toronto East Regional', 'http://www2.usfirst.org/2014comp/Events/ONTO/ScheduleQual.html', '2014-01-25 15:19:51'),
(8, 'Arkansas Regional', 'http://www2.usfirst.org/2014comp/Events/ARFA/ScheduleQual.html', '2014-01-25 15:19:51'),
(9, 'San Diego Regional', 'http://www2.usfirst.org/2014comp/Events/CASD/ScheduleQual.html', '2014-01-25 15:19:51'),
(10, 'Crossroads Regional', 'http://www2.usfirst.org/2014comp/Events/INTH/ScheduleQual.html', '2014-01-25 15:19:51'),
(11, 'Lake Superior Regional', 'http://www2.usfirst.org/2014comp/Events/MNDU/ScheduleQual.html', '2014-01-25 15:19:51'),
(12, 'Northern Lights Regional', 'http://www2.usfirst.org/2014comp/Events/MNDU2/ScheduleQual.html', '2014-01-25 15:19:51'),
(13, 'Hub City Regional', 'http://www2.usfirst.org/2014comp/Events/TXLU/ScheduleQual.html', '2014-01-25 15:19:51'),
(14, 'Central Valley Regional', 'http://www2.usfirst.org/2014comp/Events/CAMA/ScheduleQual.html', '2014-01-25 15:19:51'),
(15, 'Mexico City Regional', 'http://www2.usfirst.org/2014comp/Events/MXMC/ScheduleQual.html', '2014-01-25 15:19:51'),
(16, 'Sacramento Regional', 'http://www2.usfirst.org/2014comp/Events/CASA/ScheduleQual.html', '2014-01-25 15:19:51'),
(17, 'Orlando Regional', 'http://www2.usfirst.org/2014comp/Events/FLOR/ScheduleQual.html', '2014-01-25 15:19:51'),
(18, 'Greater Kansas City Regional', 'http://www2.usfirst.org/2014comp/Events/MOKC/ScheduleQual.html', '2014-01-25 15:19:51'),
(19, 'St. Louis Regional', 'http://www2.usfirst.org/2014comp/Events/MOSL/ScheduleQual.html', '2014-01-25 15:19:51'),
(20, 'North Carolina Regional', 'http://www2.usfirst.org/2014comp/Events/NCRE/ScheduleQual.html', '2014-01-25 15:19:51'),
(21, 'New York Tech Valley Regional', 'http://www2.usfirst.org/2014comp/Events/NYTR/ScheduleQual.html', '2014-01-25 15:19:51'),
(22, 'Dallas Regional', 'http://www2.usfirst.org/2014comp/Events/TXDA/ScheduleQual.html', '2014-01-25 15:19:51'),
(23, 'Utah Regional', 'http://www2.usfirst.org/2014comp/Events/UTWV/ScheduleQual.html', '2014-01-25 15:19:51'),
(24, 'Waterloo Regional', 'http://www2.usfirst.org/2014comp/Events/ONWA/ScheduleQual.html', '2014-01-25 15:19:51'),
(25, 'Festival de Robotique FRC a Montreal Regional', 'http://www2.usfirst.org/2014comp/Events/QCMO/ScheduleQual.html', '2014-01-25 15:19:51'),
(26, 'Arizona Regional', 'http://www2.usfirst.org/2014comp/Events/AZCH/ScheduleQual.html', '2014-01-25 15:19:51'),
(27, 'Los Angeles Regional', 'http://www2.usfirst.org/2014comp/Events/CALB/ScheduleQual.html', '2014-01-25 15:19:51'),
(28, 'Boilermaker Regional', 'http://www2.usfirst.org/2014comp/Events/INWL/ScheduleQual.html', '2014-01-25 15:19:51'),
(29, 'Buckeye Regional', 'http://www2.usfirst.org/2014comp/Events/OHCL/ScheduleQual.html', '2014-01-25 15:19:51'),
(30, 'Virginia Regional', 'http://www2.usfirst.org/2014comp/Events/VARI/ScheduleQual.html', '2014-01-25 15:19:51'),
(31, 'Wisconsin Regional', 'http://www2.usfirst.org/2014comp/Events/WIMI/ScheduleQual.html', '2014-01-25 15:19:51'),
(32, 'North Bay Regional', 'http://www2.usfirst.org/2014comp/Events/ONNB/ScheduleQual.html', '2014-01-25 15:19:51'),
(33, 'Peachtree Regional', 'http://www2.usfirst.org/2014comp/Events/GADU/ScheduleQual.html', '2014-01-25 15:19:51'),
(34, 'Hawaii Regional', 'http://www2.usfirst.org/2014comp/Events/HIHO/ScheduleQual.html', '2014-01-25 15:19:51'),
(35, 'Minnesota North Star Regional', 'http://www2.usfirst.org/2014comp/Events/MNMI2/ScheduleQual.html', '2014-01-25 15:19:51'),
(36, 'Minnesota 1000 Lakes Regional', 'http://www2.usfirst.org/2014comp/Events/MNMI/ScheduleQual.html', '2014-01-25 15:19:51'),
(37, 'SBPLI Long Island Regional', 'http://www2.usfirst.org/2014comp/Events/NYLI/ScheduleQual.html', '2014-01-25 15:19:51'),
(38, 'Finger Lakes Regional', 'http://www2.usfirst.org/2014comp/Events/NYRO/ScheduleQual.html', '2014-01-25 15:19:51'),
(39, 'Queen City Regional', 'http://www2.usfirst.org/2014comp/Events/OHCI/ScheduleQual.html', '2014-01-25 15:19:51'),
(40, 'Oklahoma Regional', 'http://www2.usfirst.org/2014comp/Events/OKOK/ScheduleQual.html', '2014-01-25 15:19:51'),
(41, 'Greater Pittsburgh Regional', 'http://www2.usfirst.org/2014comp/Events/PAPI/ScheduleQual.html', '2014-01-25 15:19:51'),
(42, 'Smoky Mountains Regional', 'http://www2.usfirst.org/2014comp/Events/TNKN/ScheduleQual.html', '2014-01-25 15:19:51'),
(43, 'Greater DC Regional', 'http://www2.usfirst.org/2014comp/Events/DCWA/ScheduleQual.html', '2014-01-25 15:19:51'),
(44, 'Western Canada Regional', 'http://www2.usfirst.org/2014comp/Events/ABCA/ScheduleQual.html', '2014-01-25 15:19:51'),
(45, 'Windsor Essex Great Lakes Regional', 'http://www2.usfirst.org/2014comp/Events/ONWI/ScheduleQual.html', '2014-01-25 15:19:51'),
(46, 'Silicon Valley Regional', 'http://www2.usfirst.org/2014comp/Events/CASJ/ScheduleQual.html', '2014-01-25 15:19:51'),
(47, 'Colorado Regional', 'http://www2.usfirst.org/2014comp/Events/CODE/ScheduleQual.html', '2014-01-25 15:19:51'),
(48, 'South Florida Regional', 'http://www2.usfirst.org/2014comp/Events/FLFO/ScheduleQual.html', '2014-01-25 15:19:51'),
(49, 'Midwest Regional', 'http://www2.usfirst.org/2014comp/Events/ILCH/ScheduleQual.html', '2014-01-25 15:19:51'),
(50, 'Bayou Regional', 'http://www2.usfirst.org/2014comp/Events/LAKE/ScheduleQual.html', '2014-01-25 15:19:51'),
(51, 'Chesapeake Regional', 'http://www2.usfirst.org/2014comp/Events/MDBA/ScheduleQual.html', '2014-01-25 15:19:51'),
(52, 'Las Vegas Regional', 'http://www2.usfirst.org/2014comp/Events/NVLV/ScheduleQual.html', '2014-01-25 15:19:51'),
(53, 'New York City Regional', 'http://www2.usfirst.org/2014comp/Events/NYNY/ScheduleQual.html', '2014-01-25 15:19:51'),
(54, 'Lone Star Regional', 'http://www2.usfirst.org/2014comp/Events/TXHO/ScheduleQual.html', '2014-01-25 15:19:51'),
(55, 'Michigan FRC State Championship', 'http://www2.usfirst.org/2014comp/Events/MICMP/ScheduleQual.html', '2014-01-25 15:19:51'),
(56, 'Mid-Atlantic Robotics FRC Region Championship', 'http://www2.usfirst.org/2014comp/Events/MRCMP/ScheduleQual.html', '2014-01-25 15:19:51'),
(57, 'New England FRC Region Championship', 'http://www2.usfirst.org/2014comp/Events/NECMP/ScheduleQual.html', '2014-01-25 15:19:51'),
(58, 'Autodesk PNW FRC Championship', 'http://www2.usfirst.org/2014comp/Events/PNCMP/ScheduleQual.html', '2014-01-25 15:19:51'),
(59, 'Center Line FIRST Robotics District Competition', 'http://www2.usfirst.org/2014comp/Events/MICEN/ScheduleQual.html', '2014-01-25 15:19:51'),
(60, 'Southfield FIRST Robotics District Competition', 'http://www2.usfirst.org/2014comp/Events/MISOU/ScheduleQual.html', '2014-01-25 15:19:51'),
(61, 'Kettering University FIRST Robotics District Competition', 'http://www2.usfirst.org/2014comp/Events/MIKET/ScheduleQual.html', '2014-01-25 15:19:51'),
(62, 'Gull Lake FIRST Robotics District Competition', 'http://www2.usfirst.org/2014comp/Events/MIGUL/ScheduleQual.html', '2014-01-25 15:19:51'),
(63, 'Escanaba FIRST Robotics District Competition', 'http://www2.usfirst.org/2014comp/Events/MIESC/ScheduleQual.html', '2014-01-25 15:19:51'),
(64, 'Howell FIRST Robotics District Competition', 'http://www2.usfirst.org/2014comp/Events/MIHOW/ScheduleQual.html', '2014-01-25 15:19:51'),
(65, 'West Michigan FIRST Robotics District Competition', 'http://www2.usfirst.org/2014comp/Events/MIWMI/ScheduleQual.html', '2014-01-25 15:19:51'),
(66, 'Great Lakes Bay Region FIRST Robotics District Competition', 'http://www2.usfirst.org/2014comp/Events/MIMID/ScheduleQual.html', '2014-01-25 15:19:51'),
(67, 'Traverse City FIRST Robotics District Competition', 'http://www2.usfirst.org/2014comp/Events/MITVC/ScheduleQual.html', '2014-01-25 15:19:51'),
(68, 'Livonia FIRST Robotics District Competition', 'http://www2.usfirst.org/2014comp/Events/MILIV/ScheduleQual.html', '2014-01-25 15:19:51'),
(69, 'St. Joseph FIRST Robotics District Competition', 'http://www2.usfirst.org/2014comp/Events/MISJO/ScheduleQual.html', '2014-01-25 15:19:51'),
(70, 'Waterford FIRST Robotics District Competition', 'http://www2.usfirst.org/2014comp/Events/MIWAT/ScheduleQual.html', '2014-01-25 15:19:51'),
(71, 'Lansing FIRST Robotics District Competition', 'http://www2.usfirst.org/2014comp/Events/MILAN/ScheduleQual.html', '2014-01-25 15:19:51'),
(72, 'Bedford FIRST Robotics District Competition', 'http://www2.usfirst.org/2014comp/Events/MIBED/ScheduleQual.html', '2014-01-25 15:19:51'),
(73, 'Troy FIRST Robotics District Competition', 'http://www2.usfirst.org/2014comp/Events/MITRY/ScheduleQual.html', '2014-01-25 15:19:51'),
(74, 'MAR FIRST Robotics Mt. Olive District Competition', 'http://www2.usfirst.org/2014comp/Events/NJFLA/ScheduleQual.html', '2014-01-25 15:19:51'),
(75, 'MAR FIRST Robotics Hatboro-Horsham District Competition', 'http://www2.usfirst.org/2014comp/Events/PAHAT/ScheduleQual.html', '2014-01-25 15:19:51'),
(76, 'MAR FIRST Robotics Springside Chestnut Hill District Competition', 'http://www2.usfirst.org/2014comp/Events/PAPHI/ScheduleQual.html', '2014-01-25 15:19:51'),
(77, 'MAR FIRST Robotics Cliffton District Competition', 'http://www2.usfirst.org/2014comp/Events/NJCLI/ScheduleQual.html', '2014-01-25 15:19:51'),
(78, 'MAR FIRST Robotics Lenape-Seneca District Competition', 'http://www2.usfirst.org/2014comp/Events/NJTAB/ScheduleQual.html', '2014-01-25 15:19:51'),
(79, 'MAR FIRST Robotics Bridgewater-Raritan District Competition', 'http://www2.usfirst.org/2014comp/Events/NJBRI/ScheduleQual.html', '2014-01-25 15:19:51'),
(80, 'Granite State District Event', 'http://www2.usfirst.org/2014comp/Events/NHNAS/ScheduleQual.html', '2014-01-25 15:19:51'),
(81, 'UNH District Event', 'http://www2.usfirst.org/2014comp/Events/NHDUR/ScheduleQual.html', '2014-01-25 15:19:51'),
(82, 'Groton District Event', 'http://www2.usfirst.org/2014comp/Events/CTGRO/ScheduleQual.html', '2014-01-25 15:19:51'),
(83, 'WPI District Event', 'http://www2.usfirst.org/2014comp/Events/NAWOR/ScheduleQual.html', '2014-01-25 15:19:51'),
(84, 'Rhode Island District Event', 'http://www2.usfirst.org/2014comp/Events/RISMI/ScheduleQual.html', '2014-01-25 15:19:51'),
(85, 'Southington District Event', 'http://www2.usfirst.org/2014comp/Events/CTSOU/ScheduleQual.html', '2014-01-25 15:19:51'),
(86, 'Northeastern University District Event', 'http://www2.usfirst.org/2014comp/Events/MABOS/ScheduleQual.html', '2014-01-25 15:19:51'),
(87, 'Hartford District Event', 'http://www2.usfirst.org/2014comp/Events/CTHAR/ScheduleQual.html', '2014-01-25 15:19:51'),
(88, 'Pinetree District Event', 'http://www2.usfirst.org/2014comp/Events/MELEW/ScheduleQual.html', '2014-01-25 15:19:51'),
(89, 'PNW FIRST Robotics Auburn Mountainview District Event', 'http://www2.usfirst.org/2014comp/Events/WAAMV/ScheduleQual.html', '2014-01-25 15:19:51'),
(90, 'PNW FIRST Robotics Oregon City District Event', 'http://www2.usfirst.org/2014comp/Events/ORORE/ScheduleQual.html', '2014-01-25 15:19:51'),
(91, 'PNW FIRST Robotics Glacier Peak District Event', 'http://www2.usfirst.org/2014comp/Events/WASNO/ScheduleQual.html', '2014-01-25 15:19:51'),
(92, 'PNW FIRST Robotics Eastern Washington University District Event', 'http://www2.usfirst.org/2014comp/Events/WACHE/ScheduleQual.html', '2014-01-25 15:19:51'),
(93, 'PNW FIRST Robotics Mt. Vernon District Event', 'http://www2.usfirst.org/2014comp/Events/WAMOU/ScheduleQual.html', '2014-01-25 15:19:51'),
(94, 'PNW FIRST Robotics Wilsonville District Event', 'http://www2.usfirst.org/2014comp/Events/ORWIL/ScheduleQual.html', '2014-01-25 15:19:51'),
(95, 'PNW FIRST Robotics Shorewood District Event', 'http://www2.usfirst.org/2014comp/Events/WASHO/ScheduleQual.html', '2014-01-25 15:19:51'),
(96, 'PNW FIRST Robotics Auburn District Event', 'http://www2.usfirst.org/2014comp/Events/WAAHS/ScheduleQual.html', '2014-01-25 15:19:51'),
(97, 'PNW FIRST Robotics Central Washington University District Event', 'http://www2.usfirst.org/2014comp/Events/WAELO/ScheduleQual.html', '2014-01-25 15:19:51'),
(98, 'PNW FIRST Robotics Oregon State University District Event', 'http://www2.usfirst.org/2014comp/Events/OROSU/ScheduleQual.html', '2014-01-25 15:19:51'),
(99, 'Championship - Archimedes', 'http://www2.usfirst.org/2014comp/Events/Archimedes/ScheduleQual.html', '2014-01-25 15:19:51'),
(100, 'Championship - Curie', 'http://www2.usfirst.org/2014comp/Events/Curie/ScheduleQual.html', '2014-01-25 15:19:51'),
(101, 'Championship - Galileo', 'http://www2.usfirst.org/2014comp/Events/Galileo/ScheduleQual.html', '2014-01-25 15:19:51'),
(102, 'Championship - Newton', 'http://www2.usfirst.org/2014comp/Events/Newton/ScheduleQual.html', '2014-01-25 15:19:51');

-- --------------------------------------------------------

--
-- Table structure for table `fact_cycle_data`
--

CREATE TABLE IF NOT EXISTS `fact_cycle_data` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `event_id` int(5) unsigned NOT NULL,
  `match_id` int(3) unsigned NOT NULL,
  `team_id` int(5) unsigned NOT NULL,
  `cycle_num` int(3) unsigned NOT NULL,
  `near_poss` tinyint(1) unsigned NOT NULL,
  `white_poss` tinyint(1) unsigned NOT NULL,
  `far_poss` tinyint(1) unsigned NOT NULL,
  `truss` tinyint(1) unsigned NOT NULL,
  `truss_attempt` tinyint(1) unsigned NOT NULL,
  `catch` tinyint(1) unsigned NOT NULL,
  `catch_attempt` tinyint(1) unsigned NOT NULL,
  `high` tinyint(1) unsigned NOT NULL,
  `high_attempt` int(3) unsigned NOT NULL,
  `low` tinyint(1) unsigned NOT NULL,
  `low_attempt` int(3) unsigned NOT NULL,
  `assists` int(3) unsigned NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `event_id` (`event_id`,`match_id`,`team_id`,`cycle_num`),
  KEY `timestamp` (`timestamp`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `fact_match_data`
--

CREATE TABLE IF NOT EXISTS `fact_match_data` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `event_id` int(5) unsigned NOT NULL,
  `match_id` int(3) unsigned NOT NULL,
  `team_id` int(5) unsigned NOT NULL,
  `auto_high` int(1) unsigned NOT NULL,
  `auto_high_hot` int(1) unsigned NOT NULL,
  `auto_low` int(1) unsigned NOT NULL,
  `auto_low_hot` int(1) unsigned NOT NULL,
  `high` int(1) unsigned NOT NULL,
  `low` int(1) unsigned NOT NULL,
  `auto_mobile` tinyint(1) unsigned NOT NULL,
  `foul` tinyint(1) unsigned NOT NULL,
  `tech_foul` tinyint(1) unsigned NOT NULL,
  `tip_over` tinyint(1) unsigned NOT NULL,
  `yellow_card` tinyint(1) unsigned NOT NULL,
  `red_card` tinyint(1) unsigned NOT NULL,
  `notes` varchar(1024) COLLATE latin1_general_cs NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `event_id` (`event_id`,`match_id`,`team_id`),
  KEY `timestamp` (`timestamp`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `notes_options`
--

CREATE TABLE IF NOT EXISTS `notes_options` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `option_text` text COLLATE latin1_general_cs NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `timestamp` (`timestamp`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs AUTO_INCREMENT=5 ;

--
-- Dumping data for table `notes_options`
--

INSERT INTO `notes_options` (`id`, `option_text`, `timestamp`) VALUES
(1, 'No Show', '2014-01-25 15:21:36'),
(2, 'Non-functional', '2014-01-25 15:21:36'),
(3, 'Defender', '2014-01-25 15:21:36'),
(4, 'Catcher', '2014-01-25 15:21:36');

-- --------------------------------------------------------

--
-- Table structure for table `robot_lu`
--

CREATE TABLE IF NOT EXISTS `robot_lu` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `team_id` int(5) unsigned NOT NULL,
  `robot_photo` text COLLATE latin1_general_cs NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `team_id` (`team_id`),
  KEY `timestamp` (`timestamp`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `scout_pit_data`
--

CREATE TABLE IF NOT EXISTS `scout_pit_data` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `team_id` int(10) unsigned NOT NULL,
  `configuration_id` int(10) unsigned NOT NULL,
  `wheel_type_id` int(10) unsigned NOT NULL,
  `wheel_base_id` int(10) unsigned NOT NULL,
  `autonomous_mode` tinyint(1) NOT NULL,
  `auto_high` tinyint(1) unsigned NOT NULL,
  `auto_low` tinyint(1) unsigned NOT NULL,
  `auto_hot` tinyint(1) unsigned NOT NULL,
  `auto_mobile` tinyint(1) unsigned NOT NULL,
  `auto_goalie` tinyint(1) unsigned NOT NULL,
  `truss` tinyint(1) unsigned NOT NULL,
  `catch` tinyint(1) unsigned NOT NULL,
  `active_control` tinyint(1) unsigned NOT NULL,
  `launch_ball` tinyint(1) unsigned NOT NULL,
  `scout_comments` text COLLATE latin1_general_cs NOT NULL,
  `score_high` tinyint(1) NOT NULL,
  `score_low` tinyint(1) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `timestamp` (`timestamp`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `wheel_base_lu`
--

CREATE TABLE IF NOT EXISTS `wheel_base_lu` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `wheel_base_desc` text COLLATE latin1_general_cs NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `timestamp` (`timestamp`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs AUTO_INCREMENT=9 ;

--
-- Dumping data for table `wheel_base_lu`
--

INSERT INTO `wheel_base_lu` (`id`, `wheel_base_desc`, `timestamp`) VALUES
(1, '2 Wheel Drive', '2014-01-25 15:23:47'),
(2, '4 Wheel Drive', '2014-01-25 15:23:47'),
(3, '6 Wheel Drive', '2014-01-25 15:23:47'),
(5, 'Crab Drive', '2014-01-25 15:23:47'),
(6, 'Swerve Drive', '2014-01-25 15:23:47'),
(7, 'Tank Drive', '2014-01-25 15:23:47'),
(8, 'Other', '2014-01-25 15:23:47'),
(4, '8 Wheel Drive (or more)', '2014-01-25 15:23:47');

-- --------------------------------------------------------

--
-- Table structure for table `wheel_type_lu`
--

CREATE TABLE IF NOT EXISTS `wheel_type_lu` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `wheel_type_desc` text COLLATE latin1_general_cs NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `timestamp` (`timestamp`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs AUTO_INCREMENT=11 ;

--
-- Dumping data for table `wheel_type_lu`
--

INSERT INTO `wheel_type_lu` (`id`, `wheel_type_desc`, `timestamp`) VALUES
(1, 'Kit Wheels', '2014-01-25 15:25:39'),
(2, 'IFI', '2014-01-25 15:25:39'),
(3, 'Omni', '2014-01-25 15:25:39'),
(4, 'Mecanum', '2014-01-25 15:25:39'),
(5, 'Nylon', '2014-01-25 15:25:39'),
(6, 'Rubber', '2014-01-25 15:25:39'),
(7, 'Tank Tread', '2014-01-25 15:25:39'),
(8, 'Swerve', '2014-01-25 15:25:39'),
(9, 'Custom', '2014-01-25 15:25:39'),
(10, 'Pneumatic', '2014-01-25 15:25:39');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
