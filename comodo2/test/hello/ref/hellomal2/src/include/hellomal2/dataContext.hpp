/**
 * @file
 * @ingroup hellomal2
 * @copyright ESO - European Southern Observatory
 * @author
 *
 * @brief DataContext class header file.
 */
#ifndef HELLOMAL2_DATACONTEXT_HPP_
#define HELLOMAL2_DATACONTEXT_HPP_

#include <hellomal2/config.hpp>
#include <hellomal2/dbInterface.hpp>

namespace hellomal2 {

/**
 * This class provide access to the application run-time data including
 * the in-memory DB.
 */
class DataContext {
 public:
    /**
     * This constructor uses the application configuration to initialize the
     * adapter object to the in-memory DB.
     * The DB adapter is then used to initialize the DB interface object that
     * can be used to access the DB key-value pairs.
     *
     * @param[in] config Reference to the application configuration.
     */

    DataContext(Config& config, rad::DbAdapter& db_adapter);

    /**
     * Destructor
     */
    virtual ~DataContext();

    /**
     * Reload the configuration from file and reconnect to the in-memory DB.
     */
    void ReloadConfig();

    /**
     * Try to connect to the DB and update the application configuration.
     */
    void UpdateDb();

    /**
     * @return A reference to the DB interface object.
     */
    DbInterface& GetDbInterface();

    DataContext(const DataContext&) = delete;       //! Disable copy constructor
    DataContext& operator=(const DataContext&) = delete;  //! Disable assignment operator

 private:
    Config&            m_config;
    rad::DbAdapter&    m_runtime_db;
    DbInterface        m_db_interface;
};

}  // namespace hellomal2

#endif  // HELLOMAL2_DATACONTEXT_HPP_
