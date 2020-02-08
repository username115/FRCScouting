

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
(1, 'Canadian Rockies Regional', 'ABCA', '2020-04-01 00:00:00', '2020-02-08 19:14:44', 0),
(2, 'Rocket City Regional', 'ALHU', '2020-04-01 00:00:00', '2020-02-08 19:14:44', 0),
(3, 'FIRST Championship - Detroit - FIRST Robotics Competition - Archimedes Division', 'ARCHIMEDES', '2020-04-29 00:00:00', '2020-02-08 19:14:44', 0),
(4, 'Arkansas Regional', 'ARLI', '2020-03-04 00:00:00', '2020-02-08 19:14:44', 0),
(5, 'Southern Cross Regional', 'AUSC', '2020-03-14 00:00:00', '2020-02-08 19:14:44', 0),
(6, 'South Pacific Regional', 'AUSP', '2020-03-20 00:00:00', '2020-02-08 19:14:44', 0),
(7, 'Arizona North Regional', 'AZFL', '2020-03-11 00:00:00', '2020-02-08 19:14:44', 0),
(8, 'Arizona West Regional', 'AZPX', '2020-04-01 00:00:00', '2020-02-08 19:14:44', 0),
(9, 'Canadian Pacific Regional', 'BCVI', '2020-03-04 00:00:00', '2020-02-08 19:14:44', 0),
(10, 'Beijing Cultural Exchange Event #1', 'BEXI', '2020-03-12 00:00:00', '2020-02-08 19:14:44', 0),
(11, 'Beijing Cultural Exchange Event #2', 'BEXI2', '2020-03-19 00:00:00', '2020-02-08 19:14:44', 0),
(12, 'Aerospace Valley Regional', 'CAAV', '2020-04-01 00:00:00', '2020-02-08 19:14:44', 0),
(13, 'Sacramento Regional', 'CADA', '2020-03-25 00:00:00', '2020-02-08 19:14:44', 0),
(14, 'Del Mar Regional', 'CADM', '2020-03-05 00:00:00', '2020-02-08 19:14:44', 0),
(15, 'Central Valley Regional', 'CAFR', '2020-03-11 00:00:00', '2020-02-08 19:14:44', 0),
(16, 'Los Angeles Regional', 'CALA', '2020-03-05 00:00:00', '2020-02-08 19:14:44', 0),
(17, 'Los Angeles North Regional', 'CALN', '2020-02-27 00:00:00', '2020-02-08 19:14:44', 0),
(18, 'Monterey Bay Regional', 'CAMB', '2020-03-25 00:00:00', '2020-02-08 19:14:44', 0),
(19, 'Orange County Regional', 'CAOC', '2020-04-01 00:00:00', '2020-02-08 19:14:44', 0),
(20, 'FIRST Championship - Detroit - FIRST Robotics Competition - Carson Division', 'CARSON', '2020-04-29 00:00:00', '2020-02-08 19:14:44', 0),
(21, 'FIRST Championship - Houston - FIRST Robotics Competition - Carver Division', 'CARVER', '2020-04-15 00:00:00', '2020-02-08 19:14:44', 0),
(22, 'San Diego Regional presented by Qualcomm', 'CASD', '2020-03-11 00:00:00', '2020-02-08 19:14:44', 0),
(23, 'San Francisco Regional', 'CASF', '2020-03-19 00:00:00', '2020-02-08 19:14:44', 0),
(24, 'Silicon Valley Regional', 'CASJ', '2020-04-01 00:00:00', '2020-02-08 19:14:44', 0),
(25, 'Ventura Regional', 'CAVE', '2020-03-19 00:00:00', '2020-02-08 19:14:44', 0),
(26, 'FIRST Chesapeake District Championship sponsored by Newport News Shipbuilding', 'CHCMP', '2020-04-02 00:00:00', '2020-02-08 19:14:44', 0),
(27, 'Science Park Taichung Regional', 'CHTA', '2020-03-05 00:00:00', '2020-02-08 19:14:44', 0),
(28, 'FIRST Championship - Detroit - FIRST Robotics Competition', 'CMPMI', '2020-04-29 00:00:00', '2020-02-08 19:14:44', 0),
(29, 'FIRST Championship - Houston - FIRST Robotics Competition', 'CMPTX', '2020-04-15 00:00:00', '2020-02-08 19:14:44', 0),
(30, 'Colorado Regional', 'CODE', '2020-03-18 00:00:00', '2020-02-08 19:14:44', 0),
(31, 'NE District Hartford Event', 'CTHAR', '2020-04-03 00:00:00', '2020-02-08 19:14:44', 0),
(32, 'NE District Northern CT Event', 'CTNCT', '2020-02-28 00:00:00', '2020-02-08 19:14:44', 0),
(33, 'NE District Waterbury Event', 'CTWAT', '2020-03-06 00:00:00', '2020-02-08 19:14:44', 0),
(34, 'FIRST Championship - Detroit - FIRST Robotics Competition - Curie Division', 'CURIE', '2020-04-29 00:00:00', '2020-02-08 19:14:44', 0),
(35, 'FIRST Championship - Detroit - FIRST Robotics Competition - Daly Division', 'DALY', '2020-04-29 00:00:00', '2020-02-08 19:14:44', 0),
(36, 'FIRST Championship - Detroit - FIRST Robotics Competition - Darwin Division', 'DARWIN', '2020-04-29 00:00:00', '2020-02-08 19:14:44', 0),
(37, 'Orlando Regional', 'FLOR', '2020-03-11 00:00:00', '2020-02-08 19:14:44', 0),
(38, 'South Florida Regional', 'FLWP', '2020-04-02 00:00:00', '2020-02-08 19:14:44', 0),
(39, 'FIRST In Texas District Championship', 'FTCMP', '2020-04-01 00:00:00', '2020-02-08 19:14:44', 0),
(40, 'PCH District Albany Event presented by Procter & Gamble', 'GAALB', '2020-03-26 00:00:00', '2020-02-08 19:14:44', 0),
(41, 'PCH District Carrollton Event', 'GACAR', '2020-03-20 00:00:00', '2020-02-08 19:14:44', 0),
(42, 'Peachtree District State Championship', 'GACMP', '2020-04-01 00:00:00', '2020-02-08 19:14:44', 0),
(43, 'PCH District Columbus Event', 'GACOL', '2020-03-12 00:00:00', '2020-02-08 19:14:44', 0),
(44, 'PCH District Dalton Event', 'GADAL', '2020-03-05 00:00:00', '2020-02-08 19:14:44', 0),
(45, 'PCH District Gainesville Event presented by Automation Direct', 'GAGAI', '2020-02-28 00:00:00', '2020-02-08 19:14:44', 0),
(46, 'FIRST Championship - Houston - FIRST Robotics Competition - Galileo Division', 'GALILEO', '2020-04-15 00:00:00', '2020-02-08 19:14:44', 0),
(47, 'Hawaii Regional', 'HIHO', '2020-03-18 00:00:00', '2020-02-08 19:14:44', 0),
(48, 'FIRST Championship - Houston - FIRST Robotics Competition - Hopper Division', 'HOPPER', '2020-04-15 00:00:00', '2020-02-08 19:14:44', 0),
(49, 'Iowa Regional', 'IACF', '2020-03-25 00:00:00', '2020-02-08 19:14:44', 0),
(50, 'Idaho Regional', 'IDBO', '2020-03-25 00:00:00', '2020-02-08 19:14:44', 0),
(51, 'Midwest Regional', 'ILCH', '2020-03-04 00:00:00', '2020-02-08 19:14:44', 0),
(52, 'Central Illinois Regional', 'ILPE', '2020-03-18 00:00:00', '2020-02-08 19:14:44', 0),
(53, 'IN District Bloomington Event', 'INBLO', '2020-03-06 00:00:00', '2020-02-08 19:14:44', 0),
(54, 'Indiana State Championship', 'INCMP', '2020-04-03 00:00:00', '2020-02-08 19:14:44', 0),
(55, 'IN District Columbus Event', 'INCOL', '2020-03-19 00:00:00', '2020-02-08 19:14:44', 0),
(56, 'IN District St. Joseph Event', 'INMIS', '2020-03-13 00:00:00', '2020-02-08 19:14:44', 0),
(57, 'IN District Perry Meridian Event', 'INPMH', '2020-03-26 00:00:00', '2020-02-08 19:14:44', 0),
(58, 'FIRST Israel District Championship', 'ISCMP', '2020-03-30 00:00:00', '2020-02-08 19:14:44', 0),
(59, 'ISR District Event #1', 'ISDE1', '2020-02-24 00:00:00', '2020-02-08 19:14:44', 0),
(60, 'ISR District Event #2', 'ISDE2', '2020-02-26 00:00:00', '2020-02-08 19:14:44', 0),
(61, 'ISR District Event #3', 'ISDE3', '2020-03-16 00:00:00', '2020-02-08 19:14:44', 0),
(62, 'ISR District Event #4', 'ISDE4', '2020-03-18 00:00:00', '2020-02-08 19:14:44', 0),
(63, 'Heartland Regional', 'KSLA', '2020-03-18 00:00:00', '2020-02-08 19:14:44', 0),
(64, 'Bayou Regional', 'LAKE', '2020-03-25 00:00:00', '2020-02-08 19:14:44', 0),
(65, 'NE District Greater Boston Event', 'MABOS', '2020-03-27 00:00:00', '2020-02-08 19:14:44', 0),
(66, 'NE District SE Mass Event', 'MABRI', '2020-03-06 00:00:00', '2020-02-08 19:14:44', 0),
(67, 'NE District North Shore Event', 'MAREA', '2020-03-13 00:00:00', '2020-02-08 19:14:44', 0),
(68, 'NE District Western NE Event', 'MAWNE', '2020-03-20 00:00:00', '2020-02-08 19:14:44', 0),
(69, 'NE District WPI Event', 'MAWOR', '2020-03-20 00:00:00', '2020-02-08 19:14:44', 0),
(70, 'CHS District Bethesda MD Event', 'MDBET', '2020-03-06 00:00:00', '2020-02-08 19:14:44', 0),
(71, 'CHS District Edgewater MD Event sponsored by Leidos', 'MDEDG', '2020-03-20 00:00:00', '2020-02-08 19:14:44', 0),
(72, 'CHS District Owings Mills MD Event', 'MDOWI', '2020-03-13 00:00:00', '2020-02-08 19:14:44', 0),
(73, 'NE District Pine Tree Event', 'MELEW', '2020-04-02 00:00:00', '2020-02-08 19:14:44', 0),
(74, 'FIM District Alpena Event #2', 'MIAL2', '2020-04-02 00:00:00', '2020-02-08 19:14:44', 0),
(75, 'FIM District Alpena Event #1', 'MIALP', '2020-03-26 00:00:00', '2020-02-08 19:14:44', 0),
(76, 'FIM District Belleville Event', 'MIBEL', '2020-03-12 00:00:00', '2020-02-08 19:14:44', 0),
(77, 'FIM District Center Line Event', 'MICEN', '2020-03-19 00:00:00', '2020-02-08 19:14:44', 0),
(78, 'Michigan State Championship', 'MICMP', '2020-04-08 00:00:00', '2020-02-08 19:14:44', 0),
(79, 'FIM District Detroit Event', 'MIDET', '2020-03-12 00:00:00', '2020-02-08 19:14:44', 0),
(80, 'FIM District Escanaba Event', 'MIESC', '2020-03-12 00:00:00', '2020-02-08 19:14:44', 0),
(81, 'FIM District Ferris State Event', 'MIFER', '2020-03-26 00:00:00', '2020-02-08 19:14:44', 0),
(82, 'FIM District Gull Lake Event', 'MIGUL', '2020-03-12 00:00:00', '2020-02-08 19:14:44', 0),
(83, 'FIM District Jackson Event', 'MIJAC', '2020-03-05 00:00:00', '2020-02-08 19:14:44', 0),
(84, 'FIM District Kettering University Event #2', 'MIKE2', '2020-03-05 00:00:00', '2020-02-08 19:14:44', 0),
(85, 'FIM District East Kentwood Event', 'MIKEN', '2020-04-02 00:00:00', '2020-02-08 19:14:44', 0),
(86, 'FIM District Kettering University Event #1', 'MIKET', '2020-02-27 00:00:00', '2020-02-08 19:14:44', 0),
(87, 'FIM District Kingsford Event', 'MIKNG', '2020-03-05 00:00:00', '2020-02-08 19:14:44', 0),
(88, 'FIM District Lakeview Event', 'MILAK', '2020-03-26 00:00:00', '2020-02-08 19:14:44', 0),
(89, 'FIM District Lansing Event', 'MILAN', '2020-03-19 00:00:00', '2020-02-08 19:14:44', 0),
(90, 'FIM District Lincoln Event', 'MILIN', '2020-03-19 00:00:00', '2020-02-08 19:14:44', 0),
(91, 'FIM District Livonia Event', 'MILIV', '2020-03-26 00:00:00', '2020-02-08 19:14:44', 0),
(92, 'FIM District Marysville Event', 'MIMAR', '2020-04-02 00:00:00', '2020-02-08 19:14:44', 0),
(93, 'FIM District Macomb Community College Event', 'MIMCC', '2020-02-27 00:00:00', '2020-02-08 19:14:44', 0),
(94, 'FIM District Midland Event', 'MIMID', '2020-03-19 00:00:00', '2020-02-08 19:14:44', 0),
(95, 'FIM District Milford Event', 'MIMIL', '2020-03-05 00:00:00', '2020-02-08 19:14:44', 0),
(96, 'FIM District Muskegon Event', 'MIMUS', '2020-03-12 00:00:00', '2020-02-08 19:14:44', 0),
(97, 'FIM District Shepherd Event', 'MISHE', '2020-04-02 00:00:00', '2020-02-08 19:14:44', 0),
(98, 'FIM District St. Joseph Event', 'MISJO', '2020-03-05 00:00:00', '2020-02-08 19:14:44', 0),
(99, 'FIM District Southfield Event', 'MISOU', '2020-02-27 00:00:00', '2020-02-08 19:14:44', 0),
(100, 'FIM District Troy Event', 'MITRY', '2020-04-02 00:00:00', '2020-02-08 19:14:44', 0),
(101, 'FIM District Traverse City Event', 'MITVC', '2020-02-27 00:00:00', '2020-02-08 19:14:44', 0),
(102, 'FIM District West Michigan Event', 'MIWMI', '2020-03-19 00:00:00', '2020-02-08 19:14:44', 0),
(103, 'FIM District Woodhaven Event', 'MIWOO', '2020-03-26 00:00:00', '2020-02-08 19:14:44', 0),
(104, 'Lake Superior Regional', 'MNDU', '2020-03-04 00:00:00', '2020-02-08 19:14:44', 0),
(105, 'Northern Lights Regional', 'MNDU2', '2020-03-04 00:00:00', '2020-02-08 19:14:44', 0),
(106, 'Minnesota 10,000 Lakes Regional Presented by the Medtronic Foundation', 'MNMI', '2020-03-25 00:00:00', '2020-02-08 19:14:44', 0),
(107, 'Minnesota North Star Regional', 'MNMI2', '2020-03-25 00:00:00', '2020-02-08 19:14:44', 0),
(108, 'Greater Kansas City Regional', 'MOKC', '2020-02-27 00:00:00', '2020-02-08 19:14:44', 0),
(109, 'Central Missouri Regional', 'MOKC3', '2020-04-02 00:00:00', '2020-02-08 19:14:44', 0),
(110, 'St. Louis Regional', 'MOSL', '2020-03-11 00:00:00', '2020-02-08 19:14:44', 0),
(111, 'FIRST Mid-Atlantic District Championship', 'MRCMP', '2020-04-01 00:00:00', '2020-02-08 19:14:44', 0),
(112, 'Regional Monterrey', 'MXMO', '2020-02-26 00:00:00', '2020-02-08 19:14:44', 0),
(113, 'Regional Laguna', 'MXTO', '2020-03-18 00:00:00', '2020-02-08 19:14:44', 0),
(114, 'FNC District UNC Asheville Event', 'NCASH', '2020-03-27 00:00:00', '2020-02-08 19:14:44', 0),
(115, 'FIRST North Carolina State Championship', 'NCCMP', '2020-04-03 00:00:00', '2020-02-08 19:14:44', 0),
(116, 'FNC District ECU Event', 'NCGRE', '2020-03-13 00:00:00', '2020-02-08 19:14:44', 0),
(117, 'FNC District Guilford County Event', 'NCGUI', '2020-03-20 00:00:00', '2020-02-08 19:14:44', 0),
(118, 'FNC District UNC Pembroke Event', 'NCPEM', '2020-03-06 00:00:00', '2020-02-08 19:14:44', 0),
(119, 'FNC District Wake County Event', 'NCWAK', '2020-02-28 00:00:00', '2020-02-08 19:14:44', 0),
(120, 'Great Northern Regional', 'NDGF', '2020-02-26 00:00:00', '2020-02-08 19:14:44', 0),
(121, 'New England District Championship', 'NECMP', '2020-04-08 00:00:00', '2020-02-08 19:14:44', 0),
(122, 'FIRST Championship - Houston - FIRST Robotics Competition - Newton Division', 'NEWTON', '2020-04-15 00:00:00', '2020-02-08 19:14:44', 0),
(123, 'NE District Granite State Event', 'NHGRS', '2020-02-27 00:00:00', '2020-02-08 19:14:44', 0),
(124, 'NE District Southern NH Event', 'NHSNH', '2020-03-27 00:00:00', '2020-02-08 19:14:44', 0),
(125, 'FMA District Bridgewater-Raritan Event', 'NJBRI', '2020-03-20 00:00:00', '2020-02-08 19:14:44', 0),
(126, 'FMA District Mount Olive Event', 'NJFLA', '2020-03-06 00:00:00', '2020-02-08 19:14:44', 0),
(127, 'FMA District Robbinsville Event', 'NJROB', '2020-03-13 00:00:00', '2020-02-08 19:14:44', 0),
(128, 'FMA District Montgomery Event', 'NJSKI', '2020-03-27 00:00:00', '2020-02-08 19:14:44', 0),
(129, 'FMA District Seneca Event', 'NJTAB', '2020-03-20 00:00:00', '2020-02-08 19:14:44', 0),
(130, 'Las Vegas Regional', 'NVLV', '2020-03-25 00:00:00', '2020-02-08 19:14:44', 0),
(131, 'SBPLI Long Island Regional #1', 'NYLI', '2020-03-22 00:00:00', '2020-02-08 19:14:44', 0),
(132, 'SBPLI Long Island Regional #2', 'NYLI2', '2020-03-26 00:00:00', '2020-02-08 19:14:44', 0),
(133, 'New York City Regional', 'NYNY', '2020-04-02 00:00:00', '2020-02-08 19:14:44', 0),
(134, 'Finger Lakes Regional', 'NYRO', '2020-03-11 00:00:00', '2020-02-08 19:14:44', 0),
(135, 'Hudson Valley Regional', 'NYSU', '2020-03-19 00:00:00', '2020-02-08 19:14:44', 0),
(136, 'New York Tech Valley Regional', 'NYTR', '2020-03-11 00:00:00', '2020-02-08 19:14:44', 0),
(137, 'Central New York Regional', 'NYUT', '2020-03-18 00:00:00', '2020-02-08 19:14:44', 0),
(138, 'Buckeye Regional', 'OHCL', '2020-03-25 00:00:00', '2020-02-08 19:14:44', 0),
(139, 'Miami Valley Regional', 'OHMV', '2020-02-26 00:00:00', '2020-02-08 19:14:44', 0),
(140, 'Oklahoma Regional', 'OKOK', '2020-03-11 00:00:00', '2020-02-08 19:14:44', 0),
(141, 'Green Country Regional', 'OKTU', '2020-04-01 00:00:00', '2020-02-08 19:14:44', 0),
(142, 'ONT District Georgian College Event', 'ONBAR', '2020-03-06 00:00:00', '2020-02-08 19:14:44', 0),
(143, 'FIRST Ontario Provincial Championship', 'ONCMP', '2020-04-08 00:00:00', '2020-02-08 19:14:44', 0),
(144, 'ONT District McMaster University Event', 'ONHAM', '2020-04-03 00:00:00', '2020-02-08 19:14:44', 0),
(145, 'ONT District Western University, Western Engineering Event', 'ONLON', '2020-04-03 00:00:00', '2020-02-08 19:14:44', 0),
(146, 'ONT District North Bay Event', 'ONNOB', '2020-03-27 00:00:00', '2020-02-08 19:14:44', 0),
(147, 'ONT District York University Event', 'ONNYO', '2020-03-20 00:00:00', '2020-02-08 19:14:44', 0),
(148, 'ONT District Durham College Event', 'ONOSH', '2020-02-28 00:00:00', '2020-02-08 19:14:44', 0),
(149, 'ONT District Carleton University Event', 'ONOTT', '2020-03-13 00:00:00', '2020-02-08 19:14:44', 0),
(150, 'ONT District Ryerson University Event', 'ONTO1', '2020-03-12 00:00:00', '2020-02-08 19:14:44', 0),
(151, 'ONT District Humber College Event', 'ONTO3', '2020-03-06 00:00:00', '2020-02-08 19:14:44', 0),
(152, 'ONT District University of Waterloo Event', 'ONWAT', '2020-03-19 00:00:00', '2020-02-08 19:14:44', 0),
(153, 'ONT District Windsor Essex Great Lakes Event', 'ONWIN', '2020-03-26 00:00:00', '2020-02-08 19:14:44', 0),
(154, 'PNW District Clackamas Academy Event', 'ORORE', '2020-02-27 00:00:00', '2020-02-08 19:14:44', 0),
(155, 'PNW District Oregon State Fairgrounds Event', 'ORSAL', '2020-03-26 00:00:00', '2020-02-08 19:14:44', 0),
(156, 'PNW District Wilsonville Event', 'ORWIL', '2020-03-12 00:00:00', '2020-02-08 19:14:44', 0),
(157, 'FMA District Bensalem Event', 'PABEN', '2020-03-27 00:00:00', '2020-02-08 19:14:44', 0),
(158, 'Greater Pittsburgh Regional', 'PACA', '2020-03-18 00:00:00', '2020-02-08 19:14:44', 0),
(159, 'FMA District Hatboro-Horsham Event', 'PAHAT', '2020-02-28 00:00:00', '2020-02-08 19:14:44', 0),
(160, 'FMA District Springside Chestnut Hill Academy Event', 'PAPHI', '2020-03-13 00:00:00', '2020-02-08 19:14:44', 0),
(161, 'Pacific Northwest FIRST District Championship', 'PNCMP', '2020-04-01 00:00:00', '2020-02-08 19:14:44', 0),
(162, 'Festival de Robotique a Montreal Regional', 'QCMO', '2020-04-08 00:00:00', '2020-02-08 19:14:44', 0),
(163, 'Festival de Robotique a Sherbrooke Regional', 'QCSH', '2020-03-04 00:00:00', '2020-02-08 19:14:44', 0),
(164, 'FIRST Championship - Houston - FIRST Robotics Competition - Roebling Division', 'ROEBLING', '2020-04-15 00:00:00', '2020-02-08 19:14:44', 0),
(165, 'Palmetto Regional', 'SCMB', '2020-02-26 00:00:00', '2020-02-08 19:14:44', 0),
(166, 'FIRST Championship - Detroit - FIRST Robotics Competition - Tesla Division', 'TESLA', '2020-04-29 00:00:00', '2020-02-08 19:14:44', 0),
(167, 'Smoky Mountains Regional', 'TNKN', '2020-03-25 00:00:00', '2020-02-08 19:14:44', 0),
(168, 'Memphis Regional', 'TNME', '2020-03-18 00:00:00', '2020-02-08 19:14:44', 0),
(169, 'Istanbul Regional', 'TUIS', '2020-03-05 00:00:00', '2020-02-08 19:14:44', 0),
(170, 'Bosphorus Regional', 'TUIS2', '2020-03-09 00:00:00', '2020-02-08 19:14:44', 0),
(171, 'FIRST Championship - Houston - FIRST Robotics Competition - Turing Division', 'TURING', '2020-04-15 00:00:00', '2020-02-08 19:14:44', 0),
(172, 'FIT District Amarillo Event', 'TXAMA', '2020-03-12 00:00:00', '2020-02-08 19:14:44', 0),
(173, 'FIT District Austin Event', 'TXAUS', '2020-03-19 00:00:00', '2020-02-08 19:14:44', 0),
(174, 'FIT District Channelview Event', 'TXCHA', '2020-03-06 00:00:00', '2020-02-08 19:14:44', 0),
(175, 'FIT District Del Rio Event', 'TXDEL', '2020-03-06 00:00:00', '2020-02-08 19:14:44', 0),
(176, 'FIT District Dallas Event', 'TXDLS', '2020-03-19 00:00:00', '2020-02-08 19:14:44', 0),
(177, 'FIT District Dripping Springs Event', 'TXDRI', '2020-02-28 00:00:00', '2020-02-08 19:14:44', 0),
(178, 'FIT District El Paso Event', 'TXELP', '2020-03-27 00:00:00', '2020-02-08 19:14:44', 0),
(179, 'FIT District Fort Worth Event', 'TXFOR', '2020-03-12 00:00:00', '2020-02-08 19:14:44', 0),
(180, 'FIT District Greenville Event', 'TXGRE', '2020-02-28 00:00:00', '2020-02-08 19:14:44', 0),
(181, 'FIT District Houston Event', 'TXHOU', '2020-03-19 00:00:00', '2020-02-08 19:14:44', 0),
(182, 'FIT District New Braunfels Event', 'TXNEW', '2020-03-13 00:00:00', '2020-02-08 19:14:44', 0),
(183, 'FIT District Pasadena Event', 'TXPAS', '2020-03-26 00:00:00', '2020-02-08 19:14:44', 0),
(184, 'FIT District Plano Event', 'TXPLA', '2020-03-06 00:00:00', '2020-02-08 19:14:44', 0),
(185, 'Utah Regional', 'UTWV', '2020-03-04 00:00:00', '2020-02-08 19:14:44', 0),
(186, 'CHS District Blacksburg VA Event', 'VABLA', '2020-03-20 00:00:00', '2020-02-08 19:14:44', 0),
(187, 'CHS District Richmond VA Event', 'VAGLE', '2020-03-06 00:00:00', '2020-02-08 19:14:44', 0),
(188, 'CHS District Haymarket VA Event', 'VAHAY', '2020-02-28 00:00:00', '2020-02-08 19:14:44', 0),
(189, 'CHS District Portsmouth VA Event', 'VAPOR', '2020-03-13 00:00:00', '2020-02-08 19:14:44', 0),
(190, 'PNW District Auburn Event', 'WAAHS', '2020-03-27 00:00:00', '2020-02-08 19:14:44', 0),
(191, 'PNW District Auburn Mountainview Event', 'WAAMV', '2020-03-06 00:00:00', '2020-02-08 19:14:44', 0),
(192, 'PNW District Bellingham Event', 'WABEL', '2020-03-20 00:00:00', '2020-02-08 19:14:44', 0),
(193, 'PNW District Glacier Peak Event', 'WASNO', '2020-02-28 00:00:00', '2020-02-08 19:14:44', 0),
(194, 'PNW District West Valley Event', 'WASPO', '2020-03-05 00:00:00', '2020-02-08 19:14:44', 0),
(195, 'PNW District SunDome Event', 'WAYAK', '2020-03-19 00:00:00', '2020-02-08 19:14:44', 0),
(196, 'Week 0', 'WEEK0', '2020-02-15 00:00:00', '2020-02-08 19:14:44', 0),
(197, 'Seven Rivers Regional', 'WILA', '2020-04-01 00:00:00', '2020-02-08 19:14:44', 0),
(198, 'Wisconsin Regional', 'WIMI', '2020-03-18 00:00:00', '2020-02-08 19:14:44', 0);

