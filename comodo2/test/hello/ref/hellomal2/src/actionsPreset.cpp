/**
 * @file
 * @ingroup hellomal2
 * @copyright ESO - European Southern Observatory
 * @author
 *
 * @brief ActionsPreset class source file.
 */

#include <hellomal2/actionsPreset.hpp>
#include <hellomal2/dataContext.hpp>
#include <hellomal2/logger.hpp>

namespace hellomal2 {

ActionsPreset::ActionsPreset(boost::asio::io_service& ios, rad::SMAdapter& sm, DataContext& data)
    : rad::ActionGroup("ActionsPreset"),
      m_io_service(ios),
      m_sm(sm),
      m_data(data) {
    RAD_TRACE(GetLogger());
}

void ActionsPreset::Start(scxml4cpp::Context* c) {
    RAD_TRACE(GetLogger());
}


bool ActionsPreset::VerifyConditions(scxml4cpp::Context* c) {
    RAD_TRACE(GetLogger());
    return false;
}


}  // namespace hellomal2
