-- phpMyAdmin SQL Dump
-- version 4.0.10.14
-- http://www.phpmyadmin.net
--
-- Host: localhost:3306
-- Generation Time: Jan 23, 2017 at 11:31 PM
-- Server version: 5.6.33-log
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
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs AUTO_INCREMENT=166 ;

--
-- Dumping data for table `event_lu`
--

INSERT INTO `event_lu` (`id`, `event_name`, `event_code`, `date_start`, `timestamp`, `invalid`) VALUES
(1, 'Western Canada Regional', 'ABCA', '2017-04-05 00:00:00', '2017-01-09 23:42:38', 0),
(2, 'Rocket City Regional', 'ALHU', '2017-03-22 00:00:00', '2017-01-09 23:42:38', 0),
(3, 'FIRST Championship - St. Louis - Archimedes Subdivision', 'ARCHIMEDES', '2017-04-26 00:00:00', '2017-01-09 23:42:38', 0),
(4, 'FIRST Championship - St. Louis - ARDA Division', 'ARDA', '2017-04-26 00:00:00', '2017-01-09 23:42:38', 0),
(5, 'Arkansas Rock City Regional', 'ARLI', '2017-03-08 00:00:00', '2017-01-09 23:42:38', 0),
(6, 'Southern Cross Regional', 'AUSC', '2017-03-13 00:00:00', '2017-01-09 23:42:38', 0),
(7, 'South Pacific Regional', 'AUSP', '2017-03-16 00:00:00', '2017-01-09 23:42:38', 0),
(8, 'Arizona North Regional', 'AZFL', '2017-03-08 00:00:00', '2017-01-09 23:42:38', 0),
(9, 'Arizona West Regional', 'AZPX', '2017-04-05 00:00:00', '2017-01-09 23:42:38', 0),
(10, 'Sacramento Regional', 'CADA', '2017-03-22 00:00:00', '2017-01-09 23:42:38', 0),
(11, 'Orange County Regional', 'CAIR', '2017-03-29 00:00:00', '2017-01-09 23:42:38', 0),
(12, 'Los Angeles Regional', 'CALB', '2017-03-23 00:00:00', '2017-01-09 23:42:38', 0),
(13, 'Central Valley Regional', 'CAMA', '2017-03-09 00:00:00', '2017-01-09 23:42:38', 0),
(14, 'FIRST Championship - Houston - CANE Division', 'CANE', '2017-04-19 00:00:00', '2017-01-09 23:42:38', 0),
(15, 'FIRST Championship - St. Louis - Carson Subdivision', 'CARSON', '2017-04-26 00:00:00', '2017-01-09 23:42:38', 0),
(16, 'FIRST Championship - Houston - Carver Subdivision', 'CARVER', '2017-04-19 00:00:00', '2017-01-09 23:42:38', 0),
(17, 'San Diego Regional presented by Qualcomm', 'CASD', '2017-03-08 00:00:00', '2017-01-09 23:42:38', 0),
(18, 'San Francisco Regional', 'CASF', '2017-03-16 00:00:00', '2017-01-09 23:42:38', 0),
(19, 'Silicon Valley Regional', 'CASJ', '2017-03-29 00:00:00', '2017-01-09 23:42:38', 0),
(20, 'FIRST Championship - St. Louis - CATE Division', 'CATE', '2017-04-26 00:00:00', '2017-01-09 23:42:38', 0),
(21, 'Ventura Regional', 'CAVE', '2017-03-15 00:00:00', '2017-01-09 23:42:38', 0),
(22, 'FIRST Chesapeake District Championship', 'CHCMP', '2017-04-05 00:00:00', '2017-01-09 23:42:38', 0),
(23, 'FIRST Championship - St. Louis', 'CMPMO', '2017-04-26 00:00:00', '2017-01-09 23:42:38', 0),
(24, 'FIRST Championship - Houston', 'CMPTX', '2017-04-19 00:00:00', '2017-01-09 23:42:38', 0),
(25, 'Colorado Regional', 'CODE', '2017-03-22 00:00:00', '2017-01-09 23:42:38', 0),
(26, 'NE District - Hartford Event', 'CTHAR', '2017-03-31 00:00:00', '2017-01-09 23:42:38', 0),
(27, 'NE District - Waterbury Event', 'CTWAT', '2017-03-03 00:00:00', '2017-01-09 23:42:38', 0),
(28, 'FIRST Championship - St. Louis - CUDA Division', 'CUDA', '2017-04-26 00:00:00', '2017-01-09 23:42:38', 0),
(29, 'FIRST Championship - St. Louis - Curie Subdivision', 'CURIE', '2017-04-26 00:00:00', '2017-01-09 23:42:38', 0),
(30, 'FIRST Championship - St. Louis - Daly Subdivision', 'DALY', '2017-04-26 00:00:00', '2017-01-09 23:42:38', 0),
(31, 'FIRST Championship - St. Louis - Darwin Subdivision', 'DARWIN', '2017-04-26 00:00:00', '2017-01-09 23:42:38', 0),
(32, 'Orlando Regional', 'FLOR', '2017-03-08 00:00:00', '2017-01-09 23:42:38', 0),
(33, 'South Florida Regional', 'FLWP', '2017-03-01 00:00:00', '2017-01-09 23:42:38', 0),
(34, 'PCH District - Albany Event', 'GAALB', '2017-03-23 00:00:00', '2017-01-09 23:42:38', 0),
(35, 'Peachtree State Championship', 'GACMP', '2017-04-05 00:00:00', '2017-01-09 23:42:38', 0),
(36, 'PCH District - Columbus Event', 'GACOL', '2017-03-16 00:00:00', '2017-01-09 23:42:38', 0),
(37, 'PCH District - Dalton Event', 'GADAL', '2017-03-09 00:00:00', '2017-01-09 23:42:38', 0),
(38, 'PCH District - Gainesville Event', 'GAGAI', '2017-03-03 00:00:00', '2017-01-09 23:42:38', 0),
(39, 'FIRST Championship - Houston - Galileo Subdivision', 'GALILEO', '2017-04-19 00:00:00', '2017-01-09 23:42:38', 0),
(40, 'FIRST Championship - Houston - GARO Division', 'GARO', '2017-04-19 00:00:00', '2017-01-09 23:42:38', 0),
(41, 'Shenzhen Regional', 'GUSH', '2017-03-08 00:00:00', '2017-01-09 23:42:38', 0),
(42, 'Hawaii Regional', 'HIHO', '2017-03-29 00:00:00', '2017-01-09 23:42:38', 0),
(43, 'FIRST Championship - Houston - Hopper Subdivision', 'HOPPER', '2017-04-19 00:00:00', '2017-01-09 23:42:38', 0),
(44, 'FIRST Championship - Houston - HOTU Division', 'HOTU', '2017-04-19 00:00:00', '2017-01-09 23:42:38', 0),
(45, 'Iowa Regional', 'IACF', '2017-03-22 00:00:00', '2017-01-09 23:42:38', 0),
(46, 'Idaho Regional', 'IDBO', '2017-03-29 00:00:00', '2017-01-09 23:42:38', 0),
(47, 'Midwest Regional', 'ILCH', '2017-03-29 00:00:00', '2017-01-09 23:42:38', 0),
(48, 'Central Illinois Regional', 'ILPE', '2017-03-15 00:00:00', '2017-01-09 23:42:38', 0),
(49, 'Indiana State Championship', 'INCMP', '2017-04-06 00:00:00', '2017-01-09 23:42:38', 0),
(50, 'IN District - St. Joseph Event', 'INMIS', '2017-03-10 00:00:00', '2017-01-09 23:42:38', 0),
(51, 'IN District - Perry Meridian Event', 'INPMH', '2017-03-23 00:00:00', '2017-01-09 23:42:38', 0),
(52, 'IN District - Tippecanoe Event', 'INWLA', '2017-03-03 00:00:00', '2017-01-09 23:42:38', 0),
(53, 'FIRST Israel District Championship', 'ISCMP', '2017-03-28 00:00:00', '2017-01-09 23:42:38', 0),
(54, 'ISR District Event #1', 'ISDE1', '2017-03-06 00:00:00', '2017-01-09 23:42:38', 0),
(55, 'ISR District Event #2', 'ISDE2', '2017-03-08 00:00:00', '2017-01-09 23:42:38', 0),
(56, 'ISR District Event #3', 'ISDE3', '2017-03-13 00:00:00', '2017-01-09 23:42:38', 0),
(57, 'ISR District Event #4', 'ISDE4', '2017-03-15 00:00:00', '2017-01-09 23:42:38', 0),
(58, 'Bayou Regional', 'LAKE', '2017-03-22 00:00:00', '2017-01-09 23:42:38', 0),
(59, 'NE District - Greater Boston Event', 'MABOS', '2017-03-17 00:00:00', '2017-01-09 23:42:38', 0),
(60, 'NE District - SE Mass Event', 'MABRI', '2017-03-10 00:00:00', '2017-01-09 23:42:38', 0),
(61, 'NE District - North Shore Event', 'MAREA', '2017-03-17 00:00:00', '2017-01-09 23:42:38', 0),
(62, 'NE District - Worcester Polytechnic Institute Event', 'MAWOR', '2017-03-08 00:00:00', '2017-01-09 23:42:38', 0),
(63, 'CHS District - Greater DC Event', 'MDBET', '2017-03-10 00:00:00', '2017-01-09 23:42:38', 0),
(64, 'CHS District - Central Maryland Event', 'MDEDG', '2017-03-24 00:00:00', '2017-01-09 23:42:38', 0),
(65, 'CHS District - Northern Maryland Event', 'MDOWI', '2017-03-17 00:00:00', '2017-01-09 23:42:38', 0),
(66, 'NE District - Pine Tree Event', 'MELEW', '2017-03-30 00:00:00', '2017-01-09 23:42:38', 0),
(67, 'FIM District - Ann Arbor Pioneer Event', 'MIANN', '2017-03-09 00:00:00', '2017-01-09 23:42:38', 0),
(68, 'FIM District - Woodhaven Event', 'MIBRO', '2017-03-23 00:00:00', '2017-01-09 23:42:38', 0),
(69, 'FIM District - Center Line Event', 'MICEN', '2017-03-09 00:00:00', '2017-01-09 23:42:38', 0),
(70, 'Michigan State Championship - See Site Information', 'MICMP', '2017-04-12 00:00:00', '2017-01-09 23:42:38', 0),
(71, 'FIM District - Escanaba Event', 'MIESC', '2017-03-16 00:00:00', '2017-01-09 23:42:38', 0),
(72, 'FIM District - Gaylord Event', 'MIGAY', '2017-03-16 00:00:00', '2017-01-09 23:42:38', 0),
(73, 'FIM District - Gull Lake Event', 'MIGUL', '2017-03-16 00:00:00', '2017-01-09 23:42:38', 0),
(74, 'FIM District - Howell Event', 'MIHOW', '2017-03-30 00:00:00', '2017-01-09 23:42:38', 0),
(75, 'FIM District - Kettering University Event #2', 'MIKE2', '2017-03-09 00:00:00', '2017-01-09 23:42:38', 0),
(76, 'FIM District - East Kentwood Event', 'MIKEN', '2017-03-30 00:00:00', '2017-01-09 23:42:38', 0),
(77, 'FIM District - Kettering University Event #1', 'MIKET', '2017-03-02 00:00:00', '2017-01-09 23:42:38', 0),
(78, 'FIM District - Lakeview Event', 'MILAK', '2017-03-02 00:00:00', '2017-01-09 23:42:38', 0),
(79, 'FIM District - Lansing Event', 'MILAN', '2017-03-23 00:00:00', '2017-01-09 23:42:38', 0),
(80, 'FIM District - Livonia Event', 'MILIV', '2017-04-06 00:00:00', '2017-01-09 23:42:38', 0),
(81, 'FIM District - Lake Superior State University Event', 'MILSU', '2017-03-30 00:00:00', '2017-01-09 23:42:38', 0),
(82, 'FIM District - Marysville Event', 'MIMAR', '2017-04-06 00:00:00', '2017-01-09 23:42:38', 0),
(83, 'FIM District - Midland Event', 'MIMID', '2017-03-23 00:00:00', '2017-01-09 23:42:38', 0),
(84, 'FIM District - Shepherd Event', 'MISHE', '2017-04-06 00:00:00', '2017-01-09 23:42:38', 0),
(85, 'FIM District - St. Joseph Event', 'MISJO', '2017-03-09 00:00:00', '2017-01-09 23:42:38', 0),
(86, 'FIM District - Southfield Event', 'MISOU', '2017-03-02 00:00:00', '2017-01-09 23:42:38', 0),
(87, 'FIM District - Troy Event', 'MITRY', '2017-03-30 00:00:00', '2017-01-09 23:42:38', 0),
(88, 'FIM District - Traverse City Event', 'MITVC', '2017-04-06 00:00:00', '2017-01-09 23:42:38', 0),
(89, 'FIM District - Waterford Event', 'MIWAT', '2017-03-16 00:00:00', '2017-01-09 23:42:38', 0),
(90, 'FIM District - West Michigan Event', 'MIWMI', '2017-03-23 00:00:00', '2017-01-09 23:42:38', 0),
(91, 'Lake Superior Regional', 'MNDU', '2017-03-01 00:00:00', '2017-01-09 23:42:38', 0),
(92, 'Northern Lights Regional', 'MNDU2', '2017-03-01 00:00:00', '2017-01-09 23:42:38', 0),
(93, 'Minnesota 10000 Lakes Regional', 'MNMI', '2017-04-05 00:00:00', '2017-01-09 23:42:38', 0),
(94, 'Minnesota North Star Regional', 'MNMI2', '2017-04-05 00:00:00', '2017-01-09 23:42:38', 0),
(95, 'Greater Kansas City Regional', 'MOKC', '2017-03-15 00:00:00', '2017-01-09 23:42:38', 0),
(96, 'St. Louis Regional', 'MOSL', '2017-03-08 00:00:00', '2017-01-09 23:42:38', 0),
(97, 'FIRST Mid-Atlantic District Championship sponsored by Johnson & Johnso', 'MRCMP', '2017-04-05 00:00:00', '2017-01-09 23:42:38', 0),
(98, 'Toluca Regional', 'MXTL', '2017-03-01 00:00:00', '2017-01-09 23:42:38', 0),
(99, 'Laguna Regional', 'MXTO', '2017-03-29 00:00:00', '2017-01-09 23:42:38', 0),
(100, 'NC District - UNC Asheville Event', 'NCASH', '2017-03-17 00:00:00', '2017-01-09 23:42:38', 0),
(101, 'FIRST North Carolina State Championship', 'NCCMP', '2017-03-31 00:00:00', '2017-01-09 23:42:38', 0),
(102, 'NC District - Greensboro Event', 'NCGRE', '2017-03-09 00:00:00', '2017-01-09 23:42:38', 0),
(103, 'NC District - Raleigh Event', 'NCRAL', '2017-03-23 00:00:00', '2017-01-09 23:42:38', 0),
(104, 'NC District - Pitt County Event', 'NCWIN', '2017-03-03 00:00:00', '2017-01-09 23:42:38', 0),
(105, 'New England District Championship', 'NECMP', '2017-04-05 00:00:00', '2017-01-09 23:42:38', 0),
(106, 'FIRST Championship - Houston - Newton Subdivision', 'NEWTON', '2017-04-19 00:00:00', '2017-01-09 23:42:38', 0),
(107, 'NE District - Southern NH Event', 'NHBED', '2017-03-24 00:00:00', '2017-01-09 23:42:38', 0),
(108, 'NE District - Granite State Event', 'NHGRS', '2017-03-03 00:00:00', '2017-01-09 23:42:38', 0),
(109, 'MAR District - Bridgewater-Raritan Event', 'NJBRI', '2017-03-17 00:00:00', '2017-01-09 23:42:38', 0),
(110, 'MAR District - Mount Olive Event', 'NJFLA', '2017-03-10 00:00:00', '2017-01-09 23:42:38', 0),
(111, 'MAR District - Montgomery Event', 'NJSKI', '2017-03-31 00:00:00', '2017-01-09 23:42:38', 0),
(112, 'MAR District - Seneca Event', 'NJTAB', '2017-03-24 00:00:00', '2017-01-09 23:42:38', 0),
(113, 'Las Vegas Regional', 'NVLV', '2017-04-05 00:00:00', '2017-01-09 23:42:38', 0),
(114, 'SBPLI Long Island Regional', 'NYLI', '2017-03-29 00:00:00', '2017-01-09 23:42:38', 0),
(115, 'New York City Regional', 'NYNY', '2017-04-06 00:00:00', '2017-01-09 23:42:38', 0),
(116, 'Finger Lakes Regional', 'NYRO', '2017-03-15 00:00:00', '2017-01-09 23:42:38', 0),
(117, 'Hudson Valley Regional', 'NYSU', '2017-03-23 00:00:00', '2017-01-09 23:42:38', 0),
(118, 'New York Tech Valley Regional', 'NYTR', '2017-03-15 00:00:00', '2017-01-09 23:42:38', 0),
(119, 'Buckeye Regional', 'OHCL', '2017-03-29 00:00:00', '2017-01-09 23:42:38', 0),
(120, 'Miami Valley Regional', 'OHSP', '2017-03-08 00:00:00', '2017-01-09 23:42:38', 0),
(121, 'Oklahoma Regional', 'OKOK', '2017-03-22 00:00:00', '2017-01-09 23:42:38', 0),
(122, 'ONT District - Georgian College Event', 'ONBAR', '2017-03-24 00:00:00', '2017-01-09 23:42:38', 0),
(123, 'FIRST Ontario Provincial Championship', 'ONCMP', '2017-04-12 00:00:00', '2017-01-09 23:42:38', 0),
(124, 'ONT District - McMaster University Event', 'ONHAM', '2017-04-07 00:00:00', '2017-01-09 23:42:38', 0),
(125, 'ONT District - Western University, Engineering Event', 'ONLON', '2017-03-31 00:00:00', '2017-01-09 23:42:38', 0),
(126, 'ONT District - North Bay Event', 'ONNOB', '2017-04-06 00:00:00', '2017-01-09 23:42:38', 0),
(127, 'ONT District - Durham College Event', 'ONOSH', '2017-03-03 00:00:00', '2017-01-09 23:42:38', 0),
(128, 'ONT District - Ryerson University Event', 'ONTO1', '2017-03-10 00:00:00', '2017-01-09 23:42:38', 0),
(129, 'ONT District - Victoria Park Collegiate Event', 'ONTO2', '2017-03-17 00:00:00', '2017-01-09 23:42:38', 0),
(130, 'ONT District - University of Waterloo Event', 'ONWAT', '2017-03-23 00:00:00', '2017-01-09 23:42:38', 0),
(131, 'ONT District - Windsor Essex Great Lakes Event', 'ONWIN', '2017-03-30 00:00:00', '2017-01-09 23:42:38', 0),
(132, 'PNW District - Lake Oswego Event', 'ORLAK', '2017-03-30 00:00:00', '2017-01-09 23:42:38', 0),
(133, 'PNW District - Clackamas Academy of Industrial Science Event', 'ORORE', '2017-03-23 00:00:00', '2017-01-09 23:42:38', 0),
(134, 'PNW District - Wilsonville Event', 'ORWIL', '2017-03-09 00:00:00', '2017-01-09 23:42:38', 0),
(135, 'Greater Pittsburgh Regional', 'PACA', '2017-03-15 00:00:00', '2017-01-09 23:42:38', 0),
(136, 'MAR District - Hatboro-Horsham Event', 'PAHAT', '2017-03-03 00:00:00', '2017-01-09 23:42:38', 0),
(137, 'MAR District - Springside Chestnut Hill Academy Event', 'PAPHI', '2017-03-17 00:00:00', '2017-01-09 23:42:38', 0),
(138, 'MAR District - Westtown Event', 'PAWCH', '2017-03-10 00:00:00', '2017-01-09 23:42:38', 0),
(139, 'Pacific Northwest District Championship', 'PNCMP', '2017-04-05 00:00:00', '2017-01-09 23:42:38', 0),
(140, 'Festival de Robotique - Montreal Regional', 'QCMO', '2017-03-22 00:00:00', '2017-01-09 23:42:38', 0),
(141, 'NE District - Rhode Island Event', 'RIPRO', '2017-03-24 00:00:00', '2017-01-09 23:42:38', 0),
(142, 'FIRST Championship - Houston - Roebling Subdivision', 'ROEBLING', '2017-04-19 00:00:00', '2017-01-09 23:42:38', 0),
(143, 'Palmetto Regional', 'SCMB', '2017-03-01 00:00:00', '2017-01-09 23:42:38', 0),
(144, 'FIRST Championship - St. Louis - Tesla Subdivision', 'TESLA', '2017-04-26 00:00:00', '2017-01-09 23:42:38', 0),
(145, 'Smoky Mountains Regional', 'TNKN', '2017-03-22 00:00:00', '2017-01-09 23:42:38', 0),
(146, 'FIRST Championship - Houston - Turing Subdivision', 'TURING', '2017-04-19 00:00:00', '2017-01-09 23:42:38', 0),
(147, 'Dallas Regional', 'TXDA', '2017-03-08 00:00:00', '2017-01-09 23:42:38', 0),
(148, 'Lone Star Central Regional', 'TXHO', '2017-03-15 00:00:00', '2017-01-09 23:42:38', 0),
(149, 'Hub City Regional', 'TXLU', '2017-03-01 00:00:00', '2017-01-09 23:42:38', 0),
(150, 'Alamo Regional', 'TXSA', '2017-04-05 00:00:00', '2017-01-09 23:42:38', 0),
(151, 'Brazos Valley Regional', 'TXWA', '2017-03-30 00:00:00', '2017-01-09 23:42:38', 0),
(152, 'Lone Star North Regional', 'TXWO', '2017-03-30 00:00:00', '2017-01-09 23:42:38', 0),
(153, 'Utah Regional', 'UTWV', '2017-03-08 00:00:00', '2017-01-09 23:42:38', 0),
(154, 'CHS District - Southwest Virginia Event', 'VABLA', '2017-03-03 00:00:00', '2017-01-09 23:42:38', 0),
(155, 'CHS District - Central Virginia Event', 'VAGLE', '2017-03-24 00:00:00', '2017-01-09 23:42:38', 0),
(156, 'CHS District - Northern Virginia Event', 'VAHAY', '2017-03-03 00:00:00', '2017-01-09 23:42:38', 0),
(157, 'CHS District - Hampton Roads Event', 'VAPOR', '2017-03-17 00:00:00', '2017-01-09 23:42:38', 0),
(158, 'PNW District - Auburn Event', 'WAAHS', '2017-03-31 00:00:00', '2017-01-09 23:42:38', 0),
(159, 'PNW District - Auburn Mountainview Event', 'WAAMV', '2017-03-03 00:00:00', '2017-01-09 23:42:38', 0),
(160, 'PNW District - Central Washington University Event', 'WAELL', '2017-03-16 00:00:00', '2017-01-09 23:42:38', 0),
(161, 'PNW District - Mount Vernon Event', 'WAMOU', '2017-03-17 00:00:00', '2017-01-09 23:42:38', 0),
(162, 'PNW District - Glacier Peak Event', 'WASNO', '2017-03-24 00:00:00', '2017-01-09 23:42:38', 0),
(163, 'PNW District - West Valley Event', 'WASPO', '2017-03-02 00:00:00', '2017-01-09 23:42:38', 0),
(164, 'Seven Rivers Regional', 'WILA', '2017-04-12 00:00:00', '2017-01-09 23:42:38', 0),
(165, 'Wisconsin Regional', 'WIMI', '2017-03-22 00:00:00', '2017-01-09 23:42:38', 0);

