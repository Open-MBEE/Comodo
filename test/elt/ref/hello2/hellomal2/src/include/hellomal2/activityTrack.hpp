/**
 * @file
 * @ingroup hellomal2
 * @copyright ESO - European Southern Observatory
 * @author
 *
 * @brief ActivityTrack class header file.
 */
#ifndef HELLOMAL2_ACTIVITYTRACK_HPP_
#define HELLOMAL2_ACTIVITYTRACK_HPP_

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
class ActivityTrack : public rad::ThreadActivity {
   public:
    /**
     * Constructor.
     *
     * @param[in] id Name of the activity.
     * @param[in] sm State Machine facade.
     * @param[in] data Data shared within the application among actions and activities.
     */
    ActivityTrack(const std::string& id, rad::SMAdapter& sm, DataContext& data);
    virtual ~ActivityTrack();

    /**
     * Thread implementation method.
     */
    void Run() override;

    ActivityTrack(const ActivityTrack&) = delete;             //! Disable copy constructor
    ActivityTrack& operator=(const ActivityTrack&) = delete;  //! Disable assignment operator

   private:
    log4cplus::Logger m_logger = log4cplus::Logger::getInstance(LOGGER_NAME + ".ActivityTrack");
    rad::SMAdapter& m_sm;
    DataContext& m_data;
};

}  // namespace hellomal2

#endif  // HELLOMAL2_ACTIVITYTRACK_HPP_
