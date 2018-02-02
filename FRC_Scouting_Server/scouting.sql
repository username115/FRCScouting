-- phpMyAdmin SQL Dump
-- version 4.0.10.14
-- http://www.phpmyadmin.net
--
-- Host: localhost:3306
-- Generation Time: Jan 08, 2018 at 07:42 PM
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
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs AUTO_INCREMENT=185 ;

--
-- Dumping data for table `event_lu`
--

INSERT INTO `event_lu` (`id`, `event_name`, `event_code`, `date_start`, `timestamp`, `invalid`) VALUES
(1, 'Canadian Rockies Regional', 'ABCA', '2018-04-04 00:00:00', '2018-01-09 00:26:01', 0),
(2, 'Rocket City Regional', 'ALHU', '2018-03-14 00:00:00', '2018-01-09 00:26:01', 0),
(3, 'FIRST Championship - Detroit - Archimedes Subdivision', 'ARCHIMEDES', '2018-04-25 00:00:00', '2018-01-09 00:26:01', 0),
(4, 'FIRST Championship - Detroit - ARDA Division', 'ARDA', '2018-04-25 00:00:00', '2018-01-09 00:26:01', 0),
(5, 'Arkansas Rock City Regional', 'ARLI', '2018-03-07 00:00:00', '2018-01-09 00:26:01', 0),
(6, 'Southern Cross Regional', 'AUSC', '2018-03-10 00:00:00', '2018-01-09 00:26:01', 0),
(7, 'South Pacific Regional', 'AUSP', '2018-03-16 00:00:00', '2018-01-09 00:26:01', 0),
(8, 'Arizona North Regional', 'AZFL', '2018-03-07 00:00:00', '2018-01-09 00:26:01', 0),
(9, 'Arizona West Regional', 'AZPX', '2018-04-04 00:00:00', '2018-01-09 00:26:01', 0),
(10, 'Canadian Pacific Regional', 'BCVI', '2018-03-13 00:00:00', '2018-01-09 00:26:01', 0),
(11, 'Aerospace Valley Regional', 'CAAV', '2018-04-04 00:00:00', '2018-01-09 00:26:01', 0),
(12, 'Sacramento Regional', 'CADA', '2018-03-21 00:00:00', '2018-01-09 00:26:01', 0),
(13, 'Central Valley Regional', 'CAFR', '2018-04-05 00:00:00', '2018-01-09 00:26:01', 0),
(14, 'Orange County Regional', 'CAIR', '2018-03-21 00:00:00', '2018-01-09 00:26:01', 0),
(15, 'FIRST Championship - Houston - CANE Division', 'CANE', '2018-04-18 00:00:00', '2018-01-09 00:26:01', 0),
(16, 'Los Angeles Regional', 'CAPO', '2018-03-14 00:00:00', '2018-01-09 00:26:01', 0),
(17, 'FIRST Championship - Detroit - Carson Subdivision', 'CARSON', '2018-04-25 00:00:00', '2018-01-09 00:26:01', 0),
(18, 'FIRST Championship - Houston - Carver Subdivision', 'CARVER', '2018-04-18 00:00:00', '2018-01-09 00:26:01', 0),
(19, 'San Diego Regional presented by Qualcomm', 'CASD', '2018-03-07 00:00:00', '2018-01-09 00:26:01', 0),
(20, 'San Francisco Regional', 'CASF', '2018-03-15 00:00:00', '2018-01-09 00:26:01', 0),
(21, 'Silicon Valley Regional', 'CASJ', '2018-03-28 00:00:00', '2018-01-09 00:26:01', 0),
(22, 'FIRST Championship - Detroit - CATE Division', 'CATE', '2018-04-25 00:00:00', '2018-01-09 00:26:01', 0),
(23, 'Ventura Regional', 'CAVE', '2018-03-28 00:00:00', '2018-01-09 00:26:01', 0),
(24, 'FIRST Chesapeake District Championship', 'CHCMP', '2018-03-28 00:00:00', '2018-01-09 00:26:01', 0),
(25, 'FIRST Championship - Detroit', 'CMPMI', '2018-04-25 00:00:00', '2018-01-09 00:26:01', 0),
(26, 'FIRST Championship - Houston', 'CMPTX', '2018-04-18 00:00:00', '2018-01-09 00:26:01', 0),
(27, 'Colorado Regional', 'CODE', '2018-03-21 00:00:00', '2018-01-09 00:26:01', 0),
(28, 'NE District Hartford Event', 'CTHAR', '2018-04-06 00:00:00', '2018-01-09 00:26:01', 0),
(29, 'NE District Southern CT Event', 'CTSCT', '2018-03-16 00:00:00', '2018-01-09 00:26:01', 0),
(30, 'NE District Waterbury Event', 'CTWAT', '2018-03-09 00:00:00', '2018-01-09 00:26:01', 0),
(31, 'FIRST Championship - Detroit - CUDA Division', 'CUDA', '2018-04-25 00:00:00', '2018-01-09 00:26:01', 0),
(32, 'FIRST Championship - Detroit - Curie Subdivision', 'CURIE', '2018-04-25 00:00:00', '2018-01-09 00:26:01', 0),
(33, 'FIRST Championship - Detroit - Daly Subdivision', 'DALY', '2018-04-25 00:00:00', '2018-01-09 00:26:01', 0),
(34, 'FIRST Championship - Detroit - Darwin Subdivision', 'DARWIN', '2018-04-25 00:00:00', '2018-01-09 00:26:01', 0),
(35, 'Orlando Regional', 'FLOR', '2018-03-07 00:00:00', '2018-01-09 00:26:01', 0),
(36, 'South Florida Regional', 'FLWP', '2018-03-28 00:00:00', '2018-01-09 00:26:01', 0),
(37, 'PCH District Albany Event', 'GAALB', '2018-03-15 00:00:00', '2018-01-09 00:26:01', 0),
(38, 'Peachtree District State Championship', 'GACMP', '2018-04-04 00:00:00', '2018-01-09 00:26:01', 0),
(39, 'PCH District Columbus Event', 'GACOL', '2018-03-22 00:00:00', '2018-01-09 00:26:01', 0),
(40, 'PCH District Dalton Event', 'GADAL', '2018-03-08 00:00:00', '2018-01-09 00:26:01', 0),
(41, 'PCH District Duluth Event', 'GADUL', '2018-03-29 00:00:00', '2018-01-09 00:26:01', 0),
(42, 'PCH District Gainesville Event', 'GAGAI', '2018-03-02 00:00:00', '2018-01-09 00:26:01', 0),
(43, 'FIRST Championship - Houston - Galileo Subdivision', 'GALILEO', '2018-04-18 00:00:00', '2018-01-09 00:26:01', 0),
(44, 'FIRST Championship - Houston - GARO Division', 'GARO', '2018-04-18 00:00:00', '2018-01-09 00:26:01', 0),
(45, 'Shenzhen Regional', 'GUSH', '2018-03-07 00:00:00', '2018-01-09 00:26:01', 0),
(46, 'Hawaii Regional', 'HIHO', '2018-03-21 00:00:00', '2018-01-09 00:26:01', 0),
(47, 'FIRST Championship - Houston - Hopper Subdivision', 'HOPPER', '2018-04-18 00:00:00', '2018-01-09 00:26:01', 0),
(48, 'FIRST Championship - Houston - HOTU Division', 'HOTU', '2018-04-18 00:00:00', '2018-01-09 00:26:01', 0),
(49, 'Iowa Regional', 'IACF', '2018-03-21 00:00:00', '2018-01-09 00:26:01', 0),
(50, 'Idaho Regional', 'IDBO', '2018-03-28 00:00:00', '2018-01-09 00:26:01', 0),
(51, 'Midwest Regional', 'ILCH', '2018-04-04 00:00:00', '2018-01-09 00:26:01', 0),
(52, 'Central Illinois Regional', 'ILPE', '2018-03-14 00:00:00', '2018-01-09 00:26:01', 0),
(53, 'Indiana State Championship', 'INCMP', '2018-04-12 00:00:00', '2018-01-09 00:26:01', 0),
(54, 'IN District St. Joseph Event', 'INMIS', '2018-03-09 00:00:00', '2018-01-09 00:26:01', 0),
(55, 'IN District Plainfield Event sponsored by Toyota', 'INPLA', '2018-03-23 00:00:00', '2018-01-09 00:26:01', 0),
(56, 'IN District Tippecanoe Event', 'INWLA', '2018-03-29 00:00:00', '2018-01-09 00:26:01', 0),
(57, 'FIRST Israel District Championship', 'ISCMP', '2018-03-27 00:00:00', '2018-01-09 00:26:01', 0),
(58, 'ISR District Event #1', 'ISDE1', '2018-03-05 00:00:00', '2018-01-09 00:26:01', 0),
(59, 'ISR District Event #2', 'ISDE2', '2018-03-07 00:00:00', '2018-01-09 00:26:01', 0),
(60, 'ISR District Event #3', 'ISDE3', '2018-03-12 00:00:00', '2018-01-09 00:26:01', 0),
(61, 'ISR District Event #4', 'ISDE4', '2018-03-14 00:00:00', '2018-01-09 00:26:01', 0),
(62, 'Bayou Regional', 'LAKE', '2018-03-21 00:00:00', '2018-01-09 00:26:01', 0),
(63, 'NE District Greater Boston Event', 'MABOS', '2018-04-06 00:00:00', '2018-01-09 00:26:01', 0),
(64, 'NE District SE Mass Event', 'MABRI', '2018-03-09 00:00:00', '2018-01-09 00:26:01', 0),
(65, 'NE District North Shore Event', 'MAREA', '2018-03-16 00:00:00', '2018-01-09 00:26:01', 0),
(66, 'NE District Worcester Polytechnic Institute Event', 'MAWOR', '2018-03-01 00:00:00', '2018-01-09 00:26:01', 0),
(67, 'CHS District Central Maryland Event sponsored by Leidos', 'MDEDG', '2018-03-23 00:00:00', '2018-01-09 00:26:01', 0),
(68, 'CHS District Southern Maryland Event', 'MDOXO', '2018-03-16 00:00:00', '2018-01-09 00:26:01', 0),
(69, 'NE District Pine Tree Event', 'MELEW', '2018-03-22 00:00:00', '2018-01-09 00:26:01', 0),
(70, 'FIM District Alpena Event', 'MIALP', '2018-04-05 00:00:00', '2018-01-09 00:26:01', 0),
(71, 'FIM District Belleville Event', 'MIBEL', '2018-03-22 00:00:00', '2018-01-09 00:26:01', 0),
(72, 'FIM District Center Line Event', 'MICEN', '2018-03-08 00:00:00', '2018-01-09 00:26:01', 0),
(73, 'Michigan State Championship', 'MICMP', '2018-04-11 00:00:00', '2018-01-09 00:26:01', 0),
(74, 'Michigan State Championship - Field 1 Division', 'MICMP1', '2018-04-11 00:00:00', '2018-01-09 00:26:01', 0),
(75, 'Michigan State Championship - Field 2 Division', 'MICMP2', '2018-04-11 00:00:00', '2018-01-09 00:26:01', 0),
(76, 'Michigan State Championship - Field 3 Division', 'MICMP3', '2018-04-11 00:00:00', '2018-01-09 00:26:01', 0),
(77, 'Michigan State Championship - Field 4 Division', 'MICMP4', '2018-04-11 00:00:00', '2018-01-09 00:26:01', 0),
(78, 'FIM District Escanaba Event', 'MIESC', '2018-03-15 00:00:00', '2018-01-09 00:26:01', 0),
(79, 'FIM District Forest Hills Event', 'MIFOR', '2018-04-05 00:00:00', '2018-01-09 00:26:01', 0),
(80, 'FIM District Gaylord Event', 'MIGAY', '2018-03-15 00:00:00', '2018-01-09 00:26:01', 0),
(81, 'FIM District Gibraltar Event', 'MIGIB', '2018-03-01 00:00:00', '2018-01-09 00:26:01', 0),
(82, 'FIM District Gull Lake Event', 'MIGUL', '2018-03-15 00:00:00', '2018-01-09 00:26:01', 0),
(83, 'FIM District Kettering University Event #2', 'MIKE2', '2018-03-08 00:00:00', '2018-01-09 00:26:01', 0),
(84, 'FIM District East Kentwood Event', 'MIKEN', '2018-03-29 00:00:00', '2018-01-09 00:26:01', 0),
(85, 'FIM District Kettering University Event #1', 'MIKET', '2018-03-01 00:00:00', '2018-01-09 00:26:01', 0),
(86, 'FIM District Lakeview Event', 'MILAK', '2018-04-05 00:00:00', '2018-01-09 00:26:01', 0),
(87, 'FIM District Lansing Event', 'MILAN', '2018-03-22 00:00:00', '2018-01-09 00:26:01', 0),
(88, 'FIM District Lincoln Event', 'MILIN', '2018-03-15 00:00:00', '2018-01-09 00:26:01', 0),
(89, 'FIM District Livonia Event', 'MILIV', '2018-03-29 00:00:00', '2018-01-09 00:26:01', 0),
(90, 'FIM District Lake Superior State University Event', 'MILSU', '2018-04-05 00:00:00', '2018-01-09 00:26:01', 0),
(91, 'FIM District Marysville Event', 'MIMAR', '2018-04-05 00:00:00', '2018-01-09 00:26:01', 0),
(92, 'FIM District Midland Event', 'MIMID', '2018-03-22 00:00:00', '2018-01-09 00:26:01', 0),
(93, 'FIM District Milford Event', 'MIMIL', '2018-03-15 00:00:00', '2018-01-09 00:26:01', 0),
(94, 'FIM District Shepherd Event', 'MISHE', '2018-03-29 00:00:00', '2018-01-09 00:26:01', 0),
(95, 'FIM District St. Joseph Event', 'MISJO', '2018-03-08 00:00:00', '2018-01-09 00:26:01', 0),
(96, 'FIM District Southfield Event', 'MISOU', '2018-03-01 00:00:00', '2018-01-09 00:26:01', 0),
(97, 'FIM District Troy Event', 'MITRY', '2018-03-29 00:00:00', '2018-01-09 00:26:01', 0),
(98, 'FIM District Traverse City Event', 'MITVC', '2018-03-01 00:00:00', '2018-01-09 00:26:01', 0),
(99, 'FIM District Waterford Event', 'MIWAT', '2018-03-08 00:00:00', '2018-01-09 00:26:01', 0),
(100, 'FIM District West Michigan Event', 'MIWMI', '2018-03-22 00:00:00', '2018-01-09 00:26:01', 0),
(101, 'Lake Superior Regional', 'MNDU', '2018-03-07 00:00:00', '2018-01-09 00:26:01', 0),
(102, 'Northern Lights Regional', 'MNDU2', '2018-03-07 00:00:00', '2018-01-09 00:26:01', 0),
(103, 'Minnesota 10000 Lakes Regional', 'MNMI', '2018-03-28 00:00:00', '2018-01-09 00:26:01', 0),
(104, 'Minnesota North Star Regional', 'MNMI2', '2018-03-28 00:00:00', '2018-01-09 00:26:01', 0),
(105, 'Greater Kansas City Regional', 'MOKC', '2018-03-14 00:00:00', '2018-01-09 00:26:01', 0),
(106, 'Heartland Regional', 'MOKC2', '2018-03-08 00:00:00', '2018-01-09 00:26:01', 0),
(107, 'St. Louis Regional', 'MOSL', '2018-03-07 00:00:00', '2018-01-09 00:26:01', 0),
(108, 'FIRST Mid-Atlantic District Championship', 'MRCMP', '2018-04-04 00:00:00', '2018-01-09 00:26:01', 0),
(109, 'Monterrey Regional', 'MXMO', '2018-02-28 00:00:00', '2018-01-09 00:26:01', 0),
(110, 'Laguna Regional', 'MXTO', '2018-03-21 00:00:00', '2018-01-09 00:26:01', 0),
(111, 'NC District UNC Asheville Event', 'NCASH', '2018-03-23 00:00:00', '2018-01-09 00:26:01', 0),
(112, 'FIRST North Carolina State Championship', 'NCCMP', '2018-04-06 00:00:00', '2018-01-09 00:26:01', 0),
(113, 'NC District Pitt County Event', 'NCGRE', '2018-03-09 00:00:00', '2018-01-09 00:26:01', 0),
(114, 'NC District UNC Pembroke Event', 'NCPEM', '2018-03-16 00:00:00', '2018-01-09 00:26:01', 0),
(115, 'NC District Forsyth County Event', 'NCWIN', '2018-03-29 00:00:00', '2018-01-09 00:26:01', 0),
(116, 'Great Northern Regional', 'NDGF', '2018-02-28 00:00:00', '2018-01-09 00:26:01', 0),
(117, 'New England District Championship', 'NECMP', '2018-04-11 00:00:00', '2018-01-09 00:26:01', 0),
(118, 'FIRST Championship - Houston - Newton Subdivision', 'NEWTON', '2018-04-18 00:00:00', '2018-01-09 00:26:01', 0),
(119, 'NE District UNH Event', 'NHDUR', '2018-03-29 00:00:00', '2018-01-09 00:26:01', 0),
(120, 'NE District Granite State Event', 'NHGRS', '2018-03-02 00:00:00', '2018-01-09 00:26:01', 0),
(121, 'MAR District Bridgewater-Raritan Event', 'NJBRI', '2018-03-16 00:00:00', '2018-01-09 00:26:01', 0),
(122, 'MAR District Mount Olive Event', 'NJFLA', '2018-03-09 00:00:00', '2018-01-09 00:26:01', 0),
(123, 'MAR District Montgomery Event', 'NJSKI', '2018-03-23 00:00:00', '2018-01-09 00:26:01', 0),
(124, 'MAR District Seneca Event', 'NJTAB', '2018-03-23 00:00:00', '2018-01-09 00:26:01', 0),
(125, 'Las Vegas Regional', 'NVLV', '2018-03-21 00:00:00', '2018-01-09 00:26:01', 0),
(126, 'SBPLI Long Island Regional #1', 'NYLI', '2018-04-09 00:00:00', '2018-01-09 00:26:01', 0),
(127, 'SBPLI Long Island Regional #2', 'NYLI2', '2018-04-12 00:00:00', '2018-01-09 00:26:01', 0),
(128, 'New York City Regional', 'NYNY', '2018-04-05 00:00:00', '2018-01-09 00:26:01', 0),
(129, 'Finger Lakes Regional', 'NYRO', '2018-03-14 00:00:00', '2018-01-09 00:26:01', 0),
(130, 'Hudson Valley Regional', 'NYSU', '2018-03-22 00:00:00', '2018-01-09 00:26:01', 0),
(131, 'New York Tech Valley Regional', 'NYTR', '2018-03-14 00:00:00', '2018-01-09 00:26:01', 0),
(132, 'Central New York Regional', 'NYUT', '2018-03-01 00:00:00', '2018-01-09 00:26:01', 0),
(133, 'Buckeye Regional', 'OHCL', '2018-03-28 00:00:00', '2018-01-09 00:26:01', 0),
(134, 'Miami Valley Regional', 'OHMV', '2018-02-28 00:00:00', '2018-01-09 00:26:01', 0),
(135, 'Oklahoma Regional', 'OKOK', '2018-03-28 00:00:00', '2018-01-09 00:26:01', 0),
(136, 'ONT District Georgian College Event', 'ONBAR', '2018-03-02 00:00:00', '2018-01-09 00:26:01', 0),
(137, 'FIRST Ontario Provincial Championship', 'ONCMP', '2018-04-11 00:00:00', '2018-01-09 00:26:01', 0),
(138, 'FIRST Ontario Provincial Championship - Science Division', 'ONCMP1', '2018-04-11 00:00:00', '2018-01-09 00:26:01', 0),
(139, 'FIRST Ontario Provincial Championship - Technology Division', 'ONCMP2', '2018-04-11 00:00:00', '2018-01-09 00:26:01', 0),
(140, 'ONT District McMaster University Event', 'ONHAM', '2018-04-06 00:00:00', '2018-01-09 00:26:01', 0),
(141, 'ONT District Western University, Western Engineering Event', 'ONLON', '2018-04-06 00:00:00', '2018-01-09 00:26:01', 0),
(142, 'ONT District North Bay Event', 'ONNOB', '2018-03-29 00:00:00', '2018-01-09 00:26:01', 0),
(143, 'ONT District York University Event', 'ONNYO', '2018-03-23 00:00:00', '2018-01-09 00:26:01', 0),
(144, 'ONT District Durham College Event', 'ONOSH', '2018-03-02 00:00:00', '2018-01-09 00:26:01', 0),
(145, 'ONT District Ryerson University Event', 'ONTO1', '2018-03-15 00:00:00', '2018-01-09 00:26:01', 0),
(146, 'ONT District University of Waterloo Event', 'ONWAT', '2018-03-22 00:00:00', '2018-01-09 00:26:01', 0),
(147, 'ONT District Windsor Essex Great Lakes Event', 'ONWIN', '2018-03-29 00:00:00', '2018-01-09 00:26:01', 0),
(148, 'PNW District Lake Oswego Event', 'ORLAK', '2018-03-29 00:00:00', '2018-01-09 00:26:01', 0),
(149, 'PNW District Clackamas Academy Event', 'ORORE', '2018-03-01 00:00:00', '2018-01-09 00:26:01', 0),
(150, 'PNW District Wilsonville Event', 'ORWIL', '2018-03-08 00:00:00', '2018-01-09 00:26:01', 0),
(151, 'Greater Pittsburgh Regional', 'PACA', '2018-03-21 00:00:00', '2018-01-09 00:26:01', 0),
(152, 'MAR District Hatboro-Horsham Event', 'PAHAT', '2018-03-02 00:00:00', '2018-01-09 00:26:01', 0),
(153, 'MAR District Springside Chestnut Hill Academy Event', 'PAPHI', '2018-03-16 00:00:00', '2018-01-09 00:26:01', 0),
(154, 'MAR District Westtown Event', 'PAWCH', '2018-03-09 00:00:00', '2018-01-09 00:26:01', 0),
(155, 'Pacific Northwest District Championship', 'PNCMP', '2018-04-04 00:00:00', '2018-01-09 00:26:01', 0),
(156, 'Festival de Robotique - Montreal Regional', 'QCMO', '2018-02-28 00:00:00', '2018-01-09 00:26:01', 0),
(157, 'NE District Rhode Island Event', 'RISMI', '2018-03-23 00:00:00', '2018-01-09 00:26:01', 0),
(158, 'FIRST Championship - Houston - Roebling Subdivision', 'ROEBLING', '2018-04-18 00:00:00', '2018-01-09 00:26:01', 0),
(159, 'Palmetto Regional', 'SCMB', '2018-02-28 00:00:00', '2018-01-09 00:26:01', 0),
(160, 'Shanghai Regional', 'SHMI', '2018-03-15 00:00:00', '2018-01-09 00:26:01', 0),
(161, 'FIRST Championship - Detroit - Tesla Subdivision', 'TESLA', '2018-04-25 00:00:00', '2018-01-09 00:26:01', 0),
(162, 'Smoky Mountains Regional', 'TNKN', '2018-03-21 00:00:00', '2018-01-09 00:26:01', 0),
(163, 'Istanbul Regional', 'TUIS', '2018-03-09 00:00:00', '2018-01-09 00:26:01', 0),
(164, 'FIRST Championship - Houston - Turing Subdivision', 'TURING', '2018-04-18 00:00:00', '2018-01-09 00:26:01', 0),
(165, 'Dallas Regional', 'TXDA', '2018-03-01 00:00:00', '2018-01-09 00:26:01', 0),
(166, 'El Paso Regional', 'TXEL', '2018-03-28 00:00:00', '2018-01-09 00:26:01', 0),
(167, 'Lone Star Central Regional', 'TXHO', '2018-03-14 00:00:00', '2018-01-09 00:26:01', 0),
(168, 'Hub City Regional', 'TXLU', '2018-03-07 00:00:00', '2018-01-09 00:26:01', 0),
(169, 'Lone Star South Regional', 'TXPA', '2018-04-04 00:00:00', '2018-01-09 00:26:01', 0),
(170, 'Alamo Regional', 'TXSA', '2018-04-04 00:00:00', '2018-01-09 00:26:01', 0),
(171, 'Utah Regional', 'UTWV', '2018-02-28 00:00:00', '2018-01-09 00:26:01', 0),
(172, 'CHS District Southwest Virginia Event', 'VABLA', '2018-03-23 00:00:00', '2018-01-09 00:26:01', 0),
(173, 'CHS District Greater DC Event co-sponsored by Micron', 'VAGDC', '2018-03-09 00:00:00', '2018-01-09 00:26:01', 0),
(174, 'CHS District Central Virginia Event', 'VAGLE', '2018-03-09 00:00:00', '2018-01-09 00:26:01', 0),
(175, 'CHS District Northern Virginia Event', 'VAHAY', '2018-03-02 00:00:00', '2018-01-09 00:26:01', 0),
(176, 'CHS District Hampton Roads Event sponsored by Newport News Shipbuildin', 'VAPOR', '2018-03-16 00:00:00', '2018-01-09 00:26:01', 0),
(177, 'PNW District Auburn Event', 'WAAHS', '2018-03-16 00:00:00', '2018-01-09 00:26:01', 0),
(178, 'PNW District Auburn Mountainview Event', 'WAAMV', '2018-03-29 00:00:00', '2018-01-09 00:26:01', 0),
(179, 'PNW District Mount Vernon Event', 'WAMOU', '2018-03-02 00:00:00', '2018-01-09 00:26:01', 0),
(180, 'PNW District Glacier Peak Event', 'WASNO', '2018-03-23 00:00:00', '2018-01-09 00:26:01', 0),
(181, 'PNW District West Valley Event', 'WASPO', '2018-03-22 00:00:00', '2018-01-09 00:26:01', 0),
(182, 'PNW District SunDome Event', 'WAYAK', '2018-03-15 00:00:00', '2018-01-09 00:26:01', 0),
(183, 'Seven Rivers Regional', 'WILA', '2018-04-04 00:00:00', '2018-01-09 00:26:01', 0),
(184, 'Wisconsin Regional', 'WIMI', '2018-03-21 00:00:00', '2018-01-09 00:26:01', 0);

