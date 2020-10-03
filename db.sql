-- phpMyAdmin SQL Dump
-- version 4.5.4.1deb2ubuntu2.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Oct 03, 2020 at 01:22 PM
-- Server version: 5.7.31-0ubuntu0.16.04.1-log
-- PHP Version: 7.0.33-0ubuntu0.16.04.15

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `mysql193359`
--

-- --------------------------------------------------------

--
-- Table structure for table `equipment`
--

CREATE TABLE `equipment` (
  `tool` int(11) NOT NULL,
  `purchase_date` date DEFAULT NULL,
  `decomissioned_date` date DEFAULT NULL,
  `purchase_price` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `fz`
--

CREATE TABLE `fz` (
  `milling_profile_entry` int(11) NOT NULL,
  `diameter` double NOT NULL,
  `fz` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `fz`
--

INSERT INTO `fz` (`milling_profile_entry`, `diameter`, `fz`) VALUES
(1, 3, 0.02),
(2, 3, 0.022),
(4, 3, 0.021),
(5, 3, 0.017),
(6, 3, 0.019),
(7, 3, 0.018),
(1, 4, 0.026),
(2, 4, 0.03),
(4, 4, 0.029),
(5, 4, 0.022),
(6, 4, 0.025),
(7, 4, 0.024),
(1, 5, 0.04),
(1, 6, 0.039),
(2, 6, 0.045),
(4, 6, 0.043),
(5, 6, 0.033),
(6, 6, 0.038),
(7, 6, 0.036),
(1, 7, 0.045),
(1, 8, 0.052),
(2, 8, 0.06),
(4, 8, 0.057),
(5, 8, 0.044),
(6, 8, 0.051),
(7, 8, 0.048),
(1, 10, 0.08),
(2, 10, 0.092),
(4, 10, 0.088),
(5, 10, 0.06),
(6, 10, 0.069),
(7, 10, 0.066),
(1, 12, 0.096),
(2, 12, 0.11),
(4, 12, 0.106),
(5, 12, 0.072),
(6, 12, 0.083),
(7, 12, 0.079),
(1, 16, 0.13),
(2, 16, 0.15),
(4, 16, 0.14),
(5, 16, 0.1),
(6, 16, 0.11),
(7, 16, 0.11),
(1, 20, 0.16),
(2, 20, 0.18),
(4, 20, 0.18),
(5, 20, 0.12),
(6, 20, 0.14),
(7, 20, 0.13),
(1, 25, 0.2),
(2, 25, 0.23),
(4, 25, 0.22),
(5, 25, 0.15),
(6, 25, 0.17),
(7, 25, 0.17);

-- --------------------------------------------------------

--
-- Table structure for table `material`
--

CREATE TABLE `material` (
  `id` int(11) NOT NULL,
  `name` text NOT NULL,
  `hardness` text NOT NULL,
  `color` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `material`
--

INSERT INTO `material` (`id`, `name`, `hardness`, `color`) VALUES
(1, 'Aluminium-cast alloys', '>= 7% Si', 'green'),
(2, 'Aluminium, Al-wrought alloys, Al-alloys', '<= 7% Si', 'green');

-- --------------------------------------------------------

--
-- Table structure for table `milling_profile`
--

CREATE TABLE `milling_profile` (
  `id` int(11) NOT NULL,
  `name` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `milling_profile`
--

INSERT INTO `milling_profile` (`id`, `name`) VALUES
(1, 'RF 100 U, F, VA, A, Ti, H for stable conditions'),
(2, 'RF 100 U/HF, VA/NF, A/WF, RS 100 U/F for unstable conditions');

-- --------------------------------------------------------

--
-- Table structure for table `milling_profile_entry`
--

CREATE TABLE `milling_profile_entry` (
  `id` int(11) NOT NULL,
  `milling_profile` int(11) NOT NULL,
  `material` int(11) NOT NULL,
  `operation` int(11) NOT NULL,
  `ae_max` double DEFAULT NULL,
  `ap_max` double DEFAULT NULL,
  `vc` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `milling_profile_entry`
--

INSERT INTO `milling_profile_entry` (`id`, `milling_profile`, `material`, `operation`, `ae_max`, `ap_max`, `vc`) VALUES
(1, 1, 2, 1, 1.1, 0, 500),
(2, 1, 2, 4, 0.75, 0, 600),
(4, 1, 2, 2, 0.02, 0, 1000),
(5, 1, 1, 1, 1, 0, 230),
(6, 1, 1, 4, 0.75, 0, 300),
(7, 1, 1, 2, 0.02, 0, 460);

-- --------------------------------------------------------

--
-- Table structure for table `operation`
--

CREATE TABLE `operation` (
  `id` int(11) NOT NULL,
  `name` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `operation`
--

INSERT INTO `operation` (`id`, `name`) VALUES
(1, 'Slotting'),
(2, 'Finishing'),
(3, 'Fine Finishing'),
(4, 'Roughing');

-- --------------------------------------------------------

--
-- Table structure for table `tool`
--

CREATE TABLE `tool` (
  `id` int(11) NOT NULL,
  `milling_profile` int(11) NOT NULL,
  `tool_type` int(11) NOT NULL,
  `product_id` text NOT NULL,
  `vendor` int(11) NOT NULL,
  `product_link` text,
  `description` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `tool_type`
--

CREATE TABLE `tool_type` (
  `id` int(11) NOT NULL,
  `name` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `vendor`
--

CREATE TABLE `vendor` (
  `id` int(11) NOT NULL,
  `name` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `vendor`
--

INSERT INTO `vendor` (`id`, `name`) VALUES
(1, 'Guhring'),
(2, 'Sorotec');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `equipment`
--
ALTER TABLE `equipment`
  ADD PRIMARY KEY (`tool`);

--
-- Indexes for table `fz`
--
ALTER TABLE `fz`
  ADD PRIMARY KEY (`diameter`,`milling_profile_entry`);

--
-- Indexes for table `material`
--
ALTER TABLE `material`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `milling_profile`
--
ALTER TABLE `milling_profile`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `milling_profile_entry`
--
ALTER TABLE `milling_profile_entry`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `milling_profile_2` (`milling_profile`,`material`,`operation`),
  ADD KEY `milling_profile_operation` (`operation`),
  ADD KEY `milling_profile_material` (`material`),
  ADD KEY `milling_profile` (`milling_profile`);

--
-- Indexes for table `operation`
--
ALTER TABLE `operation`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tool`
--
ALTER TABLE `tool`
  ADD PRIMARY KEY (`id`),
  ADD KEY `tool_type` (`tool_type`),
  ADD KEY `vendor` (`vendor`),
  ADD KEY `milling_profile` (`milling_profile`);

--
-- Indexes for table `tool_type`
--
ALTER TABLE `tool_type`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `vendor`
--
ALTER TABLE `vendor`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `equipment`
--
ALTER TABLE `equipment`
  MODIFY `tool` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `material`
--
ALTER TABLE `material`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `milling_profile`
--
ALTER TABLE `milling_profile`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `milling_profile_entry`
--
ALTER TABLE `milling_profile_entry`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;
--
-- AUTO_INCREMENT for table `operation`
--
ALTER TABLE `operation`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT for table `tool`
--
ALTER TABLE `tool`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `tool_type`
--
ALTER TABLE `tool_type`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `vendor`
--
ALTER TABLE `vendor`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