-- --------------------------------------------------------

--
-- Table structure for table `fact_match_data_2020`
--

CREATE TABLE IF NOT EXISTS `fact_match_data_2020` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `event_id` int(4) unsigned NOT NULL,
  `team_id` int(5) unsigned NOT NULL,
  `match_id` int(4) unsigned NOT NULL,
  `practice_match` tinyint(1) NOT NULL DEFAULT '0',
  `position_id` int(3) unsigned NOT NULL,
  `start_position` int(1) unsigned NOT NULL,
  `auto_initiation_move` tinyint(1) unsigned NOT NULL,
  `auto_score_low` int(3) unsigned NOT NULL,
  `auto_score_high` int(3) unsigned NOT NULL,
  `auto_miss` int(3) unsigned NOT NULL,
  `score_low` int(3) unsigned NOT NULL,
  `score_high` int(3) unsigned NOT NULL,
  `miss` int(3) unsigned NOT NULL,
  `rotation_control` tinyint(1) unsigned NOT NULL,
  `position_control` tinyint(1) unsigned NOT NULL,
  `generator_park` tinyint(1) unsigned NOT NULL,
  `generator_hang` tinyint(1) unsigned NOT NULL,
  `generator_hang_attempted` tinyint(1) unsigned NOT NULL,
  `generator_level` tinyint(1) unsigned NOT NULL,
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
-- Table structure for table `programming_lu`
--

