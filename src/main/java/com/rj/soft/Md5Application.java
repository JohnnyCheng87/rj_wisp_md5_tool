package com.rj.soft;

import com.rj.soft.utils.Md5Utils;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

/**
 * MD5程序
 * @author cjy
 * @since 2019年4月30日
 * @version 1.0
 */
public class Md5Application extends JFrame implements ActionListener, ItemListener {

    private static Logger log = Logger.getLogger(Md5Application.class);

    /**
     * 画布
     */
    private JPanel panel;

    /**
     * 文件选择器
     */
    private JFileChooser fileChooser;

    /**
     * 源文件
     */
    private JLabel srcLabel;

    private JTextField srcField;

    /**
     * 目标文件
     */
    private JLabel destLabel;

    private JTextField destField;

    /**
     * 选择源文件按钮
     */
    private JButton chooseSrcBtn;

    /**
     * 选择目标文件按钮
     */
    private JButton chooseDestBtn;

    /**
     * 校验结果
     */
    private JLabel resultLabel;

    /**
     * 校验结果区域
     */
    private JTextArea resultText;

    /**
     * 校验按钮
     */
    private JButton validBtn;

    /**
     * 退出按钮
     */
    private JButton exitBtn;

    public Md5Application() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        setTitle("MD5校验程序");
        setSize(700, 350);
        initUI();
        setLayout(null);
        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);

        // 窗口事件
        WindowListener winCloser = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                winClose();
            }
        };
        addWindowListener(winCloser);
    }

    /**
     * 初始化UI界面
     */
    public void initUI() {
        panel = new JPanel();
        fileChooser = new JFileChooser();

        srcLabel = new JLabel("源文件: ", JLabel.RIGHT);
        srcLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));
        srcLabel.setBounds(30, 28, 180, 30);
        srcField = new JTextField();
        srcField.setFont(new Font("微软雅黑", Font.BOLD, 12));
        srcField.setBounds(230, 33, 300, 20);

        destLabel = new JLabel("目标文件: ", JLabel.RIGHT);
        destLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));
        destLabel.setBounds(30, 75, 180, 30);
        destField = new JTextField();
        destField.setFont(new Font("微软雅黑", Font.BOLD, 12));
        destField.setBounds(230, 80, 300, 20);

        chooseSrcBtn = new JButton("选择文件");
        chooseSrcBtn.setName("chooseSrc");
        chooseSrcBtn.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        chooseSrcBtn.setBounds(550, 30, 100, 30);
        chooseSrcBtn.setForeground(Color.BLUE);
        chooseSrcBtn.addActionListener(this);

        chooseDestBtn = new JButton("选择文件");
        chooseDestBtn.setName("chooseDest");
        chooseDestBtn.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        chooseDestBtn.setBounds(550, 78, 100, 30);
        chooseDestBtn.setForeground(Color.BLUE);
        chooseDestBtn.addActionListener(this);

        resultLabel = new JLabel("校验结果: ", JLabel.RIGHT);
        resultLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));
        resultLabel.setBounds(30, 122, 180, 30);
        resultText = new JTextArea();
        resultText.setFont(new Font("微软雅黑", Font.BOLD, 12));
        resultText.setBorder(new LineBorder(Color.BLACK, 1));
        resultText.setBounds(230, 127, 400, 100);
        resultText.setLineWrap(true);
        resultText.setWrapStyleWord(true);
        resultText.setForeground(Color.GREEN);

        validBtn = new JButton("校验");
        validBtn.setName("validBtn");
        validBtn.addActionListener(this);
        validBtn.setBounds(230, 270, 70, 30);
        validBtn.setFont(new Font("微软雅黑", Font.PLAIN, 12));

        exitBtn = new JButton("退出");
        exitBtn.setName("exitBtn");
        exitBtn.addActionListener(this);
        exitBtn.setBounds(330, 270, 70, 30);
        exitBtn.setFont(new Font("微软雅黑", Font.PLAIN, 12));

        this.getContentPane().add(srcLabel);
        this.getContentPane().add(srcField);
        this.getContentPane().add(chooseSrcBtn);
        this.getContentPane().add(destLabel);
        this.getContentPane().add(destField);
        this.getContentPane().add(chooseDestBtn);
        this.getContentPane().add(resultLabel);
        this.getContentPane().add(resultText);
        this.getContentPane().add(validBtn);
        this.getContentPane().add(exitBtn);
    }

    /**
     * 窗口关闭事件
     */
    public void winClose() {
        System.exit(0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        if (button.getName().equals("chooseSrc")) {
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int show = fileChooser.showOpenDialog(panel);
            File selectedFile = fileChooser.getSelectedFile();
            if (selectedFile != null && show == JFileChooser.APPROVE_OPTION) {
                srcField.setText(selectedFile.getAbsolutePath());
            }
        } else if (button.getName().equals("chooseDest")) {
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int show = fileChooser.showOpenDialog(panel);
            File selectedFile = fileChooser.getSelectedFile();
            if (selectedFile != null && show == JFileChooser.APPROVE_OPTION) {
                destField.setText(selectedFile.getAbsolutePath());
            }
        } else if (button.getName().equals("validBtn")) {
            if (this.checkInput()) {
                // 源文件
                File srcFile = new File(srcField.getText().trim());
                // 目标文件
                File destFile = new File(destField.getText().trim());
                String srcMd5 = null;
                String destMd5 = null;
                try {
                    srcMd5 = Md5Utils.getFileMD5String(srcFile);
                    destMd5 = Md5Utils.getFileMD5String(destFile);
                } catch (Exception ex) {
                    log.error("读取文件MD5异常", ex);
                }
                // 校验结果
                StringBuffer sb = new StringBuffer();
                sb.append("源文件MD5：" + srcMd5);
                sb.append("\n");
                sb.append("目标文件MD5：" + destMd5);
                sb.append("\n");
                sb.append(srcMd5.equals(destMd5) ? "相同" : "不相同");
                resultText.setText(sb.toString());
            } else {
                return;
            }
        } else if (button.getName().equals("exitBtn")) {
            winClose();
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {

    }

    public boolean checkInput() {
        if (null == srcField.getText() || "".equals(srcField.getText())) {
            showMessage("请选择源文件!", "系统提示", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (null == destField.getText() || "".equals(destField.getText())) {
            showMessage("请选择目标文件!", "系统提示", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
    public void showMessage(String message, String title, int messageType) {
        JLabel label = new JLabel();
        label.setText(message);
        label.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        JOptionPane.showMessageDialog(Md5Application.this, label, title, messageType);
    }

    public int showConfirm(String message, String title, int optionType, int messageType) {
        JLabel label = new JLabel();
        label.setText(message);
        label.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        return JOptionPane.showConfirmDialog(Md5Application.this, label, title, optionType, messageType);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Md5Application();
            }
        });
    }

}
