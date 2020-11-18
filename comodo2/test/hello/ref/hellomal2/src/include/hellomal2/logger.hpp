/**
 * @file
 * @ingroup hellomal2
 * @copyright ESO - European Southern Observatory
 * @author
 *
 * @brief Logger header file.
 */
#ifndef HELLOMAL2_LOGGER_HPP_
#define HELLOMAL2_LOGGER_HPP_

#include <rad/logger.hpp>

namespace hellomal2 {

const std::string LOGGER_NAME = "hellomal2";

log4cplus::Logger& GetLogger();

}  // namespace hellomal2

#endif  // HELLOMAL2_LOGGER_HPP_
