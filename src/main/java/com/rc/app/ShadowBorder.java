package com.rc.app;

/*
 * Copyright 2012 wuyou (raistlic@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.jhlabs.image.GaussianFilter;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

/**
 * Instance can be retrieved via an overloading {@code newInstance()} method
 * or a {@code ShadowBorder.Builder} instance, using a "builder design pattern".
 *
 * It is recommended that the component to apply this border is NOT opaque(in
 * the sense of Swing painting hierarchy), because the "shadow border effect"
 * itself implies a somewhat "transparent" effect. In the case of being applied
 * to an opaque component, this class tries to use an expensive reflection hack
 * to "erase" the border area before painting the border, which is not fully
 * tested and can be dangerous.
 *
 * This class uses a third-party graphics library to draw the "gaussian blur"
 * effect.
 *
 * The dependency jar can be downloaded from the JHLabs website, or from
 * the maven central repository with the following POM coordination:
 *
 * <pre>{@code
 *
 * <groupId>com.jhlabs</groupId>
 * <artifactId>filters</artifactId>
 * <packaging>jar</packaging>
 * <version>2.0.235</version>
 * <name>JHLabs Image Processing Filters</name>
 * <description>A collection of image processing filters.</description>
 * <url>http://www.jhlabs.com/ip/index.html</url>
 *
 * }</pre>
 *
 * @author raistlic
 * @date   Nov 10, 2012
 */

public class ShadowBorder implements Border {

    public static final int DEFAULT_SIZE = 4;
    public static final Color DEFAULT_COLOR = Color.BLACK;
    public static final float DEFAULT_ALPHA = 0.3f;

    private static final float DEFAULT_PADDING_TOP = 0.0f;
    private static final float DEFAULT_PADDING_LEFT = 0.0f;

    public static ShadowBorder newInstance() {

        return newInstance(DEFAULT_SIZE, DEFAULT_COLOR, DEFAULT_ALPHA);
    }

    public static ShadowBorder newInstance(int size) {

        return newInstance(size, DEFAULT_COLOR, DEFAULT_ALPHA);
    }

    public static ShadowBorder newInstance(int size, Color c) {

        return newInstance(size, c, DEFAULT_ALPHA);
    }

    public static ShadowBorder newInstance(int size, Color c, float alpha) {

        size = Math.max(size, 0);
        alpha = Math.max(alpha, 0f);
        alpha = Math.min(alpha, 1f);

        if( c == null )
            c = DEFAULT_COLOR;

        return newInstance(size, c, alpha, DEFAULT_PADDING_TOP, DEFAULT_PADDING_LEFT);
    }

    public static ShadowBorder newInstance(int size, Color c, float alpha,
                                           float paddingLeft, float paddingTop) {

        size = Math.max(size, 0);
        alpha = Math.max(alpha, 0f);
        alpha = Math.min(alpha, 1f);

        paddingLeft = Math.max(paddingLeft, 0f);
        paddingLeft = Math.min(paddingLeft, 1f);

        paddingTop = Math.max(paddingTop, 0f);
        paddingTop = Math.min(paddingTop, 1f);

        if( c == null )
            c = DEFAULT_COLOR;

        return new ShadowBorder(size, c, alpha, paddingLeft, paddingTop);
    }

    public static Builder newBuilder() {

        return new Builder();
    }

    public static class Builder {

        private int size          = DEFAULT_SIZE;
        private Color color       = DEFAULT_COLOR;
        private float alpha       = DEFAULT_ALPHA;

        private float paddingLeft = DEFAULT_PADDING_LEFT;
        private float paddingTop  = DEFAULT_PADDING_TOP;

        // use static method ShadowBorder.newBuilder().
        private Builder() {}

        public Builder shadowSize(int size) {

            if( size < 0 )
                throw new IllegalArgumentException("Invalid shadow size: " + size);

            this.size = size;
            return this;
        }

        public Builder shadowColor(Color color) {

            if( color == null )
                throw new NullPointerException();

            this.color = color;
            return this;
        }

        public Builder shadowAlpha(float alpha) {

            alpha = Math.max(alpha, 0f);
            alpha = Math.min(alpha, 1f);

            this.alpha = alpha;
            return this;
        }

