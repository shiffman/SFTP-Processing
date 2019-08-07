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

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.SftpProgressMonitor;
import com.jcraft.jsch.UserInfo;

public class Sftp extends Thread {

    String host;
    String user;
    boolean running;
    JSch jsch;
    Session session;
    ChannelSftp sftp;
    
    boolean prompt;
    String password;
    

    public Sftp(String h, String u, boolean p) {
        host = h;
        user = u;
        prompt = p;
        password = "";
    }

    public void start() {
        super.start();
        running = true;
    }

    public void run() {
        try {
            System.out.println("Attempting to connect.");
            jsch=new JSch();
            session = jsch.getSession(user, host, 22);
            UserInfo ui=new PromptUser(prompt,password);
            session.setUserInfo(ui);
            System.out.println("Logging in.");
            session.connect();
            System.out.println("Connected, session started.");

            Channel channel= session.openChannel("sftp");
            channel.connect();
            sftp=(ChannelSftp)channel;

            while (running) {
                Thread.sleep(1000);
                // do nothing;

            }
            session.disconnect();
        } catch (JSchException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println("Thread interuppted");
            e.printStackTrace();
        }
    }

    public void executeCommand(String command) {

        String[] cmds = command.split(" ");

        if(cmds[0].equals("quit")){
            sftp.quit();
            return;
        }
        if(cmds[0].equals("exit")){
            sftp.exit();
            return;
        }
        if(cmds[0].equals("pwd") || cmds[0].equals("lpwd")){
            String str=(cmds[0].equals("pwd")?"Remote":"Local");
            str+=" working directory: ";
            if(cmds[0].equals("pwd")) str+=sftp.pwd();
            else str+=sftp.lpwd();
            System.out.println(str);
            return;
        }
        if(cmds[0].equals("ls") || cmds[0].equals("dir")){
            String path=".";
            if(cmds.length==2) path=cmds[1];
            try{
                java.util.Vector vv=sftp.ls(path);
                if(vv!=null){
                    for(int ii=0; ii<vv.size(); ii++){
                        Object obj=vv.elementAt(ii);
                        if(obj instanceof com.jcraft.jsch.ChannelSftp.LsEntry){
                            System.out.println(((com.jcraft.jsch.ChannelSftp.LsEntry)obj).getLongname());
                        }

                    }
                }
            }
            catch(SftpException e){
                System.out.println(e.toString());
            }
            return;
        }

        if(cmds[0].equals("get")) {
            String p1=cmds[1];
            String p2=".";
            if(cmds.length==3)p2=cmds[2];
            SftpProgressMonitor monitor=new Progress();
            int mode=ChannelSftp.OVERWRITE;
            try {
                sftp.get(p1, p2, monitor, mode);
            } catch (SftpException e) {
                e.printStackTrace();
            }
            return;
        }

        if(cmds[0].equals("version")){
            System.out.println("SFTP protocol version "+sftp.version());
            return;
        }
        if(cmds[0].equals("help") || cmds[0].equals("?")){
            System.out.println("help");
            return;
        }
        
        System.out.println("unimplemented command: "+cmds[0]);
    
    }

    public void setPassword(String s) {
        // DANGER IF WE'RE NOT USING A PROMPT
        password = s;
        
    }
}

//System.exit(0);





