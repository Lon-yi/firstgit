package GameOfLife;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

public class GameOfLifeFrame extends JFrame {

    private JButton openFileBtn = new JButton("ѡ���ļ�");
    private JButton startGameBtn = new JButton("��ʼ��Ϸ");
    private JLabel durationPromtLabel = new JLabel("����ÿ��֡��(fps)");
    private JLabel transfromNumLabel = new JLabel("ת�����");
    private JTextField durationTextField = new JTextField("5");
    private JTextField transfromNumField = new JTextField("0");
    
    private boolean isStart = false;//��Ϸ�Ƿ�ʼ�ı�־
    private boolean stop = false;//��Ϸ�����ı�־

    private CellMatrix cellMatrix;
    private JPanel buttonPanel = new JPanel(new GridLayout(3, 2));//3��2��
    private JPanel gridPanel = new JPanel();

    private JTextField[][] textMatrix;

    private static final int DEFAULT_DURATION = 200;//����Ĭ�ϼ��200ms

    private int duration = DEFAULT_DURATION;//�������

    public void settransfromNumField() {
    	transfromNumField.setText(Integer.toString(cellMatrix.getTransfromNum()));
    }
    
    public GameOfLifeFrame() {
        setTitle("������Ϸ");
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
        //���ô�������
        this.setLocation(250, 0);
        this.setSize(1000, 1200);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    private class OpenFileActioner implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fcDlg = new JFileChooser(".");
            fcDlg.setDialogTitle("��ѡ���ʼ�����ļ�");
            int returnVal = fcDlg.showOpenDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {

                isStart = false;
                stop = true;
                startGameBtn.setText("��ʼ��Ϸ");

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
     * ������ʾ��gridlayout���񲼾�
     */
    private void initGridLayout() {
        int rows = cellMatrix.getHeight();//��
        int cols = cellMatrix.getWidth();//��
        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(rows, cols));//ָ��������������
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
            if (!isStart) {//��ʼ״̬Ϊ��ͣ
                //��ȡ���ı��������ʱ������ת��Ϊ����
                try {
                    duration = 1000*1/Integer.parseInt(durationTextField.getText().trim());
                    //getText().trim()������ȥ��get�����ı������ݵĿո�
                } catch (NumberFormatException e1) {
                    duration = DEFAULT_DURATION;
                }

                new Thread(new GameControlTask()).start();
                isStart = true;
                stop = false;
                startGameBtn.setText("��ͣ��Ϸ");//���button����Ϸ��ʼ��button��ǩ��Ϊ��ͣ��Ϸ
            } else {//��ʼ״̬Ϊ��ʼ
                stop = true;
                isStart = false;
                startGameBtn.setText("��ʼ��Ϸ");//���button����Ϸ��ͣ��button��ǩ��Ϊ��ʼ��Ϸ
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
                    TimeUnit.MILLISECONDS.sleep(duration);//ÿn֡����֮���߳�˯��
                } catch (InterruptedException ex) 
                {
                    ex.printStackTrace();
                }
            }
        }
    }

}