

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `scouting`
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
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `event_name` varchar(120) COLLATE latin1_general_cs NOT NULL,
  `event_code` varchar(20) COLLATE latin1_general_cs NOT NULL,
  `date_start` datetime NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `invalid` tinyint(1) UNSIGNED NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `event_name` (`event_name`),
  UNIQUE KEY `event_code_2` (`event_code`),
  KEY `timestamp` (`timestamp`),
  KEY `event_code` (`event_code`)
) ENGINE=MyISAM AUTO_INCREMENT=194 DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

--
-- Dumping data for table `event_lu`
--

INSERT INTO `event_lu` (`id`, `event_name`, `event_code`, `date_start`, `timestamp`, `invalid`) VALUES
(1, 'Canadian Rockies Regional', 'ABCA', '2019-04-03 00:00:00', '2019-02-01 01:22:10', 0),
(2, 'Rocket City Regional', 'ALHU', '2019-03-13 00:00:00', '2019-02-01 01:22:10', 0),
(3, 'FIRST Championship - Detroit- FIRST Robotics Competition - Archimedes Subdivision', 'ARCHIMEDES', '2019-04-24 00:00:00', '2019-02-01 01:22:10', 0),
(4, 'FIRST Championship - Detroit- FIRST Robotics Competition - ARDA Division', 'ARDA', '2019-04-24 00:00:00', '2019-02-01 01:22:10', 0),
(5, 'Arkansas Rock City Regional', 'ARLI', '2019-03-06 00:00:00', '2019-02-01 01:22:10', 0),
(6, 'Southern Cross Regional', 'AUSC', '2019-03-09 00:00:00', '2019-02-01 01:22:10', 0),
(7, 'South Pacific Regional', 'AUSP', '2019-03-15 00:00:00', '2019-02-01 01:22:10', 0),
(8, 'Arizona North Regional', 'AZFL', '2019-03-13 00:00:00', '2019-02-01 01:22:10', 0),
(9, 'Arizona West Regional', 'AZPX', '2019-04-03 00:00:00', '2019-02-01 01:22:10', 0),
(10, 'Canadian Pacific Regional', 'BCVI', '2019-03-06 00:00:00', '2019-02-01 01:22:10', 0),
(11, 'Aerospace Valley Regional', 'CAAV', '2019-04-03 00:00:00', '2019-02-01 01:22:10', 0),
(12, 'Sacramento Regional', 'CADA', '2019-03-20 00:00:00', '2019-02-01 01:22:10', 0),
(13, 'Del Mar Regional presented by Qualcomm', 'CADM', '2019-02-28 00:00:00', '2019-02-01 01:22:10', 0),
(14, 'Central Valley Regional', 'CAFR', '2019-03-06 00:00:00', '2019-02-01 01:22:10', 0),
(15, 'Los Angeles Regional', 'CALA', '2019-03-20 00:00:00', '2019-02-01 01:22:10', 0),
(16, 'Los Angeles North Regional', 'CALN', '2019-03-14 00:00:00', '2019-02-01 01:22:10', 0),
(17, 'Monterey Bay Regional', 'CAMB', '2019-03-21 00:00:00', '2019-02-01 01:22:10', 0),
(18, 'FIRST Championship - Houston- FIRST Robotics Competition - CANE Division', 'CANE', '2019-04-17 00:00:00', '2019-02-01 01:22:10', 0),
(19, 'Orange County Regional', 'CAOC', '2019-02-27 00:00:00', '2019-02-01 01:22:10', 0),
(20, 'FIRST Championship - Detroit- FIRST Robotics Competition - Carson Subdivision', 'CARSON', '2019-04-24 00:00:00', '2019-02-01 01:22:10', 0),
(21, 'FIRST Championship - Houston- FIRST Robotics Competition - Carver Subdivision', 'CARVER', '2019-04-17 00:00:00', '2019-02-01 01:22:10', 0),
(22, 'San Diego Regional presented by Qualcomm', 'CASD', '2019-03-06 00:00:00', '2019-02-01 01:22:10', 0),
(23, 'San Francisco Regional', 'CASF', '2019-03-14 00:00:00', '2019-02-01 01:22:10', 0),
(24, 'Silicon Valley Regional', 'CASJ', '2019-03-28 00:00:00', '2019-02-01 01:22:10', 0),
(25, 'FIRST Championship - Detroit- FIRST Robotics Competition - CATE Division', 'CATE', '2019-04-24 00:00:00', '2019-02-01 01:22:10', 0),
(26, 'Ventura Regional', 'CAVE', '2019-03-27 00:00:00', '2019-02-01 01:22:10', 0),
(27, 'FIRST Chesapeake District Championship', 'CHCMP', '2019-04-10 00:00:00', '2019-02-01 01:22:10', 0),
(28, 'FIRST Championship - Detroit- FIRST Robotics Competition', 'CMPMI', '2019-04-24 00:00:00', '2019-02-01 01:22:10', 0),
(29, 'FIRST Championship - Houston- FIRST Robotics Competition', 'CMPTX', '2019-04-17 00:00:00', '2019-02-01 01:22:10', 0),
(30, 'Colorado Regional', 'CODE', '2019-03-20 00:00:00', '2019-02-01 01:22:10', 0),
(31, 'NE District Hartford Event', 'CTHAR', '2019-04-05 00:00:00', '2019-02-01 01:22:10', 0),
(32, 'NE District Waterbury Event', 'CTWAT', '2019-03-08 00:00:00', '2019-02-01 01:22:10', 0),
(33, 'FIRST Championship - Detroit- FIRST Robotics Competition - CUDA Division', 'CUDA', '2019-04-24 00:00:00', '2019-02-01 01:22:10', 0),
(34, 'FIRST Championship - Detroit- FIRST Robotics Competition - Curie Subdivision', 'CURIE', '2019-04-24 00:00:00', '2019-02-01 01:22:10', 0),
(35, 'FIRST Championship - Detroit- FIRST Robotics Competition - Daly Subdivision', 'DALY', '2019-04-24 00:00:00', '2019-02-01 01:22:10', 0),
(36, 'FIRST Championship - Detroit- FIRST Robotics Competition - Darwin Subdivision', 'DARWIN', '2019-04-24 00:00:00', '2019-02-01 01:22:10', 0),
(37, 'Orlando Regional', 'FLOR', '2019-03-13 00:00:00', '2019-02-01 01:22:10', 0),
(38, 'South Florida Regional ', 'FLWP', '2019-03-27 00:00:00', '2019-02-01 01:22:10', 0),
(39, 'FIRST In Texas District Championship', 'FTCMP', '2019-04-03 00:00:00', '2019-02-01 01:22:10', 0),
(40, 'PCH District Albany Event presented by Procter & Gamble', 'GAALB', '2019-03-14 00:00:00', '2019-02-01 01:22:10', 0),
(41, 'Peachtree District State Championship', 'GACMP', '2019-04-03 00:00:00', '2019-02-01 01:22:10', 0),
(42, 'PCH District Columbus Event', 'GACOL', '2019-03-21 00:00:00', '2019-02-01 01:22:10', 0),
(43, 'PCH District Dalton Event', 'GADAL', '2019-03-07 00:00:00', '2019-02-01 01:22:10', 0),
(44, 'PCH District Forsyth District Event', 'GAFOR', '2019-03-29 00:00:00', '2019-02-01 01:22:10', 0),
(45, 'PCH District Gainesville Event', 'GAGAI', '2019-03-01 00:00:00', '2019-02-01 01:22:10', 0),
(46, 'FIRST Championship - Houston- FIRST Robotics Competition - Galileo Subdivision', 'GALILEO', '2019-04-17 00:00:00', '2019-02-01 01:22:10', 0),
(47, 'FIRST Championship - Houston- FIRST Robotics Competition - GARO Division', 'GARO', '2019-04-17 00:00:00', '2019-02-01 01:22:10', 0),
(48, 'Hawaii Regional', 'HIHO', '2019-03-27 00:00:00', '2019-02-01 01:22:10', 0),
(49, 'FIRST Championship - Houston- FIRST Robotics Competition - Hopper Subdivision', 'HOPPER', '2019-04-17 00:00:00', '2019-02-01 01:22:10', 0),
(50, 'FIRST Championship - Houston- FIRST Robotics Competition - HOTU Division', 'HOTU', '2019-04-17 00:00:00', '2019-02-01 01:22:10', 0),
(51, 'Iowa Regional', 'IACF', '2019-03-20 00:00:00', '2019-02-01 01:22:10', 0),
(52, 'Idaho Regional', 'IDBO', '2019-03-27 00:00:00', '2019-02-01 01:22:10', 0),
(53, 'Midwest Regional', 'ILCH', '2019-03-06 00:00:00', '2019-02-01 01:22:10', 0),
(54, 'Central Illinois Regional', 'ILPE', '2019-03-20 00:00:00', '2019-02-01 01:22:10', 0),
(55, 'Indiana State Championship', 'INCMP', '2019-04-11 00:00:00', '2019-02-01 01:22:10', 0),
(56, 'IN District Center Grove Event sponsored by Toyota', 'INGRE', '2019-03-28 00:00:00', '2019-02-01 01:22:10', 0),
(57, 'IN District St. Joseph Event', 'INMIS', '2019-03-08 00:00:00', '2019-02-01 01:22:10', 0),
(58, 'IN District Tippecanoe Event', 'INWLA', '2019-03-22 00:00:00', '2019-02-01 01:22:10', 0),
(59, 'FIRST Israel District Championship   ***See Site Information Document for more information***', 'ISCMP', '2019-04-02 00:00:00', '2019-02-01 01:22:10', 0),
(60, 'ISR District Event #1   ***See Site Information Document for more information***', 'ISDE1', '2019-03-04 00:00:00', '2019-02-01 01:22:10', 0),
(61, 'ISR District Event #2   ***See Site Information Document for more information***', 'ISDE2', '2019-03-06 00:00:00', '2019-02-01 01:22:10', 0),
(62, 'ISR District Event #3   ***See Site Information Document for more information***', 'ISDE3', '2019-03-11 00:00:00', '2019-02-01 01:22:10', 0),
(63, 'ISR District Event #4   ***See Site Information Document for more information***', 'ISDE4', '2019-03-13 00:00:00', '2019-02-01 01:22:10', 0),
(64, 'Heartland Regional', 'KSLA', '2019-03-13 00:00:00', '2019-02-01 01:22:10', 0),
(65, 'Bayou Regional', 'LAKE', '2019-03-20 00:00:00', '2019-02-01 01:22:10', 0),
(66, 'NE District Greater Boston Event', 'MABOS', '2019-03-22 00:00:00', '2019-02-01 01:22:10', 0),
(67, 'NE District SE Mass Event', 'MABRI', '2019-03-08 00:00:00', '2019-02-01 01:22:10', 0),
(68, 'NE District Central Mass Event', 'MACMA', '2019-03-29 00:00:00', '2019-02-01 01:22:10', 0),
(69, 'NE District North Shore Event', 'MAREA', '2019-03-15 00:00:00', '2019-02-01 01:22:10', 0),
(70, 'NE District Western NE Event', 'MAWNE', '2019-03-22 00:00:00', '2019-02-01 01:22:10', 0),
(71, 'CHS District Bethesda MD Event', 'MDBET', '2019-03-08 00:00:00', '2019-02-01 01:22:10', 0),
(72, 'CHS District Owings Mills MD Event', 'MDOWI', '2019-03-15 00:00:00', '2019-02-01 01:22:10', 0),
(73, 'CHS District Oxon Hill MD Event', 'MDOXO', '2019-03-22 00:00:00', '2019-02-01 01:22:10', 0),
(74, 'NE District Pine Tree Event', 'MELEW', '2019-04-04 00:00:00', '2019-02-01 01:22:10', 0),
(75, 'FIM District Alpena Event #2', 'MIAL2', '2019-03-28 00:00:00', '2019-02-01 01:22:10', 0),
(76, 'FIM District Alpena Event #1', 'MIALP', '2019-03-21 00:00:00', '2019-02-01 01:22:10', 0),
(77, 'FIM District Belleville Event', 'MIBEL', '2019-03-07 00:00:00', '2019-02-01 01:22:10', 0),
(78, 'FIM District Center Line Event', 'MICEN', '2019-03-14 00:00:00', '2019-02-01 01:22:10', 0),
(79, 'Michigan State Championship', 'MICMP', '2019-04-10 00:00:00', '2019-02-01 01:22:10', 0),
(80, 'FIM District Detroit Event', 'MIDET', '2019-03-14 00:00:00', '2019-02-01 01:22:10', 0),
(81, 'FIM District Forest Hills Event', 'MIFOR', '2019-04-04 00:00:00', '2019-02-01 01:22:10', 0),
(82, 'FIM District Gibraltar Event', 'MIGIB', '2019-02-28 00:00:00', '2019-02-01 01:22:10', 0),
(83, 'FIM District Gull Lake Event', 'MIGUL', '2019-03-14 00:00:00', '2019-02-01 01:22:10', 0),
(84, 'FIM District Jackson Event', 'MIJAC', '2019-03-21 00:00:00', '2019-02-01 01:22:10', 0),
(85, 'FIM District Kettering University Event #2', 'MIKE2', '2019-03-07 00:00:00', '2019-02-01 01:22:10', 0),
(86, 'FIM District East Kentwood Event', 'MIKEN', '2019-03-28 00:00:00', '2019-02-01 01:22:10', 0),
(87, 'FIM District Kettering University Event #1', 'MIKET', '2019-02-28 00:00:00', '2019-02-01 01:22:10', 0),
(88, 'FIM District Kingsford Event', 'MIKNG', '2019-03-14 00:00:00', '2019-02-01 01:22:10', 0),
(89, 'FIM District Lakeview Event', 'MILAK', '2019-03-07 00:00:00', '2019-02-01 01:22:10', 0),
(90, 'FIM District Lansing Event', 'MILAN', '2019-04-04 00:00:00', '2019-02-01 01:22:10', 0),
(91, 'FIM District Lincoln Event', 'MILIN', '2019-03-21 00:00:00', '2019-02-01 01:22:10', 0),
(92, 'FIM District Livonia Event', 'MILIV', '2019-04-04 00:00:00', '2019-02-01 01:22:10', 0),
(93, 'FIM District Lake Superior State University Event', 'MILSU', '2019-04-04 00:00:00', '2019-02-01 01:22:10', 0),
(94, 'FIM District Marysville Event', 'MIMAR', '2019-03-28 00:00:00', '2019-02-01 01:22:10', 0),
(95, 'FIM District Midland Event', 'MIMID', '2019-03-21 00:00:00', '2019-02-01 01:22:10', 0),
(96, 'FIM District Milford Event', 'MIMIL', '2019-03-07 00:00:00', '2019-02-01 01:22:10', 0),
(97, 'FIM District Muskegon Event', 'MIMUS', '2019-03-14 00:00:00', '2019-02-01 01:22:10', 0),
(98, 'FIM District Shepherd Event', 'MISHE', '2019-03-28 00:00:00', '2019-02-01 01:22:10', 0),
(99, 'FIM District St. Joseph Event', 'MISJO', '2019-03-07 00:00:00', '2019-02-01 01:22:10', 0),
(100, 'FIM District Southfield Event', 'MISOU', '2019-02-28 00:00:00', '2019-02-01 01:22:10', 0),
(101, 'FIM District Troy Event', 'MITRY', '2019-03-28 00:00:00', '2019-02-01 01:22:10', 0),
(102, 'FIM District Traverse City Event', 'MITVC', '2019-04-04 00:00:00', '2019-02-01 01:22:10', 0),
(103, 'FIM District West Michigan Event', 'MIWMI', '2019-03-21 00:00:00', '2019-02-01 01:22:10', 0),
(104, 'Lake Superior Regional', 'MNDU', '2019-03-06 00:00:00', '2019-02-01 01:22:10', 0),
(105, 'Northern Lights Regional', 'MNDU2', '2019-03-06 00:00:00', '2019-02-01 01:22:10', 0),
(106, 'Minnesota 10,000 Lakes Regional presented by the Medtronic Foundation', 'MNMI', '2019-03-27 00:00:00', '2019-02-01 01:22:10', 0),
(107, 'Minnesota North Star Regional', 'MNMI2', '2019-03-27 00:00:00', '2019-02-01 01:22:10', 0),
(108, 'Greater Kansas City Regional', 'MOKC', '2019-03-20 00:00:00', '2019-02-01 01:22:10', 0),
(109, 'Central Missouri Regional', 'MOKC3', '2019-04-04 00:00:00', '2019-02-01 01:22:10', 0),
(110, 'St. Louis Regional', 'MOSL', '2019-03-13 00:00:00', '2019-02-01 01:22:10', 0),
(111, 'FIRST Mid-Atlantic District Championship', 'MRCMP', '2019-04-03 00:00:00', '2019-02-01 01:22:10', 0),
(112, 'Regional de la Ciudad de Mexico', 'MXCM', '2019-03-13 00:00:00', '2019-02-01 01:22:10', 0),
(113, 'Regional Monterrey', 'MXMO', '2019-03-06 00:00:00', '2019-02-01 01:22:10', 0),
(114, 'Regional Laguna', 'MXTO', '2019-03-20 00:00:00', '2019-02-01 01:22:10', 0),
(115, 'FNC District UNC Asheville Event', 'NCASH', '2019-03-22 00:00:00', '2019-02-01 01:22:10', 0),
(116, 'FIRST North Carolina State Championship', 'NCCMP', '2019-04-05 00:00:00', '2019-02-01 01:22:10', 0),
(117, 'FNC District Guilford County Event', 'NCGUI', '2019-03-15 00:00:00', '2019-02-01 01:22:10', 0),
(118, 'FNC District UNC Pembroke Event', 'NCPEM', '2019-03-29 00:00:00', '2019-02-01 01:22:10', 0),
(119, 'FNC District Wake County Event', 'NCWAK', '2019-03-08 00:00:00', '2019-02-01 01:22:10', 0),
(120, 'Great Northern Regional', 'NDGF', '2019-03-13 00:00:00', '2019-02-01 01:22:10', 0),
(121, 'New England District Championship', 'NECMP', '2019-04-10 00:00:00', '2019-02-01 01:22:10', 0),
(122, 'FIRST Championship - Houston- FIRST Robotics Competition - Newton Subdivision', 'NEWTON', '2019-04-17 00:00:00', '2019-02-01 01:22:10', 0),
(123, 'NE District UNH Event', 'NHDUR', '2019-03-29 00:00:00', '2019-02-01 01:22:10', 0),
(124, 'NE District Granite State Event', 'NHGRS', '2019-02-28 00:00:00', '2019-02-01 01:22:10', 0),
(125, 'NE District Southern NH Event', 'NHSNH', '2019-03-15 00:00:00', '2019-02-01 01:22:10', 0),
(126, 'FMA District Bridgewater-Raritan Event', 'NJBRI', '2019-03-15 00:00:00', '2019-02-01 01:22:10', 0),
(127, 'FMA District Mount Olive Event', 'NJFLA', '2019-03-08 00:00:00', '2019-02-01 01:22:10', 0),
(128, 'FMA District Montgomery Event', 'NJSKI', '2019-03-29 00:00:00', '2019-02-01 01:22:10', 0),
(129, 'FMA District Seneca Event', 'NJTAB', '2019-03-22 00:00:00', '2019-02-01 01:22:10', 0),
(130, 'Las Vegas Regional', 'NVLV', '2019-03-27 00:00:00', '2019-02-01 01:22:10', 0),
(131, 'SBPLI Long Island Regional #1', 'NYLI', '2019-03-24 00:00:00', '2019-02-01 01:22:10', 0),
(132, 'SBPLI Long Island Regional #2', 'NYLI2', '2019-03-27 00:00:00', '2019-02-01 01:22:10', 0),
(133, 'New York City Regional', 'NYNY', '2019-04-04 00:00:00', '2019-02-01 01:22:10', 0),
(134, 'Finger Lakes Regional ', 'NYRO', '2019-03-13 00:00:00', '2019-02-01 01:22:10', 0),
(135, 'Hudson Valley Regional', 'NYSU', '2019-03-21 00:00:00', '2019-02-01 01:22:10', 0),
(136, 'New York Tech Valley Regional', 'NYTR', '2019-03-06 00:00:00', '2019-02-01 01:22:10', 0),
(137, 'Central New York Regional', 'NYUT', '2019-03-13 00:00:00', '2019-02-01 01:22:10', 0),
(138, 'Buckeye Regional', 'OHCL', '2019-03-27 00:00:00', '2019-02-01 01:22:10', 0),
(139, 'Miami Valley Regional', 'OHMV', '2019-03-06 00:00:00', '2019-02-01 01:22:10', 0),
(140, 'Oklahoma Regional ', 'OKOK', '2019-03-06 00:00:00', '2019-02-01 01:22:10', 0),
(141, 'ONT District Georgian College Event', 'ONBAR', '2019-03-15 00:00:00', '2019-02-01 01:22:10', 0),
(142, 'FIRST Ontario Provincial Championship', 'ONCMP', '2019-04-10 00:00:00', '2019-02-01 01:22:10', 0),
(143, 'ONT District McMaster University Event', 'ONHAM', '2019-04-05 00:00:00', '2019-02-01 01:22:10', 0),
(144, 'ONT District Western University, Western Engineering Event', 'ONLON', '2019-04-05 00:00:00', '2019-02-01 01:22:10', 0),
(145, 'ONT District North Bay Event', 'ONNOB', '2019-03-28 00:00:00', '2019-02-01 01:22:10', 0),
(146, 'ONT District York University Event', 'ONNYO', '2019-03-22 00:00:00', '2019-02-01 01:22:10', 0),
(147, 'ONT District Durham College Event', 'ONOSH', '2019-03-01 00:00:00', '2019-02-01 01:22:10', 0),
(148, 'ONT District Ryerson University Event', 'ONTO1', '2019-03-14 00:00:00', '2019-02-01 01:22:10', 0),
(149, 'ONT District Humber College Event', 'ONTO3', '2019-03-08 00:00:00', '2019-02-01 01:22:10', 0),
(150, 'ONT District University of Waterloo Event', 'ONWAT', '2019-03-21 00:00:00', '2019-02-01 01:22:10', 0),
(151, 'ONT District Windsor Essex Great Lakes Event', 'ONWIN', '2019-03-28 00:00:00', '2019-02-01 01:22:10', 0),
(152, 'PNW District Lake Oswego Event', 'ORLAK', '2019-03-28 00:00:00', '2019-02-01 01:22:10', 0),
(153, 'PNW District Clackamas Academy Event', 'ORORE', '2019-03-14 00:00:00', '2019-02-01 01:22:10', 0),
(154, 'PNW District Wilsonville Event', 'ORWIL', '2019-03-07 00:00:00', '2019-02-01 01:22:10', 0),
(155, 'FMA District Bensalem Event', 'PABEN', '2019-03-29 00:00:00', '2019-02-01 01:22:10', 0),
(156, 'Greater Pittsburgh Regional', 'PACA', '2019-03-20 00:00:00', '2019-02-01 01:22:10', 0),
(157, 'FMA District Hatboro-Horsham Event', 'PAHAT', '2019-03-01 00:00:00', '2019-02-01 01:22:10', 0),
(158, 'FMA District Springside Chestnut Hill Academy Event', 'PAPHI', '2019-03-15 00:00:00', '2019-02-01 01:22:10', 0),
(159, 'FMA District Westtown Event', 'PAWCH', '2019-03-08 00:00:00', '2019-02-01 01:22:10', 0),
(160, 'Pacific Northwest FIRST District Championship', 'PNCMP', '2019-04-03 00:00:00', '2019-02-01 01:22:10', 0),
(161, 'Festival de Robotique a Montreal Regional', 'QCMO', '2019-02-27 00:00:00', '2019-02-01 01:22:10', 0),
(162, 'Festival de Robotique a Quebec City Regional', 'QCQC', '2019-04-03 00:00:00', '2019-02-01 01:22:10', 0),
(163, 'NE District Rhode Island Event', 'RISMI', '2019-03-22 00:00:00', '2019-02-01 01:22:10', 0),
(164, 'FIRST Championship - Houston- FIRST Robotics Competition - Roebling Subdivision', 'ROEBLING', '2019-04-17 00:00:00', '2019-02-01 01:22:10', 0),
(165, 'Palmetto Regional', 'SCMB', '2019-02-27 00:00:00', '2019-02-01 01:22:10', 0),
(166, 'FIRST Championship - Detroit- FIRST Robotics Competition - Tesla Subdivision', 'TESLA', '2019-04-24 00:00:00', '2019-02-01 01:22:10', 0),
(167, 'Smoky Mountains Regional', 'TNKN', '2019-03-27 00:00:00', '2019-02-01 01:22:10', 0),
(168, 'Istanbul Regional', 'TUIS', '2019-03-01 00:00:00', '2019-02-01 01:22:10', 0),
(169, 'Bosphorus Regional', 'TUIS2', '2019-03-05 00:00:00', '2019-02-01 01:22:10', 0),
(170, 'FIRST Championship - Houston- FIRST Robotics Competition - Turing Subdivision', 'TURING', '2019-04-17 00:00:00', '2019-02-01 01:22:10', 0),
(171, 'FIT District Amarillo Event', 'TXAMA', '2019-03-07 00:00:00', '2019-02-01 01:22:10', 0),
(172, 'FIT District Austin Event', 'TXAUS', '2019-03-01 00:00:00', '2019-02-01 01:22:10', 0),
(173, 'FIT District Channelview Event', 'TXCHA', '2019-03-14 00:00:00', '2019-02-01 01:22:10', 0),
(174, 'FIT District Del Rio Event', 'TXDEL', '2019-03-22 00:00:00', '2019-02-01 01:22:10', 0),
(175, 'FIT District Dallas Event', 'TXDLS', '2019-03-29 00:00:00', '2019-02-01 01:22:10', 0),
(176, 'FIT District El Paso Event', 'TXELP', '2019-03-01 00:00:00', '2019-02-01 01:22:10', 0),
(177, 'FIT District Greenville Event', 'TXGRE', '2019-03-21 00:00:00', '2019-02-01 01:22:10', 0),
(178, 'FIT District Pasadena Event', 'TXPAS', '2019-03-28 00:00:00', '2019-02-01 01:22:10', 0),
(179, 'FIT District Plano Event', 'TXPLA', '2019-03-14 00:00:00', '2019-02-01 01:22:10', 0),
(180, 'FIT District San Antonio Event', 'TXSAN', '2019-03-08 00:00:00', '2019-02-01 01:22:10', 0),
(181, 'Utah Regional', 'UTWV', '2019-03-27 00:00:00', '2019-02-01 01:22:10', 0),
(182, 'CHS District Blacksburg VA Event', 'VABLA', '2019-03-29 00:00:00', '2019-02-01 01:22:10', 0),
(183, 'CHS District Richmond VA Event', 'VAGLE', '2019-03-01 00:00:00', '2019-02-01 01:22:10', 0),
(184, 'CHS District Haymarket VA Event', 'VAHAY', '2019-03-01 00:00:00', '2019-02-01 01:22:10', 0),
(185, 'CHS District Portsmouth VA Event', 'VAPOR', '2019-03-15 00:00:00', '2019-02-01 01:22:10', 0),
(186, 'PNW District Auburn Event', 'WAAHS', '2019-03-29 00:00:00', '2019-02-01 01:22:10', 0),
(187, 'PNW District Auburn Mountainview Event', 'WAAMV', '2019-03-08 00:00:00', '2019-02-01 01:22:10', 0),
(188, 'PNW District Mount Vernon Event', 'WAMOU', '2019-03-01 00:00:00', '2019-02-01 01:22:10', 0),
(189, 'PNW District Glacier Peak Event', 'WASNO', '2019-03-22 00:00:00', '2019-02-01 01:22:10', 0),
(190, 'PNW District West Valley Event', 'WASPO', '2019-03-21 00:00:00', '2019-02-01 01:22:10', 0),
(191, 'PNW District SunDome Event', 'WAYAK', '2019-03-14 00:00:00', '2019-02-01 01:22:10', 0),
(192, 'Seven Rivers Regional', 'WILA', '2019-04-03 00:00:00', '2019-02-01 01:22:10', 0),
(193, 'Wisconsin Regional', 'WIMI', '2019-03-27 00:00:00', '2019-02-01 01:22:10', 0);