-- --------------------------------------------------------

--
-- Table structure for table `fact_match_data_2018`
--

CREATE TABLE IF NOT EXISTS `fact_match_data_2018` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `event_id` int(4) unsigned NOT NULL,
  `team_id` int(5) unsigned NOT NULL,
  `match_id` int(4) unsigned NOT NULL,
  `practice_match` tinyint(1) NOT NULL DEFAULT '0',
  `position_id` int(3) unsigned NOT NULL,
  `near_switch_right` tinyint(1) unsigned NOT NULL,
  `scale_right` tinyint(1) unsigned NOT NULL,
  `far_switch_right` tinyint(1) unsigned NOT NULL,
  `auto_run` tinyint(1) unsigned NOT NULL,
  `auto_switch_count` int(2) unsigned NOT NULL,
  `auto_switch_wrong_side_count` int(2) unsigned NOT NULL,
  `auto_scale_count` int(2) unsigned NOT NULL,
  `auto_scale_wrong_side_count` int(2) unsigned NOT NULL,
  `auto_exchange_count` int(2) unsigned NOT NULL,
  `switch_count` int(3) unsigned NOT NULL,
  `switch_wrong_side_count` int(3) unsigned NOT NULL,
  `scale_count` int(3) unsigned NOT NULL,
  `scale_wrong_side_count` int(3) unsigned NOT NULL,
  `opposite_switch_count` int(3) unsigned NOT NULL,
  `opposite_switch_wrong_side_count` int(3) unsigned NOT NULL,
  `exchange_count` int(3) unsigned NOT NULL,
  `parked` tinyint(1) unsigned NOT NULL,
  `climbed` tinyint(1) unsigned NOT NULL,
  `climb_attempt` tinyint(1) unsigned NOT NULL,
  `supported_others` tinyint(1) unsigned NOT NULL,
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
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs AUTO_INCREMENT=24 ;

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
-- Table structure for table `scout_pit_data_2018`
--

CREATE TABLE IF NOT EXISTS `scout_pit_data_2018` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `team_id` int(5) unsigned NOT NULL,
  `auto_run` tinyint(1) unsigned NOT NULL,
  `auto_switch_count` int(2) unsigned NOT NULL,
  `auto_scale_count` int(2) unsigned NOT NULL,
  `switch_score` tinyint(1) unsigned NOT NULL,
  `scale_score` tinyint(1) unsigned NOT NULL,
  `exchange` tinyint(1) unsigned NOT NULL,
  `climb` tinyint(1) unsigned NOT NULL,
  `supports_others` tinyint(1) unsigned NOT NULL,
  `floor_acquire` tinyint(1) unsigned NOT NULL,
  `exchange_acquire` tinyint(1) unsigned NOT NULL,
  `portal_acquire` tinyint(1) unsigned NOT NULL,
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
