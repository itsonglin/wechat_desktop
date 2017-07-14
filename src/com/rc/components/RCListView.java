package com.rc.components;

import com.rc.adapter.BaseAdapter;
import com.rc.adapter.HeaderViewHolder;
import com.rc.adapter.SelectUserItemViewHolder;
import com.rc.adapter.ViewHolder;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by song on 17-5-30.
 */
public class RCListView extends JScrollPane
{
    private BaseAdapter adapter;
    private JPanel contentPanel;
    private int vGap;
    private int hGap;
    private java.util.List<Rectangle> rectangleList = new ArrayList<>();
    boolean scrollToBottom = false;
    private AdjustmentListener adjustmentListener;
    private MouseAdapter mouseAdapter;
    private ScrollUI scrollUI;

    // 监听滚动到顶部事件
    private ScrollToTopListener scrollToTopListener;
    private boolean scrollBarPressed = false;
    private int lastScrollValue = -1;

    private static int lastItemCount = 0;
    private MouseAdapter scrollMouseListener;
    private boolean scrollAttachMouseListener = false;
    private boolean messageLoading = false;
    private long lastWeelTime = 0;

    public RCListView()
    {
        this(0, 0);
    }

    public RCListView(int hGap, int vGap)
    {
        this.vGap = vGap;
        this.hGap = hGap;

        initComponents();
        setListeners();
    }

    public void setScrollHiddenOnMouseLeave(JComponent component)
    {
        if (scrollMouseListener == null)
        {

            scrollMouseListener = new MouseAdapter()
            {
                @Override
                public void mouseEntered(MouseEvent e)
                {
                    setScrollBarColor(Colors.SCROLL_BAR_THUMB, Colors.WINDOW_BACKGROUND);
                    getVerticalScrollBar().repaint();

                    super.mouseEntered(e);
                }

                @Override
                public void mouseExited(MouseEvent e)
                {
                    setScrollBarColor(Colors.WINDOW_BACKGROUND, Colors.WINDOW_BACKGROUND);
                    getVerticalScrollBar().repaint();

                    super.mouseExited(e);
                }
            };
        }

        if (!scrollAttachMouseListener)
        {
            getVerticalScrollBar().addMouseListener(scrollMouseListener);
            scrollAttachMouseListener = true;
        }

        component.addMouseListener(scrollMouseListener);
    }

    /**
     * 设置滚动条的颜色，此方法必须在setAdapter()方法之前执行
     *
     * @param thumbColor
     * @param trackColor
     */
    public void setScrollBarColor(Color thumbColor, Color trackColor)
    {
        if (scrollUI == null)
        {
            scrollUI = new ScrollUI(thumbColor, trackColor);
            this.getVerticalScrollBar().setUI(scrollUI);
        }
        else
        {
            scrollUI.setThumbColor(thumbColor);
            scrollUI.setTrackColor(trackColor);
        }
        //this.getVerticalScrollBar().setUI(new ScrollUI(thumbColor, trackColor));
    }

