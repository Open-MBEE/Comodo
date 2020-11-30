/**
 * @file
 * @ingroup hellomal
 * @copyright ESO - European Southern Observatory
 * @author
 *
 * @brief ActionMgr class source file.
 */

#include <hellomal/actionMgr.hpp>
#include <hellomal/actionsStd.hpp>

#include <hellomal/dataContext.hpp>
#include <hellomal/logger.hpp>

#include <rad/actionCallback.hpp>
#include <rad/guardCallback.hpp>
#include <rad/actionGroup.hpp>
#include <rad/assert.hpp>

#include <functional>

namespace hellomal {

ActionMgr::ActionMgr() { RAD_TRACE(GetLogger()); }

ActionMgr::~ActionMgr() { RAD_TRACE(GetLogger()); }

void ActionMgr::CreateActions(boost::asio::io_service& ios, rad::SMAdapter& sm,
                              DataContext& the_data) {
    RAD_TRACE(GetLogger());

    scxml4cpp::Action* the_action = nullptr;    
    using std::placeholders::_1;

    ActionsStd* my_actionsstd = new ActionsStd(ios, sm, the_data);
    if (my_actionsstd == nullptr) {
        LOG4CPLUS_ERROR(GetLogger(), "Cannot create ActionsStd object.");
        // @TODO throw exception
        return;
    }
    AddActionGroup(my_actionsstd);

    the_action = new rad::ActionCallback("ActionsStd.Disable",
                                         std::bind(&ActionsStd::Disable, my_actionsstd, _1));
    AddAction(the_action);

    the_action = new rad::ActionCallback("ActionsStd.Enable",
                                         std::bind(&ActionsStd::Enable, my_actionsstd, _1));
    AddAction(the_action);

    the_action = new rad::ActionCallback("ActionsStd.Exit",
                                         std::bind(&ActionsStd::Exit, my_actionsstd, _1));
    AddAction(the_action);

    the_action = new rad::ActionCallback("ActionsStd.ExitNoReply",
                                         std::bind(&ActionsStd::ExitNoReply, my_actionsstd, _1));
    AddAction(the_action);

    the_action = new rad::ActionCallback("ActionsStd.GetState",
                                         std::bind(&ActionsStd::GetState, my_actionsstd, _1));
    AddAction(the_action);

    the_action = new rad::ActionCallback("ActionsStd.Init",
                                         std::bind(&ActionsStd::Init, my_actionsstd, _1));
    AddAction(the_action);

    the_action = new rad::ActionCallback("ActionsStd.Reset",
                                         std::bind(&ActionsStd::Reset, my_actionsstd, _1));
    AddAction(the_action);

    the_action = new rad::ActionCallback("ActionsStd.SetLogLevel",
                                         std::bind(&ActionsStd::SetLogLevel, my_actionsstd, _1));
    AddAction(the_action);

    the_action = new rad::ActionCallback("ActionsStd.Stop",
                                         std::bind(&ActionsStd::Stop, my_actionsstd, _1));
    AddAction(the_action);

}

void ActionMgr::CreateActivities(rad::SMAdapter& sm, DataContext& the_data) {
    RAD_TRACE(GetLogger());
}

}  // namespace hellomal