        public Builder paddingLeft(float paddingLeft) {

            paddingLeft = Math.max(paddingLeft, 0f);
            paddingLeft = Math.min(paddingLeft, 1f);

            this.paddingLeft = paddingLeft;
            return this;
        }

        public Builder paddingTop(float paddingTop) {

            paddingTop = Math.max(paddingTop, 0f);
            paddingTop = Math.min(paddingTop, 1f);

            this.paddingTop = paddingTop;
            return this;
        }

        public Builder topLeft() {

            return paddingLeft(0f).paddingTop(0f);
        }

        public Builder left() {

            return paddingLeft(0f).paddingTop(0.5f);
        }

        public Builder bottomLeft() {

            return paddingLeft(0f).paddingTop(1f);
        }

        public Builder bottom() {

            return paddingLeft(0.5f).paddingTop(1f);
        }

        public Builder bottomRight() {

            return paddingLeft(1f).paddingTop(1f);
        }

        public Builder right() {

            return paddingLeft(1f).paddingTop(0.5f);
        }

        public Builder topRight() {

            return paddingLeft(1f).paddingTop(0f);
        }

        public Builder top() {

            return paddingLeft(0.5f).paddingTop(0f);
        }

        public Builder center() {

            return paddingLeft(0.5f).paddingTop(0.5f);
        }

        public ShadowBorder build() {

            return new ShadowBorder(size, color, alpha,
                    paddingLeft, paddingTop);
        }
    }

    private BufferedImage[] shadows;
    private final int size;
    private final Color color;
    private final float alpha;
    private final Insets insetsMax, insetsActual;
    private ShadowBorder(int size, Color color, float alpha,
                         float paddingLeft, float paddingTop) {

        assert size > 0;
        assert color != null;

        assert alpha > 0f;
        assert alpha <= 1f;

        assert paddingLeft >= 0f;
        assert paddingLeft <= 1f;

        assert paddingTop <= 0f;
        assert paddingTop >= 1f;

        this.size = size;
        this.color = color;
        this.alpha = alpha;

        int dsize = size * 2;
        insetsMax = new Insets(dsize, dsize, dsize, dsize);
        insetsActual = new Insets(8,8,8,10);
        /*insetsActual.left = Math.max(0, Math.min(dsize, Math.round(dsize * paddingLeft)));
        insetsActual.top = Math.max(0, Math.min(dsize, Math.round(dsize * paddingTop)));
        insetsActual.right = dsize - insetsActual.left;
        insetsActual.bottom = dsize - insetsActual.top;*/
    }

    @Override
    @SuppressWarnings("unchecked")
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {

        Rectangle bounds = new Rectangle(x, y, width, height);

        @SuppressWarnings("unchecked")
        Graphics2D gg = (Graphics2D)g.create();

        Insets insets = getBorderInsets(c);
        Area shadowArea = new Area(bounds);
        shadowArea.subtract( new Area( new Rectangle(

                bounds.x + insets.left,
                bounds.y + insets.top,
                bounds.width - insets.left - insets.right,
                bounds.height - insets.top - insets.bottom
        )));

        gg.setClip(shadowArea);

        // expensive hack =)
        if( c.isOpaque() )
            hackOpaque(c, gg, bounds);

        for(Edge edge : Edge.values()) {

            edge.fill(this, gg, bounds);
        }

        gg.dispose();
    }

    private void hackOpaque(Component c, Graphics2D g, Rectangle bounds) {

        List<Component> chain = new ArrayList<Component>();
        chain.add(c);
        for(Component component = c.getParent();
            component != null;
            component = component.getParent()) {

            Component child = chain.get(chain.size()-1);
            g.translate(-child.getX(), -child.getY());
            chain.add(component);
            if( component.isOpaque() )
                break;
        }

        for(int i=chain.size()-1; i>0; i--) {

            Component component = chain.get(i);
            try {

                Method m = null;
                Class<?> type = component.getClass();
                while(m == null && type != null) {

                    for(Method method : type.getDeclaredMethods()) {

                        if( !method.getName().equals("paintComponent") )
                            continue;
                        Class<?>[] params = method.getParameterTypes();
                        if( params.length != 1 )
                            continue;
                        if( !params[0].equals(Graphics.class) )
                            continue;

                        m = method;
                        break;
                    }
                    type = type.getSuperclass();
                }
                if( m != null ) {

                    m.setAccessible(true);
                    m.invoke(component, g);
                }
            }
            catch(Exception e) {

                e.printStackTrace();
            }
            g.translate(chain.get(i-1).getX(), chain.get(i-1).getY());
        }
    }

