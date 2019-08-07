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

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.jcraft.jsch.UserInfo;

public class PromptUser implements UserInfo {
    boolean prompt;

    public PromptUser(boolean p, String pss) {
        super();
        passwd = pss;
        prompt = p;
    }


    public String getPassword(){ return passwd; }

    public boolean promptYesNo(String str){
        if (!prompt) {
            return true;
        } else {

            Object[] options={ "yes", "no" };
            int foo=JOptionPane.showOptionDialog(null, 
                    str,
                    "Warning", 
                    JOptionPane.DEFAULT_OPTION, 
                    JOptionPane.WARNING_MESSAGE,
                    null, options, options[0]);
            return foo==0;
        }
    }

    String passwd;

    JTextField passwordField=(JTextField)new JPasswordField(20);

    public String getPassphrase(){ return null; }
    public boolean promptPassphrase(String message){ return true; }

    public boolean promptPassword(String message){
        if (!prompt) {
            // password should have been set in constructor
            return true;
        } else {
            Object[] ob={passwordField}; 
            int result=
                JOptionPane.showConfirmDialog(null, ob, message,
                        JOptionPane.OK_CANCEL_OPTION);
            if(result==JOptionPane.OK_OPTION){
                passwd=passwordField.getText();
                return true;
            }
            else{ return false; }
        }
    }

    public void showMessage(String message){
        JOptionPane.showMessageDialog(null, message);
    }

    final GridBagConstraints gbc = 
        new GridBagConstraints(0,0,1,1,1,1,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE,
                new Insets(0,0,0,0),0,0);
    private Container panel;
    public String[] promptKeyboardInteractive(String destination,
            String name,
            String instruction,
            String[] prompt,
            boolean[] echo){
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        gbc.weightx = 1.0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridx = 0;
        panel.add(new JLabel(instruction), gbc);
        gbc.gridy++;

        gbc.gridwidth = GridBagConstraints.RELATIVE;

        JTextField[] texts=new JTextField[prompt.length];
        for(int i=0; i<prompt.length; i++){
            gbc.fill = GridBagConstraints.NONE;
            gbc.gridx = 0;
            gbc.weightx = 1;
            panel.add(new JLabel(prompt[i]),gbc);

            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weighty = 1;
            if(echo[i]){
                texts[i]=new JTextField(20);
            }
            else{
                texts[i]=new JPasswordField(20);
            }
            panel.add(texts[i], gbc);
            gbc.gridy++;
        }

        if(JOptionPane.showConfirmDialog(null, panel, 
                destination+": "+name,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE)
                ==JOptionPane.OK_OPTION){
            String[] response=new String[prompt.length];
            for(int i=0; i<prompt.length; i++){
                response[i]=texts[i].getText();
            }
            return response;
        }
        else{
            return null;  // cancel
        }
    }

}

