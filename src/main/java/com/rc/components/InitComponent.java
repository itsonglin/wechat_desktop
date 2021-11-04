package com.rc.components;

/**
 * @author song
 * @date 21-11-4 16:01
 * @description
 * @since
 */
public interface InitComponent
{
    default void initialize()
    {
        initComponents();
        initView();
        setListeners();
    }

    void initComponents();

    void initView();

    void setListeners();
}
