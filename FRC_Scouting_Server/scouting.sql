SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;


CREATE TABLE IF NOT EXISTS `configuration_lu` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `configuration_desc` text COLLATE latin1_general_cs NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs AUTO_INCREMENT=8 ;

INSERT INTO `configuration_lu` (`id`, `configuration_desc`) VALUES
(1, 'Wide'),
(2, 'Long'),
(3, 'Square'),
(4, 'Round'),
(5, 'Hex'),
(6, 'Triangle'),
(7, 'Other');

CREATE TABLE IF NOT EXISTS `event_lu` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `event_name` varchar(40) COLLATE latin1_general_cs NOT NULL,
  `match_url` text COLLATE latin1_general_cs NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `event_name` (`event_name`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs AUTO_INCREMENT=86 ;

INSERT INTO `event_lu` (`id`, `event_name`, `match_url`) VALUES
(1, 'Chesapeake Regional', 'http://www2.usfirst.org/2013comp/events/MDBA/ScheduleQual.html'),
(57, 'Midwest Regional', 'http://www2.usfirst.org/2013comp/Events/ILCH/ScheduleQual.html'),
(54, 'Silicon Valley Regional', 'http://www2.usfirst.org/2013comp/Events/CASJ/ScheduleQual.html'),
(4, 'Buckeye Regional', 'http://www2.usfirst.org/2013comp/Events/OHCL/ScheduleQual.html'),
(52, 'Western Canadian Regional', 'http://www2.usfirst.org/2013comp/Events/ABCA/ScheduleQual.html'),
(55, 'Colorado Regional', 'http://www2.usfirst.org/2013comp/Events/CODE/ScheduleQual.html'),
(56, 'Hawaii Regional', 'http://www2.usfirst.org/2013comp/Events/HIHO/ScheduleQual.html'),
(53, 'Razorback Regional', 'http://www2.usfirst.org/2013comp/Events/ARFA/ScheduleQual.html'),
(50, 'Alamo Regional', 'http://www2.usfirst.org/2013comp/Events/TXSA/ScheduleQual.html'),
(51, 'Seattle Regional', 'http://www2.usfirst.org/2013comp/Events/WASE/ScheduleQual.html'),
(48, 'Oklahoma Regional', 'http://www2.usfirst.org/2013comp/Events/OKOK/ScheduleQual.html'),
(49, 'Smoky Mountains Regional', 'http://www2.usfirst.org/2013comp/Events/TNKN/ScheduleQual.html'),
(7, 'Palmetto Regional', 'http://www2.usfirst.org/2013comp/Events/SCMB/ScheduleQual.html'),
(8, 'Granite State Regional', 'http://www2.usfirst.org/2013comp/Events/NHMA/ScheduleQual.html'),
(9, 'Finger Lakes Regional', 'http://www2.usfirst.org/2013comp/Events/NYRO/ScheduleQual.html'),
(10, 'Hub City Regional', 'http://www2.usfirst.org/2013comp/Events/TXLU/ScheduleQual.html'),
(11, 'Central Valley Regional', 'http://www2.usfirst.org/2013comp/Events/CAMA/ScheduleQual.html'),
(12, 'Greater Toronto East Regional', 'http://www2.usfirst.org/2013comp/Events/ONTO/ScheduleQual.html'),
(13, 'San Diego Regional', 'http://www2.usfirst.org/2013comp/Events/CASD/ScheduleQual.html'),
(14, 'Orlando Regional', 'http://www2.usfirst.org/2013comp/Events/FLOR/ScheduleQual.html'),
(15, 'WPI Regional', 'http://www2.usfirst.org/2013comp/Events/MAWO/ScheduleQual.html'),
(16, 'Lake Superior Regional', 'http://www2.usfirst.org/2013comp/Events/MNDU/ScheduleQual.html'),
(17, 'Northern Lights Regional', 'http://www2.usfirst.org/2013comp/Events/MNDU2/ScheduleQual.html'),
(18, 'New York City Regional', 'http://www2.usfirst.org/2013comp/Events/NYNY/ScheduleQual.html'),
(19, 'Oregon Regional', 'http://www2.usfirst.org/2013comp/Events/ORPO/ScheduleQual.html'),
(20, 'Lone Star Regional', 'http://www2.usfirst.org/2013comp/Events/TXHO/ScheduleQual.html'),
(21, 'Israel Regional', 'http://www2.usfirst.org/2013comp/Events/ISTA/ScheduleQual.html'),
(22, 'Montreal Regional', 'http://www2.usfirst.org/2013comp/Events/QCMO/ScheduleQual.html'),
(23, 'Peachtree Regional', 'http://www2.usfirst.org/2013comp/Events/GADU/ScheduleQual.html'),
(24, 'Boilermaker Regional', 'http://www2.usfirst.org/2013comp/Events/INWL/ScheduleQual.html'),
(25, 'Greater Kansas City Regional', 'http://www2.usfirst.org/2013comp/Events/MOKC/ScheduleQual.html'),
(26, 'St. Louis Regional', 'http://www2.usfirst.org/2013comp/Events/MOSL/ScheduleQual.html'),
(27, 'North Carolina Regional', 'http://www2.usfirst.org/2013comp/Events/NCRE/ScheduleQual.html'),
(28, 'Pittsburgh Regional', 'http://www2.usfirst.org/2013comp/Events/PAPI/ScheduleQual.html'),
(29, 'Virginia Regional', 'http://www2.usfirst.org/2013comp/Events/VARI/ScheduleQual.html'),
(30, 'Waterloo Regional', 'http://www2.usfirst.org/2013comp/Events/ONWA/ScheduleQual.html'),
(31, 'Phoenix Regional', 'http://www2.usfirst.org/2013comp/Events/AZCH/ScheduleQual.html'),
(32, 'Sacramento Regional', 'http://www2.usfirst.org/2013comp/Events/CASA/ScheduleQual.html'),
(33, 'Los Angeles Regional', 'http://www2.usfirst.org/2013comp/Events/CALB/ScheduleQual.html'),
(34, 'Bayou Regional', 'http://www2.usfirst.org/2013comp/Events/LAKE/ScheduleQual.html'),
(35, 'Boston Regional', 'http://www2.usfirst.org/2013comp/Events/MABO/ScheduleQual.html'),
(36, 'Queen City Regional', 'http://www2.usfirst.org/2013comp/Events/OHIC/ScheduleQual.html'),
(37, 'Dallas Regional', 'http://www2.usfirst.org/2013comp/Events/TXDA/ScheduleQual.html'),
(38, 'Utah Regional', 'http://www2.usfirst.org/2013comp/Events/UTWV/ScheduleQual.html'),
(39, 'Central Washington Regional', 'http://www2.usfirst.org/2013comp/Events/WASE2/ScheduleQual.html'),
(40, 'Wisconsin Regional', 'http://www2.usfirst.org/2013comp/Events/WIMI/ScheduleQual.html'),
(41, 'Greater Toronto West Regional', 'http://www2.usfirst.org/2013comp/Events/ONTO2/ScheduleQual.html'),
(42, 'Inland Empire Regional', 'http://www2.usfirst.org/2013comp/Events/CASB/ScheduleQual.html'),
(43, 'Connecticut Regional', 'http://www2.usfirst.org/2013comp/Events/CTHA/ScheduleQual.html'),
(44, 'Washington DC Regional', 'http://www2.usfirst.org/2013comp/Events/DCWA/ScheduleQual.html'),
(45, 'South Florida Regional', 'http://www2.usfirst.org/2013comp/Events/FLBR/ScheduleQual.html'),
(46, 'Minnesota 10000 Lakes Regional', 'http://www2.usfirst.org/2013comp/Events/MNMI/ScheduleQual.html'),
(47, 'Minnesota North Star Regional', 'http://www2.usfirst.org/2013comp/Events/MNMI2/ScheduleQual.html'),
(58, 'Crossroads Regional', 'http://www2.usfirst.org/2013comp/Events/INTH/ScheduleQual.html'),
(59, 'Pine Tree Regional', 'http://www2.usfirst.org/2013comp/Events/MELE/ScheduleQual.html'),
(60, 'Las Vegas Regional', 'http://www2.usfirst.org/2013comp/Events/NVLV/ScheduleQual.html'),
(61, 'Long Island Regional', 'http://www2.usfirst.org/2013comp/Events/NYLI/ScheduleQual.html'),
(62, 'Spokane Regional', 'http://www2.usfirst.org/2013comp/Events/WACH/ScheduleQual.html'),
(63, 'Michigan State Championship', 'http://www2.usfirst.org/2013comp/Events/MICMP/ScheduleQual.html'),
(64, 'Mid-Atlantic Championship', 'http://www2.usfirst.org/2013comp/Events/MRCMP/ScheduleQual.html'),
(65, 'Kettering University District', 'http://www2.usfirst.org/2013comp/Events/MIKET/ScheduleQual.html'),
(66, 'Traverse City District', 'http://www2.usfirst.org/2013comp/Events/MITVC/ScheduleQual.html'),
(67, 'Hatboro-Horsham District', 'http://www2.usfirst.org/2013comp/Events/PAHAT/ScheduleQual.html'),
(68, 'Gull Lake District', 'http://www2.usfirst.org/2013comp/Events/MIGUL/ScheduleQual.html'),
(69, 'Waterford District', 'http://www2.usfirst.org/2013comp/Events/MIWFD/ScheduleQual.html'),
(70, 'Detroit District', 'http://www2.usfirst.org/2013comp/Events/MIDET/ScheduleQual.html'),
(71, 'St Joseph District', 'http://www2.usfirst.org/2013comp/Events/MISJO/ScheduleQual.html'),
(72, 'TCNJ District', 'http://www2.usfirst.org/2013comp/Events/NJEWN/ScheduleQual.html'),
(73, 'Springside - Chestnut Hill District', 'http://www2.usfirst.org/2013comp/Events/PAPHI/ScheduleQual.html'),
(74, 'West Michigan District', 'http://www2.usfirst.org/2013comp/Events/MIWMI/ScheduleQual.html'),
(75, 'Grand Blanc District', 'http://www2.usfirst.org/2013comp/Events/MIGBL/ScheduleQual.html'),
(76, 'Mount Olive District', 'http://www2.usfirst.org/2013comp/Events/NJFLA/ScheduleQual.html'),
(77, 'Lenape Seneca District', 'http://www2.usfirst.org/2013comp/Events/NJLEN/ScheduleQual.html'),
(78, 'Livonia District', 'http://www2.usfirst.org/2013comp/Events/MILIV/ScheduleQual.html'),
(79, 'Troy District', 'http://www2.usfirst.org/2013comp/Events/MITRY/ScheduleQual.html'),
(80, 'Bedford District', 'http://www2.usfirst.org/2013comp/Events/MIBED/ScheduleQual.html'),
(81, 'Bridgewater-Raritan District', 'http://www2.usfirst.org/2013comp/Events/NJBRG/ScheduleQual.html'),
(82, 'Championship - Archimedes', 'http://www2.usfirst.org/2012comp/Events/Archimedes/ScheduleQual.html'),
(83, 'Championship - Curie', 'http://www2.usfirst.org/2012comp/Events/Curie/ScheduleQual.html'),
(84, 'Championship - Galileo', 'http://www2.usfirst.org/2012comp/Events/Galileo/ScheduleQual.html'),
(85, 'Championship - Newton', 'http://www2.usfirst.org/2012comp/Events/Newton/ScheduleQual.html');

CREATE TABLE IF NOT EXISTS `fact_match_data` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `event_id` int(5) unsigned NOT NULL,
  `match_id` int(3) unsigned NOT NULL,
  `team_id` int(5) unsigned NOT NULL,
  `auto_score_high` int(3) unsigned NOT NULL,
  `auto_score_high_attempt` int(3) unsigned NOT NULL,
  `auto_score_mid_left` int(3) unsigned NOT NULL,
  `auto_score_mid_left_attempt` int(3) unsigned NOT NULL,
  `auto_score_mid_right` int(3) unsigned NOT NULL,
  `auto_score_mid_right_attempt` int(3) unsigned NOT NULL,
  `auto_score_low` int(3) unsigned NOT NULL,
  `auto_score_low_attempt` int(3) unsigned NOT NULL,
  `score_high` int(3) unsigned NOT NULL,
  `score_high_attempt` int(3) unsigned NOT NULL,
  `score_mid_left` int(3) unsigned NOT NULL,
  `score_mid_left_attempt` int(3) unsigned NOT NULL,
  `score_mid_right` int(3) unsigned NOT NULL,
  `score_mid_right_attempt` int(3) unsigned NOT NULL,
  `score_low` int(3) unsigned NOT NULL,
  `score_low_attempt` int(3) unsigned NOT NULL,
  `score_pyramid` int(3) unsigned NOT NULL,
  `score_pyramid_attempt` int(3) unsigned NOT NULL,
  `pyramid_level_climbed` int(3) unsigned NOT NULL,
  `pyramid_attempted` int(3) unsigned NOT NULL,
  `penalty` tinyint(1) unsigned NOT NULL,
  `mpenalty` tinyint(1) unsigned NOT NULL,
  `tip_over` tinyint(1) unsigned NOT NULL,
  `yellow_card` tinyint(1) unsigned NOT NULL,
  `red_card` tinyint(1) unsigned NOT NULL,
  `notes` varchar(1024) COLLATE latin1_general_cs NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `event_index` (`event_id`),
  KEY `team_index` (`team_id`),
  KEY `match_id` (`match_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs AUTO_INCREMENT=0 ;



CREATE TABLE IF NOT EXISTS `notes_options` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `option_text` text COLLATE latin1_general_cs NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs AUTO_INCREMENT=4 ;

INSERT INTO `notes_options` (`id`, `option_text`) VALUES
(1, 'No Show'),
(2, 'Non-functional'),
(3, 'Defender');



CREATE TABLE IF NOT EXISTS `robot_lu` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `team_id` int(5) unsigned NOT NULL,
  `robot_photo` text COLLATE latin1_general_cs NOT NULL,
  PRIMARY KEY (`id`),
  KEY `team_id` (`team_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;



CREATE TABLE IF NOT EXISTS `scout_pit_data` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `team_id` int(10) unsigned NOT NULL,
  `configuration_id` int(10) unsigned NOT NULL,
  `wheel_type_id` int(10) unsigned NOT NULL,
  `wheel_base_id` int(10) unsigned NOT NULL,
  `autonomous_mode` tinyint(1) NOT NULL,
  `scout_comments` text COLLATE latin1_general_cs NOT NULL,
  `pyramid_climb_level` int(3) NOT NULL,
  `score_high` tinyint(1) NOT NULL,
  `score_mid` tinyint(1) NOT NULL,
  `score_low` tinyint(1) NOT NULL,
  `score_pyramid` tinyint(1) NOT NULL,
  `load_floor` tinyint(1) NOT NULL,
  `load_player` tinyint(1) NOT NULL,
  `load_preload` int(3) NOT NULL,
  `disc_carry` int(3) NOT NULL,
  `max_height` int(10) unsigned NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs AUTO_INCREMENT=0 ;


CREATE TABLE IF NOT EXISTS `wheel_base_lu` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `wheel_base_desc` text COLLATE latin1_general_cs NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs AUTO_INCREMENT=9 ;

INSERT INTO `wheel_base_lu` (`id`, `wheel_base_desc`) VALUES
(1, '2 Wheel Drive'),
(2, '4 Wheel Drive'),
(3, '6 Wheel Drive'),
(5, 'Crab Drive'),
(6, 'Swerve Drive'),
(7, 'Tank Drive'),
(8, 'Other'),
(4, '8 Wheel Drive (or more)');

CREATE TABLE IF NOT EXISTS `wheel_type_lu` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `wheel_type_desc` text COLLATE latin1_general_cs NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs AUTO_INCREMENT=11 ;

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
