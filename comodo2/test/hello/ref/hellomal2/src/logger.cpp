/**
 * @file
 * @ingroup hellomal2
 * @copyright ESO - European Southern Observatory
 * @author
 *
 * @brief Logger source file.
 */

#include <hellomal2/logger.hpp>

namespace hellomal2 {

log4cplus::Logger& GetLogger() {
    static log4cplus::Logger logger = log4cplus::Logger::getInstance(LOGGER_NAME);
    return logger;
}

}  // namespace hellomal2
