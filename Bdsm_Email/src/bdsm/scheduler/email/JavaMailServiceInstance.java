/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bdsm.scheduler.email;

import java.util.Properties;
import javax.mail.Session;

/**
 *
 * @author bdsm
 */
public class JavaMailServiceInstance {    
    private static Session session;

    static{        
        Properties properties = new Properties();
        properties.put("","");
        JavaMailServiceInstance.session = Session.getDefaultInstance(properties);
    }
    
    public static Session getSession(){
        return JavaMailServiceInstance.session;
    }
    
}
