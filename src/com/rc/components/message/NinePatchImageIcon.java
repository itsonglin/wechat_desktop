package com.rc.components.message;


import com.android.ninepatch.NinePatch;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class NinePatchImageIcon extends ImageIcon {
	
	private static final long serialVersionUID = -5004430700627593660L;
	
	private NinePatch mNinePatch;
	
	public NinePatchImageIcon(URL urlRes) {
		try {
			mNinePatch = NinePatch.load(urlRes, true);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
	
	@Override
	public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
		
		int iCompWidth = c.getWidth();
		int iCompHeigth = c.getHeight();
		mNinePatch.draw((Graphics2D) g, 0, 0,iCompWidth, iCompHeigth);
	}

}
