SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;


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
) ENGINE=MyISAM AUTO_INCREMENT=184 DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

INSERT INTO `event_lu` (`id`, `event_name`, `event_code`, `date_start`, `timestamp`, `invalid`) VALUES
(1, 'Rocket City Regional', 'ALHU', '2023-04-05 00:00:00', '2023-01-16 19:47:42', 0),
(2, 'Arkansas Regional', 'ARLI', '2023-03-01 00:00:00', '2023-01-16 19:47:42', 0),
(3, 'Southern Cross Regional', 'AUSC', '2023-03-09 00:00:00', '2023-01-16 19:47:42', 0),
(4, 'Arizona West Regional', 'AZGL', '2023-03-22 00:00:00', '2023-01-16 19:47:42', 0),
(5, 'Arizona East Regional', 'AZVA', '2023-03-15 00:00:00', '2023-01-16 19:47:42', 0),
(6, 'Canadian Pacific Regional', 'BCVI', '2023-02-28 00:00:00', '2023-01-16 19:47:42', 0),
(7, 'Brazil Regional', 'BRBR', '2023-03-16 00:00:00', '2023-01-16 19:47:42', 0),
(8, 'Aerospace Valley Regional', 'CAAV', '2023-03-29 00:00:00', '2023-01-16 19:47:42', 0),
(9, 'Sacramento Regional', 'CADA', '2023-03-23 00:00:00', '2023-01-16 19:47:42', 0),
(10, 'Central Valley Regional', 'CAFR', '2023-03-09 00:00:00', '2023-01-16 19:47:42', 0),
(11, 'Los Angeles Regional', 'CALA', '2023-03-16 00:00:00', '2023-01-16 19:47:42', 0),
(12, 'Monterey Bay Regional', 'CAMB', '2023-03-29 00:00:00', '2023-01-16 19:47:42', 0),
(13, 'Orange County Regional', 'CAOC', '2023-03-09 00:00:00', '2023-01-16 19:47:42', 0),
(14, 'Hueneme Port Regional', 'CAPH', '2023-03-02 00:00:00', '2023-01-16 19:47:42', 0),
(15, 'San Diego Regional presented by Qualcomm', 'CASD', '2023-03-23 00:00:00', '2023-01-16 19:47:42', 0),
(16, 'San Francisco Regional', 'CASF', '2023-03-16 00:00:00', '2023-01-16 19:47:42', 0),
(17, 'Silicon Valley Regional', 'CASJ', '2023-04-05 00:00:00', '2023-01-16 19:47:42', 0),
(18, 'Ventura County Regional', 'CAVE', '2023-03-09 00:00:00', '2023-01-16 19:47:42', 0),
(19, 'FIRST Chesapeake District Championship', 'CHCMP', '2023-04-05 00:00:00', '2023-01-16 19:47:42', 0),
(20, 'FIRST Championship - FIRST Robotics Competition', 'CMPTX', '2023-04-19 00:00:00', '2023-01-16 19:47:42', 0),
(21, 'Colorado Regional', 'CODE', '2023-03-22 00:00:00', '2023-01-16 19:47:42', 0),
(22, 'NE District Hartford Event', 'CTHAR', '2023-03-31 00:00:00', '2023-01-16 19:47:42', 0),
(23, 'NE District Waterbury Event', 'CTWAT', '2023-03-10 00:00:00', '2023-01-16 19:47:42', 0),
(24, 'Orlando Regional', 'FLOR', '2023-03-15 00:00:00', '2023-01-16 19:47:42', 0),
(25, 'Tallahassee Regional', 'FLTA', '2023-03-23 00:00:00', '2023-01-16 19:47:42', 0),
(26, 'South Florida Regional', 'FLWP', '2023-03-01 00:00:00', '2023-01-16 19:47:42', 0),
(27, 'PCH District Albany Event', 'GAALB', '2023-03-02 00:00:00', '2023-01-16 19:47:42', 0),
(28, 'PCH District Carrollton Event', 'GACAR', '2023-03-23 00:00:00', '2023-01-16 19:47:42', 0),
(29, 'Peachtree District Championship', 'GACMP', '2023-04-05 00:00:00', '2023-01-16 19:47:42', 0),
(30, 'PCH District Dalton Event', 'GADAL', '2023-03-09 00:00:00', '2023-01-16 19:47:42', 0),
(31, 'PCH District Gwinnett Event', 'GAGWI', '2023-03-17 00:00:00', '2023-01-16 19:47:42', 0),
(32, 'PCH District Macon Event', 'GAMAC', '2023-03-30 00:00:00', '2023-01-16 19:47:42', 0),
(33, 'Hawaii Regional', 'HIHO', '2023-03-22 00:00:00', '2023-01-16 19:47:42', 0),
(34, 'Iowa Regional', 'IACF', '2023-03-22 00:00:00', '2023-01-16 19:47:42', 0),
(35, 'Idaho Regional', 'IDBO', '2023-03-29 00:00:00', '2023-01-16 19:47:42', 0),
(36, 'Midwest Regional', 'ILCH', '2023-03-08 00:00:00', '2023-01-16 19:47:42', 0),
(37, 'Central Illinois Regional', 'ILPE', '2023-03-15 00:00:00', '2023-01-16 19:47:42', 0),
(38, 'FIRST Indiana State Championship', 'INCMP', '2023-04-06 00:00:00', '2023-01-16 19:47:42', 0),
(39, 'FIN District Greenwood Event', 'INGRE', '2023-03-30 00:00:00', '2023-01-16 19:47:42', 0),
(40, 'FIN District Mishawaka Event', 'INMIS', '2023-03-03 00:00:00', '2023-01-16 19:47:42', 0),
(41, 'FIN District Princeton Event presented by Toyota', 'INPRI', '2023-03-10 00:00:00', '2023-01-16 19:47:42', 0),
(42, 'FIN District Tippecanoe Event', 'INWLA', '2023-03-24 00:00:00', '2023-01-16 19:47:42', 0),
(43, 'FIRST Israel District Championship', 'ISCMP', '2023-03-20 00:00:00', '2023-01-16 19:47:42', 0),
(44, 'ISR District Event #1', 'ISDE1', '2023-02-26 00:00:00', '2023-01-16 19:47:42', 0),
(45, 'ISR District Event #2', 'ISDE2', '2023-03-01 00:00:00', '2023-01-16 19:47:42', 0),
(46, 'ISR District Event #3', 'ISDE3', '2023-03-12 00:00:00', '2023-01-16 19:47:42', 0),
(47, 'ISR District Event #4', 'ISDE4', '2023-03-15 00:00:00', '2023-01-16 19:47:42', 0),
(48, 'Heartland Regional', 'KSLA', '2023-03-15 00:00:00', '2023-01-16 19:47:42', 0),
(49, 'Bayou Regional', 'LAKE', '2023-03-29 00:00:00', '2023-01-16 19:47:42', 0),
(50, 'NE District Greater Boston Event', 'MABOS', '2023-03-24 00:00:00', '2023-01-16 19:47:42', 0),
(51, 'NE District SE Mass Event', 'MABRI', '2023-03-03 00:00:00', '2023-01-16 19:47:42', 0),
(52, 'NE District North Shore Event', 'MAREA', '2023-03-17 00:00:00', '2023-01-16 19:47:42', 0),
(53, 'NE District Western NE Event', 'MAWNE', '2023-03-16 00:00:00', '2023-01-16 19:47:42', 0),
(54, 'NE District WPI Event', 'MAWOR', '2023-03-31 00:00:00', '2023-01-16 19:47:42', 0),
(55, 'CHS District Bethesda MD Event', 'MDBET', '2023-03-10 00:00:00', '2023-01-16 19:47:42', 0),
(56, 'CHS District Timonium MD Event', 'MDTIM', '2023-03-24 00:00:00', '2023-01-16 19:47:42', 0),
(57, 'FIM District Belleville Event', 'MIBEL', '2023-03-16 00:00:00', '2023-01-16 19:47:42', 0),
(58, 'FIRST in Michigan State Championship', 'MICMP', '2023-04-06 00:00:00', '2023-01-16 19:47:42', 0),
(59, 'FIM District Detroit Event', 'MIDET', '2023-03-16 00:00:00', '2023-01-16 19:47:42', 0),
(60, 'FIM District Wayne State University Event', 'MIDTR', '2023-03-10 00:00:00', '2023-01-16 19:47:42', 0),
(61, 'FIM District Escanaba Event', 'MIESC', '2023-03-02 00:00:00', '2023-01-16 19:47:42', 0),
(62, 'FIM District Calvin University Event', 'MIFOR', '2023-03-02 00:00:00', '2023-01-16 19:47:42', 0),
(63, 'FIM District Jackson Event', 'MIJAC', '2023-03-02 00:00:00', '2023-01-16 19:47:42', 0),
(64, 'FIM District Kettering University Event #2', 'MIKE2', '2023-03-09 00:00:00', '2023-01-16 19:47:42', 0),
(65, 'FIM District Kentwood Event', 'MIKEN', '2023-03-30 00:00:00', '2023-01-16 19:47:42', 0),
(66, 'FIM District Kettering University Event #1', 'MIKET', '2023-03-02 00:00:00', '2023-01-16 19:47:42', 0),
(67, 'FIM District Lakeview Event #2', 'MILA2', '2023-03-25 00:00:00', '2023-01-16 19:47:42', 0),
(68, 'FIM District Lakeview Event #1', 'MILAK', '2023-03-23 00:00:00', '2023-01-16 19:47:42', 0),
(69, 'FIM District Lansing Event', 'MILAN', '2023-03-09 00:00:00', '2023-01-16 19:47:42', 0),
(70, 'FIM District Livonia Event', 'MILIV', '2023-03-30 00:00:00', '2023-01-16 19:47:42', 0),
(71, 'FIM District LSSU Event', 'MILSU', '2023-03-23 00:00:00', '2023-01-16 19:47:42', 0),
(72, 'FIM District Macomb Community College Event', 'MIMCC', '2023-03-30 00:00:00', '2023-01-16 19:47:42', 0),
(73, 'FIM District Midland Event', 'MIMID', '2023-03-23 00:00:00', '2023-01-16 19:47:42', 0),
(74, 'FIM District Milford Event', 'MIMIL', '2023-03-02 00:00:00', '2023-01-16 19:47:42', 0),
(75, 'FIM District Muskegon Event', 'MIMUS', '2023-03-16 00:00:00', '2023-01-16 19:47:42', 0),
(76, 'FIM District Saline Event', 'MISAL', '2023-03-30 00:00:00', '2023-01-16 19:47:42', 0),
(77, 'FIM District St. Joseph Event', 'MISJO', '2023-03-09 00:00:00', '2023-01-16 19:47:42', 0),
(78, 'FIM District Standish-Sterling Event', 'MISTA', '2023-03-16 00:00:00', '2023-01-16 19:47:42', 0),
(79, 'FIM District Troy Event #2', 'MITR2', '2023-03-30 00:00:00', '2023-01-16 19:47:42', 0),
(80, 'FIM District Troy Event #1', 'MITRY', '2023-03-24 00:00:00', '2023-01-16 19:47:42', 0),
(81, 'FIM District Traverse City Event', 'MITVC', '2023-03-16 00:00:00', '2023-01-16 19:47:42', 0),
(82, 'FIM District West Michigan Event', 'MIWMI', '2023-03-23 00:00:00', '2023-01-16 19:47:42', 0),
(83, 'Lake Superior Regional', 'MNDU', '2023-03-01 00:00:00', '2023-01-16 19:47:42', 0),
(84, 'Northern Lights Regional', 'MNDU2', '2023-03-01 00:00:00', '2023-01-16 19:47:42', 0),
(85, 'Minnesota 10,000 Lakes Regional presented by Medtronic', 'MNMI', '2023-03-29 00:00:00', '2023-01-16 19:47:42', 0),
(86, 'Minnesota North Star Regional at La Crosse', 'MNMI2', '2023-03-23 00:00:00', '2023-01-16 19:47:42', 0),
(87, 'Greater Kansas City Regional', 'MOKC', '2023-03-29 00:00:00', '2023-01-16 19:47:42', 0),
(88, 'Central Missouri Regional', 'MOSE', '2023-03-22 00:00:00', '2023-01-16 19:47:42', 0),
(89, 'St. Louis Regional', 'MOSL', '2023-03-08 00:00:00', '2023-01-16 19:47:42', 0),
(90, 'FIRST Mid-Atlantic District Championship', 'MRCMP', '2023-04-05 00:00:00', '2023-01-16 19:47:42', 0),
(91, 'Magnolia Regional', 'MSLR', '2023-03-15 00:00:00', '2023-01-16 19:47:42', 0),
(92, 'Regional Monterrey', 'MXMO', '2023-03-01 00:00:00', '2023-01-16 19:47:42', 0),
(93, 'Regional Puebla', 'MXPU', '2023-03-15 00:00:00', '2023-01-16 19:47:42', 0),
(94, 'Regional Laguna', 'MXTO', '2023-03-22 00:00:00', '2023-01-16 19:47:42', 0),
(95, 'FNC District UNC Asheville Event', 'NCASH', '2023-03-03 00:00:00', '2023-01-16 19:47:42', 0),
(96, 'FIRST North Carolina District State Championship', 'NCCMP', '2023-03-31 00:00:00', '2023-01-16 19:47:42', 0),
(97, 'FNC District Johnston County Event', 'NCJOH', '2023-03-10 00:00:00', '2023-01-16 19:47:42', 0),
(98, 'FNC District Mecklenburg County Event', 'NCMEC', '2023-03-17 00:00:00', '2023-01-16 19:47:42', 0),
(99, 'FNC District UNC Pembroke Event', 'NCPEM', '2023-03-24 00:00:00', '2023-01-16 19:47:42', 0),
(100, 'FNC District Wake County Event', 'NCWAK', '2023-03-17 00:00:00', '2023-01-16 19:47:42', 0),
(101, 'Great Northern Regional', 'NDGF', '2023-03-08 00:00:00', '2023-01-16 19:47:42', 0),
(102, 'New England FIRST District Championship', 'NECMP', '2023-04-05 00:00:00', '2023-01-16 19:47:42', 0),
(103, 'NE District UNH Event', 'NHDUR', '2023-03-24 00:00:00', '2023-01-16 19:47:42', 0),
(104, 'NE District Granite State Event', 'NHGRS', '2023-03-02 00:00:00', '2023-01-16 19:47:42', 0),
(105, 'FMA District Mount Olive Event', 'NJFLA', '2023-03-10 00:00:00', '2023-01-16 19:47:42', 0),
(106, 'FMA District Robbinsville Event', 'NJROB', '2023-03-17 00:00:00', '2023-01-16 19:47:42', 0),
(107, 'FMA District Montgomery Event', 'NJSKI', '2023-03-31 00:00:00', '2023-01-16 19:47:42', 0),
(108, 'FMA District Seneca Event', 'NJTAB', '2023-03-24 00:00:00', '2023-01-16 19:47:42', 0),
(109, 'FMA District Warren Hills Event', 'NJWAS', '2023-03-24 00:00:00', '2023-01-16 19:47:42', 0),
(110, 'Las Vegas Regional', 'NVLV', '2023-03-29 00:00:00', '2023-01-16 19:47:42', 0),
(111, 'FIRST Long Island Regional #1', 'NYLI', '2023-03-20 00:00:00', '2023-01-16 19:47:42', 0),
(112, 'FIRST Long Island Regional #2', 'NYLI2', '2023-03-23 00:00:00', '2023-01-16 19:47:42', 0),
(113, 'New York City Regional', 'NYNY', '2023-04-05 00:00:00', '2023-01-16 19:47:42', 0),
(114, 'Finger Lakes Regional', 'NYRO', '2023-03-15 00:00:00', '2023-01-16 19:47:42', 0),
(115, 'New York Tech Valley Regional', 'NYTR', '2023-03-29 00:00:00', '2023-01-16 19:47:42', 0),
(116, 'Buckeye Regional', 'OHCL', '2023-03-29 00:00:00', '2023-01-16 19:47:42', 0),
(117, 'Miami Valley Regional', 'OHMV', '2023-03-15 00:00:00', '2023-01-16 19:47:42', 0),
(118, 'Oklahoma Regional', 'OKOK', '2023-03-08 00:00:00', '2023-01-16 19:47:42', 0),
(119, 'Green Country Regional', 'OKTU', '2023-04-05 00:00:00', '2023-01-16 19:47:42', 0),
(120, 'ONT District Georgian Event', 'ONBAR', '2023-03-03 00:00:00', '2023-01-16 19:47:42', 0),
(121, 'FIRST Ontario Provincial Championship', 'ONCMP', '2023-04-05 00:00:00', '2023-01-16 19:47:42', 0),
(122, 'ONT District McMaster University', 'ONHAM', '2023-03-31 00:00:00', '2023-01-16 19:47:42', 0),
(123, 'ONT District Western University Engineering Event', 'ONLON', '2023-03-17 00:00:00', '2023-01-16 19:47:42', 0),
(124, 'ONT District Newmarket Complex Event', 'ONNEW', '2023-03-03 00:00:00', '2023-01-16 19:47:42', 0),
(125, 'ONT District North Bay Event', 'ONNOB', '2023-03-24 00:00:00', '2023-01-16 19:47:42', 0),
(126, 'ONT District Humber College Event', 'ONTOR', '2023-03-17 00:00:00', '2023-01-16 19:47:42', 0),
(127, 'ONT District University of Waterloo Event', 'ONWAT', '2023-03-23 00:00:00', '2023-01-16 19:47:42', 0),
(128, 'ONT District Windsor Essex Great Lakes Event', 'ONWIN', '2023-03-30 00:00:00', '2023-01-16 19:47:42', 0),
(129, 'PNW District Clackamas Academy Event', 'ORORE', '2023-03-02 00:00:00', '2023-01-16 19:47:42', 0),
(130, 'PNW District Oregon State Fairgrounds Event', 'ORSAL', '2023-03-23 00:00:00', '2023-01-16 19:47:42', 0),
(131, 'PNW District Wilsonville Event', 'ORWIL', '2023-03-09 00:00:00', '2023-01-16 19:47:42', 0),
(132, 'FMA District Bensalem Event', 'PABEN', '2023-03-31 00:00:00', '2023-01-16 19:47:42', 0),
(133, 'Greater Pittsburgh Regional presented by Argo AI', 'PACA', '2023-04-05 00:00:00', '2023-01-16 19:47:42', 0),
(134, 'FMA District Hatboro-Horsham Event', 'PAHAT', '2023-03-03 00:00:00', '2023-01-16 19:47:42', 0),
(135, 'FMA District Springside Chestnut Hill Academy Event', 'PAPHI', '2023-03-17 00:00:00', '2023-01-16 19:47:42', 0),
(136, 'Pacific Northwest FIRST District Championship', 'PNCMP', '2023-04-05 00:00:00', '2023-01-16 19:47:42', 0),
(137, 'Festival de Robotique Regional', 'QCMO', '2023-03-29 00:00:00', '2023-01-16 19:47:42', 0),
(138, 'NE District Rhode Island Event', 'RINSC', '2023-03-10 00:00:00', '2023-01-16 19:47:42', 0),
(139, 'PCH District Anderson Event', 'SCAND', '2023-03-10 00:00:00', '2023-01-16 19:47:42', 0),
(140, 'PCH District Hartsville Event', 'SCHAR', '2023-03-23 00:00:00', '2023-01-16 19:47:42', 0),
(141, 'Smoky Mountains Regional', 'TNKN', '2023-03-29 00:00:00', '2023-01-16 19:47:42', 0),
(142, 'Istanbul Regional', 'TUIS', '2023-03-24 00:00:00', '2023-01-16 19:47:42', 0),
(143, 'Bosphorus Regional', 'TUIS2', '2023-03-27 00:00:00', '2023-01-16 19:47:42', 0),
(144, 'Izmir Regional', 'TUIS3', '2023-03-03 00:00:00', '2023-01-16 19:47:42', 0),
(145, 'Mersin Regional', 'TUME', '2023-03-10 00:00:00', '2023-01-16 19:47:42', 0),
(146, 'FIT District Amarillo Event', 'TXAMA', '2023-03-30 00:00:00', '2023-01-16 19:47:42', 0),
(147, 'FIT District Belton Event', 'TXBEL', '2023-03-09 00:00:00', '2023-01-16 19:47:42', 0),
(148, 'FIT District Channelview Event', 'TXCHA', '2023-03-10 00:00:00', '2023-01-16 19:47:42', 0),
(149, 'FIT District Space City @ Clear Lake Event', 'TXCLE', '2023-03-31 00:00:00', '2023-01-16 19:47:42', 0),
(150, 'FIRST In Texas District Championship sponsored by Phillips 66', 'TXCMP', '2023-04-05 00:00:00', '2023-01-16 19:47:42', 0),
(151, 'FIT District Dallas Event', 'TXDAL', '2023-03-03 00:00:00', '2023-01-16 19:47:42', 0),
(152, 'FIT District Fort Worth Event', 'TXFOR', '2023-03-16 00:00:00', '2023-01-16 19:47:42', 0),
(153, 'FIT District Houston Event', 'TXHOU', '2023-03-23 00:00:00', '2023-01-16 19:47:42', 0),
(154, 'FIT District San Antonio Event', 'TXSAN', '2023-03-16 00:00:00', '2023-01-16 19:47:42', 0),
(155, 'FIT District Waco Event', 'TXWAC', '2023-03-02 00:00:00', '2023-01-16 19:47:42', 0),
(156, 'Utah Regional', 'UTWV', '2023-03-01 00:00:00', '2023-01-16 19:47:42', 0),
(157, 'CHS District Alexandria VA Event', 'VAALE', '2023-03-17 00:00:00', '2023-01-16 19:47:42', 0),
(158, 'CHS District Blacksburg VA Event', 'VABLA', '2023-03-03 00:00:00', '2023-01-16 19:47:42', 0),
(159, 'CHS District Glen Allen VA Event', 'VAGLE', '2023-03-24 00:00:00', '2023-01-16 19:47:42', 0),
(160, 'CHS District Portsmouth VA Event', 'VAPOR', '2023-03-17 00:00:00', '2023-01-16 19:47:42', 0),
(161, 'PNW District Auburn Event', 'WAAHS', '2023-03-31 00:00:00', '2023-01-16 19:47:42', 0),
(162, 'PNW District Bonney Lake Event', 'WABON', '2023-03-17 00:00:00', '2023-01-16 19:47:42', 0),
(163, 'PNW District Sammamish Event', 'WASAM', '2023-03-24 00:00:00', '2023-01-16 19:47:42', 0),
(164, 'PNW District Glacier Peak Event', 'WASNO', '2023-03-03 00:00:00', '2023-01-16 19:47:42', 0),
(165, 'PNW District SunDome Event', 'WAYAK', '2023-03-16 00:00:00', '2023-01-16 19:47:42', 0),
(166, 'Seven Rivers Regional', 'WILA', '2023-03-29 00:00:00', '2023-01-16 19:47:42', 0),
(167, 'Wisconsin Regional', 'WIMI', '2023-03-22 00:00:00', '2023-01-16 19:47:42', 0);