-- --------------------------------------------------------

--
-- Table structure for table `fact_match_data_2018`
--

CREATE TABLE IF NOT EXISTS `fact_match_data_2019` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `event_id` int(4) unsigned NOT NULL,
  `team_id` int(5) unsigned NOT NULL,
  `match_id` int(4) unsigned NOT NULL,
  `practice_match` tinyint(1) NOT NULL DEFAULT '0',
  `position_id` int(3) unsigned NOT NULL,
  `prematch_robot_cargo` tinyint(1) unsigned NOT NULL,
  `prematch_robot_hatch` tinyint(1) unsigned NOT NULL,
  `prematch_hab2_left` tinyint(1) unsigned NOT NULL,
  `prematch_hab_level` int(2) unsigned NOT NULL,
  `sandstorm_bonus` tinyint(1) unsigned NOT NULL,
  `sandstorm_hatch_ship` int(2) unsigned NOT NULL,
  `sandstorm_hatch_rocket_1` int(2) unsigned NOT NULL,
  `sandstorm_hatch_rocket_2` int(2) unsigned NOT NULL,
  `sandstorm_hatch_rocket_3` int(2) unsigned NOT NULL,
  `sandstorm_hatch_dropped` int(2) unsigned NOT NULL,
  `sandstorm_cargo_ship` int(2) unsigned NOT NULL,
  `sandstorm_cargo_rocket_1` int(2) unsigned NOT NULL,
  `sandstorm_cargo_rocket_2` int(2) unsigned NOT NULL,
  `sandstorm_cargo_rocket_3` int(2) unsigned NOT NULL,
  `sandstorm_cargo_dropped` int(2) unsigned NOT NULL,
  `hatch_ship` int(2) unsigned NOT NULL,
  `hatch_rocket_1` int(2) unsigned NOT NULL,
  `hatch_rocket_2` int(2) unsigned NOT NULL,
  `hatch_rocket_3` int(2) unsigned NOT NULL,
  `hatch_dropped` int(2) unsigned NOT NULL,
  `cargo_ship` int(2) unsigned NOT NULL,
  `cargo_rocket_1` int(2) unsigned NOT NULL,
  `cargo_rocket_2` int(2) unsigned NOT NULL,
  `cargo_rocket_3` int(2) unsigned NOT NULL,
  `cargo_dropped` int(2) unsigned NOT NULL,
  `hab_climb_level` int(2) unsigned NOT NULL,
  `hab_climb_2_left` tinyint(1) unsigned NOT NULL,
  `hab_climb_level_attempted` int(2) unsigned NOT NULL,
  `hab_climb_2_left_attempted` tinyint(1) unsigned NOT NULL,
  `floor_pickup_cargo` tinyint(1) unsigned NOT NULL,
  `floor_pickup_hatch` tinyint(1) unsigned NOT NULL,
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

