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
public class ClockLabelUpdater {
    private JLabel clockLabel;
    private Timer timer;
    
    public ClockLabelUpdater(JLabel clockLabel) {
        this.clockLabel = clockLabel;
        startClock();
    }
    
    private void startClock() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateClock();
            }
        });
        timer.start();
    }
    
    private void updateClock() {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        clockLabel.setText(sdf.format(new Date()));
    }
    
    public void stopClock() {
        if (timer != null) {
            timer.stop();
        }
    }
}
