package com.project.contap.common.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;

public class AddLog {
    public static void addAPIUseTime(
            HttpServletRequest request,
            Long runTime) throws IOException
    {
        Logger log = LogManager.getLogger("AddApiTime");
        StringBuilder sb = new StringBuilder();
        sb.append("[Method : "+request.getMethod()+"]  [URI : "+request.getRequestURI()+"]  [Runtime : "+runTime.toString()+"ms]");
        log.error(sb);
    }
}
