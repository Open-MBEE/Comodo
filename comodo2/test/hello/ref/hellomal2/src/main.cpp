/**
 * @file
 * @ingroup hellomal2
 * @copyright ESO - European Southern Observatory
 * @author
 *
 * @brief main source file.
 */

#include <hellomal2/logger.hpp>
#include <hellomal2/dataContext.hpp>
#include <hellomal2/dbInterface.hpp>
#include <hellomal2/actionMgr.hpp>
#include <hellomal2/stdCmdsImpl.hpp>
#include <hellomal2/externalCmdsImpl.hpp>

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
	rad::LogInitialize();
	LOG4CPLUS_INFO(hellomal2::GetLogger(), "Application hellomal2 started.");

    try {
        /*
         *  Load CII/MAL middleware here because it resets
         *  the log4cplus configuration!
         */
        rad::cii::LoadMiddlewares({"zpb"});

        /* Read only configuration */
        hellomal2::Config config;
        if (config.ParseOptions(argc, argv) == false) {
            // request for help
            return EXIT_SUCCESS;
        }
        config.LoadConfig();
        rad::LogConfigure(rad::Helper::FindFile(config.GetLogProperties()));

        /*
         * LAN 2020-07-09 EICSSW-717
         * Create CII/MAL replier as soon as possible to avoid problems when
         * an exceptions is thrown from and Action/Guard.
         */
        rad::cii::Replier mal_replier(elt::mal::Uri(config.GetMsgReplierEndpoint()));

        /* Runtime DB */
        rad::DbAdapterRedis redis_db;

        /* Runtime data context */
        hellomal2::DataContext data_ctx(config, redis_db);

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
        hellomal2::ActionMgr action_mgr;
        action_mgr.CreateActions(io_service, state_machine, data_ctx);
        action_mgr.CreateActivities(state_machine, data_ctx);

        // Load SM model
        state_machine.Load(config.GetSmScxmlFilename(), &action_mgr.GetActions(),
                &action_mgr.GetActivities());

        // Register handlers to reject events
        //state_machine.RegisterDefaultRequestRejectHandler<StdCmds::Init>();
        //state_machine.RegisterDefaultRequestRejectHandler<StdCmds::Enable>();
        //state_machine.RegisterDefaultRequestRejectHandler<StdCmds::Disable>();

        // Register publisher to export state information
        state_machine.SetStatusRepresentation(false);
        using std::placeholders::_1;
        state_machine.SetStatusPublisher(std::bind(
                &hellomal2::DbInterface::SetControlState,
                &data_ctx.GetDbInterface(), _1));

        /*
         * Register CII/MAL interfaces.
         */
        mal_replier.RegisterService<hellomalif2::AsyncStdCmds>("StdCmds",
            std::make_shared<hellomal2::StdCmdsImpl>(state_machine));
        mal_replier.RegisterService<externalif2::AsyncExternalCmds>("ExternalCmds",
            std::make_shared<hellomal2::ExternalCmdsImpl>(state_machine));


        /*
         * Start event loop
         */
        state_machine.Start();
        io_service.run();
        state_machine.Stop();
    } catch (rad::Exception& e) {
        LOG4CPLUS_ERROR(hellomal2::GetLogger(), e.what());
        return EXIT_FAILURE;
    } catch (...) {
        LOG4CPLUS_ERROR(hellomal2::GetLogger(), boost::current_exception_diagnostic_information());
        return EXIT_FAILURE;
    }

    // to avoid valgrind warnings on potential memory loss
    google::protobuf::ShutdownProtobufLibrary();

    LOG4CPLUS_INFO(hellomal2::GetLogger(), "Application hellomal2 terminated.");
    return EXIT_SUCCESS;
}
