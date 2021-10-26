package com.rc.components.message;

import javax.swing.text.BoxView;
import javax.swing.text.Element;
import javax.swing.text.View;

/**
 * @author song
 * @date 19-10-29 15:57
 * @description
 * @since
 */
public class MyBoxView extends BoxView
{
    /**
     * Constructs a <code>BoxView</code>.
     *
     * @param elem the element this view is responsible for
     * @param axis either <code>View.X_AXIS</code> or <code>View.Y_AXIS</code>
     */
    public MyBoxView(Element elem, int axis)
    {
        super(elem, axis);
    }

    @Override
    protected void layoutMajorAxis(int targetSpan, int axis, int[] offsets, int[] spans)
    {
        super.layoutMajorAxis(targetSpan, axis, offsets, spans);
    }

    @Override
    protected void layoutMinorAxis(int targetSpan, int axis, int[] offsets, int[] spans)
    {
       super.layoutMinorAxis(targetSpan, axis, offsets, spans);
        /*int n = getViewCount();
        for (int i = 0; i < n; i++) {
            View v = getView(i);
            int max = (int) v.getMaximumSpan(axis);
            if (max < targetSpan) {
                System.out.println("11111   " + max + ", " + targetSpan);
                // can't make the child this wide, align it
                float align = v.getAlignment(axis);
                offsets[i] = (int) ((targetSpan - max) * align);
                spans[i] = max;
            } else {
                // make it the target width, or as small as it can get.
                int min = (int)v.getMinimumSpan(axis);
                System.out.println("22222   " + max + ", " + targetSpan + ", " + min);

                offsets[i] = 0;
                spans[i] = Math.max(min, targetSpan);
            }
        }*/
    }
}
