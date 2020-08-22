/**
 * @file
 * @ingroup hellomal2
 * @copyright ESO - European Southern Observatory
 * @author
 *
 * @brief StdCmdsImpl class header file.
 */
#ifndef HELLOMAL2_STDCMDSIMPL_HPP_
#define HELLOMAL2_STDCMDSIMPL_HPP_

#include "stdCmds.rad.hpp"	
#include <hellomal2/logger.hpp>

#include <rad/exceptions.hpp>
#include <rad/smAdapter.hpp>

namespace hellomal2 {

/**
 * This class implements the CII/MAL interface.
 */
class StdCmdsImpl : public hellomalif2::AsyncStdCmds {
 public:
    explicit StdCmdsImpl(rad::SMAdapter& sm) : m_sm(sm) { RAD_TRACE(GetLogger()); }

    virtual ~StdCmdsImpl() { RAD_TRACE(GetLogger()); }

    virtual elt::mal::future<std::string> Disable() override {
        RAD_TRACE(GetLogger());
        auto ev = std::make_shared<StdCmds::Disable>();
        m_sm.PostEvent(ev);
        return ev->GetPayload().GetReplyFuture();
    }

    virtual elt::mal::future<std::string> Enable() override {
        RAD_TRACE(GetLogger());
        auto ev = std::make_shared<StdCmds::Enable>();
        m_sm.PostEvent(ev);
        return ev->GetPayload().GetReplyFuture();
    }

    virtual elt::mal::future<std::string> Exit() override {
        RAD_TRACE(GetLogger());
        auto ev = std::make_shared<StdCmds::Exit>();
        m_sm.PostEvent(ev);
        return ev->GetPayload().GetReplyFuture();
    }

    virtual elt::mal::future<std::string> GetState() override {
        RAD_TRACE(GetLogger());
        auto ev = std::make_shared<StdCmds::GetState>();
        m_sm.PostEvent(ev);
        return ev->GetPayload().GetReplyFuture();
    }

    virtual elt::mal::future<std::string> Init() override {
        RAD_TRACE(GetLogger());
        auto ev = std::make_shared<StdCmds::Init>();
        m_sm.PostEvent(ev);
        return ev->GetPayload().GetReplyFuture();
    }

    virtual elt::mal::future<std::string> Reset() override {
        RAD_TRACE(GetLogger());
        auto ev = std::make_shared<StdCmds::Reset>();
        m_sm.PostEvent(ev);
        return ev->GetPayload().GetReplyFuture();
    }

    virtual elt::mal::future<std::string> SetLogLevel(
        const std::shared_ptr<hellomalif2::LogInfo>& mal_param) override {
        RAD_TRACE(GetLogger());
        auto ev = std::make_shared<StdCmds::SetLogLevel>(mal_param->clone());
        m_sm.PostEvent(ev);
        return ev->GetPayload().GetReplyFuture();
    }

    virtual elt::mal::future<std::string> Stop() override {
        RAD_TRACE(GetLogger());
        auto ev = std::make_shared<StdCmds::Stop>();
        m_sm.PostEvent(ev);
        return ev->GetPayload().GetReplyFuture();
    }
   
 private:
	rad::SMAdapter& m_sm;
};

}  // namespace hellomal2

#endif  // HELLOMAL2_STDCMDSIMPL_HPP_
