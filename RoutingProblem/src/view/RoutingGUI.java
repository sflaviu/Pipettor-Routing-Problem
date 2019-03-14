package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import java.awt.event.ActionListener;

public class RoutingGUI extends JFrame {
    private JPanel panel;
    private JScrollPane panMap;
    private JTable tableMap;
    private JButton btnStart;
    private JLabel lblRoute;
    private JLabel lblRouteStart;
    private JLabel lblRouteEnd;
    private JRadioButton pointAButton;
    private JRadioButton pointBButton;
    private JRadioButton pointCButton;
    private JRadioButton pointDButton;

    private ButtonGroup bg;

    public RoutingGUI(Object[][] data, Object[] columns, TableCellRenderer tr)
    {
        int height=data.length;
        int width=data[0].length;

        setTitle("RoutingGUI");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        int assignWidth=(width+1)*30+50;
        if(assignWidth<780)
            assignWidth=780;

        setBounds(100,100,assignWidth,(height+1)*17+200);

        panel=new JPanel();
        panel.setBorder(new EmptyBorder(5,5,5,5));
        setContentPane(panel);
        panel.setLayout(null);

        tableMap=new JTable(data,columns);
        tableMap.setEnabled(false);

        for(int j=0; j<data[0].length;j++)
            tableMap.getColumnModel().getColumn(j).setCellRenderer(tr);

        panMap=new JScrollPane(tableMap);
        panMap.setBounds(20,20,(width+1)*30,(height+1)*17);
        panel.add(panMap);


        btnStart=new JButton("Start next route!");
        btnStart.setBounds(20,(height+1)*17+50,150,30);
        btnStart.setActionCommand("btnStart");
        panel.add(btnStart);

        lblRoute=new JLabel("Route number: ");
        lblRoute.setBounds(185,(height+1)*17+50,150,30);
        panel.add(lblRoute);

        lblRouteStart=new JLabel("Route start: ");
        lblRouteStart.setBounds(300,(height+1)*17+20,150,30);
        panel.add(lblRouteStart);

        lblRouteEnd=new JLabel("Route end: ");
        lblRouteEnd.setBounds(300,(height+1)*17+80,150,30);
        panel.add(lblRouteEnd);

        bg=new ButtonGroup();

        pointAButton = new JRadioButton("(A) No contamination");
        pointAButton.setBounds(420,(height+1)*17+20,300,30);
        panel.add(pointAButton);
        bg.add(pointAButton);

        pointBButton = new JRadioButton("(B) Shortest path (allows contamination)");
        pointBButton.setBounds(420,(height+1)*17+50,300,30);
        panel.add(pointBButton);
        bg.add(pointBButton);

        pointCButton = new JRadioButton( "(C) Height map (+ same conditions as B)");
        pointCButton.setBounds(420,(height+1)*17+80,300,30);
        panel.add(pointCButton);
        bg.add(pointCButton);

        pointDButton = new JRadioButton( "(D) Acceleration (+ same conditions as C)");
        pointDButton.setBounds(420,(height)*17+125,300,30);
        panel.add(pointDButton);
        bg.add(pointDButton);

        pointDButton.setSelected(true);
    }

    public String getSelectedAction()
    {
        if(pointAButton.isSelected())
            return "A";
        if(pointBButton.isSelected())
            return "B";
        if(pointCButton.isSelected())
            return "C";
        if(pointDButton.isSelected())
            return "D";
        return "";
    }
    public void setStartBtnListener(ActionListener al) {
        btnStart.addActionListener(al);
    }
    public void redrawCell(int row,int col,int val) {
        tableMap.getModel().setValueAt(val, row, col);
    }

    public void updateRouteLabel(int routeNr) {
        lblRoute.setText("Route number: "+routeNr);
    }

    public void updateRouteStartLabel(int routeStartRow,int routeStartCol) {
        lblRouteStart.setText("Route start: ("+routeStartRow+","+routeStartCol+")");
    }
    public void updateRouteEndLabel(int routeEndRow,int routeEndCol) {
        lblRouteEnd.setText("Route end ("+routeEndRow+","+routeEndCol+")");
    }

    public void dislabeStartButton() {
        btnStart.setEnabled(false);
    }
    public void endableStartButton() {
        btnStart.setEnabled(true);
    }
}
