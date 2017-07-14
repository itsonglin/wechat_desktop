package com.rc.panels;

import javax.swing.*;
import java.awt.*;

/**
 * Created by song on 17-5-30.
 */
public class ListPanel extends ParentAvailablePanel
{
    private static ListPanel context;
    private RoomsPanel roomsPanel;
    private ContactsPanel contactsPanel;
    private CollectionsPanel collectionPanel;
    private SearchResultPanel searchResultPanel;

    public static final String CHAT = "CHAT";
    public static final String CONTACTS = "CONTACTS";
    public static final String COLLECTIONS = "COLLECTIONS";
    public static final String SEARCH = "SEARCH";

    private String previousTab = CHAT;
    private String currentTab = CHAT;

    private CardLayout cardLayout = new CardLayout();


    public ListPanel(JPanel parent)
    {
        super(parent);
        context = this;

        initComponents();
        initView();
    }


    private void initComponents()
    {
        roomsPanel = new RoomsPanel(this);

        contactsPanel = new ContactsPanel(this);

        collectionPanel = new CollectionsPanel(this);

        searchResultPanel = new SearchResultPanel(this);

    }

    private void initView()
    {
        this.setLayout(cardLayout);
        add(roomsPanel, CHAT);
        add(contactsPanel, CONTACTS);
        add(collectionPanel, COLLECTIONS);
        add(searchResultPanel, SEARCH);
    }

    /**
     * 显示指定的card
     *
     * @param who
     */
    public void showPanel(String who)
    {
        previousTab = currentTab;
        if (!who.equals(SEARCH))
        {
            currentTab = who;
        }
        cardLayout.show(this, who);
    }

    /**
     * 获取上一个tab，如果上一个tab是搜索tab，则返回搜索tab之前的tab
     * @return
     */
    public String getPreviousTab()
    {
        return previousTab;
    }

    /**
     * 获取当前选中的tab, 如果当前的tab是搜索tab，则返回搜索tab之前的tab
     * @return
     */
    public String getCurrentTab()
    {
        return currentTab;
    }

    public static ListPanel getContext()
    {
        return context;
    }

}
