package com.rc.panels;

import com.rc.components.GBC;
import com.rc.frames.MainFrame;
import com.sun.javafx.application.PlatformImpl;
import javafx.embed.swing.JFXPanel;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import org.w3c.dom.Document;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author song
 * @date 19-11-12 14:45
 * @description
 * @since
 */
public class WebBrowserPanel extends ParentAvailablePanel
{
    private static WebBrowserPanel context;

    private JFXPanel jfxPanel;
    WebView webView;

    public WebBrowserPanel(JPanel parent)
    {
        super(parent);

        context = this;
        initComponents();
        intiView();
    }

    private void intiView()
    {
        this.setLayout(new GridBagLayout());
        add(jfxPanel, new GBC(0, 0).setFill(GBC.BOTH).setAnchor(GBC.CENTER).setWeight(1,1).setInsets(0, 0, 0, 0));

    }

    private void initComponents()
    {
        jfxPanel = new JFXPanel();
        PlatformImpl.startup(new Runnable()
        {

            @Override
            public void run()
            {
                webView = new WebView();
                webView.getEngine().setJavaScriptEnabled(true);
                final StackPane layout = new StackPane();
                Scene scene = new Scene(layout);
                layout.getChildren().addAll(webView);
                jfxPanel.setScene(scene);
                jfxPanel.setVerifyInputWhenFocusTarget(true);
            }
        });
    }

    public static WebBrowserPanel getContext()
    {
        return context;
    }

    public void loadUrl(String url, Map<String, List<String>> header)
    {
        PlatformImpl.startup(() ->
        {
            URI uri = URI.create(url);

            try
            {
                java.net.CookieHandler.getDefault().put(uri, header);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            webView.getEngine().load(url);
        });

    }
}