CREATE TABLE IF NOT EXISTS `programming_lu` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `language_name` text COLLATE latin1_general_cs NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `invalid` tinyint(1) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `timestamp` (`timestamp`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs AUTO_INCREMENT=1 ;

--
-- Dumping data for table `notes_options`
--

INSERT INTO `programming_lu` (`id`, `language_name`, `timestamp`, `invalid`) VALUES
(1, 'LabView', '2020-01-20 15:21:36', 0),
(2, 'Java', '2020-01-20 15:21:36', 0),
(3, 'C++', '2020-01-20 15:53:21', 0),
(4, 'Python', '2020-01-20 16:30:02', 0),
(5, 'Other', '2020-01-20 16:30:02', 0);

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
-- Table structure for table `scout_pit_data_2020`
--

CREATE TABLE IF NOT EXISTS `scout_pit_data_2020` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `team_id` int(5) unsigned NOT NULL,
  `auto_move` tinyint(1) unsigned NOT NULL,
  `auto_score_low` tinyint(1) unsigned NOT NULL,
  `auto_score_outer` tinyint(1) unsigned NOT NULL,
  `auto_score_inner` tinyint(1) unsigned NOT NULL,
  `score_low` tinyint(1) unsigned NOT NULL,
  `score_outer` tinyint(1) unsigned NOT NULL,
  `score_inner` tinyint(1) unsigned NOT NULL,
  `position_control` tinyint(1) unsigned NOT NULL,
  `rotation_control` tinyint(1) unsigned NOT NULL,
  `generator_hang` tinyint(1) unsigned NOT NULL,
  `power_cell_capacity` int(1) unsigned NOT NULL,
  `trench_run` tinyint(1) unsigned NOT NULL,
  `traction_wheels` tinyint(1) unsigned NOT NULL,
  `pneumatic_wheels` tinyint(1) unsigned NOT NULL,
  `omni_wheels` tinyint(1) unsigned NOT NULL,
  `mecanum_wheels` tinyint(1) unsigned NOT NULL,
  `swerve_drive` tinyint(1) unsigned NOT NULL,
  `tank_drive` tinyint(1) unsigned NOT NULL,
  `other_drive_wheels` tinyint(1) unsigned NOT NULL,
  `team_batteries` int(3) unsigned NOT NULL,
  `team_battery_chargers` int(3) unsigned NOT NULL,
  `robot_gross_weight_lbs` int(4) unsigned NOT NULL,
  `programming_id` int(2) unsigned NOT NULL,
  `mechanical_appearance` int(1) unsigned NOT NULL,
  `electrical_appearance` int(1) unsigned NOT NULL,
  `notes` text COLLATE utf8_unicode_ci NOT NULL,
  `invalid` tinyint(1) NOT NULL DEFAULT '0',
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `team_id` (`team_id`),
  KEY `invalid` (`invalid`),
  KEY `timestamp` (`timestamp`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;


/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
