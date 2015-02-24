-- phpMyAdmin SQL Dump
-- version 4.0.10.7
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Feb 21, 2015 at 12:28 PM
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
  `event_code` varchar(20) COLLATE latin1_general_cs NOT NULL,
  `date_start` datetime NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `invalid` tinyint(1) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `event_name` (`event_name`),
  UNIQUE KEY `event_code_2` (`event_code`),
  KEY `timestamp` (`timestamp`),
  KEY `event_code` (`event_code`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs AUTO_INCREMENT=123 ;

--
-- Dumping data for table `event_lu`
--

INSERT INTO `event_lu` (`id`, `event_name`, `event_code`, `date_start`, `timestamp`, `invalid`) VALUES
(1, 'Western Canada Regional', 'ABCA', '2015-04-01 00:00:00', '2015-02-21 17:18:53', 0),
(2, 'Arkansas Rock City Regional', 'ARFA', '2015-03-04 00:00:00', '2015-02-21 17:18:53', 0),
(3, 'Australia Regional', 'AUSY', '2015-03-11 00:00:00', '2015-02-21 17:18:53', 0),
(4, 'Arizona East Regional', 'AZCH', '2015-03-18 00:00:00', '2015-02-21 17:18:53', 0),
(5, 'Arizona West Regional', 'AZPX', '2015-04-01 00:00:00', '2015-02-21 17:18:53', 0),
(6, 'Los Angeles Regional sponsored by The Roddenberry Foundation', 'CALB', '2015-03-11 00:00:00', '2015-02-21 17:18:53', 0),
(7, 'Central Valley Regional', 'CAMA', '2015-03-05 00:00:00', '2015-02-21 17:18:53', 0),
(8, 'Inland Empire Regional', 'CARM', '2015-02-26 00:00:00', '2015-02-21 17:18:53', 0),
(9, 'Sacramento Regional', 'CASA', '2015-03-18 00:00:00', '2015-02-21 17:18:53', 0),
(10, 'San Diego Regional', 'CASD', '2015-04-01 00:00:00', '2015-02-21 17:18:53', 0),
(11, 'Silicon Valley Regional', 'CASJ', '2015-04-01 00:00:00', '2015-02-21 17:18:53', 0),
(12, 'Ventura Regional', 'CAVE', '2015-03-26 00:00:00', '2015-02-21 17:18:53', 0),
(13, 'FIRST Championship', 'CMP', '2015-04-22 00:00:00', '2015-02-21 17:18:53', 0),
(14, 'FIRST Championship - Archimedes Subdivision', 'CMP-ARCHIMEDES', '2015-04-22 00:00:00', '2015-02-21 17:18:53', 0),
(15, 'FIRST Championship - ARTE Division', 'CMP-ARTE', '2015-04-22 00:00:00', '2015-02-21 17:18:53', 0),
(16, 'FIRST Championship - Carson Subdivision', 'CMP-CARSON', '2015-04-22 00:00:00', '2015-02-21 17:18:53', 0),
(17, 'FIRST Championship - Carver Subdivision', 'CMP-CARVER', '2015-04-22 00:00:00', '2015-02-21 17:18:53', 0),
(18, 'FIRST Championship - CUCA Division', 'CMP-CUCA', '2015-04-22 00:00:00', '2015-02-21 17:18:53', 0),
(19, 'FIRST Championship - Curie Subdivision', 'CMP-CURIE', '2015-04-22 00:00:00', '2015-02-21 17:18:53', 0),
(20, 'FIRST Championship - GACA Division', 'CMP-GACA', '2015-04-22 00:00:00', '2015-02-21 17:18:53', 0),
(21, 'FIRST Championship - Galileo Subdivision', 'CMP-GALILEO', '2015-04-22 00:00:00', '2015-02-21 17:18:53', 0),
(22, 'FIRST Championship - Hopper Subdivision', 'CMP-HOPPER', '2015-04-22 00:00:00', '2015-02-21 17:18:53', 0),
(23, 'FIRST Championship - NEHO Division', 'CMP-NEHO', '2015-04-22 00:00:00', '2015-02-21 17:18:53', 0),
(24, 'FIRST Championship - Newton Subdivision', 'CMP-NEWTON', '2015-04-22 00:00:00', '2015-02-21 17:18:53', 0),
(25, 'FIRST Championship - Tesla Subdivision', 'CMP-TESLA', '2015-04-22 00:00:00', '2015-02-21 17:18:53', 0),
(26, 'Colorado Regional', 'CODE', '2015-03-25 00:00:00', '2015-02-21 17:18:53', 0),
(27, 'NE District - Hartford Event', 'CTHAR', '2015-03-27 00:00:00', '2015-02-21 17:18:53', 0),
(28, 'NE District - Waterbury Event', 'CTWAT', '2015-02-27 00:00:00', '2015-02-21 17:18:53', 0),
(29, 'Greater DC Regional', 'DCWA', '2015-03-25 00:00:00', '2015-02-21 17:18:53', 0),
(30, 'South Florida Regional', 'FLFO', '2015-02-25 00:00:00', '2015-02-21 17:18:53', 0),
(31, 'Orlando Regional', 'FLOR', '2015-03-11 00:00:00', '2015-02-21 17:18:53', 0),
(32, 'Peachtree Regional', 'GADU', '2015-03-25 00:00:00', '2015-02-21 17:18:53', 0),
(33, 'Georgia Southern Classic Regional', 'GAPE', '2015-02-26 00:00:00', '2015-02-21 17:18:53', 0),
(34, 'Hawaii Regional', 'HIHO', '2015-03-25 00:00:00', '2015-02-21 17:18:53', 0),
(35, 'Midwest Regional', 'ILCH', '2015-04-01 00:00:00', '2015-02-21 17:18:53', 0),
(36, 'Central Illinois Regional', 'ILIL', '2015-03-18 00:00:00', '2015-02-21 17:18:53', 0),
(37, 'Indiana FIRST District Championship', 'INCMP', '2015-04-02 00:00:00', '2015-02-21 17:18:53', 0),
(38, 'IN District - Indianapolis Event', 'ININD', '2015-02-27 00:00:00', '2015-02-21 17:18:53', 0),
(39, 'IN District - Kokomo City of Firsts Event sponsored by AndyMark', 'INKOK', '2015-03-12 00:00:00', '2015-02-21 17:18:53', 0),
(40, 'IN District - Purdue Event', 'INWLA', '2015-03-19 00:00:00', '2015-02-21 17:18:53', 0),
(41, 'Israel Regional', 'ISTA', '2015-03-09 00:00:00', '2015-02-21 17:18:53', 0),
(42, 'Bayou Regional', 'LAKE', '2015-03-18 00:00:00', '2015-02-21 17:18:53', 0),
(43, 'NE District - Northeastern University Event', 'MABOS', '2015-03-26 00:00:00', '2015-02-21 17:18:53', 0),
(44, 'NE District - UMass - Dartmouth Event', 'MANDA', '2015-03-12 00:00:00', '2015-02-21 17:18:53', 0),
(45, 'NE District - Reading Event', 'MAREA', '2015-03-06 00:00:00', '2015-02-21 17:18:53', 0),
(46, 'NE District - Pioneer Valley Event', 'MASPR', '2015-03-05 00:00:00', '2015-02-21 17:18:53', 0),
(47, 'Chesapeake Regional', 'MDCP', '2015-04-01 00:00:00', '2015-02-21 17:18:53', 0),
(48, 'NE District - Pine Tree Event', 'MELEW', '2015-03-12 00:00:00', '2015-02-21 17:18:53', 0),
(49, 'FIM District - Bedford Event', 'MIBED', '2015-04-02 00:00:00', '2015-02-21 17:18:53', 0),
(50, 'FIM District - Center Line Event', 'MICEN', '2015-03-26 00:00:00', '2015-02-21 17:18:53', 0),
(51, 'FIRST in Michigan District Championship', 'MICMP', '2015-04-08 00:00:00', '2015-02-21 17:18:53', 0),
(52, 'FIM District - Escanaba Event', 'MIESC', '2015-03-26 00:00:00', '2015-02-21 17:18:53', 0),
(53, 'FIM District - Woodhaven Event', 'MIFLA', '2015-03-12 00:00:00', '2015-02-21 17:18:53', 0),
(54, 'FIM District - Gull Lake Event', 'MIGUL', '2015-03-12 00:00:00', '2015-02-21 17:18:53', 0),
(55, 'FIM District - Howell Event', 'MIHOW', '2015-02-26 00:00:00', '2015-02-21 17:18:53', 0),
(56, 'FIM District - Kentwood Event', 'MIKEN', '2015-03-05 00:00:00', '2015-02-21 17:18:53', 0),
(57, 'FIM District - Kettering University Event', 'MIKET', '2015-03-05 00:00:00', '2015-02-21 17:18:53', 0),
(58, 'FIM District - Lansing Event', 'MILAN', '2015-04-02 00:00:00', '2015-02-21 17:18:53', 0),
(59, 'FIM District - Livonia Event', 'MILIV', '2015-03-26 00:00:00', '2015-02-21 17:18:53', 0),
(60, 'FIM District - Great Lakes Bay Region Event', 'MIMID', '2015-03-19 00:00:00', '2015-02-21 17:18:53', 0),
(61, 'FIM District - St. Joseph Event', 'MISJO', '2015-03-19 00:00:00', '2015-02-21 17:18:53', 0),
(62, 'FIM District - Southfield Event', 'MISOU', '2015-02-26 00:00:00', '2015-02-21 17:18:53', 0),
(63, 'FIM District - Standish Event', 'MISTA', '2015-02-26 00:00:00', '2015-02-21 17:18:53', 0),
(64, 'FIM District - Troy Event', 'MITRY', '2015-04-02 00:00:00', '2015-02-21 17:18:53', 0),
(65, 'FIM District - Traverse City Event', 'MITVC', '2015-03-12 00:00:00', '2015-02-21 17:18:53', 0),
(66, 'FIM District - Waterford Event', 'MIWAT', '2015-03-05 00:00:00', '2015-02-21 17:18:53', 0),
(67, 'FIM District - West Michigan Event', 'MIWMI', '2015-03-19 00:00:00', '2015-02-21 17:18:53', 0),
(68, 'Lake Superior Regional', 'MNDU', '2015-02-25 00:00:00', '2015-02-21 17:18:53', 0),
(69, 'Northern Lights Regional', 'MNDU2', '2015-02-25 00:00:00', '2015-02-21 17:18:53', 0),
(70, 'Minnesota 10000 Lakes Regional', 'MNMI', '2015-04-01 00:00:00', '2015-02-21 17:18:53', 0),
(71, 'Minnesota North Star Regional', 'MNMI2', '2015-04-01 00:00:00', '2015-02-21 17:18:53', 0),
(72, 'Greater Kansas City Regional', 'MOKC', '2015-03-11 00:00:00', '2015-02-21 17:18:53', 0),
(73, 'St. Louis Regional', 'MOSL', '2015-03-18 00:00:00', '2015-02-21 17:18:53', 0),
(74, 'Mid-Atlantic Robotics District Championship', 'MRCMP', '2015-04-08 00:00:00', '2015-02-21 17:18:53', 0),
(75, 'Mexico City Regional', 'MXMC', '2015-03-04 00:00:00', '2015-02-21 17:18:53', 0),
(76, 'North Carolina Regional', 'NCRE', '2015-03-18 00:00:00', '2015-02-21 17:18:53', 0),
(77, 'NE FIRST District Championship presented by United Technologies', 'NECMP', '2015-04-08 00:00:00', '2015-02-21 17:18:53', 0),
(78, 'NE District - UNH Event', 'NHDUR', '2015-03-20 00:00:00', '2015-02-21 17:18:53', 0),
(79, 'NE District - Granite State Event', 'NHNAS', '2015-02-26 00:00:00', '2015-02-21 17:18:53', 0),
(80, 'MAR District - Bridgewater-Raritan Event', 'NJBRI', '2015-03-27 00:00:00', '2015-02-21 17:18:53', 0),
(81, 'MAR District - Mt. Olive Event', 'NJFLA', '2015-03-06 00:00:00', '2015-02-21 17:18:53', 0),
(82, 'MAR District - North Brunswick Event', 'NJNBR', '2015-04-02 00:00:00', '2015-02-21 17:18:53', 0),
(83, 'MAR District - Seneca Event', 'NJTAB', '2015-03-20 00:00:00', '2015-02-21 17:18:53', 0),
(84, 'Las Vegas Regional', 'NVLV', '2015-03-25 00:00:00', '2015-02-21 17:18:53', 0),
(85, 'SBPLI Long Island Regional', 'NYLI', '2015-03-25 00:00:00', '2015-02-21 17:18:53', 0),
(86, 'New York City Regional', 'NYNY', '2015-03-12 00:00:00', '2015-02-21 17:18:53', 0),
(87, 'Finger Lakes Regional', 'NYRO', '2015-03-25 00:00:00', '2015-02-21 17:18:53', 0),
(88, 'New York Tech Valley Regional', 'NYTR', '2015-03-18 00:00:00', '2015-02-21 17:18:53', 0),
(89, 'Queen City Regional', 'OHCI', '2015-04-01 00:00:00', '2015-02-21 17:18:53', 0),
(90, 'Buckeye Regional', 'OHCL', '2015-03-25 00:00:00', '2015-02-21 17:18:53', 0),
(91, 'Oklahoma Regional', 'OKOK', '2015-03-25 00:00:00', '2015-02-21 17:18:53', 0),
(92, 'North Bay Regional', 'ONNB', '2015-03-25 00:00:00', '2015-02-21 17:18:53', 0),
(93, 'Greater Toronto East Regional', 'ONTO', '2015-03-11 00:00:00', '2015-02-21 17:18:53', 0),
(94, 'Greater Toronto Central Regional', 'ONTO2', '2015-03-04 00:00:00', '2015-02-21 17:18:53', 0),
(95, 'Waterloo Regional', 'ONWA', '2015-03-18 00:00:00', '2015-02-21 17:18:53', 0),
(96, 'Windsor Essex Great Lakes Regional', 'ONWI', '2015-04-01 00:00:00', '2015-02-21 17:18:53', 0),
(97, 'PNW District - Oregon City Event', 'ORORE', '2015-02-26 00:00:00', '2015-02-21 17:18:53', 0),
(98, 'PNW District - Philomath Event', 'ORPHI', '2015-03-26 00:00:00', '2015-02-21 17:18:53', 0),
(99, 'PNW District - Wilsonville Event', 'ORWIL', '2015-03-12 00:00:00', '2015-02-21 17:18:53', 0),
(100, 'MAR District - Upper Darby Event', 'PADRE', '2015-03-27 00:00:00', '2015-02-21 17:18:53', 0),
(101, 'MAR District - Hatboro-Horsham Event', 'PAHAT', '2015-02-27 00:00:00', '2015-02-21 17:18:53', 0),
(102, 'MAR District - Springside Chestnut Hill Event', 'PAPHI', '2015-03-12 00:00:00', '2015-02-21 17:18:53', 0),
(103, 'Greater Pittsburgh Regional', 'PAPI', '2015-03-04 00:00:00', '2015-02-21 17:18:53', 0),
(104, 'Pacific Northwest District Championship', 'PNCMP', '2015-04-01 00:00:00', '2015-02-21 17:18:53', 0),
(105, 'FRC Festival de Robotique - Montreal Regional', 'QCMO', '2015-03-18 00:00:00', '2015-02-21 17:18:53', 0),
(106, 'NE District - Rhode Island Event', 'RISMI', '2015-03-20 00:00:00', '2015-02-21 17:18:53', 0),
(107, 'Palmetto Regional', 'SCMB', '2015-02-25 00:00:00', '2015-02-21 17:18:53', 0),
(108, 'Smoky Mountains Regional', 'TNKN', '2015-04-01 00:00:00', '2015-02-21 17:18:53', 0),
(109, 'Dallas Regional', 'TXDA', '2015-02-25 00:00:00', '2015-02-21 17:18:53', 0),
(110, 'Lone Star Regional', 'TXHO', '2015-04-01 00:00:00', '2015-02-21 17:18:53', 0),
(111, 'Hub City Regional', 'TXLU', '2015-03-25 00:00:00', '2015-02-21 17:18:53', 0),
(112, 'Alamo Regional sponsored by Rackspace Hosting', 'TXSA', '2015-03-11 00:00:00', '2015-02-21 17:18:53', 0),
(113, 'Utah Regional', 'UTWV', '2015-03-11 00:00:00', '2015-02-21 17:18:53', 0),
(114, 'Virginia Regional', 'VARI', '2015-03-18 00:00:00', '2015-02-21 17:18:53', 0),
(115, 'PNW District - Auburn Event', 'WAAHS', '2015-03-26 00:00:00', '2015-02-21 17:18:53', 0),
(116, 'PNW District - Auburn Mountainview Event', 'WAAMV', '2015-02-26 00:00:00', '2015-02-21 17:18:53', 0),
(117, 'PNW District - Central Washington University Event', 'WAELL', '2015-03-19 00:00:00', '2015-02-21 17:18:53', 0),
(118, 'PNW District - Mount Vernon Event', 'WAMOU', '2015-03-13 00:00:00', '2015-02-21 17:18:53', 0),
(119, 'PNW District - Shorewood Event', 'WASHO', '2015-03-20 00:00:00', '2015-02-21 17:18:53', 0),
(120, 'PNW District - Glacier Peak Event', 'WASNO', '2015-03-06 00:00:00', '2015-02-21 17:18:53', 0),
(121, 'PNW District - West Valley Event', 'WASPO', '2015-03-05 00:00:00', '2015-02-21 17:18:53', 0),
(122, 'Wisconsin Regional', 'WIMI', '2015-03-18 00:00:00', '2015-02-21 17:18:53', 0);

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs AUTO_INCREMENT=25 ;


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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs AUTO_INCREMENT=8 ;


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
