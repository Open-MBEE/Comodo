/**
 * @file
 * @ingroup hellomal2
 * @copyright ESO - European Southern Observatory
 * @author
 *
 * @brief ActivityPreset class source file.
 */

#include <hellomal2/activityPreset.hpp>
#include <hellomal2/dataContext.hpp>
#include <hellomal2/dbInterface.hpp>

#include <events.rad.hpp>

#include <rad/assert.hpp>
#include <rad/exceptions.hpp>
#include <rad/mal/publisher.hpp>

#include <chrono>

namespace hellomal2 {

ActivityPreset::ActivityPreset(const std::string& id, rad::SMAdapter& sm, DataContext& data)
    : rad::ThreadActivity(id), m_sm(sm), m_data(data) {
    RAD_TRACE(GetLogger());
}

ActivityPreset::~ActivityPreset() { RAD_TRACE(GetLogger()); }

void ActivityPreset::Run() {
    /*
     * Inside the thread we use dedicated logger to be able
     * to enable/disable logs independently from the main thread.
     */
    RAD_TRACE(m_logger);

    using namespace std::chrono_literals;
    while (IsStopRequested() == false) {
        LOG4CPLUS_DEBUG(m_logger, "ActivityPreset is working... ");
        std::this_thread::sleep_for(1s);
    }
}

}  // namespace hellomal2
