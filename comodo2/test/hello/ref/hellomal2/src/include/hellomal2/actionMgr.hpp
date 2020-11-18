/**
 * @file
 * @ingroup hellomal2
 * @copyright ESO - European Southern Observatory
 * @author
 *
 * @brief ActionMgr class header file.
 */
#ifndef HELLOMAL2_ACTIONMGR_HPP_
#define HELLOMAL2_ACTIONMGR_HPP_

#include <rad/actionMgr.hpp>
#include <rad/smAdapter.hpp>

#include <boost/asio.hpp>

namespace hellomal2 {

class DataContext;

/**
 * This class is responsible for the life-cycle management of
 * actions and activities.
 */
class ActionMgr : public rad::ActionMgr {
   public:
    /**
     * Default constructor.
     */
    ActionMgr();

    /**
     * Default destructor.
     */
    virtual ~ActionMgr();

    /**
     * Method to instantiate the action objects.
     *
     * @param[in] ios Event loop.
     * @param[in] sm SM adapter used to inject internal events.
     * @param[in] the_data Data shared within the application among actions and activities.
     */
    void CreateActions(boost::asio::io_service& ios, rad::SMAdapter& sm, DataContext& the_data);

    /**
     * Method to instantiate activity objects.
     *
     * @param[in] the_data Data shared within the application.
     * @param[in] sm Reference to the State Machine adapter needed to trigger internal events.
     */
    void CreateActivities(rad::SMAdapter& sm, DataContext& the_data);

    ActionMgr(const ActionMgr&) = delete;             //! Disable copy constructor
    ActionMgr& operator=(const ActionMgr&) = delete;  //! Disable assignment operator
};

}  // namespace hellomal2

#endif  // HELLOMAL2_ACTIONMGR_HPP_
