/**
 * @file
 * @ingroup hellomal2
 * @copyright ESO - European Southern Observatory
 * @author
 *
 * @brief  ExternalCmds Interface implementation header file.
 */
#ifndef HELLOMAL2_EXTERNALCMDS_IMPL_HPP_
#define HELLOMAL2_EXTERNALCMDS_IMPL_HPP_

#include "externalCmds.rad.hpp"	
#include <hellomal2/logger.hpp>

#include <rad/exceptions.hpp>
#include <rad/smAdapter.hpp>

namespace hellomal2 {

class ExternalCmdsImpl : public externalif2::AsyncExternalCmds {
public:
    explicit ExternalCmdsImpl(rad::SMAdapter& sm) : m_sm(sm) { RAD_TRACE(GetLogger()); }

    virtual ~ExternalCmdsImpl() { RAD_TRACE(GetLogger()); }
    
    virtual elt::mal::future<std::string> Preset(
        const std::shared_ptr<externalif2::PresetParams>& mal_param) override {
    	RAD_TRACE(GetLogger());
    	auto ev = std::make_shared<ExternalCmds::Preset>(mal_param->clone());	
    	m_sm.PostEvent(ev);
    	return ev->GetPayload().GetReplyFuture();
    }
    
    virtual elt::mal::future<std::string> Setup(
        const std::shared_ptr<externalif2::SetupParams>& mal_param) override {
    	RAD_TRACE(GetLogger());
    	auto ev = std::make_shared<ExternalCmds::Setup>(mal_param->clone());	
    	m_sm.PostEvent(ev);
    	return ev->GetPayload().GetReplyFuture();
    }
    

private:
	rad::SMAdapter& m_sm;
};

}  // namespace hellomal2

#endif  // HELLOMAL2_EXTERNALCMDS_IMPL_HPP_
