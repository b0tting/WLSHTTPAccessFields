HTTP logging in WebLogic uses the [common log format](https://en.wikipedia.org/wiki/Common_Log_Format) by [default](https://docs.oracle.com/cd/E24329_01/web.1211/e24432/web_server.htm#CNFGD204). This means you log the IP address from which the HTTP request originated. And if basic auth was used, the authenticating username is logged as well. This has little use in a web container based environment, where you would use form based authentication or some fancy single-sign variant. 

WebLogic also offers the ["extended log format"](https://en.wikipedia.org/wiki/Extended_Log_Format). The available values allow for some additional fields in your logfile, but a huge ommission is the username in the list of available options. To roll your own, you need to implement the CustomELFLogger interface. This project adds some commonly used

## Installation

Installation and configuration takes two steps:
* Put the included WLSHTTPAccessFields.jar in your WebLogic classpath or in the domain /lib directory on every involved machine.
* Change the HTTP access log to the extended format and include the x-remoteuser

A WLST script that does the same:   


## Roll your own custom access fields
This project is just a small convenience jar for administrators. It's not very hard to build your own.  

To roll your own you need to implement the CustomELFLogger. The easiest way to get this into your IDE is to roll the WebLogic full client jar, which is a jar created by a small script that rolls one big jar from an existing WebLogic installation. Find the instructions [here](https://docs.oracle.com/cd/E24329_01/web.1211/e24378/jarbuilder.htm#SACLT421). This file will turn out at about 100mb. Since you just need the interface, any version will do. 

Any request information can be extracted from the [HTTPAccountingInfo](https://docs.oracle.com/middleware/1221/wls/WLAPI/weblogic/servlet/logging/HttpAccountingInfo.html) class. Please take into consideration that your class is run on every HTTP request, so don't do things like shooting JMS messages or access databases. 




(...working on it!)