CREATE TABLE IF NOT EXISTS `scout_pit_data_2019` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `team_id` int(5) unsigned NOT NULL,
  `start_hab_level` int(2) unsigned NOT NULL,
  `preload_cargo` tinyint(1) unsigned NOT NULL,
  `preload_hatch` tinyint(1) unsigned NOT NULL,
  `sandstorm_bonus` tinyint(1) unsigned NOT NULL,
  `sandstorm_hatch_ship_front` tinyint(1) unsigned NOT NULL,
  `sandstorm_hatch_ship_side` tinyint(1) unsigned NOT NULL,
  `sandstorm_hatch_rocket_1` tinyint(1) unsigned NOT NULL,
  `sandstorm_hatch_rocket_2` tinyint(1) unsigned NOT NULL,
  `sandstorm_hatch_rocket_3` tinyint(1) unsigned NOT NULL,
  `sandstorm_cargo_ship` tinyint(1) unsigned NOT NULL,
  `sandstorm_cargo_rocket_1` tinyint(1) unsigned NOT NULL,
  `sandstorm_cargo_rocket_2` tinyint(1) unsigned NOT NULL,
  `sandstorm_cargo_rocket_3` tinyint(1) unsigned NOT NULL,
  `sandstorm_hatch_count` int(2) unsigned NOT NULL,
  `sandstorm_cargo_count` int(2) unsigned NOT NULL,
  `hatch_1` tinyint(1) unsigned NOT NULL,
  `hatch_2` tinyint(1) unsigned NOT NULL,
  `hatch_3` tinyint(1) unsigned NOT NULL,
  `cargo_1` tinyint(1) unsigned NOT NULL,
  `cargo_2` tinyint(1) unsigned NOT NULL,
  `cargo_3` tinyint(1) unsigned NOT NULL,
  `hab_climb_2` tinyint(1) unsigned NOT NULL,
  `hab_climb_3` tinyint(1) unsigned NOT NULL,
  `hab_climb_speed_lvl_2_sec` int(3) unsigned NOT NULL,
  `hab_climb_speed_lvl_3_sec` int(3) unsigned NOT NULL,
  `floor_pickup_hatch` tinyint(1) unsigned NOT NULL,
  `floor_pickup_cargo` tinyint(1) unsigned NOT NULL,
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
