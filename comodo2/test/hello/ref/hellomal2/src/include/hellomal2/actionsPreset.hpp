/**
 * @file
 * @ingroup hellomal2
 * @copyright ESO - European Southern Observatory
 * @author
 *
 * @brief ActionsPreset class header file.
 */
#ifndef HELLOMAL2_ACTIONSPRESET_HPP_
#define HELLOMAL2_ACTIONSPRESET_HPP_

#include <rad/actionGroup.hpp>
#include <rad/smAdapter.hpp>

#include <string>

namespace hellomal2 {

class DataContext;

/**
 * This class implements the action methods related
 * to ActionsPreset.
 */
class ActionsPreset : public rad::ActionGroup {
   public:
    /**
     * Constructor.
     *
     * @param[in] ios Reference to the event loop.
     * @param[in] sm State Machine facade.
     * @param[in] data Data shared within the application among actions and activities.
     */
    ActionsPreset(boost::asio::io_service& ios, rad::SMAdapter& sm, DataContext& data);

    /**
     * Method implementing the Start action.
     * @param[in] c Context containing the last event received by the State Machine.
     */
    void Start(scxml4cpp::Context* c);

    /**
     * Method implementing the VerifyConditions guard.
     * @param[in] c Context containing the last event received by the State Machine.
     * @return true If the guard is satisfied, false otherwise.
     */
    bool VerifyConditions(scxml4cpp::Context* c);

    ActionsPreset(const ActionsPreset&) = delete;             //! Disable copy constructor
    ActionsPreset& operator=(const ActionsPreset&) = delete;  //! Disable assignment operator

   private:
    boost::asio::io_service& m_io_service;
    rad::SMAdapter& m_sm;
    DataContext& m_data;
};

}  // namespace hellomal2

#endif  // HELLOMAL2_ACTIONSPRESET_HPP_
