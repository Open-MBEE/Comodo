/**
 * @file
 * @ingroup hellomal2
 * @copyright ESO - European Southern Observatory
 * @author
 *
 * @brief DbInterface class header file.
 */
#ifndef HELLOMAL2_DBINTERFACE_HPP_
#define HELLOMAL2_DBINTERFACE_HPP_

#include <rad/dbAdapter.hpp>

#include <string>

namespace hellomal2 {

const std::string KEY_CONTROL_STATE = "ctr.state";

const std::string KEY_CONFIG_REQ_ENDPOINT = "cfg.req.endpoint";
const std::string KEY_CONFIG_DB_ENDPOINT = "cfg.db.endpoint";
const std::string KEY_CONFIG_DB_TIMEOUT_SEC = "cfg.db.timeout.sec";
const std::string KEY_CONFIG_SM_SCXML = "cfg.sm.scxml";
const std::string KEY_CONFIG_FILENAME = "cfg.filename";
const std::string KEY_CONFIG_LOG_LEVEL = "cfg.log.level";
const std::string KEY_CONFIG_LOG_PROPERTIES = "cfg.log.properties";

class Config;


/**
 * This class is the interface to the in-memory DB.
 */
class DbInterface {
 public:
    /**
     * Constructor.
     *
     * @param[in] prefix String used as prefix when building the keys.
     * @param[in] runtime_db Reference to the in-memory DB adapter.
     */
    DbInterface(const std::string& prefix, rad::DbAdapter& runtime_db);

    /**
     * Destructor
     */
    virtual ~DbInterface();

    /**
     * @return The current state of the application stored in the DB.
     */
    std::string GetControlState();

    /**
     * @param[in] key Key in the DB.
     * @return The value stored in the DB associated to the given key.
     */
    std::string Get(const std::string& key);

    /**
     * @param[in] value State to be stored in the DB.
     */
    void SetControlState(const std::string& value);

    /**
     * Set the application configuration information in the DB.
     *
     * @param[in] cfg Application configuration.
     */
    void SetConfig(Config& cfg);

    /**
     * @param[in] key Key to be written in the DB.
     * @param[in] value Value, associated to the given key, to be written in the DB.
     */
    void Set(const std::string& key, const std::string& value);

    DbInterface(const DbInterface&) = delete;       //! Disable copy constructor
    DbInterface& operator=(const DbInterface&) = delete;  //! Disable assignment operator

 private:
    std::string m_prefix;
    rad::DbAdapter& m_runtime_db;
};

}  // namespace hellomal2

#endif  // HELLOMAL2_DBINTERFACE_HPP_
