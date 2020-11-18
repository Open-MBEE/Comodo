/**
 * @file
 * @ingroup hellomal
 * @copyright ESO - European Southern Observatory
 * @author
 *
 * @brief Logger header file.
 */
#ifndef HELLOMAL_LOGGER_HPP_
#define HELLOMAL_LOGGER_HPP_

#include <rad/logger.hpp>

namespace hellomal {

const std::string LOGGER_NAME = "hellomal";

log4cplus::Logger& GetLogger();

}  // namespace hellomal

#endif  // HELLOMAL_LOGGER_HPP_
