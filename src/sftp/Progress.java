/*
 * Code for JAVA SFTP library
 * For use with Processing.org
 * Heavily based off of examples from JSCH: http://www.jcraft.com/jsch/
 * Oh, and doesn't work at all without JSCH: http://www.jcraft.com/jsch/
 * 
 * Daniel Shiffman, June 2007
 * http://www.shiffman.net
 * 
 * JSCH:
 * Copyright (c) 2002,2003,2004,2005,2006,2007 Atsuhiko Yamanaka, JCraft,Inc. 
 * All rights reserved
 * 
 */
package sftp;

import com.jcraft.jsch.SftpProgressMonitor;

public class Progress implements SftpProgressMonitor{

    long count=0;
    long max=0;
    
    public void init(int op, String src, String dest, long max){
        this.max=max;
        count=0;
        percent=-1;
    }
    
    private long percent=-1;
    
    public boolean count(long count){
        this.count+=count;
        if(percent>=this.count*100/max){ return true; }
        percent=this.count*100/max;
        System.out.println("Completed "+this.count+"("+percent+"%) out of "+max+".");     
        //monitor.setProgress((int)this.count);
        return true; // it can never be canceled;
        //return !(monitor.isCanceled());
    }
    
    public void end(){
        //monitor.close();
    }
}
