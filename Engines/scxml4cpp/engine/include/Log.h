/*
 *    Logging facility described by Petru Marginean at:
 *    http://www.drdobbs.com/cpp/logging-in-c/201804215
 *
 *    Code downloaded from
 *    ftp://ftp.drdobbs.com/sourcecode/ddj/2007/0710.zip
 */

/*
 * $Id: Log.h 1061 2015-07-13 15:03:59Z landolfa $
 */

#ifndef __LOG_H__
#define __LOG_H__

#include <sstream>
#include <string>
#include <stdio.h>

namespace scxml4cpp
{
   /*
    * Supported Log Levels.
    */
    enum LogLevel {
	logERROR = 0, 
	logWARNING, 
	logINFO, 
	logDEBUG, 
	logDEBUG1, 
	logDEBUG2, 
	logDEBUG3, 
	logDEBUG4, 
	logTRACE
    };
    const int LOG_MAX_LEVEL = logTRACE;

    inline std::string LogNowTime();

   /*
    * Log class.
    * typename T is the output policy: stderr, stdout, Output2File, etc.
    */
    template <typename T> class Log 
    {
      public:
	                    Log();
	virtual             ~Log();
	std::ostringstream& Get(LogLevel level = logINFO);

	static LogLevel&    ReportingLevel();
	static std::string  ToString(LogLevel level);
	static LogLevel     FromString(const std::string& level);

      protected:
	std::ostringstream  os;

      private:
	Log(const Log&);
	Log& operator =(const Log&);
    };

    template <typename T> Log<T>::Log()
    {
    }

    template <typename T> std::ostringstream& Log<T>::Get(LogLevel level)
    {
	os << LogNowTime();
	os << " " << ToString(level) << " ";
	return os;
    }

   /*
    * In the destructor print out the message.
    */
    template <typename T> Log<T>::~Log()
    {
	os << std::endl;
	T::Output(os.str());
    }

    template <typename T> LogLevel& Log<T>::ReportingLevel()
    {
	static LogLevel reportingLevel = logINFO;
	return reportingLevel;
    }

   /*
    * Returns the Log Level name from the Log Level number.
    */
    template <typename T> std::string Log<T>::ToString(LogLevel level)
    {
	static const char* const buffer[] = { 
	    "ERROR", 
	    "WARNING", 
	    "INFO", 
	    "DEBUG", 
	    "DEBUG1", 
	    "DEBUG2", 
	    "DEBUG3", 
	    "DEBUG4", 
	    "TRACE"
	};
	return buffer[level];
    }

   /*
    * Returns the Log Level number from the Log Level name.
    */
    template <typename T> LogLevel Log<T>::FromString(const std::string& level)
    {
	if (level == "TRACE")
	    return logTRACE;
	if (level == "DEBUG4")
	    return logDEBUG4;
	if (level == "DEBUG3")
	    return logDEBUG3;
	if (level == "DEBUG2")
	    return logDEBUG2;
	if (level == "DEBUG1")
	    return logDEBUG1;
	if (level == "DEBUG")
	    return logDEBUG;
	if (level == "INFO")
	    return logINFO;
	if (level == "WARNING")
	    return logWARNING;
	if (level == "ERROR")
	    return logERROR;
	Log<T>().Get(logWARNING) << "Unknown logging level '" << level << "'. Using INFO level as default.";
	return logINFO;
    }

   /*
    * Class to print the logs to file.
    */

    class Output2FILE
    {
      public:
	static FILE*& Stream();
	static void Output(const std::string& msg);
    };

    inline FILE*& Output2FILE::Stream()
    {
	static FILE* pStream = stderr;
	return pStream;
    }

    inline void Output2FILE::Output(const std::string& msg)
    {   
	FILE* pStream = Stream();
	if (!pStream) return;

	fprintf(pStream, "%s", msg.c_str());
	fflush(pStream);
    }

/*
 * Declaration of FILELog class as Log<Output2FILE>
 * that works also on windows.
 */

#if defined(WIN32) || defined(_WIN32) || defined(__WIN32__)
#   if defined (BUILDING_FILELOG_DLL)
#       define FILELOG_DECLSPEC   __declspec (dllexport)
#   elif defined (USING_FILELOG_DLL)
#       define FILELOG_DECLSPEC   __declspec (dllimport)
#   else
#       define FILELOG_DECLSPEC
#   endif // BUILDING_DBSIMPLE_DLL
#else
#   define FILELOG_DECLSPEC
#endif // _WIN32

    class FILELOG_DECLSPEC FILELog : public Log<Output2FILE> {};
    //typedef Log<Output2FILE> FILELog;



   /*
    * Definition of the macro to use when logging.
    */

#define FILE_LOG(level)							\
    if (level > LOG_MAX_LEVEL) ;					\
    else if (level > FILELog::ReportingLevel() || !Output2FILE::Stream()) ; \
    else FILELog().Get(level) << "scxml4cpp " << __FILE__ << " " << __FUNCTION__ << " " << __LINE__ << " "


    inline void LogSetLevel(const std::string& levelName) { FILELog::ReportingLevel() = FILELog::FromString(levelName); }

   /*
    * Implementaiton of NowTime function to print the timestamp.
    */

#if defined(WIN32) || defined(_WIN32) || defined(__WIN32__)

#include <windows.h>

    inline std::string LogNowTime()
    {
	const int MAX_LEN = 200;
	char buffer[MAX_LEN];
	if (GetTimeFormatA(LOCALE_USER_DEFAULT, 0, 0, 
			   "HH':'mm':'ss", buffer, MAX_LEN) == 0)
	    return "Error in NowTime()";

	char result[100] = {0};
	static DWORD first = GetTickCount();
	std::sprintf(result, "%s.%03ld", buffer, (long)(GetTickCount() - first) % 1000); 
	return result;
    }

#else

#include <sys/time.h>

    inline std::string LogNowTime()
    {
	char buffer[64];
	time_t t;
	time(&t);
	tm r = {0};
	strftime(buffer, sizeof(buffer), "%Y-%m-%d  %H:%M:%S", localtime_r(&t, &r));
	struct timeval tv;
	gettimeofday(&tv, 0);
	char result[100] = {0};
	//std::sprintf(result, "%s.%03ld", buffer, (long)tv.tv_usec / 1000); 
	sprintf(result, "%s.%06ld", buffer, (long)tv.tv_usec / 1000); 
	return result;
    }

#endif //WIN32

}
#endif //__LOG_H__
