/**
 * @file
 * @ingroup hellomal2
 * @copyright ESO - European Southern Observatory
 * @author
 *
 * @brief DataContext class source file.
 */

#include <hellomal2/dataContext.hpp>
#include <hellomal2/logger.hpp>

#include <rad/assert.hpp>
#include <rad/exceptions.hpp>

namespace hellomal2 {

DataContext::DataContext(Config& config, rad::DbAdapter& db_adapter)
: m_config(config),
  m_runtime_db(db_adapter),
  m_db_interface(m_config.GetProcName(), m_runtime_db) {
    RAD_TRACE(GetLogger());
    UpdateDb();
}

DataContext::~DataContext() {
    RAD_TRACE(GetLogger());
}

void DataContext::ReloadConfig() {
    RAD_TRACE(GetLogger());

    m_config.LoadConfig(m_config.GetConfigFilename());
    UpdateDb();
}

void DataContext::UpdateDb() {
    RAD_TRACE(GetLogger());

    try {
        m_runtime_db.Disconnect();
        m_runtime_db.Config(m_config.GetDbEndpoint(), m_config.GetDbTimeout());
        m_runtime_db.Connect();
        RAD_ASSERT(m_runtime_db.IsConnected());
        m_db_interface.SetConfig(m_config);
    } catch (rad::RuntimeDbException& e) {
        /*
         * Log error but do not terminate, DB could be temporarily unavailable.
         */
        LOG4CPLUS_ERROR(GetLogger(), e.what());
    }
}

DbInterface& DataContext::GetDbInterface() {
    RAD_TRACE(GetLogger());
    return m_db_interface;
}

}  // namespace hellomal2
