/**
 * @file
 * @ingroup hellomal2
 * @copyright ESO - European Southern Observatory
 * @author
 *
 * @brief DbInterface class source file.
 */

#include <hellomal2/dbInterface.hpp>
#include <hellomal2/config.hpp>
#include <hellomal2/logger.hpp>

#include <rad/assert.hpp>
#include <rad/exceptions.hpp>

namespace hellomal2 {

DbInterface::DbInterface(const std::string& prefix, rad::DbAdapter& runtime_db)
                : m_prefix(prefix),
                  m_runtime_db(runtime_db) {
    RAD_TRACE(GetLogger());

    if (prefix.size() > 0) {
        m_prefix = prefix + ".";
    }
}

DbInterface::~DbInterface() {
    RAD_TRACE(GetLogger());
}

std::string DbInterface::GetControlState() {
    RAD_TRACE(GetLogger());
    return m_runtime_db.Get(m_prefix + KEY_CONTROL_STATE);
}

std::string DbInterface::Get(const std::string& key) {
    RAD_TRACE(GetLogger());
    return m_runtime_db.Get(m_prefix + key);
}

void DbInterface::SetControlState(const std::string& value) {
    RAD_TRACE(GetLogger());
    m_runtime_db.Set(m_prefix + KEY_CONTROL_STATE, value);
}

void DbInterface::SetConfig(Config& cfg) {
    RAD_TRACE(GetLogger());

    // @TODO avoid copying strings
    std::vector < std::string > kvs;
    kvs.push_back(m_prefix + KEY_CONFIG_REQ_ENDPOINT);
    kvs.push_back(cfg.GetMsgReplierEndpoint());
    kvs.push_back(m_prefix + KEY_CONFIG_DB_ENDPOINT);
    kvs.push_back(cfg.GetDbEndpoint());
    kvs.push_back(m_prefix + KEY_CONFIG_DB_TIMEOUT_SEC);
    kvs.push_back(std::to_string(cfg.GetDbTimeout().tv_sec));
    kvs.push_back(m_prefix + KEY_CONFIG_SM_SCXML);
    kvs.push_back(cfg.GetSmScxmlFilename());
    kvs.push_back(m_prefix + KEY_CONFIG_FILENAME);
    kvs.push_back(cfg.GetConfigFilename());
    kvs.push_back(m_prefix + KEY_CONFIG_LOG_LEVEL);
    kvs.push_back(cfg.GetLogLevel());
    kvs.push_back(m_prefix + KEY_CONFIG_LOG_PROPERTIES);
    kvs.push_back(cfg.GetLogProperties());

    m_runtime_db.MultiSet(kvs);
}

void DbInterface::Set(const std::string& key, const std::string& value) {
    RAD_TRACE(GetLogger());
    m_runtime_db.Set(m_prefix + key, value);
}

}  // namespace hellomal2
