package GameOfLife;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

public class GameOfLifeFrame extends JFrame {

    private JButton openFileBtn = new JButton("选择文件");
    private JButton startGameBtn = new JButton("开始游戏");
    private JLabel durationPromtLabel = new JLabel("设置每秒帧数(fps)");
    private JLabel transfromNumLabel = new JLabel("转变次数");
    private JTextField durationTextField = new JTextField("5");
    private JTextField transfromNumField = new JTextField("0");
    
    private boolean isStart = false;//游戏是否开始的标志
    private boolean stop = false;//游戏结束的标志

    private CellMatrix cellMatrix;
    private JPanel buttonPanel = new JPanel(new GridLayout(3, 2));//3行2列
    private JPanel gridPanel = new JPanel();

    private JTextField[][] textMatrix;

    private static final int DEFAULT_DURATION = 200;//动画默认间隔200ms

    private int duration = DEFAULT_DURATION;//动画间隔

    public void settransfromNumField() {
    	transfromNumField.setText(Integer.toString(cellMatrix.getTransfromNum()));
    }
    
    public GameOfLifeFrame() {
        setTitle("生命游戏");
        openFileBtn.addActionListener(new OpenFileActioner());
        startGameBtn.addActionListener(new StartGameActioner());

        buttonPanel.add(openFileBtn);
        buttonPanel.add(startGameBtn);
        buttonPanel.add(durationPromtLabel);
        buttonPanel.add(durationTextField);
        buttonPanel.add(transfromNumLabel);
        buttonPanel.add(transfromNumField);
        buttonPanel.setBackground(Color.WHITE);
        getContentPane().add("North", buttonPanel);
        //设置窗口属性
        this.setLocation(250, 0);
        this.setSize(1000, 1200);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    private class OpenFileActioner implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fcDlg = new JFileChooser(".");
            fcDlg.setDialogTitle("请选择初始配置文件");
            int returnVal = fcDlg.showOpenDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {

                isStart = false;
                stop = true;
                startGameBtn.setText("开始游戏");

                String filepath = fcDlg.getSelectedFile().getPath();
                cellMatrix = Utils.initMatrixFromFile(filepath);
                initGridLayout();
                showMatrix();
                gridPanel.updateUI();
            }
        }


    }

    private void showMatrix() {

        int[][] matrix = cellMatrix.getMatrix();
        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[0].length; x++) {
                if (matrix[y][x] == 1) {
                    textMatrix[y][x].setBackground(new Color(32,98,40));
                } else {
                    textMatrix[y][x].setBackground(new Color(226,245,226));
                }
            }
        }
    }

    /**
     * 创建显示的gridlayout网格布局
     */
    private void initGridLayout() {
        int rows = cellMatrix.getHeight();//行
        int cols = cellMatrix.getWidth();//列
        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(rows, cols));//指定容器的行列数
        textMatrix = new JTextField[rows][cols];
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                JTextField text = new JTextField();
                textMatrix[y][x] = text;
                gridPanel.add(text);
            }
        }
        this.add("Center", gridPanel);
    }


    private class StartGameActioner implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!isStart) {//初始状态为暂停
                //获取从文本框输入的时间间隔，转化为整型
                try {
                    duration = 1000*1/Integer.parseInt(durationTextField.getText().trim());
                    //getText().trim()作用是去除get到的文本框内容的空格
                } catch (NumberFormatException e1) {
                    duration = DEFAULT_DURATION;
                }

                new Thread(new GameControlTask()).start();
                isStart = true;
                stop = false;
                startGameBtn.setText("暂停游戏");//点击button后游戏开始，button标签变为暂停游戏
            } else {//初始状态为开始
                stop = true;
                isStart = false;
                startGameBtn.setText("开始游戏");//点击button后游戏暂停，button标签变为开始游戏
            }
        }
    }

    private class GameControlTask implements Runnable {
    	@Override
        public void run() {	
            while (!stop)
            {
                cellMatrix.transform();
                showMatrix();
                settransfromNumField();
                cellMatrix.changeTransfromNum();
                try {
                    TimeUnit.MILLISECONDS.sleep(duration);//每n帧动画之间线程睡眠
                } catch (InterruptedException ex) 
                {
                    ex.printStackTrace();
                }
            }
        }
    }

}