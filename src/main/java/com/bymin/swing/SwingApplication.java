package com.bymin.swing;

import com.bymin.swing.app.AppFrame;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.*;
import java.awt.*;

@SpringBootApplication
public class SwingApplication {

    public static void main(String[] args) {
        SpringApplication.run(SwingApplication.class, args);
        System.setProperty("java.awt.headless", "false"); //Disables headless
        SwingUtilities.invokeLater(() -> {
            AppFrame frame = new AppFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 400);
            frame.setVisible(true);
        });
    }

}