-- --------------------------------------------------------

--
-- Table structure for table `fact_match_data_2017`
--

CREATE TABLE IF NOT EXISTS `fact_match_data_2017` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `event_id` int(4) unsigned NOT NULL,
  `team_id` int(5) unsigned NOT NULL,
  `match_id` int(4) unsigned NOT NULL,
  `practice_match` tinyint(1) NOT NULL DEFAULT '0',
  `position_id` int(3) unsigned NOT NULL,
  `auto_score_low` int(4) unsigned NOT NULL,
  `auto_score_high` int(4) unsigned NOT NULL,
  `auto_miss_high` int(4) unsigned NOT NULL,
  `auto_cross_baseline` tinyint(1) NOT NULL,
  `auto_gear_delivered_left` int(3) unsigned NOT NULL,
  `auto_gear_delivered_right` int(3) unsigned NOT NULL,
  `auto_gear_delivered_center` int(3) unsigned NOT NULL,
  `auto_dump_hopper` tinyint(1) NOT NULL,
  `score_low` int(4) unsigned NOT NULL,
  `score_high` int(4) unsigned NOT NULL,
  `miss_high` int(4) unsigned NOT NULL,
  `gear_delivered_left` int(3) unsigned NOT NULL,
  `gear_delivered_right` int(3) unsigned NOT NULL,
  `gear_delivered_center` int(3) unsigned NOT NULL,
  `climb_rope` tinyint(1) NOT NULL,
  `climb_attempt` tinyint(1) unsigned NOT NULL,
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
-- Table structure for table `fact_pilot_data_2017`
--

