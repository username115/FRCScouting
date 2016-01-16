-- phpMyAdmin SQL Dump
-- version 4.0.10.7
-- http://www.phpmyadmin.net
--
-- Host: localhost:3306
-- Generation Time: Jan 16, 2016 at 01:49 PM
-- Server version: 5.6.28-log
-- PHP Version: 5.4.31

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
-- Table structure for table `defense_lu`
--

CREATE TABLE IF NOT EXISTS `defense_lu` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `defense_desc` text COLLATE utf8_unicode_ci NOT NULL,
  `invalid` tinyint(1) NOT NULL DEFAULT '0',
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `invalid` (`invalid`,`timestamp`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=10 ;

--
-- Dumping data for table `defense_lu`
--

INSERT INTO `defense_lu` (`id`, `defense_desc`, `invalid`, `timestamp`) VALUES
(1, 'Portcullis', 0, '2016-01-16 18:48:49'),
(2, 'Cheval de frise', 0, '2016-01-16 18:48:49'),
(3, 'Moat', 0, '2016-01-16 18:48:49'),
(4, 'Ramparts', 0, '2016-01-16 18:48:49'),
(5, 'Drawbridge', 0, '2016-01-16 18:48:49'),
(6, 'Sally port', 0, '2016-01-16 18:48:49'),
(7, 'Rock wall', 0, '2016-01-16 18:48:49'),
(8, 'Rough Terrain', 0, '2016-01-16 18:48:49'),
(9, 'Low bar', 0, '2016-01-16 18:48:49');

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
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs AUTO_INCREMENT=140 ;

--
-- Dumping data for table `event_lu`
--

INSERT INTO `event_lu` (`id`, `event_name`, `event_code`, `date_start`, `timestamp`, `invalid`) VALUES
(1, 'Western Canada Regional', 'ABCA', '2016-04-03 00:00:00', '2016-01-15 01:37:21', 0),
(2, 'Rocket City Regional', 'ALHU', '2016-03-23 00:00:00', '2016-01-15 01:37:21', 0),
(3, 'Arkansas Rock City Regional', 'ARLR', '2016-03-09 00:00:00', '2016-01-15 01:37:21', 0),
(4, 'Australia Regional', 'AUSY', '2016-03-16 00:00:00', '2016-01-15 01:37:21', 0),
(5, 'Arizona North Regional', 'AZFL', '2016-03-09 00:00:00', '2016-01-15 01:37:21', 0),
(6, 'Arizona West Regional', 'AZPX', '2016-04-06 00:00:00', '2016-01-15 01:37:21', 0),
(7, 'Sacramento Regional', 'CADA', '2016-03-23 00:00:00', '2016-01-15 01:37:21', 0),
(8, 'Los Angeles Regional sponsored by The Roddenberry Foundation', 'CALB', '2016-03-09 00:00:00', '2016-01-15 01:37:21', 0),
(9, 'Central Valley Regional', 'CAMA', '2016-03-10 00:00:00', '2016-01-15 01:37:21', 0),
(10, 'Orange County Regional', 'CAPL', '2016-03-30 00:00:00', '2016-01-15 01:37:21', 0),
(11, 'San Diego Regional', 'CASD', '2016-03-02 00:00:00', '2016-01-15 01:37:21', 0),
(12, 'Silicon Valley Regional presented by Google.org', 'CASJ', '2016-04-06 00:00:00', '2016-01-15 01:37:21', 0),
(13, 'Ventura Regional', 'CAVE', '2016-03-23 00:00:00', '2016-01-15 01:37:21', 0),
(14, 'FIRST Chesapeake District Championship', 'CHCMP', '2016-04-06 00:00:00', '2016-01-15 01:37:21', 0),
(15, 'FIRST Championship', 'CMP', '2016-04-27 00:00:00', '2016-01-15 01:37:21', 0),
(16, 'FIRST Championship - Archimedes Subdivision', 'CMP-ARCHIMEDES', '2016-04-27 00:00:00', '2016-01-15 01:37:21', 0),
(17, 'FIRST Championship - ARTE Division', 'CMP-ARTE', '2016-04-27 00:00:00', '2016-01-15 01:37:21', 0),
(18, 'FIRST Championship - Carson Subdivision', 'CMP-CARSON', '2016-04-27 00:00:00', '2016-01-15 01:37:21', 0),
(19, 'FIRST Championship - Carver Subdivision', 'CMP-CARVER', '2016-04-27 00:00:00', '2016-01-15 01:37:21', 0),
(20, 'FIRST Championship - CUCA Division', 'CMP-CUCA', '2016-04-27 00:00:00', '2016-01-15 01:37:21', 0),
(21, 'FIRST Championship - Curie Subdivision', 'CMP-CURIE', '2016-04-27 00:00:00', '2016-01-15 01:37:21', 0),
(22, 'FIRST Championship - GACA Division', 'CMP-GACA', '2016-04-27 00:00:00', '2016-01-15 01:37:21', 0),
(23, 'FIRST Championship - Galileo Subdivision', 'CMP-GALILEO', '2016-04-27 00:00:00', '2016-01-15 01:37:21', 0),
(24, 'FIRST Championship - Hopper Subdivision', 'CMP-HOPPER', '2016-04-27 00:00:00', '2016-01-15 01:37:21', 0),
(25, 'FIRST Championship - NEHO Division', 'CMP-NEHO', '2016-04-27 00:00:00', '2016-01-15 01:37:21', 0),
(26, 'FIRST Championship - Newton Subdivision', 'CMP-NEWTON', '2016-04-27 00:00:00', '2016-01-15 01:37:21', 0),
(27, 'FIRST Championship - Tesla Subdivision', 'CMP-TESLA', '2016-04-27 00:00:00', '2016-01-15 01:37:21', 0),
(28, 'Colorado Regional', 'CODE', '2016-03-23 00:00:00', '2016-01-15 01:37:21', 0),
(29, 'NE District - Hartford Event', 'CTHAR', '2016-04-01 00:00:00', '2016-01-15 01:37:21', 0),
(30, 'NE District - Waterbury Event', 'CTWAT', '2016-03-04 00:00:00', '2016-01-15 01:37:21', 0),
(31, 'Orlando Regional', 'FLOR', '2016-03-09 00:00:00', '2016-01-15 01:37:21', 0),
(32, 'South Florida Regional', 'FLWP', '2016-03-30 00:00:00', '2016-01-15 01:37:21', 0),
(33, 'PCH District - Albany Event', 'GAALB', '2016-03-17 00:00:00', '2016-01-15 01:37:21', 0),
(34, 'Peachtree District State Championship', 'GACMP', '2016-04-14 00:00:00', '2016-01-15 01:37:21', 0),
(35, 'PCH District - Columbus Event', 'GACOL', '2016-03-10 00:00:00', '2016-01-15 01:37:21', 0),
(36, 'PCH District - Dalton Event', 'GADAL', '2016-03-17 00:00:00', '2016-01-15 01:37:21', 0),
(37, 'PCH District - Kennesaw Event', 'GAKEN', '2016-04-08 00:00:00', '2016-01-15 01:37:21', 0),
(38, 'Hawaii Regional', 'HIHO', '2016-03-30 00:00:00', '2016-01-15 01:37:21', 0),
(39, 'Iowa Regional', 'IACF', '2016-03-23 00:00:00', '2016-01-15 01:37:21', 0),
(40, 'Idaho Regional', 'IDBO', '2016-03-30 00:00:00', '2016-01-15 01:37:21', 0),
(41, 'Midwest Regional', 'ILCH', '2016-03-30 00:00:00', '2016-01-15 01:37:21', 0),
(42, 'Central Illinois Regional', 'ILPE', '2016-03-16 00:00:00', '2016-01-15 01:37:21', 0),
(43, 'Indiana State Championship', 'INCMP', '2016-04-13 00:00:00', '2016-01-15 01:37:21', 0),
(44, 'IN District - Perry Meridian Event', 'INPMH', '2016-03-24 00:00:00', '2016-01-15 01:37:21', 0),
(45, 'IN District - Walker Warren Event', 'INWCH', '2016-03-18 00:00:00', '2016-01-15 01:37:21', 0),
(46, 'IN District - Tippecanoe Event', 'INWLA', '2016-03-11 00:00:00', '2016-01-15 01:37:21', 0),
(47, 'Israel Regional', 'ISTA', '2016-03-08 00:00:00', '2016-01-15 01:37:21', 0),
(48, 'Bayou Regional', 'LAKE', '2016-03-16 00:00:00', '2016-01-15 01:37:21', 0),
(49, 'NE District - Boston Event', 'MABOS', '2016-04-01 00:00:00', '2016-01-15 01:37:21', 0),
(50, 'NE District - UMass-Dartmouth Event', 'MANDA', '2016-03-18 00:00:00', '2016-01-15 01:37:21', 0),
(51, 'NE District - North Shore Event', 'MAREA', '2016-03-11 00:00:00', '2016-01-15 01:37:21', 0),
(52, 'NE District - WPI Event', 'MAWOR', '2016-03-11 00:00:00', '2016-01-15 01:37:21', 0),
(53, 'CHS District - Greater DC Event', 'MDBET', '2016-03-11 00:00:00', '2016-01-15 01:37:21', 0),
(54, 'CHS District - Northern Maryland Event', 'MDBLR', '2016-03-18 00:00:00', '2016-01-15 01:37:21', 0),
(55, 'CHS District - Central Maryland Event', 'MDEDG', '2016-03-24 00:00:00', '2016-01-15 01:37:21', 0),
(56, 'NE District - Pine Tree Event', 'MELEW', '2016-04-07 00:00:00', '2016-01-15 01:37:21', 0),
(57, 'FIM District - Ann Arbor Skyline Event', 'MIANN', '2016-04-07 00:00:00', '2016-01-15 01:37:21', 0),
(58, 'FIM District - Woodhaven Event', 'MIBRO', '2016-04-07 00:00:00', '2016-01-15 01:37:21', 0),
(59, 'FIM District - Center Line Event', 'MICEN', '2016-03-17 00:00:00', '2016-01-15 01:37:21', 0),
(60, 'Michigan State Championship', 'MICMP', '2016-04-13 00:00:00', '2016-01-15 01:37:21', 0),
(61, 'FIM District - Escanaba Event', 'MIESC', '2016-03-17 00:00:00', '2016-01-15 01:37:21', 0),
(62, 'FIM District - Howell Event', 'MIHOW', '2016-03-31 00:00:00', '2016-01-15 01:37:21', 0),
(63, 'FIM District - Kettering University Event #2', 'MIKE2', '2016-03-10 00:00:00', '2016-01-15 01:37:21', 0),
(64, 'FIM District - East Kentwood Event', 'MIKEN', '2016-03-31 00:00:00', '2016-01-15 01:37:21', 0),
(65, 'FIM District - Kettering University Event #1', 'MIKET', '2016-03-03 00:00:00', '2016-01-15 01:37:21', 0),
(66, 'FIM District - Lakeview Event', 'MILAK', '2016-03-10 00:00:00', '2016-01-15 01:37:21', 0),
(67, 'FIM District - Lansing Event', 'MILAN', '2016-03-24 00:00:00', '2016-01-15 01:37:21', 0),
(68, 'FIM District - Livonia Event', 'MILIV', '2016-03-24 00:00:00', '2016-01-15 01:37:21', 0),
(69, 'FIM District - Lake Superior State University Event', 'MILSU', '2016-03-31 00:00:00', '2016-01-15 01:37:21', 0),
(70, 'FIM District - Marysville Event', 'MIMAR', '2016-03-24 00:00:00', '2016-01-15 01:37:21', 0),
(71, 'FIM District - Midland Event', 'MIMID', '2016-03-17 00:00:00', '2016-01-15 01:37:21', 0),
(72, 'FIM District - St. Joseph Event', 'MISJO', '2016-03-10 00:00:00', '2016-01-15 01:37:21', 0),
(73, 'FIM District - Southfield Event', 'MISOU', '2016-03-03 00:00:00', '2016-01-15 01:37:21', 0),
(74, 'FIM District - Standish-Sterling Event', 'MISTA', '2016-03-03 00:00:00', '2016-01-15 01:37:21', 0),
(75, 'FIM District - Troy Event', 'MITRY', '2016-03-31 00:00:00', '2016-01-15 01:37:21', 0),
(76, 'FIM District - Traverse City Event', 'MITVC', '2016-04-07 00:00:00', '2016-01-15 01:37:21', 0),
(77, 'FIM District - Waterford Event', 'MIWAT', '2016-03-03 00:00:00', '2016-01-15 01:37:21', 0),
(78, 'FIM District - West Michigan Event', 'MIWMI', '2016-03-24 00:00:00', '2016-01-15 01:37:21', 0),
(79, 'Lake Superior Regional', 'MNDU', '2016-03-02 00:00:00', '2016-01-15 01:37:21', 0),
(80, 'Northern Lights Regional', 'MNDU2', '2016-03-02 00:00:00', '2016-01-15 01:37:21', 0),
(81, 'Minnesota 10000 Lakes Regional', 'MNMI', '2016-04-06 00:00:00', '2016-01-15 01:37:21', 0),
(82, 'Minnesota North Star Regional', 'MNMI2', '2016-04-06 00:00:00', '2016-01-15 01:37:21', 0),
(83, 'Greater Kansas City Regional', 'MOKC', '2016-03-09 00:00:00', '2016-01-15 01:37:21', 0),
(84, 'St. Louis Regional', 'MOSL', '2016-03-09 00:00:00', '2016-01-15 01:37:21', 0),
(85, 'Mid-Atlantic Robotics District Championship', 'MRCMP', '2016-04-13 00:00:00', '2016-01-15 01:37:21', 0),
(86, 'Mexico City Regional', 'MXMC', '2016-03-09 00:00:00', '2016-01-15 01:37:21', 0),
(87, 'NC District - UNC Asheville Event', 'NCASH', '2016-03-18 00:00:00', '2016-01-15 01:37:21', 0),
(88, 'NC District - Campbell University/Johnston Community College Event', 'NCBUI', '2016-04-01 00:00:00', '2016-01-15 01:37:21', 0),
(89, 'NC FIRST Robotics State Championship', 'NCCMP', '2016-04-08 00:00:00', '2016-01-15 01:37:21', 0),
(90, 'NC District - Guilford County Event', 'NCMCL', '2016-03-04 00:00:00', '2016-01-15 01:37:21', 0),
(91, 'NC District - Wake County Event', 'NCRAL', '2016-03-11 00:00:00', '2016-01-15 01:37:21', 0),
(92, 'New England District Championship', 'NECMP', '2016-04-13 00:00:00', '2016-01-15 01:37:21', 0),
(93, 'NE District - UNH Event', 'NHDUR', '2016-03-24 00:00:00', '2016-01-15 01:37:21', 0),
(94, 'NE District - Granite State Event', 'NHGRS', '2016-03-04 00:00:00', '2016-01-15 01:37:21', 0),
(95, 'MAR District - Bridgewater-Raritan Event', 'NJBRI', '2016-04-01 00:00:00', '2016-01-15 01:37:21', 0),
(96, 'MAR District - Mt. Olive Event', 'NJFLA', '2016-03-04 00:00:00', '2016-01-15 01:37:21', 0),
(97, 'MAR District - Montgomery Event', 'NJSKI', '2016-04-08 00:00:00', '2016-01-15 01:37:21', 0),
(98, 'MAR District - Seneca Event', 'NJTAB', '2016-03-18 00:00:00', '2016-01-15 01:37:21', 0),
(99, 'Las Vegas Regional', 'NVLV', '2016-03-30 00:00:00', '2016-01-15 01:37:21', 0),
(100, 'SBPLI Long Island Regional', 'NYLI', '2016-03-30 00:00:00', '2016-01-15 01:37:21', 0),
(101, 'New York City Regional', 'NYNY', '2016-03-10 00:00:00', '2016-01-15 01:37:21', 0),
(102, 'Finger Lakes Regional', 'NYRO', '2016-03-23 00:00:00', '2016-01-15 01:37:21', 0),
(103, 'New York Tech Valley Regional', 'NYTR', '2016-03-16 00:00:00', '2016-01-15 01:37:21', 0),
(104, 'Queen City Regional', 'OHCI', '2016-03-30 00:00:00', '2016-01-15 01:37:21', 0),
(105, 'Buckeye Regional', 'OHCL', '2016-03-16 00:00:00', '2016-01-15 01:37:21', 0),
(106, 'Oklahoma Regional', 'OKOK', '2016-03-23 00:00:00', '2016-01-15 01:37:21', 0),
(107, 'North Bay Regional', 'ONNB', '2016-03-23 00:00:00', '2016-01-15 01:37:21', 0),
(108, 'Greater Toronto East Regional', 'ONTO', '2016-03-09 00:00:00', '2016-01-15 01:37:21', 0),
(109, 'Greater Toronto Central Regional', 'ONTO2', '2016-03-02 00:00:00', '2016-01-15 01:37:21', 0),
(110, 'Waterloo Regional', 'ONWA', '2016-03-30 00:00:00', '2016-01-15 01:37:21', 0),
(111, 'Windsor Essex Great Lakes Regional', 'ONWI', '2016-04-06 00:00:00', '2016-01-15 01:37:21', 0),
(112, 'PNW District - Clackamas Academy of Industrial Science Event', 'ORORE', '2016-03-31 00:00:00', '2016-01-15 01:37:21', 0),
(113, 'PNW District - Philomath Event', 'ORPHI', '2016-03-24 00:00:00', '2016-01-15 01:37:21', 0),
(114, 'PNW District - Wilsonville Event', 'ORWIL', '2016-03-10 00:00:00', '2016-01-15 01:37:21', 0),
(115, 'Greater Pittsburgh Regional', 'PACA', '2016-03-09 00:00:00', '2016-01-15 01:37:21', 0),
(116, 'MAR District - Hatboro-Horsham Event', 'PAHAT', '2016-03-04 00:00:00', '2016-01-15 01:37:21', 0),
(117, 'MAR District - Springside Chestnut Hill Event', 'PAPHI', '2016-03-18 00:00:00', '2016-01-15 01:37:21', 0),
(118, 'MAR District - Westtown Event', 'PAWCH', '2016-04-01 00:00:00', '2016-01-15 01:37:21', 0),
(119, 'Pacific Northwest District Championship sponsored by Autodesk', 'PNCMP', '2016-04-06 00:00:00', '2016-01-15 01:37:21', 0),
(120, 'FRC Festival de Robotique - Montreal Regional', 'QCMO', '2016-03-30 00:00:00', '2016-01-15 01:37:21', 0),
(121, 'NE District - Rhode Island Event', 'RIPRO', '2016-03-24 00:00:00', '2016-01-15 01:37:21', 0),
(122, 'Palmetto Regional', 'SCMB', '2016-02-24 00:00:00', '2016-01-15 01:37:21', 0),
(123, 'Smoky Mountains Regional', 'TNKN', '2016-03-30 00:00:00', '2016-01-15 01:37:21', 0),
(124, 'Dallas Regional', 'TXDA', '2016-03-23 00:00:00', '2016-01-15 01:37:21', 0),
(125, 'Lone Star Regional', 'TXHO', '2016-04-06 00:00:00', '2016-01-15 01:37:21', 0),
(126, 'Hub City Regional', 'TXLU', '2016-03-31 00:00:00', '2016-01-15 01:37:21', 0),
(127, 'Alamo Regional sponsored by Rackspace Hosting', 'TXSA', '2016-03-09 00:00:00', '2016-01-15 01:37:21', 0),
(128, 'Utah Regional', 'UTWV', '2016-03-16 00:00:00', '2016-01-15 01:37:21', 0),
(129, 'CHS District - Southwest Virginia Event', 'VABLA', '2016-03-11 00:00:00', '2016-01-15 01:37:21', 0),
(130, 'CHS District - Central Virginia Event', 'VADOS', '2016-03-24 00:00:00', '2016-01-15 01:37:21', 0),
(131, 'CHS District - Northern Virginia Event', 'VAHAY', '2016-03-04 00:00:00', '2016-01-15 01:37:21', 0),
(132, 'CHS District - Hampton Roads Event', 'VAPOR', '2016-03-18 00:00:00', '2016-01-15 01:37:21', 0),
(133, 'PNW District - Auburn Event', 'WAAHS', '2016-04-01 00:00:00', '2016-01-15 01:37:21', 0),
(134, 'PNW District - Auburn Mountainview Event', 'WAAMV', '2016-03-03 00:00:00', '2016-01-15 01:37:21', 0),
(135, 'PNW District - Central Washington University Event', 'WAELL', '2016-03-17 00:00:00', '2016-01-15 01:37:21', 0),
(136, 'PNW District - Mount Vernon Event', 'WAMOU', '2016-03-18 00:00:00', '2016-01-15 01:37:21', 0),
(137, 'PNW District - Glacier Peak Event', 'WASNO', '2016-03-11 00:00:00', '2016-01-15 01:37:21', 0),
(138, 'PNW District - West Valley Event', 'WASPO', '2016-03-03 00:00:00', '2016-01-15 01:37:21', 0),
(139, 'Wisconsin Regional', 'WIMI', '2016-03-23 00:00:00', '2016-01-15 01:37:21', 0);

-- --------------------------------------------------------

--
-- Table structure for table `fact_match_data_2016`
--

CREATE TABLE IF NOT EXISTS `fact_match_data_2016` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `event_id` int(4) unsigned NOT NULL,
  `team_id` int(5) unsigned NOT NULL,
  `match_id` int(4) unsigned NOT NULL,
  `practice_match` tinyint(1) NOT NULL DEFAULT '0',
  `position_id` int(3) unsigned NOT NULL,
  `red_def_2` int(2) unsigned NOT NULL,
  `red_def_3` int(2) unsigned NOT NULL,
  `red_def_4` int(2) unsigned NOT NULL,
  `red_def_5` int(2) unsigned NOT NULL,
  `blue_def_2` int(2) unsigned NOT NULL,
  `blue_def_3` int(2) unsigned NOT NULL,
  `blue_def_4` int(2) unsigned NOT NULL,
  `blue_def_5` int(2) unsigned NOT NULL,
  `auto_reach` tinyint(1) NOT NULL,
  `auto_cross_portcullis_for` int(3) unsigned NOT NULL,
  `auto_cross_portcullis_rev` int(3) unsigned NOT NULL,
  `auto_cross_cheval_for` int(3) unsigned NOT NULL,
  `auto_cross_cheval_rev` int(3) unsigned NOT NULL,
  `auto_cross_moat_for` int(3) unsigned NOT NULL,
  `auto_cross_moat_rev` int(3) unsigned NOT NULL,
  `auto_cross_ramparts_for` int(3) unsigned NOT NULL,
  `auto_cross_ramparts_rev` int(3) unsigned NOT NULL,
  `auto_cross_drawbridge_for` int(3) unsigned NOT NULL,
  `auto_cross_drawbridge_for_with_help` int(3) unsigned NOT NULL,
  `auto_cross_drawbridge_rev` int(3) unsigned NOT NULL,
  `auto_cross_sally_for` int(3) unsigned NOT NULL,
  `auto_cross_sally_for_with_help` int(3) unsigned NOT NULL,
  `auto_cross_sally_rev` int(3) unsigned NOT NULL,
  `auto_cross_rock_wall_for` int(3) unsigned NOT NULL,
  `auto_cross_rock_wall_rev` int(3) unsigned NOT NULL,
  `auto_cross_rough_terrain_for` int(3) unsigned NOT NULL,
  `auto_cross_rough_terrain_rev` int(3) unsigned NOT NULL,
  `auto_cross_low_bar_for` int(3) unsigned NOT NULL,
  `auto_cross_low_bar_rev` int(3) unsigned NOT NULL,
  `auto_score_low` int(3) unsigned NOT NULL,
  `auto_score_high` int(3) unsigned NOT NULL,
  `cross_portcullis_for` int(3) unsigned NOT NULL,
  `cross_portcullis_rev` int(3) unsigned NOT NULL,
  `cross_cheval_for` int(3) unsigned NOT NULL,
  `cross_cheval_rev` int(3) unsigned NOT NULL,
  `cross_moat_for` int(3) unsigned NOT NULL,
  `cross_moat_rev` int(3) unsigned NOT NULL,
  `cross_ramparts_for` int(3) unsigned NOT NULL,
  `cross_ramparts_rev` int(3) unsigned NOT NULL,
  `cross_drawbridge_for` int(3) unsigned NOT NULL,
  `cross_drawbridge_for_with_help` int(3) unsigned NOT NULL,
  `cross_drawbridge_rev` int(3) unsigned NOT NULL,
  `cross_sally_for` int(3) unsigned NOT NULL,
  `cross_sally_for_with_help` int(3) unsigned NOT NULL,
  `cross_sally_rev` int(3) unsigned NOT NULL,
  `cross_rock_wall_for` int(3) unsigned NOT NULL,
  `cross_rock_wall_rev` int(3) unsigned NOT NULL,
  `cross_rough_terrain_for` int(3) unsigned NOT NULL,
  `cross_rough_terrain_rev` int(3) unsigned NOT NULL,
  `cross_low_bar_for` int(3) unsigned NOT NULL,
  `cross_low_bar_rev` int(3) unsigned NOT NULL,
  `score_low` int(3) unsigned NOT NULL,
  `score_high` int(3) unsigned NOT NULL,
  `challenge` tinyint(1) NOT NULL,
  `scale` tinyint(1) NOT NULL,
  `foul` tinyint(1) NOT NULL,
  `yellow_card` tinyint(1) NOT NULL,
  `red_card` tinyint(1) NOT NULL,
  `tip_over` tinyint(1) NOT NULL,
  `notes` text COLLATE utf8_unicode_ci NOT NULL,
  `invalid` tinyint(1) NOT NULL DEFAULT '0',
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `event_id` (`event_id`,`team_id`,`match_id`,`practice_match`,`position_id`),
  KEY `invalid` (`invalid`,`timestamp`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;

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
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs AUTO_INCREMENT=10 ;

--
-- Dumping data for table `notes_options`
--

INSERT INTO `notes_options` (`id`, `option_text`, `timestamp`, `invalid`) VALUES
(1, 'No Show', '2014-01-25 15:21:36', 0),
(2, 'Non-functional', '2014-01-25 15:21:36', 0),
(3, 'Defender', '2016-01-15 16:30:02', 0);

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
-- Table structure for table `scout_pit_data_2016`
--

CREATE TABLE IF NOT EXISTS `scout_pit_data_2016` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `team_id` int(5) unsigned NOT NULL,
  `start_spy` tinyint(1) NOT NULL,
  `auto_reach` tinyint(1) NOT NULL,
  `auto_cross` tinyint(1) NOT NULL,
  `auto_score_low` int(2) unsigned NOT NULL,
  `auto_score_high` int(2) unsigned NOT NULL,
  `cross_portcullis` tinyint(1) NOT NULL,
  `cross_cheval` tinyint(1) NOT NULL,
  `cross_moat` tinyint(1) NOT NULL,
  `cross_ramparts` tinyint(1) NOT NULL,
  `cross_drawbridge_for` tinyint(1) NOT NULL,
  `cross_drawbridge_for_with_help` tinyint(1) NOT NULL,
  `cross_drawbridge_rev` tinyint(1) NOT NULL,
  `cross_sally_for` tinyint(1) NOT NULL,
  `cross_sally_for_with_help` tinyint(1) NOT NULL,
  `cross_sally_rev` tinyint(1) NOT NULL,
  `cross_rock_wall` tinyint(1) NOT NULL,
  `cross_rough_terrain` tinyint(1) NOT NULL,
  `cross_low_bar` tinyint(1) NOT NULL,
  `score_high` tinyint(1) NOT NULL,
  `score_low` tinyint(1) NOT NULL,
  `challenge` tinyint(1) NOT NULL,
  `scale` tinyint(1) NOT NULL,
  `config_id` int(3) unsigned NOT NULL,
  `wheel_base_id` int(3) unsigned NOT NULL,
  `wheel_type_id` int(3) unsigned NOT NULL,
  `notes` text COLLATE utf8_unicode_ci NOT NULL,
  `invalid` tinyint(1) NOT NULL DEFAULT '0',
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `team_id` (`team_id`),
  KEY `invalid` (`invalid`),
  KEY `timestamp` (`timestamp`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;

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
