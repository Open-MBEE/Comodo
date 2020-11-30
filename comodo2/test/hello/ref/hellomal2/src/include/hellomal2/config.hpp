/**
 * @file
 * @ingroup hellomal2
 * @copyright ESO - European Southern Observatory
 * @author
 *
 * @brief Config class header file.
 */
#ifndef HELLOMAL2_CONFIG_HPP_
#define HELLOMAL2_CONFIG_HPP_

#include <yaml-cpp/yaml.h>

#include <string>

namespace hellomal2 {

/**
 * Default application configuration values.
 */
const std::string CONFIG_DEFAULT_PROCNAME       = "hellomal2";
//! no default value to force user to specify config filename since it overwrites the command line options.
const std::string CONFIG_DEFAULT_FILENAME       = "";
const std::string CONFIG_DEFAULT_SCXML_FILENAME = "hellomal2/sm.xml";
const std::string CONFIG_DEFAULT_LOGLEVEL       = "INFO";
const std::string CONFIG_DEFAULT_LOG_PROPERTIES = "hellomal2/log.properties";
const std::string CONFIG_DEFAULT_DB_ENDPOINT    = "127.0.0.1:6379";
const int CONFIG_DEFAULT_DB_TIMEOUT_SEC         = 2;
const std::string CONFIG_DEFAULT_REQ_ENDPOINT   = "zpb.rr://127.0.0.1:12081/";

/**
 * Application configuration environment variables
 */
const std::string CONFIG_ENVVAR_DBHOST          = "DB_HOST";

/**
 * This class provide access to the command line options and
 * the configuration parameters stored in the configuration file.
 */
class Config {
 public:
    /**
     * Default constructor.
     *
     * Initialize application configuration attributes by
     * - first use the default constant values defined in the header
     * - override the constant values with environment variables (if defined)
     */
    Config();

    /**
     * Default destructor.
     */
    virtual ~Config();

    /**
     * This method parses the command line parameters overriding
     * the initialization done in the constructor.
     *
     * @param[in] argc Number of command line options.
     * @param[in] argv Pointer to the array of command line options.
     * @return false if the help option has been invoked, true otherwise.
     */
    bool ParseOptions(int argc, char* argv[]);

    /**
     * This method load from a configuration file the application
     * configuration overriding the initialization done in the constructor
     * and the command line options.
     *
     * @param[in] filename Application configuration filename.
     */
    void LoadConfig(const std::string& filename = "");

    /**
     * @return The network endpoint to send request to this application.
     * The format is "<middleware>.<protocol>://<ipaddr>:<port>".
     * For example: "zpb.rr://127.0.0.1:12081/"
     */
    const std::string& GetMsgReplierEndpoint() const;

    /**
     * @return The IP address and port used to connect to the runtime DB.
     */
    const std::string& GetDbEndpoint() const;

    /**
     * @return The timeout used when communicating to the runtime DB.
     */
    const timeval GetDbTimeout() const;

    /**
     * @return The SCXML State Machine model filename used by the application.
     */
    const std::string& GetSmScxmlFilename() const;

    /**
     * @return The application configuration filename.
     */
    const std::string& GetConfigFilename() const;

    /**
     * @return The application process name.
     */
    const std::string& GetProcName() const;

    /**
     * @return The configured log level.
     */
    const std::string& GetLogLevel() const;

    /**
     * @return The log properties config filename.
     */
    const std::string& GetLogProperties() const;

    Config(const Config&) = delete;             //! Disable copy constructor
    Config& operator=(const Config&) = delete;  //! Disable assignment operator

 private:
    YAML::Node  m_config_node;
    std::string m_proc_name;
    std::string m_log_level;
    std::string m_log_properties;
    std::string m_config_filename;
    std::string m_scxml_filename;
    std::string m_db_host_endpoint;
    int         m_db_timeout_sec;
    std::string m_req_endpoint;
};

}  // namespace hellomal2

#endif  // HELLOMAL2_CONFIG_HPP_
