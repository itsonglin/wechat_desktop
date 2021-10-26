package com.rc.components;

import java.awt.*;

/**
 * Created by song on 17-5-28.
 */
public class GBC extends GridBagConstraints{
    /*
     * constructs a GBC with a given gridx and gridy position and all other grid
     * bag constraint values set to the default
     * @param gridx the gridx position
     * @param gridy the gridy position
     */
    public GBC(int gridx, int gridy){
        this.gridx = gridx;
        this.gridy = gridy;
    }

    public GBC(int gridx, int gridy, int gridWidth, int gridHeight){
        this.gridx = gridx;
        this.gridy = gridy;
        this.gridwidth = gridWidth;
        this.gridheight = gridHeight;
    }

    /*
     * sets the anchor
     * @param anchor the anchor style
     * @return this object for further modification
     */

    public GBC setAnchor(int anchor){
        this.anchor = anchor;
        return this;
    }

    /*
     * sets the fill direction
     * @param fill the fill direction
     * @return this object for further modification
     */

    public GBC setFill(int fill){
        this.fill = fill;
        return this;
    }

    /*
     * sets the cell weights
     * @param weightx the cell weight in x direction
     * @param weighty the cell weight in y direction
     * @return this object for further modification
     */

    public GBC setWeight(int weightx, int weighty){
        this.weightx = weightx;
        this.weighty = weighty;
        return this;
    }

    /*
     * sets the insets of this cell
     * @param insets distance ths spacing to use in all directions
     * @return this object for further modification
     */

    public GBC setInsets(int distance){
        this.insets = new Insets(distance, distance, distance, distance);
        return this;
    }

    /*
     * sets the insets of this cell
     * @param top distance ths spacing to use on top
     * @param bottom distance ths spacing to use on bottom
     * @param left distance ths spacing to use to the left
     * @param right distance ths spacing to use to the  right
     * @return this object for further modification
     */

    public GBC setInsets(int top, int left,int bottom,int right){
        this.insets = new Insets(top, left, bottom, right);
        return this;
    }

    /*
     * sets the Ipad of this cell
     * @param Ipad distance ths spacing to use in all directions
     * @return this object for further modification
     */

    public GBC setIpad(int ipadx, int ipady){
        this.ipadx = ipadx;
        this.ipadx = ipadx;
        return this;
    }
}