    private BufferedImage[] getShadowPieces() {

        assert SwingUtilities.isEventDispatchThread();

        if( shadows == null ) {

            Rectangle bufferBounds = new Rectangle(0, 0, size * 10, size * 10);
            int widthFix = bufferBounds.width - (size * 2);
            int heightFix = bufferBounds.height - (size * 2);

            BufferedImage shadow = new BufferedImage(
                    bufferBounds.width,
                    bufferBounds.height,
                    BufferedImage.TYPE_INT_ARGB);

            Graphics2D imgGraphics = shadow.createGraphics();
            imgGraphics.setColor(color);

            imgGraphics.fillRect(size, size, widthFix, heightFix);

            imgGraphics.setComposite(AlphaComposite.getInstance(
                    AlphaComposite.SRC_IN, alpha));
            imgGraphics.fillRect(0, 0, shadow.getWidth(), shadow.getHeight());
            imgGraphics.dispose();

            GaussianFilter filter = new GaussianFilter(size * 2);
            shadow = filter.filter(shadow, null);

            Edge[] edges = Edge.values();
            shadows = new BufferedImage[edges.length];
            Rectangle r = new Rectangle();
            for(Edge edge : edges) {

                edge.locate(bufferBounds, insetsMax, r);
                shadows[edge.ordinal()] = shadow.getSubimage(r.x, r.y, r.width, r.height);
            }
        }
        return shadows;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Insets getBorderInsets(Component c) {

        // defensive copy
        return (Insets)insetsActual.clone();
    }

    @Override
    public boolean isBorderOpaque() {

        return true;
    }

    private static enum Edge {

        TopLeft {

            @Override
            int x(Rectangle r, Insets i) {

                return r.x;
            }

            @Override
            int y(Rectangle r, Insets i) {

                return r.y;
            }

            @Override
            int width(Rectangle r, Insets i) {

                return i.left;
            }

            @Override
            int height(Rectangle r, Insets i) {

                return i.top;
            }
        },

        Left {

            @Override
            int x(Rectangle r, Insets i) {

                return r.x;
            }

            @Override
            int y(Rectangle r, Insets i) {

                return r.y + i.top;
            }

            @Override
            int width(Rectangle r, Insets i) {

                return i.left;
            }

            @Override
            int height(Rectangle r, Insets i) {

                return r.height - i.top - i.bottom;
            }

            @Override
            void fill(ShadowBorder b, Graphics2D g, Rectangle r) {

                BufferedImage image = b.getShadowPieces()[ordinal()];

                Rectangle clipRec = new Rectangle();
                locate(r, b.insetsMax, clipRec);

                Shape originalClip = g.getClip();
                Area clip = new Area(originalClip);
                clip.intersect(new Area(clipRec));
                g.setClip(clip);

                for(int x = clipRec.x, y = clipRec.y;
                    y < clipRec.y + clipRec.height;
                    y += image.getHeight()) {

                    g.drawImage(image, x, y, null);
                }

                g.setClip(originalClip);
            }
        },

        BottomLeft {

            @Override
            int x(Rectangle r, Insets i) {

                return r.x;
            }

            @Override
            int y(Rectangle r, Insets i) {

                return r.y + r.height - i.bottom;
            }

            @Override
            int width(Rectangle r, Insets i) {

                return i.left;
            }

            @Override
            int height(Rectangle r, Insets i) {

                return i.bottom;
            }
        },

        Bottom {

            @Override
            int x(Rectangle r, Insets i) {

                return r.x + i.left;
            }

            @Override
            int y(Rectangle r, Insets i) {

                return r.y + r.height - i.bottom;
            }

            @Override
            int width(Rectangle r, Insets i) {

                return r.width - i.left - i.right;
            }

            @Override
            int height(Rectangle r, Insets i) {

                return i.bottom;
            }

            @Override
            void fill(ShadowBorder b, Graphics2D g, Rectangle r) {

                BufferedImage image = b.getShadowPieces()[ordinal()];

                Rectangle clipRec = new Rectangle();
                locate(r, b.insetsMax, clipRec);

                Shape originalClip = g.getClip();
                Area clip = new Area(originalClip);
                clip.intersect(new Area(clipRec));
                g.setClip(clip);

                for(int x = clipRec.x, y = clipRec.y;
                    x < clipRec.x + clipRec.width;
                    x += image.getWidth()) {

                    g.drawImage(image, x, y, null);
                }

                g.setClip(originalClip);
            }
        },

        BottomRight {

            @Override
            int x(Rectangle r, Insets i) {

                return r.x + r.width - i.right;
            }

            @Override
            int y(Rectangle r, Insets i) {

                return r.y + r.height - i.bottom;
            }

            @Override
            int width(Rectangle r, Insets i) {

                return i.right;
            }

            @Override
            int height(Rectangle r, Insets i) {

                return i.bottom;
            }
        },

        Right {

            @Override
            int x(Rectangle r, Insets i) {

                return r.x + r.width - i.right;
            }

            @Override
            int y(Rectangle r, Insets i) {

                return r.y + i.top;
            }

            @Override
            int width(Rectangle r, Insets i) {

                return i.right;
            }

            @Override
            int height(Rectangle r, Insets i) {

                return r.height - i.top - i.bottom;
            }

            @Override
            void fill(ShadowBorder b, Graphics2D g, Rectangle r) {

                BufferedImage image = b.getShadowPieces()[ordinal()];

                Rectangle clipRec = new Rectangle();
                locate(r, b.insetsMax, clipRec);

                Shape originalClip = g.getClip();
                Area clip = new Area(originalClip);
                clip.intersect(new Area(clipRec));
                g.setClip(clip);

                for(int x = clipRec.x, y = clipRec.y;
                    y < clipRec.y + clipRec.height;
                    y += image.getHeight()) {

                    g.drawImage(image, x, y, null);
                }

                g.setClip(originalClip);
            }
        },

        TopRight {

            @Override
            int x(Rectangle r, Insets i) {

                return r.x + r.width - i.right;
            }

            @Override
            int y(Rectangle r, Insets i) {

                return r.y;
            }

            @Override
            int width(Rectangle r, Insets i) {

                return i.right;
            }

            @Override
            int height(Rectangle r, Insets i) {

                return i.top;
            }
        },

        Top {

            @Override
            int x(Rectangle r, Insets i) {

                return r.x + i.left;
            }

            @Override
            int y(Rectangle r, Insets i) {

                return r.y;
            }

            @Override
            int width(Rectangle r, Insets i) {

                return r.width - i.left - i.right;
            }

            @Override
            int height(Rectangle r, Insets i) {

                return i.top;
            }

            @Override
            void fill(ShadowBorder b, Graphics2D g, Rectangle r) {

                BufferedImage image = b.getShadowPieces()[ordinal()];

                Rectangle clipRec = new Rectangle();
                locate(r, b.insetsMax, clipRec);

                Shape originalClip = g.getClip();
                Area clip = new Area(originalClip);
                clip.intersect(new Area(clipRec));
                g.setClip(clip);

                for(int x = clipRec.x, y = clipRec.y;
                    x < clipRec.x + clipRec.width;
                    x += image.getWidth()) {

                    g.drawImage(image, x, y, null);
                }

                g.setClip(originalClip);
            }
        };

        abstract int x(Rectangle r, Insets i);
        abstract int y(Rectangle r, Insets i);
        abstract int width(Rectangle r, Insets i);
        abstract int height(Rectangle r, Insets i);

        void locate(Rectangle b, Insets i, Rectangle r) {

            r.x = x(b, i);
            r.y = y(b, i);
            r.width = width(b, i);
            r.height = height(b, i);
        }

        void fill(ShadowBorder b, Graphics2D g, Rectangle r) {

            BufferedImage image = b.getShadowPieces()[ordinal()];

            Rectangle clipRec = new Rectangle();
            locate(r, b.insetsMax, clipRec);

            Shape originalClip = g.getClip();
            Area clip = new Area(originalClip);
            clip.intersect(new Area(clipRec));
            g.setClip(clip);

            g.drawImage(image, clipRec.x, clipRec.y, null);

            g.setClip(originalClip);
        }
    }
}