CREATE TABLE IF NOT EXISTS `fact_match_data_2023` (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `event_id` int(4) UNSIGNED NOT NULL,
  `team_id` int(5) UNSIGNED NOT NULL,
  `match_id` int(4) UNSIGNED NOT NULL,
  `practice_match` tinyint(1) NOT NULL DEFAULT '0',
  `position_id` int(3) UNSIGNED NOT NULL,
  `auto_mobility` tinyint(1) NOT NULL DEFAULT '0',
  `auto_substn_grid_top_substn` tinyint(1) NOT NULL DEFAULT '0',
  `auto_substn_grid_top_mid` tinyint(1) NOT NULL DEFAULT '0',
  `auto_substn_grid_top_wall` tinyint(1) NOT NULL DEFAULT '0',
  `auto_substn_grid_mid_substn` tinyint(1) NOT NULL DEFAULT '0',
  `auto_substn_grid_mid_mid` tinyint(1) NOT NULL DEFAULT '0',
  `auto_substn_grid_mid_wall` tinyint(1) NOT NULL DEFAULT '0',
  `auto_substn_grid_hyb_substn` tinyint(1) NOT NULL DEFAULT '0',
  `auto_substn_grid_hyb_mid` tinyint(1) NOT NULL DEFAULT '0',
  `auto_substn_grid_hyb_wall` tinyint(1) NOT NULL DEFAULT '0',
  `auto_coop_grid_top_substn` tinyint(1) NOT NULL DEFAULT '0',
  `auto_coop_grid_top_mid` tinyint(1) NOT NULL DEFAULT '0',
  `auto_coop_grid_top_wall` tinyint(1) NOT NULL DEFAULT '0',
  `auto_coop_grid_mid_substn` tinyint(1) NOT NULL DEFAULT '0',
  `auto_coop_grid_mid_mid` tinyint(1) NOT NULL DEFAULT '0',
  `auto_coop_grid_mid_wall` tinyint(1) NOT NULL DEFAULT '0',
  `auto_coop_grid_hyb_substn` tinyint(1) NOT NULL DEFAULT '0',
  `auto_coop_grid_hyb_mid` tinyint(1) NOT NULL DEFAULT '0',
  `auto_coop_grid_hyb_wall` tinyint(1) NOT NULL DEFAULT '0',
  `auto_wall_grid_top_substn` tinyint(1) NOT NULL DEFAULT '0',
  `auto_wall_grid_top_mid` tinyint(1) NOT NULL DEFAULT '0',
  `auto_wall_grid_top_wall` tinyint(1) NOT NULL DEFAULT '0',
  `auto_wall_grid_mid_substn` tinyint(1) NOT NULL DEFAULT '0',
  `auto_wall_grid_mid_mid` tinyint(1) NOT NULL DEFAULT '0',
  `auto_wall_grid_mid_wall` tinyint(1) NOT NULL DEFAULT '0',
  `auto_wall_grid_hyb_substn` tinyint(1) NOT NULL DEFAULT '0',
  `auto_wall_grid_hyb_mid` tinyint(1) NOT NULL DEFAULT '0',
  `auto_wall_grid_hyb_wall` tinyint(1) NOT NULL DEFAULT '0',
  `auto_charge_station` int(2) UNSIGNED NOT NULL,
  `substn_grid_top_substn` tinyint(1) NOT NULL DEFAULT '0',
  `substn_grid_top_mid` tinyint(1) NOT NULL DEFAULT '0',
  `substn_grid_top_wall` tinyint(1) NOT NULL DEFAULT '0',
  `substn_grid_mid_substn` tinyint(1) NOT NULL DEFAULT '0',
  `substn_grid_mid_mid` tinyint(1) NOT NULL DEFAULT '0',
  `substn_grid_mid_wall` tinyint(1) NOT NULL DEFAULT '0',
  `substn_grid_hyb_substn` tinyint(1) NOT NULL DEFAULT '0',
  `substn_grid_hyb_mid` tinyint(1) NOT NULL DEFAULT '0',
  `substn_grid_hyb_wall` tinyint(1) NOT NULL DEFAULT '0',
  `coop_grid_top_substn` tinyint(1) NOT NULL DEFAULT '0',
  `coop_grid_mid_substn` tinyint(1) NOT NULL DEFAULT '0',
  `coop_grid_mid_mid` tinyint(1) NOT NULL DEFAULT '0',
  `coop_grid_mid_wall` tinyint(1) NOT NULL DEFAULT '0',
  `coop_grid_hyb_substn` tinyint(1) NOT NULL DEFAULT '0',
  `coop_grid_hyb_mid` tinyint(1) NOT NULL DEFAULT '0',
  `coop_grid_hyb_wall` tinyint(1) NOT NULL DEFAULT '0',
  `wall_grid_top_substn` tinyint(1) NOT NULL DEFAULT '0',
  `wall_grid_top_mid` tinyint(1) NOT NULL DEFAULT '0',
  `wall_grid_top_wall` tinyint(1) NOT NULL DEFAULT '0',
  `coop_grid_top_mid` tinyint(1) NOT NULL DEFAULT '0',
  `coop_grid_top_wall` tinyint(1) NOT NULL DEFAULT '0',
  `wall_grid_mid_substn` tinyint(1) NOT NULL DEFAULT '0',
  `wall_grid_mid_mid` tinyint(1) NOT NULL DEFAULT '0',
  `wall_grid_mid_wall` tinyint(1) NOT NULL DEFAULT '0',
  `wall_grid_hyb_substn` tinyint(1) NOT NULL DEFAULT '0',
  `wall_grid_hyb_mid` tinyint(1) NOT NULL DEFAULT '0',
  `wall_grid_hyb_wall` tinyint(1) NOT NULL DEFAULT '0',
  `dropped_gp_count` int(3) NOT NULL,
  `charge_station` int(2) UNSIGNED NOT NULL,
  `feeder` tinyint(1) NOT NULL DEFAULT '0',
  `defense` tinyint(1) NOT NULL DEFAULT '0',
  `foul_count` int(3) NOT NULL,
  `yellow_card` tinyint(1) NOT NULL DEFAULT '0',
  `red_card` tinyint(1) NOT NULL DEFAULT '0',
  `notes` text COLLATE utf8_unicode_ci NOT NULL,
  `invalid` tinyint(1) NOT NULL DEFAULT '0',
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `event_id` (`event_id`,`team_id`,`match_id`,`practice_match`,`position_id`),
  KEY `invalid` (`invalid`,`timestamp`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE IF NOT EXISTS `game_info` (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `keystring` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `intvalue` int(15) NOT NULL,
  `stringval` text COLLATE latin1_general_cs NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `invalid` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `key` (`keystring`),
  KEY `timestamp` (`timestamp`,`invalid`)
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

INSERT INTO `game_info` (`id`, `keystring`, `intvalue`, `stringval`, `invalid`) VALUES
(8, '2023_charge_none', 0, 'No Charge Station', '2023-01-28 15:01:52', 0),
(9, '2023_charge_attempt', 1, 'Attempted Charge Station', '2023-01-28 15:01:52', 0),
(10, '2023_charge_dock', 2, 'Docked Charge Station', '2023-01-28 15:01:52', 0),
(11, '2023_charge_engage', 3, 'Engaged Charge Station', '2023-01-28 15:01:52', 0)

CREATE TABLE IF NOT EXISTS `notes_options` (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `option_text` text COLLATE latin1_general_cs NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `invalid` tinyint(1) UNSIGNED NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `timestamp` (`timestamp`)
) ENGINE=MyISAM AUTO_INCREMENT=11 DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

INSERT INTO `notes_options` (`id`, `option_text`, `timestamp`, `invalid`) VALUES
(1, 'No Show', '2014-01-25 15:21:36', 0),
(2, 'Non-functional', '2014-01-25 15:21:36', 0),
(10, 'Stopped responding mid-match', '2016-03-17 23:53:21', 0),
(3, 'Defender', '2016-01-15 16:30:02', 0);

CREATE TABLE IF NOT EXISTS `position_lu` (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `position` text COLLATE latin1_general_cs NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `invalid` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `timestamp` (`timestamp`,`invalid`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

INSERT INTO `position_lu` (`id`, `position`, `timestamp`, `invalid`) VALUES
(1, 'Red 1', '2015-02-12 00:30:10', 0),
(2, 'Red 2', '2015-02-12 00:30:10', 0),
(3, 'Red 3', '2015-02-12 00:30:36', 0),
(4, 'Blue 1', '2015-02-12 00:30:36', 0),
(5, 'Blue 2', '2015-02-12 00:30:50', 0),
(6, 'Blue 3', '2015-02-12 00:30:50', 0);

CREATE TABLE IF NOT EXISTS `programming_lu` (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `language_name` text COLLATE latin1_general_cs NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `invalid` tinyint(1) UNSIGNED NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `timestamp` (`timestamp`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

INSERT INTO `programming_lu` (`id`, `language_name`, `timestamp`, `invalid`) VALUES
(1, 'LabView', '2020-01-20 15:21:36', 0),
(2, 'Java', '2020-01-20 15:21:36', 0),
(3, 'C++', '2020-01-20 15:53:21', 0),
(4, 'Python', '2020-01-20 16:30:02', 0),
(5, 'Other', '2020-01-20 16:30:02', 0);

CREATE TABLE IF NOT EXISTS `robot_lu` (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `team_id` int(5) UNSIGNED NOT NULL,
  `robot_photo` text COLLATE latin1_general_cs NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `invalid` tinyint(1) UNSIGNED NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `team_id` (`team_id`),
  KEY `timestamp` (`timestamp`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

CREATE TABLE IF NOT EXISTS `scout_pit_data_2023` (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `team_id` int(5) UNSIGNED NOT NULL,
  `traction_wheels` tinyint(1) UNSIGNED NOT NULL,
  `pneumatic_wheels` tinyint(1) UNSIGNED NOT NULL,
  `omni_wheels` tinyint(1) UNSIGNED NOT NULL,
  `mecanum_wheels` tinyint(1) UNSIGNED NOT NULL,
  `swerve_drive` tinyint(1) UNSIGNED NOT NULL,
  `tank_drive` tinyint(1) UNSIGNED NOT NULL,
  `other_drive_wheels` tinyint(1) UNSIGNED NOT NULL,
  `robot_gross_weight_lbs` int(4) UNSIGNED NOT NULL,
  `robot_gross_width_in` int(4) UNSIGNED NOT NULL,
  `robot_gross_length_in` int(4) UNSIGNED NOT NULL,
  `notes` text COLLATE utf8_unicode_ci NOT NULL,
  `invalid` tinyint(1) NOT NULL DEFAULT '0',
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `team_id` (`team_id`),
  KEY `invalid` (`invalid`),
  KEY `timestamp` (`timestamp`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
