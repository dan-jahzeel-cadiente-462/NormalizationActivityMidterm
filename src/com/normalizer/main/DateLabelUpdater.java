/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.normalizer.main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author USER
 */

public class DateLabelUpdater {
    private JLabel clockLabel;
    private Timer timer;
    
    public DateLabelUpdater(JLabel clockLabel) {
        this.clockLabel = clockLabel;
        startDate();
    }
    
    private void startDate() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setDate();
            }
        });
        timer.start();
    }
    
    private void setDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("E, MMM dd, yyyy");
        clockLabel.setText(sdf.format(new Date()));
    }
    
    public void stopDate() {
        if (timer != null) {
            timer.stop();
        }
    }
}

