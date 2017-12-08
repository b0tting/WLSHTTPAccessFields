package nl.qualogy.wls;

import weblogic.servlet.logging.CustomELFLogger;
import weblogic.servlet.logging.FormatStringBuffer;
import weblogic.servlet.logging.HttpAccountingInfo;


public class HTTPAccessFields implements CustomELFLogger {
    public void logField(HttpAccountingInfo metrics, FormatStringBuffer buff) {
        buff.appendValueOrDash(metrics.getRemoteUser());
        buff.appendValueOrDash(metrics.getRemoteAddr());
    }
}