    private void initComponents()
    {
        contentPanel = new JPanel();
        contentPanel.setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP, hGap, vGap, true, false));
        contentPanel.setBackground(Colors.WINDOW_BACKGROUND);

        this.setViewportView(contentPanel);
        this.setBorder(null);
        this.getVerticalScrollBar().setUnitIncrement(25);
        this.getVerticalScrollBar().setUI(new ScrollUI());
    }

    private void setListeners()
    {
        adjustmentListener = new AdjustmentListener()
        {
            public void adjustmentValueChanged(AdjustmentEvent evt)
            {
                // 之所以要加上!scrollBarPressed这个条件，scrollBar在顶部的时间，scrollbar点击和释放都分别会触发adjustmentValueChanged这个事件
                // 所以只让scrollBar释放的时候触发这个回调
                // !scrollToBottom 这个条件保证在自动滚动到底部之前，不会调用此回调
                if (evt.getValue() == 0 && evt.getValue() != lastScrollValue && scrollToTopListener != null && !scrollBarPressed && !scrollToBottom)
                {
                    messageLoading = true;
                    scrollToTopListener.onScrollToTop();
                }

                if (evt.getAdjustmentType() == AdjustmentEvent.TRACK && scrollToBottom)
                {
                    getVerticalScrollBar().setValue(getVerticalScrollBar().getModel().getMaximum()
                            - getVerticalScrollBar().getModel().getExtent());
                }

                lastScrollValue = evt.getValue();

            }
        };

        mouseAdapter = new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                scrollToBottom = false;
                scrollBarPressed = true;
                super.mouseEntered(e);
            }

            @Override
            public void mouseReleased(MouseEvent e)
            {
                scrollBarPressed = false;
                super.mouseReleased(e);
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e)
            {
                // 如果两次鼠标滚轮间隔小于1秒，则忽略
                if (System.currentTimeMillis() - lastWeelTime < 1000)
                {
                    lastWeelTime = System.currentTimeMillis();
                    return;
                }

                if (getVerticalScrollBar().getValue() == 0)
                {
                    if (messageLoading)
                    {
                        messageLoading = false;
                    }
                    else
                    {
                        System.out.println("鼠标滚轮到顶，自动加载");
                        if (scrollToTopListener != null)
                        {
                            scrollToTopListener.onScrollToTop();
                        }
                    }

                }

                scrollToBottom = false;

                lastWeelTime = System.currentTimeMillis();

                super.mouseWheelMoved(e);
            }
        };

        getVerticalScrollBar().addAdjustmentListener(adjustmentListener);
        getVerticalScrollBar().addMouseListener(mouseAdapter);
        addMouseListener(mouseAdapter);
        addMouseWheelListener(mouseAdapter);
    }

    public void setAutoScrollToBottom()
    {
        scrollToBottom = true;
    }

    public void setAutoScrollToTop()
    {
        scrollToBottom = false;
        getVerticalScrollBar().setValue(1);
    }

    public void fillComponents()
    {
        if (adapter == null)
        {
            return;
        }

        lastItemCount = adapter.getCount();
        for (int i = 0; i < adapter.getCount(); i++)
        {
            int viewType = adapter.getItemViewType(i);
            HeaderViewHolder headerViewHolder = adapter.onCreateHeaderViewHolder(viewType, i);
            if (headerViewHolder != null)
            {
                adapter.onBindHeaderViewHolder(headerViewHolder, i);
                contentPanel.add(headerViewHolder);
                rectangleList.add(headerViewHolder.getBounds());
            }

            //long startTime = System.currentTimeMillis();
            ViewHolder holder = adapter.onCreateViewHolder(viewType);
            adapter.onBindViewHolder(holder, i);
            contentPanel.add(holder);
            //System.out.println("加载完成 ，用时 " + (System.currentTimeMillis() - startTime));
        }
    }

    public BaseAdapter getAdapter()
    {
        return adapter;
    }

    public void setAdapter(BaseAdapter adapter)
    {
        this.adapter = adapter;

        fillComponents();
        //scrollToPosition(0);
    }

    public void setContentPanelBackground(Color color)
    {
        contentPanel.setOpaque(true);
        contentPanel.setBackground(color);
    }

    public void scrollToPosition(int position)
    {
    }

    /**
     * 获取滚动条在底部时显示的条目数
     */
    private int getLastVisibleItemCount()
    {
        int height = getHeight();

        int elemHeight = 0;
        int count = 0;
        for (int i = contentPanel.getComponentCount() - 1; i >= 0; i--)
        {
            count++;
            int h = contentPanel.getComponent(i).getHeight() + 20;
            elemHeight += h;

            if (elemHeight >= height)
            {
                break;
            }
        }

        return count;
    }


    /**
     * 重绘整个listView
     */
    public void notifyDataSetChanged(boolean keepSize)
    {
        if (keepSize)
        {
            if (lastItemCount == adapter.getCount())
            {
                System.out.println("数量相同");
                // 保持原来内容面板的宽高，避免滚动条长度改变或可见状态改变时闪屏
                contentPanel.setPreferredSize(new Dimension(contentPanel.getWidth(), contentPanel.getHeight()));
            }
        }

        contentPanel.removeAll();
        contentPanel.repaint();
        fillComponents();
        contentPanel.revalidate();

    }

    /**
     * 重绘指定区间内的元素
     *
     * @param startPosition
     * @param count
     */
    public void notifyItemRangeInserted(int startPosition, int count)
    {
        /*for (int i = startPosition; i < count; i++)
        {
            int viewType = adapter.getItemViewType(i);
            ViewHolder holder = adapter.onCreateViewHolder(viewType);
            adapter.onBindViewHolder(holder, i);
            contentPanel.add(holder, startPosition);
        }*/

        for (int i = count - 1; i >= startPosition; i--)
        {
            int viewType = adapter.getItemViewType(i);
            ViewHolder holder = adapter.onCreateViewHolder(viewType);
            adapter.onBindViewHolder(holder, i);
            contentPanel.add(holder, startPosition);
        }
    }

    /**
     * 重绘指定位置的元素
     *
     * @param position
     */
    public void notifyItemChanged(int position)
    {
        //contentPanel.remove(position);
        //int viewType = adapter.getItemViewType(position);
        //ViewHolder holder = adapter.onCreateViewHolder(viewType);
        ViewHolder holder = (ViewHolder) getItem(position);
        adapter.onBindViewHolder(holder, position);
        //contentPanel.revalidate();
        holder.repaint();

        /*contentPanel.getComponent(position).setBackground(Color.red);
        contentPanel.getComponent(position).revalidate();*/
    }

    public Component getItem(int n)
    {
        return contentPanel.getComponent(n);
    }

    public JPanel getContentPanel()
    {
        return contentPanel;
    }


    public void setScrollToTopListener(ScrollToTopListener listener)
    {
        this.scrollToTopListener = listener;
    }

    public void notifyItemInserted(int position, boolean end)
    {
        int viewType = adapter.getItemViewType(position);
        ViewHolder holder = adapter.onCreateViewHolder(viewType);
        adapter.onBindViewHolder(holder, position);

        position = end ? -1 : position;
        contentPanel.add(holder, position);
        contentPanel.revalidate();
    }

    public void notifyItemRemoved(int position)
    {
        contentPanel.remove(position);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    /**
     * 获取列表中所有的ViewHolder项目，不包括HeaderViewHolder
     * @return
     */
    public List<Component> getItems()
    {
        Component[] components = contentPanel.getComponents();
        List<Component> viewHolders = new ArrayList<>();
        for (Component com : components)
        {
            if (!(com instanceof HeaderViewHolder))
            {
                viewHolders.add(com);
            }
        }

        return viewHolders;
    }

    public interface ScrollToTopListener
    {
        void onScrollToTop();
    }
}
