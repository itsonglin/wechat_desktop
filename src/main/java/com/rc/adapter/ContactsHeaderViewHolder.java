package com.rc.adapter;

import javax.swing.*;

/**
 * Created by song on 17-5-30.
 */
public class ContactsHeaderViewHolder extends HeaderViewHolder
{
    private String letter;
    public JLabel letterLabel;

    public ContactsHeaderViewHolder(String ch)
    {
        this.letter = ch;
    }


    public String getLetter()
    {
        return letter;
    }
}