CREATE TABLE IF NOT EXISTS `fact_pilot_data_2017` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `event_id` int(4) unsigned NOT NULL,
  `team_id` int(5) unsigned NOT NULL,
  `match_id` int(4) unsigned NOT NULL,
  `practice_match` tinyint(1) NOT NULL DEFAULT '0',
  `position_id` int(3) unsigned NOT NULL,
  `gears_installed_2` int(3) unsigned NOT NULL,
  `gears_installed_3` int(3) unsigned NOT NULL,
  `gears_installed_4` int(3) unsigned NOT NULL,
  `gears_lifted` int(3) unsigned NOT NULL,
  `rotor_1_started` tinyint(1) NOT NULL,
  `rotor_2_started` tinyint(1) NOT NULL,
  `rotor_3_started` tinyint(1) NOT NULL,
  `rotor_4_started` tinyint(1) NOT NULL,
  `foul` tinyint(1) NOT NULL,
  `yellow_card` tinyint(1) NOT NULL,
  `red_card` tinyint(1) NOT NULL,
  `notes` text COLLATE utf8_unicode_ci NOT NULL,
  `invalid` tinyint(1) NOT NULL DEFAULT '0',
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `event_id` (`event_id`,`team_id`,`match_id`,`practice_match`,`position_id`),
  KEY `invalid` (`invalid`,`timestamp`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `game_info`
--

CREATE TABLE IF NOT EXISTS `game_info` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `keystring` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `intvalue` int(15) NOT NULL,
  `stringval` text COLLATE latin1_general_cs NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `invalid` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `key` (`keystring`),
  KEY `timestamp` (`timestamp`,`invalid`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs AUTO_INCREMENT=3 ;

--
-- Dumping data for table `game_info`
--

INSERT INTO `game_info` (`id`, `keystring`, `intvalue`, `stringval`, `timestamp`, `invalid`) VALUES
(1, '2017_rotor_3_preinstalled', 1, '', '2017-01-13 21:09:25', 0),
(2, '2017_rotor_4_preinstalled', 2, '', '2017-01-13 21:09:36', 0);

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
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs AUTO_INCREMENT=11 ;

--
-- Dumping data for table `notes_options`
--

INSERT INTO `notes_options` (`id`, `option_text`, `timestamp`, `invalid`) VALUES
(1, 'No Show', '2014-01-25 15:21:36', 0),
(2, 'Non-functional', '2014-01-25 15:21:36', 0),
(10, 'Stopped responding mid-match', '2016-03-17 23:53:21', 0),
(3, 'Defender', '2016-01-15 16:30:02', 0);

-- --------------------------------------------------------

--
-- Table structure for table `picklist`
--

CREATE TABLE IF NOT EXISTS `picklist` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `event_id` int(10) unsigned NOT NULL,
  `team_id` int(10) unsigned NOT NULL,
  `sort` int(3) unsigned NOT NULL,
  `picked` tinyint(1) NOT NULL DEFAULT '0',
  `removed` tinyint(1) unsigned NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `invalid` tinyint(1) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `event_id` (`event_id`,`team_id`,`picked`),
  KEY `removed` (`removed`),
  KEY `sort` (`sort`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs AUTO_INCREMENT=10 ;

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs AUTO_INCREMENT=9 ;

--
-- Dumping data for table `position_lu`
--

INSERT INTO `position_lu` (`id`, `position`, `timestamp`, `invalid`) VALUES
(1, 'Red 1', '2015-02-12 00:30:10', 0),
(2, 'Red 2', '2015-02-12 00:30:10', 0),
(3, 'Red 3', '2015-02-12 00:30:36', 0),
(4, 'Blue 1', '2015-02-12 00:30:36', 0),
(5, 'Blue 2', '2015-02-12 00:30:50', 0),
(6, 'Blue 3', '2015-02-12 00:30:50', 0),
(7, 'Red Pilot', '2017-01-08 18:03:16', 0),
(8, 'Blue Pilot', '2017-01-08 18:03:16', 0);

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
-- Table structure for table `scout_pit_data_2017`
--

CREATE TABLE IF NOT EXISTS `scout_pit_data_2017` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `team_id` int(5) unsigned NOT NULL,
  `can_score_high` tinyint(1) NOT NULL,
  `can_score_low` tinyint(1) NOT NULL,
  `can_score_gears` tinyint(1) NOT NULL,
  `can_climb` tinyint(1) NOT NULL,
  `ground_load_fuel` tinyint(1) NOT NULL,
  `hopper_load_fuel` tinyint(1) NOT NULL,
  `station_load_fuel` tinyint(1) NOT NULL,
  `ground_load_gear` tinyint(1) NOT NULL,
  `station_load_gear` tinyint(1) NOT NULL,
  `custom_rope` tinyint(1) NOT NULL,
  `auto_score_high_count` int(4) unsigned NOT NULL,
  `auto_score_low_count` int(4) unsigned NOT NULL,
  `auto_gear` tinyint(1) NOT NULL,
  `auto_hopper` tinyint(1) NOT NULL,
  `tele_score_high_count` int(4) unsigned NOT NULL,
  `tele_score_low_count` int(4) unsigned NOT NULL,
  `accuracy` int(3) unsigned NOT NULL,
  `fuel_capacity` int(4) unsigned NOT NULL,
  `scoring_speed_bps` int(2) unsigned NOT NULL,
  `loading_speed_bps` int(3) unsigned NOT NULL,
  `max_robot_speed_fts` int(3) unsigned NOT NULL,
  `robot_gross_weight_lbs` int(4) unsigned NOT NULL,
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
