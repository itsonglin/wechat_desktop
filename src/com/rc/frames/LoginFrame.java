package com.rc.frames;

import com.rc.app.Launcher;
import com.rc.components.*;
import com.rc.db.model.CurrentUser;
import com.rc.db.service.CurrentUserService;
import com.rc.listener.AbstractMouseListener;
import com.rc.utils.*;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONObject;
import com.rc.tasks.HttpPostTask;
import com.rc.tasks.HttpResponseListener;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by song on 08/06/2017.
 */
public class LoginFrame extends JFrame
{
    private static final int windowWidth = 300;
    private static final int windowHeight = 400;

    private JPanel controlPanel;
    private JLabel closeLabel;
    private JPanel editPanel;
    private RCTextField usernameField;
    private RCPasswordField passwordField;
    private RCButton loginButton;
    private JLabel statusLabel;
    private JLabel titleLabel;

    private static Point origin = new Point();

    private SqlSession sqlSession;
    private CurrentUserService currentUserService ;
    private String username;


    public LoginFrame()
    {
        initService();
        initComponents();
        initView();
        centerScreen();
        setListeners();
    }

    public LoginFrame(String username)
    {
        this();
        this.username = username;
        if (username != null && !username.isEmpty())
        {
            usernameField.setText(username);
        }
    }

    private void initService()
    {
        sqlSession = DbUtils.getSqlSession();
        currentUserService = new CurrentUserService(sqlSession);
    }


