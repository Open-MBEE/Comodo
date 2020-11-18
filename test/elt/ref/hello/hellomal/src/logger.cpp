/**
 * @file
 * @ingroup hellomal
 * @copyright ESO - European Southern Observatory
 * @author
 *
 * @brief Logger source file.
 */

#include <hellomal/logger.hpp>

namespace hellomal {

log4cplus::Logger& GetLogger() {
    static log4cplus::Logger logger = log4cplus::Logger::getInstance(LOGGER_NAME);
    return logger;
}

}  // namespace hellomal
