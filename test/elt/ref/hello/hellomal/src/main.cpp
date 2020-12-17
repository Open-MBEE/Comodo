/**
 * @file
 * @ingroup hellomal
 * @copyright ESO - European Southern Observatory
 * @author
 *
 * @brief main source file.
 */

#include <hellomal/logger.hpp>
#include <hellomal/dataContext.hpp>
#include <hellomal/dbInterface.hpp>
#include <hellomal/actionMgr.hpp>
#include <hellomal/stdCmdsImpl.hpp>

#include <events.rad.hpp>

#include <rad/mal/replier.hpp>
#include <rad/mal/utils.hpp>
#include <rad/exceptions.hpp>
#include <rad/dbAdapterRedis.hpp>
#include <rad/smAdapter.hpp>

#include <scxml4cpp/Context.h>
#include <scxml4cpp/EventQueue.h>

#include <boost/asio.hpp>
#include <boost/exception/diagnostic_information.hpp>

#include <google/protobuf/stubs/common.h>

#include <memory>

/**
 * Application main.
 *
 * @param[in] argc Number of command line options.
 * @param[in] argv Command line options.
 */
int main(int argc, char *argv[]) {
	rad::LogInitializer log_initializer;
	LOG4CPLUS_INFO(hellomal::GetLogger(), "Application hellomal started.");

    try {
        /*
         *  Load CII/MAL middleware here because it resets
         *  the log4cplus configuration!
         */
        rad::cii::LoadMiddlewares({"zpb"});

        /* Read only configuration */
        hellomal::Config config;
        if (config.ParseOptions(argc, argv) == false) {
            // request for help
            return EXIT_SUCCESS;
        }
        config.LoadConfig();
        log_initializer.Configure(rad::Helper::FindFile(config.GetLogProperties()));

        /*
         * LAN 2020-07-09 EICSSW-717
         * Create CII/MAL replier as soon as possible to avoid problems when
         * an exceptions is thrown from and Action/Guard.
         */
        rad::cii::Replier mal_replier(elt::mal::Uri(config.GetMsgReplierEndpoint()));

        /* Runtime DB */
        rad::DbAdapterRedis redis_db;

        /* Runtime data context */
        hellomal::DataContext data_ctx(config, redis_db);

        /*
         * Create event loop
         */

        // event loop
        boost::asio::io_service io_service;

        /*
         * State Machine related objects
         */

        // SM event queue and context
        scxml4cpp::EventQueue external_events;
        scxml4cpp::Context state_machine_ctx;

        // State Machine facade
        rad::SMAdapter state_machine(io_service,
                                     &state_machine_ctx,
                                     external_events);

        // actions and activities
        hellomal::ActionMgr action_mgr;
        action_mgr.CreateActions(io_service, state_machine, data_ctx);
        action_mgr.CreateActivities(state_machine, data_ctx);

        // Load SM model
        state_machine.Load(config.GetSmScxmlFilename(), &action_mgr.GetActions(),
                &action_mgr.GetActivities());

        // Register handlers to reject events
        state_machine.RegisterDefaultRequestRejectHandler<StdCmds::Init>();
        state_machine.RegisterDefaultRequestRejectHandler<StdCmds::Reset>();
        state_machine.RegisterDefaultRequestRejectHandler<StdCmds::Enable>();
        state_machine.RegisterDefaultRequestRejectHandler<StdCmds::Disable>();
        state_machine.RegisterDefaultRequestRejectHandler<StdCmds::GetState>();
        state_machine.RegisterDefaultRequestRejectHandler<StdCmds::Stop>();
        state_machine.RegisterDefaultRequestRejectHandler<StdCmds::Exit>();
        state_machine.RegisterDefaultRequestRejectHandler<StdCmds::SetLogLevel>();


        // Register publisher to export state information
        state_machine.SetStatusRepresentation(false);
        using std::placeholders::_1;
        state_machine.SetStatusPublisher(std::bind(
                &hellomal::DbInterface::SetControlState,
                &data_ctx.GetDbInterface(), _1));

        /*
         * Register CII/MAL interfaces.
         */
        mal_replier.RegisterService<hellomalif::AsyncStdCmds>("StdCmds",
            std::make_shared<hellomal::StdCmdsImpl>(state_machine));


        /*
         * Start event loop
         */
        state_machine.Start();
        io_service.run();
        state_machine.Stop();
    } catch (rad::Exception& e) {
        LOG4CPLUS_ERROR(hellomal::GetLogger(), e.what());
        return EXIT_FAILURE;
    } catch (...) {
        LOG4CPLUS_ERROR(hellomal::GetLogger(), boost::current_exception_diagnostic_information());
        return EXIT_FAILURE;
    }

    // to avoid valgrind warnings on potential memory loss
    google::protobuf::ShutdownProtobufLibrary();

    LOG4CPLUS_INFO(hellomal::GetLogger(), "Application hellomal terminated.");
    return EXIT_SUCCESS;
}