    private void initComponents()
    {
        Dimension windowSize = new Dimension(windowWidth, windowHeight);
        setMinimumSize(windowSize);
        setMaximumSize(windowSize);


        controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        //controlPanel.setBounds(0,5, windowWidth, 30);

        closeLabel = new JLabel();
        closeLabel.setIcon(IconUtil.getIcon(this, "/image/close.png"));
        closeLabel.setHorizontalAlignment(JLabel.CENTER);
        //closeLabel.setPreferredSize(new Dimension(30,30));
        closeLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        titleLabel = new JLabel();
        titleLabel.setText("登  录");
        titleLabel.setFont(FontUtil.getDefaultFont(16));


        editPanel = new JPanel();
        editPanel.setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP, 0, 5, true, false));

        Dimension textFieldDimension = new Dimension(200, 35);
        usernameField = new RCTextField();
        usernameField.setPlaceholder("用户名");
        usernameField.setPreferredSize(textFieldDimension);
        usernameField.setFont(FontUtil.getDefaultFont(14));
        usernameField.setForeground(Colors.FONT_BLACK);
        usernameField.setMargin(new Insets(0, 15, 0, 0));

        passwordField = new RCPasswordField();
        passwordField.setPreferredSize(textFieldDimension);
        passwordField.setPlaceholder("密码");
        //passwordField.setBorder(new RCBorder(RCBorder.BOTTOM, Colors.LIGHT_GRAY));
        passwordField.setFont(FontUtil.getDefaultFont(14));
        passwordField.setForeground(Colors.FONT_BLACK);
        passwordField.setMargin(new Insets(0, 15, 0, 0));


        loginButton = new RCButton("登 录", Colors.MAIN_COLOR, Colors.MAIN_COLOR_DARKER, Colors.MAIN_COLOR_DARKER);
        loginButton.setFont(FontUtil.getDefaultFont(14));
        loginButton.setPreferredSize(new Dimension(200, 40));

        statusLabel = new JLabel();
        statusLabel.setForeground(Colors.RED);
        statusLabel.setText("密码不正确");
        statusLabel.setVisible(false);
    }

    private void initView()
    {
        JPanel contentPanel = new JPanel();
        contentPanel.setBorder(new LineBorder(Colors.LIGHT_GRAY));
        contentPanel.setLayout(new GridBagLayout());

        controlPanel.add(closeLabel);

        if (OSUtil.getOsType() != OSUtil.Mac_OS)
        {
            setUndecorated(true);
            contentPanel.add(controlPanel, new GBC(0, 0).setFill(GBC.BOTH).setWeight(1, 1).setInsets(5, 0, 0, 0));
        }

        JPanel titlePanel = new JPanel();
        titlePanel.add(titleLabel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        buttonPanel.add(loginButton, new GBC(0, 0).setFill(GBC.HORIZONTAL).setWeight(1, 1).setInsets(10, 0, 0, 0));

        editPanel.add(usernameField);
        editPanel.add(passwordField);
        editPanel.add(buttonPanel);

        editPanel.add(statusLabel);



        add(contentPanel);
        contentPanel.add(titlePanel, new GBC(0, 1).setFill(GBC.BOTH).setWeight(1, 1).setInsets(10, 10, 0, 10));
        contentPanel.add(editPanel, new GBC(0, 2).setFill(GBC.BOTH).setWeight(1, 10).setInsets(10, 10, 0, 10));
    }

    /**
     * 使窗口在屏幕中央显示
     */
    private void centerScreen()
    {
        Toolkit tk = Toolkit.getDefaultToolkit();
        this.setLocation((tk.getScreenSize().width - windowWidth) / 2,
                (tk.getScreenSize().height - windowHeight) / 2);
    }

    private void setListeners()
    {
        closeLabel.addMouseListener(new AbstractMouseListener()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                System.exit(1);
                super.mouseClicked(e);
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
                closeLabel.setBackground(Colors.LIGHT_GRAY);
                super.mouseEntered(e);
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                closeLabel.setBackground(Colors.WINDOW_BACKGROUND);
                super.mouseExited(e);
            }
        });

        if (OSUtil.getOsType() != OSUtil.Mac_OS)
        {
            addMouseListener(new MouseAdapter()
            {
                public void mousePressed(MouseEvent e)
                {
                    // 当鼠标按下的时候获得窗口当前的位置
                    origin.x = e.getX();
                    origin.y = e.getY();
                }
            });

            addMouseMotionListener(new MouseMotionAdapter()
            {
                public void mouseDragged(MouseEvent e)
                {
                    // 当鼠标拖动时获取窗口当前位置
                    Point p = LoginFrame.this.getLocation();
                    // 设置窗口的位置
                    LoginFrame.this.setLocation(p.x + e.getX() - origin.x, p.y + e.getY()
                            - origin.y);
                }
            });
        }

        loginButton.addMouseListener(new AbstractMouseListener()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (loginButton.isEnabled())
                {
                    doLogin();
                }

                super.mouseClicked(e);
            }
        });

        KeyListener keyListener = new KeyListener()
        {
            @Override
            public void keyTyped(KeyEvent e)
            {
            }

            @Override
            public void keyPressed(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    doLogin();
                }
            }

            @Override
            public void keyReleased(KeyEvent e)
            {

            }
        };
        usernameField.addKeyListener(keyListener);
        passwordField.addKeyListener(keyListener);
    }

    private void doLogin()
    {

        this.dispose();

        MainFrame frame = new MainFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);


        // TODO: 登录逻辑
        /*String name = usernameField.getText();
        String pwd = new String(passwordField.getPassword());

        if (name == null || name.isEmpty())
        {
            showMessage("请输入用户，注意首字母可能为大写");
        }
        else if (pwd == null || pwd.isEmpty())
        {
            showMessage("请输入密码");
        }
        else
        {
            statusLabel.setVisible(false);

            loginButton.setEnabled(false);
            usernameField.setEditable(false);
            passwordField.setEditable(false);
            HttpPostTask task = new HttpPostTask();
            task.setListener(new HttpResponseListener<JSONObject>()
            {
                @Override
                public void onSuccess(JSONObject ret)
                {
                    processLoginResult(ret);
                }

                @Override
                public void onFailed()
                {
                    showMessage("登录失败，请检查网络设置");
                    loginButton.setEnabled(true);
                    usernameField.setEditable(true);
                    passwordField.setEditable(true);
                }
            });

            task.addRequestParam("username", usernameField.getText());
            task.addRequestParam("password", new String(passwordField.getPassword()));
            task.execute(Launcher.HOSTNAME + "/api/v1/login");
        }*/
    }

    private void processLoginResult(JSONObject ret)
    {
        if (ret.get("status").equals("success"))
        {

            JSONObject data = ret.getJSONObject("data");
            String authToken = data.getString("authToken");
            String userId = data.getString("userId");

            CurrentUser currentUser = new CurrentUser();
            currentUser.setUserId(userId);
            currentUser.setAuthToken(authToken);
            currentUser.setRawPassword(new String(passwordField.getPassword()));
            currentUser.setPassword(PasswordUtil.encryptPassword(currentUser.getRawPassword()));
            currentUser.setUsername(usernameField.getText());
            currentUserService.insertOrUpdate(currentUser);

            this.dispose();

            MainFrame frame = new MainFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        }
        else
        {
            showMessage("用户不存在或密码错误");
            loginButton.setEnabled(true);
            usernameField.setEditable(true);
            passwordField.setEditable(true);
        }

    }

    private void showMessage(String message)
    {
        if (!statusLabel.isVisible())
        {
            statusLabel.setVisible(true);
        }

        statusLabel.setText(message);
    }
}
