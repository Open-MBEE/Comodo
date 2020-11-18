/**
 * @file
 * @ingroup hellomal2
 * @copyright ESO - European Southern Observatory
 * @author
 *
 * @brief Config class source file.
 */

#include <hellomal2/config.hpp>
#include <hellomal2/dbInterface.hpp>
#include <hellomal2/logger.hpp>

#include <rad/assert.hpp>
#include <rad/exceptions.hpp>
#include <rad/helper.hpp>

#include <boost/program_options.hpp>

#include <iostream>

namespace bpo = boost::program_options;

namespace hellomal2 {

Config::Config()
: m_proc_name(CONFIG_DEFAULT_PROCNAME),
  m_log_level(CONFIG_DEFAULT_LOGLEVEL),
  m_log_properties(CONFIG_DEFAULT_LOG_PROPERTIES),
  m_config_filename(CONFIG_DEFAULT_FILENAME),
  m_scxml_filename(CONFIG_DEFAULT_SCXML_FILENAME),
  m_db_host_endpoint(CONFIG_DEFAULT_DB_ENDPOINT),
  m_db_timeout_sec(CONFIG_DEFAULT_DB_TIMEOUT_SEC),
  m_req_endpoint(CONFIG_DEFAULT_REQ_ENDPOINT) {
    RAD_TRACE(GetLogger());

    /*
     * @todo these msgs won't be displayed until the DEBUG logLevel is applied.
     */
    LOG4CPLUS_DEBUG(GetLogger(), "Default - Log level: <" << m_log_level << ">");
    LOG4CPLUS_DEBUG(GetLogger(), "Default - Log properties: <" << m_log_properties << ">");
    LOG4CPLUS_DEBUG(GetLogger(), "Default - Configuration filename: <"
                    << m_config_filename << ">");
    LOG4CPLUS_DEBUG(GetLogger(), "Default - DB host: <" << m_db_host_endpoint
                    << "> (timeout " << m_db_timeout_sec << " sec)");
    LOG4CPLUS_DEBUG(GetLogger(), "Default - Requests endpoint: <" << m_req_endpoint
                    << ">");

    /*
     * Read environment variables.
     */
    std::string db_addr = rad::Helper::GetEnvVar(CONFIG_ENVVAR_DBHOST);
    if (db_addr.size() > 0) {
        m_db_host_endpoint = db_addr;
        LOG4CPLUS_DEBUG(GetLogger(), "EnvVar - DB host: <" << m_db_host_endpoint << ">");
    }
}

Config::~Config() {
    RAD_TRACE(GetLogger());
}

bool Config::ParseOptions(int argc, char *argv[]) {
    RAD_TRACE(GetLogger());

    /*
     * Define command line options.
     */
    bpo::options_description options_desc("Options");
    options_desc.add_options()("help,h", "Print help messages")(
                    "proc-name,n", bpo::value < std::string > (&m_proc_name),
                    "Process name")(
                    "log-level,l",
                    bpo::value < std::string > (&m_log_level),
                    "Log level: ERROR, WARNING, STATE, EVENT, ACTION, INFO, DEBUG, TRACE")(
                    "config,c", bpo::value < std::string > (&m_config_filename),
                    "Configuration filename")(
                    "db-host,d",
                    bpo::value < std::string > (&m_db_host_endpoint),
                    "In-memory DB host (ipaddr:port)");

    // Parse the options.
    try {
        bpo::variables_map options_map;
        bpo::store(bpo::parse_command_line(argc, argv, options_desc),
                   options_map);
        if (options_map.count("help")) {
            std::cout << options_desc << std::endl;
            return false;
        }

        /*
         * Throws on error, so do after help in case
         * there are any problems.
         */
        bpo::notify(options_map);

        if (options_map.count("log-level")) {
        	log4cplus::LogLevelManager& log_mgr = log4cplus::getLogLevelManager();
        	log4cplus::LogLevel ll = log_mgr.fromString(m_log_level);
        	if (ll != log4cplus::NOT_SET_LOG_LEVEL) {
        		GetLogger().setLogLevel(ll);
        		LOG4CPLUS_DEBUG(GetLogger(), "CmdOpt - Log level: <" << m_log_level
                                << ">");
            } else {
                std::cout << options_desc << std::endl;
                throw rad::InvalidOptionException("Invalid log level.");
            }
        }

        if (options_map.count("proc-name") == 0) {
            m_proc_name = std::string(argv[0]);
            LOG4CPLUS_DEBUG(GetLogger(), "Default - Process name: <" << m_proc_name
                            << ">");
        } else {
            LOG4CPLUS_DEBUG(GetLogger(), "CmdOpt - Process name: <" << m_proc_name
                            << ">");
        }

        if (options_map.count("db-host")) {
            LOG4CPLUS_DEBUG(GetLogger(), "CmdOpt - DB host: <" << m_db_host_endpoint
                            << ">");
        } else {
            LOG4CPLUS_DEBUG(GetLogger(), "Default - DB host: <" << m_db_host_endpoint
                            << ">");
        }

        if (options_map.count("config")) {
            LOG4CPLUS_DEBUG(GetLogger(), "CmdOpt - Configuration filename: <"
                            << m_config_filename << ">");
        } else {
            LOG4CPLUS_DEBUG(GetLogger(), "Default - Configuration filename: <"
                            << m_config_filename << ">");
        }
    } catch (bpo::error& e) {
        std::cerr << "ERROR: " << e.what() << std::endl << std::endl;
        std::cerr << options_desc << std::endl;
        throw rad::InvalidOptionException(e.what());
    }

    return true;
}

void Config::LoadConfig(const std::string& filename) {
    RAD_TRACE(GetLogger());

    std::string config_filename = filename;
    if (config_filename == "") {
        config_filename = m_config_filename;
    }

    // resolve filename
    std::string resolved_config_filename = rad::Helper::FindFile(
                    config_filename);
    if (resolved_config_filename.size() == 0) {
        LOG4CPLUS_ERROR(GetLogger(), "Cannot find <" << config_filename << ">");
        throw rad::Exception(rad::errorMsg::CFG_LOAD, resolved_config_filename);
    }

    try {
        m_config_node = YAML::LoadFile(resolved_config_filename);

        if (m_config_node[KEY_CONFIG_LOG_PROPERTIES]) {
        	m_log_properties = m_config_node[KEY_CONFIG_LOG_PROPERTIES].as<std::string>();
            LOG4CPLUS_DEBUG(GetLogger(), "CfgFile - " << KEY_CONFIG_LOG_PROPERTIES
                    << " = <" << m_log_properties << ">");
        }

        if (m_config_node[KEY_CONFIG_REQ_ENDPOINT]) {
            m_req_endpoint = m_config_node[KEY_CONFIG_REQ_ENDPOINT]
                            .as<std::string>();
            LOG4CPLUS_DEBUG(GetLogger(), "CfgFile - " << KEY_CONFIG_REQ_ENDPOINT
                            << " = <" << m_req_endpoint << ">");
        }

        if (m_config_node[KEY_CONFIG_DB_ENDPOINT]) {
            m_db_host_endpoint = m_config_node[KEY_CONFIG_DB_ENDPOINT]
                            .as<std::string>();
            LOG4CPLUS_DEBUG(GetLogger(), "CfgFile - " << KEY_CONFIG_DB_ENDPOINT
                            << " = <" << m_db_host_endpoint << ">");
        }

        if (m_config_node[KEY_CONFIG_DB_TIMEOUT_SEC]) {
            m_db_timeout_sec =
                            m_config_node[KEY_CONFIG_DB_TIMEOUT_SEC].as<int>();
            LOG4CPLUS_DEBUG(GetLogger(), "CfgFile - " << KEY_CONFIG_DB_TIMEOUT_SEC
                            << " = <" << m_db_timeout_sec << "> sec");
        }

        if (m_config_node[KEY_CONFIG_SM_SCXML]) {
            m_scxml_filename = m_config_node[KEY_CONFIG_SM_SCXML]
                            .as<std::string>();
            LOG4CPLUS_DEBUG(GetLogger(), "CfgFile - " << KEY_CONFIG_SM_SCXML << " = <"
                            << m_scxml_filename << ">");
        }
    } catch (YAML::Exception& e) {
        throw rad::Exception(rad::errorMsg::CFG_LOAD, resolved_config_filename);
    }
    RAD_ASSERT(m_config_node.IsNull() == false);

    m_config_filename = resolved_config_filename;
    LOG4CPLUS_DEBUG(GetLogger(), "Loaded configuration file <" << m_config_filename
                    << ">");
}

const std::string& Config::GetMsgReplierEndpoint() const {
    RAD_TRACE(GetLogger());
    return m_req_endpoint;
}

const std::string& Config::GetDbEndpoint() const {
    RAD_TRACE(GetLogger());
    return m_db_host_endpoint;
}

const timeval Config::GetDbTimeout() const {
    RAD_TRACE(GetLogger());
    timeval timeout = { m_db_timeout_sec, 0 };  // default
    return timeout;
}

const std::string& Config::GetSmScxmlFilename() const {
    RAD_TRACE(GetLogger());
    return m_scxml_filename;
}

const std::string& Config::GetConfigFilename() const {
    RAD_TRACE(GetLogger());
    return m_config_filename;
}

const std::string& Config::GetProcName() const {
    RAD_TRACE(GetLogger());
    return m_proc_name;
}

const std::string& Config::GetLogLevel() const {
    RAD_TRACE(GetLogger());
    return m_log_level;
}

const std::string& Config::GetLogProperties() const {
    RAD_TRACE(GetLogger());
    return m_log_properties;
}

}  // namespace hellomal2
