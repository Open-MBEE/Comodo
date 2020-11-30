/**
 * @file
 * @ingroup hellomal2
 * @copyright ESO - European Southern Observatory
 * @author
 *
 * @brief ActivityPreset class header file.
 */
#ifndef HELLOMAL2_ACTIVITYPRESET_HPP_
#define HELLOMAL2_ACTIVITYPRESET_HPP_

#include <hellomal2/logger.hpp>

#include <rad/activity.hpp>
#include <rad/smAdapter.hpp>

#include <string>

namespace hellomal2 {

class DataContext;

/**
 * This class contains the implementation of the do-activity used
 * to simulate the axes movements.
 */
class ActivityPreset : public rad::ThreadActivity {
   public:
    /**
     * Constructor.
     *
     * @param[in] id Name of the activity.
     * @param[in] sm State Machine facade.
     * @param[in] data Data shared within the application among actions and activities.
     */
    ActivityPreset(const std::string& id, rad::SMAdapter& sm, DataContext& data);
    virtual ~ActivityPreset();

    /**
     * Thread implementation method.
     */
    void Run() override;

    ActivityPreset(const ActivityPreset&) = delete;             //! Disable copy constructor
    ActivityPreset& operator=(const ActivityPreset&) = delete;  //! Disable assignment operator

   private:
    log4cplus::Logger m_logger = log4cplus::Logger::getInstance(LOGGER_NAME + ".ActivityPreset");
    rad::SMAdapter& m_sm;
    DataContext& m_data;
};

}  // namespace hellomal2

#endif  // HELLOMAL2_ACTIVITYPRESET_HPP_
