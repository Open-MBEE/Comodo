/**
 * @file
 * @ingroup hellomal
 * @copyright ESO - European Southern Observatory
 * @author
 *
 * @brief ActionsStd class header file.
 */
#ifndef HELLOMAL_ACTIONSSTD_HPP_
#define HELLOMAL_ACTIONSSTD_HPP_

#include <rad/actionGroup.hpp>
#include <rad/signal.hpp>
#include <rad/smAdapter.hpp>

#include <string>

namespace hellomal {

class DataContext;

/**
 * This class contains the implementation of the actions dealing with
 * the following "standard" commands:
 *
 * - Exit
 * - GetState
 * - Reset
 * - Stop
 * - Init
 * - Enable
 * - Disable
 *
 * and the following signals:
 *
 * - SIGINT
 * - SIGTERM
 *
 * In addition it implements the:
 * - scxml4cpp::StatusListener interface to receive the notification of change of state from the State Machine engine.
 * - rad::EventRejectListener interface to receive the notification of rejected event from the State Machine adapter.
 */
class ActionsStd : public rad::ActionGroup {
 public:
    /**
     * Constructor.
     *
     * @param[in] ios Reference to the event loop.
     * @param[in] sm Reference to the SM Adapter used to inject internal events.
     * @param[in] data Data shared within the application among actions and activities.
     */
    ActionsStd(boost::asio::io_service& ios,
    		   rad::SMAdapter& sm,
               DataContext& data);

    /**
     * Implementation of the scxml4cpp::StatusListener interface
     * to receive the change of state notification.
     *
     * @param[in] status Current State Machine active states configuration.
     */
    void notifyStatus(std::set<scxml4cpp::State*>& status);

    /**
     * Implementation of the GetState action. This action:
     * - replies back for the originator of the GetState request with the
     * current State Machine active state configuration.
     *
     * @param[in] c Context containing the last event received by the State Machine.
     */
    void GetState(scxml4cpp::Context* c);

    /**
     * Implementation of the Stop action. This action:
     * - replies back for the originator of the ReqStop request.
     *
     * @param[in] c Context containing the last event received by the State Machine.
     */
    void Stop(scxml4cpp::Context* c);

    /**
     * Implementation of the Init action. This action:
     * - re-initialize the application run-time data,
     * - replies back for the originator of the ReqInit request.
     *
     * @param[in] c Context containing the last event received by the State Machine.
     */
    void Init(scxml4cpp::Context* c);

    /**
     * Implementation of the Enable action. This action:
     * - replies back for the originator of the ReqEnable request.
     *
     * @param[in] c Context containing the last event received by the State Machine.
     */
    void Enable(scxml4cpp::Context* c);

    /**
     * Implementation of the Disable action. This action:
     * - replies back for the originator of the ReqDisable request.
     *
     * @param[in] c Context containing the last event received by the State Machine.
     */
    void Disable(scxml4cpp::Context* c);

    /**
     * Implementation of the Reset action. This action:
     * - replies back for the originator of the ReqReset request.
     *
     * @param[in] c Context containing the last event received by the State Machine.
     */
    void Reset(scxml4cpp::Context* c);

    /**
     * Implementation of the SetLogLevel action. This action:
     * - changes the log level,
     * - replies back for the originator of the ReqSetLogLevel request.
     *
     * @param[in] c Context containing the last event received by the State Machine.
     */
    void SetLogLevel(scxml4cpp::Context* c);

    /**
     * Implementation of the Exit action. This action:
     * - replies back for the originator of the ReqExit request.
     * - stops the event loop.
     *
     * @param[in] c Context containing the last event received by the State Machine.
     */
    void Exit(scxml4cpp::Context* c);

    /**
     * Implementation of the ExitNoReply action. This action:
     * - stops the event loop.
     *
     * @param[in] c Context containing the last event received by the State Machine.
     */
    void ExitNoReply(scxml4cpp::Context* c);

    ActionsStd(const ActionsStd&) = delete;         //! Disable copy constructor
    ActionsStd& operator=(const ActionsStd&) = delete;  //! Disable assignment operator

 private:
    boost::asio::io_service& m_io_service;
    rad::SMAdapter&          m_sm;
    rad::Signal              m_signal;
    DataContext&             m_data;
};

}  // namespace hellomal

#endif  // HELLOMAL_ACTIONSSTD_HPP_
