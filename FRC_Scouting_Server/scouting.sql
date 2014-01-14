-- phpMyAdmin SQL Dump
-- version 4.0.8
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jan 13, 2014 at 07:48 PM
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
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs AUTO_INCREMENT=8 ;

--
-- Dumping data for table `configuration_lu`
--

INSERT INTO `configuration_lu` (`id`, `configuration_desc`) VALUES
(1, 'Wide'),
(2, 'Long'),
(3, 'Square'),
(4, 'Round'),
(5, 'Hex'),
(6, 'Triangle'),
(7, 'Other');

-- --------------------------------------------------------

--
-- Table structure for table `event_lu`
--

CREATE TABLE IF NOT EXISTS `event_lu` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `event_name` varchar(70) COLLATE latin1_general_cs NOT NULL,
  `match_url` text COLLATE latin1_general_cs NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `event_name` (`event_name`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs AUTO_INCREMENT=103 ;

--
-- Dumping data for table `event_lu`
--

INSERT INTO `event_lu` (`id`, `event_name`, `match_url`) VALUES
(1, 'Central Illinois Regional', 'http://www2.usfirst.org/2014comp/Events/ILIL/ScheduleQual.html'),
(2, 'Palmetto Regional', 'http://www2.usfirst.org/2014comp/Events/SCMB/ScheduleQual.html'),
(3, 'Alamo Regional', 'http://www2.usfirst.org/2014comp/Events/TXSA/ScheduleQual.html'),
(4, 'Greater Toronto West Regional', 'http://www2.usfirst.org/2014comp/Events/ONTO2/ScheduleQual.html'),
(5, 'Inland Empire Regional', 'http://www2.usfirst.org/2014comp/Events/CASB/ScheduleQual.html'),
(6, 'Israel Regional', 'http://www2.usfirst.org/2014comp/Events/ISTA/ScheduleQual.html'),
(7, 'Greater Toronto East Regional', 'http://www2.usfirst.org/2014comp/Events/ONTO/ScheduleQual.html'),
(8, 'Arkansas Regional', 'http://www2.usfirst.org/2014comp/Events/ARFA/ScheduleQual.html'),
(9, 'San Diego Regional', 'http://www2.usfirst.org/2014comp/Events/CASD/ScheduleQual.html'),
(10, 'Crossroads Regional', 'http://www2.usfirst.org/2014comp/Events/INTH/ScheduleQual.html'),
(11, 'Lake Superior Regional', 'http://www2.usfirst.org/2014comp/Events/MNDU/ScheduleQual.html'),
(12, 'Northern Lights Regional', 'http://www2.usfirst.org/2014comp/Events/MNDU2/ScheduleQual.html'),
(13, 'Hub City Regional', 'http://www2.usfirst.org/2014comp/Events/TXLU/ScheduleQual.html'),
(14, 'Central Valley Regional', 'http://www2.usfirst.org/2014comp/Events/CAMA/ScheduleQual.html'),
(15, 'Mexico City Regional', 'http://www2.usfirst.org/2014comp/Events/MXMC/ScheduleQual.html'),
(16, 'Sacramento Regional', 'http://www2.usfirst.org/2014comp/Events/CASA/ScheduleQual.html'),
(17, 'Orlando Regional', 'http://www2.usfirst.org/2014comp/Events/FLOR/ScheduleQual.html'),
(18, 'Greater Kansas City Regional', 'http://www2.usfirst.org/2014comp/Events/MOKC/ScheduleQual.html'),
(19, 'St. Louis Regional', 'http://www2.usfirst.org/2014comp/Events/MOSL/ScheduleQual.html'),
(20, 'North Carolina Regional', 'http://www2.usfirst.org/2014comp/Events/NCRE/ScheduleQual.html'),
(21, 'New York Tech Valley Regional', 'http://www2.usfirst.org/2014comp/Events/NYTR/ScheduleQual.html'),
(22, 'Dallas Regional', 'http://www2.usfirst.org/2014comp/Events/TXDA/ScheduleQual.html'),
(23, 'Utah Regional', 'http://www2.usfirst.org/2014comp/Events/UTWV/ScheduleQual.html'),
(24, 'Waterloo Regional', 'http://www2.usfirst.org/2014comp/Events/ONWA/ScheduleQual.html'),
(25, 'Festival de Robotique FRC a Montreal Regional', 'http://www2.usfirst.org/2014comp/Events/QCMO/ScheduleQual.html'),
(26, 'Arizona Regional', 'http://www2.usfirst.org/2014comp/Events/AZCH/ScheduleQual.html'),
(27, 'Los Angeles Regional', 'http://www2.usfirst.org/2014comp/Events/CALB/ScheduleQual.html'),
(28, 'Boilermaker Regional', 'http://www2.usfirst.org/2014comp/Events/INWL/ScheduleQual.html'),
(29, 'Buckeye Regional', 'http://www2.usfirst.org/2014comp/Events/OHCL/ScheduleQual.html'),
(30, 'Virginia Regional', 'http://www2.usfirst.org/2014comp/Events/VARI/ScheduleQual.html'),
(31, 'Wisconsin Regional', 'http://www2.usfirst.org/2014comp/Events/WIMI/ScheduleQual.html'),
(32, 'North Bay Regional', 'http://www2.usfirst.org/2014comp/Events/ONNB/ScheduleQual.html'),
(33, 'Peachtree Regional', 'http://www2.usfirst.org/2014comp/Events/GADU/ScheduleQual.html'),
(34, 'Hawaii Regional', 'http://www2.usfirst.org/2014comp/Events/HIHO/ScheduleQual.html'),
(35, 'Minnesota North Star Regional', 'http://www2.usfirst.org/2014comp/Events/MNMI2/ScheduleQual.html'),
(36, 'Minnesota 1000 Lakes Regional', 'http://www2.usfirst.org/2014comp/Events/MNMI/ScheduleQual.html'),
(37, 'SBPLI Long Island Regional', 'http://www2.usfirst.org/2014comp/Events/NYLI/ScheduleQual.html'),
(38, 'Finger Lakes Regional', 'http://www2.usfirst.org/2014comp/Events/NYRO/ScheduleQual.html'),
(39, 'Queen City Regional', 'http://www2.usfirst.org/2014comp/Events/OHCI/ScheduleQual.html'),
(40, 'Oklahoma Regional', 'http://www2.usfirst.org/2014comp/Events/OKOK/ScheduleQual.html'),
(41, 'Greater Pittsburgh Regional', 'http://www2.usfirst.org/2014comp/Events/PAPI/ScheduleQual.html'),
(42, 'Smoky Mountains Regional', 'http://www2.usfirst.org/2014comp/Events/TNKN/ScheduleQual.html'),
(43, 'Greater DC Regional', 'http://www2.usfirst.org/2014comp/Events/DCWA/ScheduleQual.html'),
(44, 'Western Canada Regional', 'http://www2.usfirst.org/2014comp/Events/ABCA/ScheduleQual.html'),
(45, 'Windsor Essex Great Lakes Regional', 'http://www2.usfirst.org/2014comp/Events/ONWI/ScheduleQual.html'),
(46, 'Silicon Valley Regional', 'http://www2.usfirst.org/2014comp/Events/CASJ/ScheduleQual.html'),
(47, 'Colorado Regional', 'http://www2.usfirst.org/2014comp/Events/CODE/ScheduleQual.html'),
(48, 'South Florida Regional', 'http://www2.usfirst.org/2014comp/Events/FLFO/ScheduleQual.html'),
(49, 'Midwest Regional', 'http://www2.usfirst.org/2014comp/Events/ILCH/ScheduleQual.html'),
(50, 'Bayou Regional', 'http://www2.usfirst.org/2014comp/Events/LAKE/ScheduleQual.html'),
(51, 'Chesapeake Regional', 'http://www2.usfirst.org/2014comp/Events/MDBA/ScheduleQual.html'),
(52, 'Las Vegas Regional', 'http://www2.usfirst.org/2014comp/Events/NVLV/ScheduleQual.html'),
(53, 'New York City Regional', 'http://www2.usfirst.org/2014comp/Events/NYNY/ScheduleQual.html'),
(54, 'Lone Star Regional', 'http://www2.usfirst.org/2014comp/Events/TXHO/ScheduleQual.html'),
(55, 'Michigan FRC State Championship', 'http://www2.usfirst.org/2014comp/Events/MICMP/ScheduleQual.html'),
(56, 'Mid-Atlantic Robotics FRC Region Championship', 'http://www2.usfirst.org/2014comp/Events/MRCMP/ScheduleQual.html'),
(57, 'New England FRC Region Championship', 'http://www2.usfirst.org/2014comp/Events/NECMP/ScheduleQual.html'),
(58, 'Autodesk PNW FRC Championship', 'http://www2.usfirst.org/2014comp/Events/PNCMP/ScheduleQual.html'),
(59, 'Center Line FIRST Robotics District Competition', 'http://www2.usfirst.org/2014comp/Events/MICEN/ScheduleQual.html'),
(60, 'Southfield FIRST Robotics District Competition', 'http://www2.usfirst.org/2014comp/Events/MISOU/ScheduleQual.html'),
(61, 'Kettering University FIRST Robotics District Competition', 'http://www2.usfirst.org/2014comp/Events/MIKET/ScheduleQual.html'),
(62, 'Gull Lake FIRST Robotics District Competition', 'http://www2.usfirst.org/2014comp/Events/MIGUL/ScheduleQual.html'),
(63, 'Escanaba FIRST Robotics District Competition', 'http://www2.usfirst.org/2014comp/Events/MIESC/ScheduleQual.html'),
(64, 'Howell FIRST Robotics District Competition', 'http://www2.usfirst.org/2014comp/Events/MIHOW/ScheduleQual.html'),
(65, 'West Michigan FIRST Robotics District Competition', 'http://www2.usfirst.org/2014comp/Events/MIWMI/ScheduleQual.html'),
(66, 'Great Lakes Bay Region FIRST Robotics District Competition', 'http://www2.usfirst.org/2014comp/Events/MIMID/ScheduleQual.html'),
(67, 'Traverse City FIRST Robotics District Competition', 'http://www2.usfirst.org/2014comp/Events/MITVC/ScheduleQual.html'),
(68, 'Livonia FIRST Robotics District Competition', 'http://www2.usfirst.org/2014comp/Events/MILIV/ScheduleQual.html'),
(69, 'St. Joseph FIRST Robotics District Competition', 'http://www2.usfirst.org/2014comp/Events/MISJO/ScheduleQual.html'),
(70, 'Waterford FIRST Robotics District Competition', 'http://www2.usfirst.org/2014comp/Events/MIWAT/ScheduleQual.html'),
(71, 'Lansing FIRST Robotics District Competition', 'http://www2.usfirst.org/2014comp/Events/MILAN/ScheduleQual.html'),
(72, 'Bedford FIRST Robotics District Competition', 'http://www2.usfirst.org/2014comp/Events/MIBED/ScheduleQual.html'),
(73, 'Troy FIRST Robotics District Competition', 'http://www2.usfirst.org/2014comp/Events/MITRY/ScheduleQual.html'),
(74, 'MAR FIRST Robotics Mt. Olive District Competition', 'http://www2.usfirst.org/2014comp/Events/NJFLA/ScheduleQual.html'),
(75, 'MAR FIRST Robotics Hatboro-Horsham District Competition', 'http://www2.usfirst.org/2014comp/Events/PAHAT/ScheduleQual.html'),
(76, 'MAR FIRST Robotics Springside Chestnut Hill District Competition', 'http://www2.usfirst.org/2014comp/Events/PAPHI/ScheduleQual.html'),
(77, 'MAR FIRST Robotics Cliffton District Competition', 'http://www2.usfirst.org/2014comp/Events/NJCLI/ScheduleQual.html'),
(78, 'MAR FIRST Robotics Lenape-Seneca District Competition', 'http://www2.usfirst.org/2014comp/Events/NJTAB/ScheduleQual.html'),
(79, 'MAR FIRST Robotics Bridgewater-Raritan District Competition', 'http://www2.usfirst.org/2014comp/Events/NJBRI/ScheduleQual.html'),
(80, 'Granite State District Event', 'http://www2.usfirst.org/2014comp/Events/NHNAS/ScheduleQual.html'),
(81, 'UNH District Event', 'http://www2.usfirst.org/2014comp/Events/NHDUR/ScheduleQual.html'),
(82, 'Groton District Event', 'http://www2.usfirst.org/2014comp/Events/CTGRO/ScheduleQual.html'),
(83, 'WPI District Event', 'http://www2.usfirst.org/2014comp/Events/NAWOR/ScheduleQual.html'),
(84, 'Rhode Island District Event', 'http://www2.usfirst.org/2014comp/Events/RISMI/ScheduleQual.html'),
(85, 'Southington District Event', 'http://www2.usfirst.org/2014comp/Events/CTSOU/ScheduleQual.html'),
(86, 'Northeastern University District Event', 'http://www2.usfirst.org/2014comp/Events/MABOS/ScheduleQual.html'),
(87, 'Hartford District Event', 'http://www2.usfirst.org/2014comp/Events/CTHAR/ScheduleQual.html'),
(88, 'Pinetree District Event', 'http://www2.usfirst.org/2014comp/Events/MELEW/ScheduleQual.html'),
(89, 'PNW FIRST Robotics Auburn Mountainview District Event', 'http://www2.usfirst.org/2014comp/Events/WAAMV/ScheduleQual.html'),
(90, 'PNW FIRST Robotics Oregon City District Event', 'http://www2.usfirst.org/2014comp/Events/ORORE/ScheduleQual.html'),
(91, 'PNW FIRST Robotics Glacier Peak District Event', 'http://www2.usfirst.org/2014comp/Events/WASNO/ScheduleQual.html'),
(92, 'PNW FIRST Robotics Eastern Washington University District Event', 'http://www2.usfirst.org/2014comp/Events/WACHE/ScheduleQual.html'),
(93, 'PNW FIRST Robotics Mt. Vernon District Event', 'http://www2.usfirst.org/2014comp/Events/WAMOU/ScheduleQual.html'),
(94, 'PNW FIRST Robotics Wilsonville District Event', 'http://www2.usfirst.org/2014comp/Events/ORWIL/ScheduleQual.html'),
(95, 'PNW FIRST Robotics Shorewood District Event', 'http://www2.usfirst.org/2014comp/Events/WASHO/ScheduleQual.html'),
(96, 'PNW FIRST Robotics Auburn District Event', 'http://www2.usfirst.org/2014comp/Events/WAAHS/ScheduleQual.html'),
(97, 'PNW FIRST Robotics Central Washington University District Event', 'http://www2.usfirst.org/2014comp/Events/WAELO/ScheduleQual.html'),
(98, 'PNW FIRST Robotics Oregon State University District Event', 'http://www2.usfirst.org/2014comp/Events/OROSU/ScheduleQual.html'),
(99, 'Championship - Archimedes', 'http://www2.usfirst.org/2014comp/Events/Archimedes/ScheduleQual.html'),
(100, 'Championship - Curie', 'http://www2.usfirst.org/2014comp/Events/Curie/ScheduleQual.html'),
(101, 'Championship - Galileo', 'http://www2.usfirst.org/2014comp/Events/Galileo/ScheduleQual.html'),
(102, 'Championship - Newton', 'http://www2.usfirst.org/2014comp/Events/Newton/ScheduleQual.html');

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
  `red_poss` tinyint(1) unsigned NOT NULL,
  `white_poss` tinyint(1) unsigned NOT NULL,
  `blue_poss` tinyint(1) unsigned NOT NULL,
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
  KEY `event_id` (`event_id`,`match_id`,`team_id`,`cycle_num`)
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
  `auto_high_attempt` int(1) unsigned NOT NULL,
  `auto_high_hot` int(1) unsigned NOT NULL,
  `auto_high_hot_attempt` int(1) unsigned NOT NULL,
  `auto_low` int(1) unsigned NOT NULL,
  `auto_low_attempt` int(1) unsigned NOT NULL,
  `auto_low_hot` int(1) unsigned NOT NULL,
  `auto_low_hot_attempt` int(1) unsigned NOT NULL,
  `high` int(1) unsigned NOT NULL,
  `high_attempt` int(1) unsigned NOT NULL,
  `low` int(1) unsigned NOT NULL,
  `low_attempt` int(1) unsigned NOT NULL,
  `auto_mobile` tinyint(1) unsigned NOT NULL,
  `foul` tinyint(1) unsigned NOT NULL,
  `tech_foul` tinyint(1) unsigned NOT NULL,
  `tip_over` tinyint(1) unsigned NOT NULL,
  `yellow_card` tinyint(1) unsigned NOT NULL,
  `red_card` tinyint(1) unsigned NOT NULL,
  `notes` varchar(1024) COLLATE latin1_general_cs NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `event_id` (`event_id`,`match_id`,`team_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `notes_options`
--

CREATE TABLE IF NOT EXISTS `notes_options` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `option_text` text COLLATE latin1_general_cs NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs AUTO_INCREMENT=8 ;

--
-- Dumping data for table `notes_options`
--

INSERT INTO `notes_options` (`id`, `option_text`) VALUES
(1, 'No Show'),
(2, 'Non-functional'),
(3, 'Defender'),
(5, 'Trouble loading'),
(6, 'Floor pickup'),
(7, 'Long-range shooter');

-- --------------------------------------------------------

--
-- Table structure for table `robot_lu`
--

CREATE TABLE IF NOT EXISTS `robot_lu` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `team_id` int(5) unsigned NOT NULL,
  `robot_photo` text COLLATE latin1_general_cs NOT NULL,
  PRIMARY KEY (`id`),
  KEY `team_id` (`team_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs AUTO_INCREMENT=80 ;

--
-- Dumping data for table `robot_lu`
--

INSERT INTO `robot_lu` (`id`, `team_id`, `robot_photo`) VALUES
(1, 3824, 'http://robobees.org/robot_pics/3824.jpg'),
(2, 1051, 'http://robobees.org/robot_pics/1051.jpg'),
(3, 342, 'http://robobees.org/robot_pics/342.jpg'),
(4, 245, 'http://robobees.org/robot_pics/245.jpg'),
(5, 4823, 'http://robobees.org/robot_pics/4823.jpg'),
(6, 1915, 'http://robobees.org/robot_pics/1915.jpg'),
(7, 1553, 'http://robobees.org/robot_pics/1553.jpg'),
(8, 2363, 'http://robobees.org/robot_pics/2363.jpg'),
(9, 1758, 'http://robobees.org/robot_pics/1758.jpg'),
(10, 4075, 'http://robobees.org/robot_pics/4075.jpg'),
(11, 3196, 'http://robobees.org/robot_pics/3196.jpg'),
(12, 3140, 'http://robobees.org/robot_pics/3140.jpg'),
(13, 4248, 'http://robobees.org/robot_pics/4248.jpg'),
(14, 1319, 'http://robobees.org/robot_pics/1319.jpg'),
(15, 1598, 'http://robobees.org/robot_pics/1598.jpg'),
(16, 346, 'http://robobees.org/robot_pics/346.jpg'),
(17, 4847, 'http://robobees.org/robot_pics/4847.jpg'),
(18, 4452, 'http://robobees.org/robot_pics/4452.jpg'),
(19, 4261, 'http://robobees.org/robot_pics/4261.jpg'),
(20, 1027, 'http://robobees.org/robot_pics/1027.jpg'),
(21, 4533, 'http://robobees.org/robot_pics/4533.jpg'),
(22, 2059, 'http://robobees.org/robot_pics/2059.jpg'),
(23, 88, 'http://robobees.org/robot_pics/88.jpg'),
(24, 1539, 'http://robobees.org/robot_pics/1539.jpg'),
(25, 1610, 'http://robobees.org/robot_pics/1610.jpg'),
(26, 4074, 'http://robobees.org/robot_pics/4074.jpg'),
(27, 3959, 'http://robobees.org/robot_pics/3959.jpg'),
(28, 3459, 'http://robobees.org/robot_pics/3459.jpg'),
(29, 343, 'http://robobees.org/robot_pics/343.jpg'),
(30, 3976, 'http://robobees.org/robot_pics/3976.jpg'),
(31, 435, 'http://robobees.org/robot_pics/435.jpg'),
(32, 2187, 'http://robobees.org/robot_pics/2187.jpg'),
(33, 4582, 'http://robobees.org/robot_pics/4582.jpg'),
(34, 11, 'http://robobees.org/robot_pics/11.jpg'),
(35, 2483, 'http://robobees.org/robot_pics/2483.jpg'),
(36, 836, 'http://robobees.org/robot_pics/836.jpg'),
(37, 4243, 'http://robobees.org/robot_pics/4243.jpg'),
(38, 1398, 'http://robobees.org/robot_pics/1398.jpg'),
(39, 2815, 'http://robobees.org/robot_pics/2815.jpg'),
(40, 3506, 'http://robobees.org/robot_pics/3506.jpg'),
(41, 1772, 'http://robobees.org/robot_pics/1772.jpg'),
(42, 2632, 'http://robobees.org/robot_pics/2632.jpg'),
(43, 20, 'http://robobees.org/robot_pics/20.jpg'),
(44, 379, 'http://robobees.org/robot_pics/379.jpg'),
(45, 955, 'http://robobees.org/robot_pics/955.jpg'),
(46, 51, 'http://robobees.org/robot_pics/51.jpg'),
(47, 71, 'http://robobees.org/robot_pics/71.jpg'),
(48, 812, 'http://robobees.org/robot_pics/812.jpg'),
(49, 32, 'http://robobees.org/robot_pics/32.jpg'),
(50, 126, 'http://robobees.org/robot_pics/126.jpg'),
(51, 1714, 'http://robobees.org/robot_pics/1714.jpg'),
(52, 948, 'http://robobees.org/robot_pics/948.jpg'),
(53, 326, 'http://robobees.org/robot_pics/326.jpg'),
(54, 172, 'http://robobees.org/robot_pics/172.jpg'),
(55, 1100, 'http://robobees.org/robot_pics/1100.jpg'),
(56, 910, 'http://robobees.org/robot_pics/910.jpg'),
(57, 1519, 'http://robobees.org/robot_pics/1519.jpg'),
(58, 291, 'http://robobees.org/robot_pics/291.jpg'),
(59, 840, 'http://robobees.org/robot_pics/840.jpg'),
(60, 1011, 'http://robobees.org/robot_pics/1011.jpg'),
(61, 78, 'http://robobees.org/robot_pics/78.jpg'),
(62, 846, 'http://robobees.org/robot_pics/846.jpg'),
(63, 1075, 'http://robobees.org/robot_pics/1075.jpg'),
(64, 4466, 'http://robobees.org/robot_pics/4466.jpg'),
(65, 433, 'http://robobees.org/robot_pics/433.jpg'),
(66, 2468, 'http://robobees.org/robot_pics/2468.jpg'),
(67, 1595, 'http://robobees.org/robot_pics/1595.jpg'),
(68, 4471, 'http://robobees.org/robot_pics/4471.jpg'),
(69, 2137, 'http://robobees.org/robot_pics/2137.jpg'),
(70, 1902, 'http://robobees.org/robot_pics/1902.jpg'),
(71, 868, 'http://robobees.org/robot_pics/868.jpg'),
(72, 801, 'http://robobees.org/robot_pics/801.jpg'),
(73, 2543, 'http://robobees.org/robot_pics/2543.jpg'),
(74, 33, 'http://robobees.org/robot_pics/33.jpg'),
(75, 1796, 'http://robobees.org/robot_pics/1796.jpg'),
(76, 365, 'http://robobees.org/robot_pics/365.jpg'),
(77, 2959, 'http://robobees.org/robot_pics/2959.jpg'),
(78, 2457, 'http://robobees.org/robot_pics/2457.jpg'),
(79, 2590, 'http://robobees.org/robot_pics/2590.jpg');

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
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `wheel_base_lu`
--

CREATE TABLE IF NOT EXISTS `wheel_base_lu` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `wheel_base_desc` text COLLATE latin1_general_cs NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs AUTO_INCREMENT=9 ;

--
-- Dumping data for table `wheel_base_lu`
--

INSERT INTO `wheel_base_lu` (`id`, `wheel_base_desc`) VALUES
(1, '2 Wheel Drive'),
(2, '4 Wheel Drive'),
(3, '6 Wheel Drive'),
(5, 'Crab Drive'),
(6, 'Swerve Drive'),
(7, 'Tank Drive'),
(8, 'Other'),
(4, '8 Wheel Drive (or more)');

-- --------------------------------------------------------

--
-- Table structure for table `wheel_type_lu`
--

CREATE TABLE IF NOT EXISTS `wheel_type_lu` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `wheel_type_desc` text COLLATE latin1_general_cs NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs AUTO_INCREMENT=11 ;

--
-- Dumping data for table `wheel_type_lu`
--

INSERT INTO `wheel_type_lu` (`id`, `wheel_type_desc`) VALUES
(1, 'Kit Wheels'),
(2, 'IFI'),
(3, 'Omni'),
(4, 'Mecanum'),
(5, 'Nylon'),
(6, 'Rubber'),
(7, 'Tank Tread'),
(8, 'Swerve'),
(9, 'Custom'),
(10, 'Pneumatic');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
