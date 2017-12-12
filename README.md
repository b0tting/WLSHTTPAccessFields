HTTP logging in WebLogic uses the [common log format](https://en.wikipedia.org/wiki/Common_Log_Format) by [default](https://docs.oracle.com/cd/E24329_01/web.1211/e24432/web_server.htm#CNFGD204). This means you log the IP address from which the HTTP request originated. And if basic auth was used, the authenticating username is logged as well. This has little use in a web container based environment, where you would use form based authentication or some fancy single-sign variant. 

WebLogic also offers the ["extended log format"](https://en.wikipedia.org/wiki/Extended_Log_Format). The available values allow for some additional fields in your logfile, but a huge ommission is the username in the list of available options. To roll your own, you need to implement the CustomELFLogger interface, which I did for you in this small project. This jar adds the username and originating IP address (if passed by proxies) to the access log.
 
 An example log line showing the loggin in user and origin IP:
 ```
 2017-12-12      11:34:49        weblogic_dev 111.110.110.129    -       GET     0a226714-44cd-4aa1-b2c0-90d04afabd96-0000018d   0       /servicebus/images/navi_designer_24_dwn.png     200     623

 ```

## Installation

Installation and configuration takes two steps:
* Put the included WLSHTTPAccessFields.jar in your WebLogic classpath or in the domain /lib directory on every involved machine.
* Change the HTTP access log to the extended format and include the x-HTTPAccessFields field

A WLST script that automates that seconds step:

```python
edit()
startEdit()
  
for server in ls('/Servers', returnMap='true'):
    cd('/Servers/' + server + '/WebServer/' + server + '/WebServerLog/' + server)
    cmo.setLogFileFormat('extended')
    cmo.setELFFields('date time x-HTTPAccessFields cs-method ctx-ecid ctx-rid cs-uri sc-status bytes ')

save()
activate()
```


## Roll your own custom access fields
This project is just a small convenience jar for administrators. It's not very hard to build your own.  

To compile your own you need to implement the CustomELFLogger. The easiest way to get this into your IDE is to roll the WebLogic full client jar, which is a jar created by a small script that balls one big jar from an existing WebLogic installation. Find the instructions [here](https://docs.oracle.com/cd/E24329_01/web.1211/e24378/jarbuilder.htm#SACLT421). This file will turn out at about 100mb. Since you just need the interface, any version will do.
 
As you might note in the WLSHTTPAccessFields.java file, you should not include a "package" statement, or at least in WebLogic 12.1 the application server could not resolve my class if it was in a package. The name of your class prepended with "x-" will be the field name to be used in the ELF format. Any request information can be extracted from the [HTTPAccountingInfo](https://docs.oracle.com/middleware/1221/wls/WLAPI/weblogic/servlet/logging/HttpAccountingInfo.html) class which also offers the Principal and ServerChannel objects. The given FormatStringBuffer is the current ELF format line and includes several convenience methods to deal with trimming and empty values. Please take into consideration that your class is run on every HTTP request, so don't do things like shooting JMS messages or access databases. 

