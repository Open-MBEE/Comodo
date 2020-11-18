/**
 * @file
 * @ingroup hellomal
 * @copyright ESO - European Southern Observatory
 * @author
 *
 * @brief ActionsStd class source file.
 */

#include <hellomal/actionsStd.hpp>
#include <hellomal/dataContext.hpp>
#include <hellomal/logger.hpp>
#include <events.rad.hpp>
#include <stdCmds.rad.hpp>

#include <rad/exceptions.hpp>
#include <rad/getPayload.hpp>

namespace hellomal {

ActionsStd::ActionsStd(boost::asio::io_service& ios,
					   rad::SMAdapter& sm,
                       DataContext& data)
                : rad::ActionGroup("ActionsStd"),
                  m_io_service(ios),
				  m_sm(sm),
                  m_signal(ios, sm,
                  rad::UniqueEvent(new Events::CtrlC())),
                  m_data(data) {
    RAD_TRACE(GetLogger());

    m_signal.Add(SIGINT);
    m_signal.Add(SIGTERM);
    m_signal.Install();
}

void ActionsStd::Exit(scxml4cpp::Context* c) {
    RAD_TRACE(GetLogger());

    auto req = rad::GetLastEventPayloadNothrow< StdCmds::Exit > (c);
    if (req == nullptr) {
        LOG4CPLUS_ERROR(GetLogger(), "Exit event has no associated request!");
        return;
    }
    req->SetReplyValue("OK");
    m_io_service.stop();
}

void ActionsStd::ExitNoReply(scxml4cpp::Context* c) {
    RAD_TRACE(GetLogger());
    m_io_service.stop();
}

void ActionsStd::GetState(scxml4cpp::Context* c) {
    RAD_TRACE(GetLogger());

    auto req = rad::GetLastEventPayloadNothrow< StdCmds::GetState > (c);
    if (req == nullptr) {
        LOG4CPLUS_ERROR(GetLogger(), "Status event has no associated request!");
        return;
    }
    req->SetReplyValue(m_sm.GetStatus());
}

void ActionsStd::Stop(scxml4cpp::Context* c) {
    RAD_TRACE(GetLogger());

    auto req = rad::GetLastEventPayloadNothrow< StdCmds::Stop > (c);
    if (req == nullptr) {
        LOG4CPLUS_ERROR(GetLogger(), "Stop event has no associated request!");
        return;
    }
    req->SetReplyValue("OK");
}

void ActionsStd::Init(scxml4cpp::Context* c) {
    RAD_TRACE(GetLogger());

    auto req = rad::GetLastEventPayloadNothrow< StdCmds::Init > (c);
    if (req == nullptr) {
        LOG4CPLUS_ERROR(GetLogger(), "Init event has no associated request!");
        return;
    }
    req->SetReplyValue("OK");
}

void ActionsStd::Enable(scxml4cpp::Context* c) {
    RAD_TRACE(GetLogger());

    auto req = rad::GetLastEventPayloadNothrow< StdCmds::Enable > (c);
    if (req == nullptr) {
        LOG4CPLUS_ERROR(GetLogger(), "Enable event has no associated request!");
        return;
    }
    req->SetReplyValue("OK");
}

void ActionsStd::Disable(scxml4cpp::Context* c) {
    RAD_TRACE(GetLogger());

    auto req = rad::GetLastEventPayloadNothrow< StdCmds::Disable > (c);
    if (req == nullptr) {
        LOG4CPLUS_ERROR(GetLogger(), "Disable event has no associated request!");
        return;
    }
    req->SetReplyValue("OK");
}

void ActionsStd::Reset(scxml4cpp::Context* c) {
    RAD_TRACE(GetLogger());

    auto req = rad::GetLastEventPayloadNothrow< StdCmds::Reset > (c);
    if (req == nullptr) {
        LOG4CPLUS_ERROR(GetLogger(), "Reset event has no associated request!");
        return;
    }
    req->SetReplyValue("OK");
}

void ActionsStd::SetLogLevel(scxml4cpp::Context* c) {
    RAD_TRACE(GetLogger());

    auto req = rad::GetLastEventPayloadNothrow< StdCmds::SetLogLevel > (c);
    if (req == nullptr) {
        LOG4CPLUS_ERROR(GetLogger(), "SetLogLevel event has no associated request!");
        return;
    }

    auto req_params = req->GetRequestPayload();
    std::string level = req_params->getLevel();
    std::string logger_name = req_params->getLogger();

    log4cplus::LogLevelManager& log_mgr = log4cplus::getLogLevelManager();
	//LOG4CPLUS_DEBUG(GetLogger(), "Log level" << level);
	log4cplus::LogLevel ll = log_mgr.fromString(level);
	if (ll == log4cplus::NOT_SET_LOG_LEVEL) {
		req->SetReplyValue("ERR unknown logging level: " + level);
		return;
	}

	if (logger_name == "" || logger_name == LOGGER_NAME) {
		GetLogger().setLogLevel(ll);
	} else {
		log4cplus::Logger::getInstance(logger_name).setLogLevel(ll);
	}
	LOG4CPLUS_DEBUG(GetLogger(), "Log level set to " << level << " for logger " << logger_name);

	req->SetReplyValue("OK");
}

}  // namespace hellomal
