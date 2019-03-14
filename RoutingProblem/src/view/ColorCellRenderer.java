package view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 *  Changing the cell renderer in order to colour the cells in the table and update their values.
 */

public class ColorCellRenderer extends DefaultTableCellRenderer
{
    private Map<Integer,Color> colors=new HashMap<Integer,Color>();

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        TableModel t=table.getModel();

        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
        if ((Integer)value==-1)
            setBackground(Color.GRAY);
        if ((Integer)value==0)
            setBackground(Color.WHITE);
        if((Integer)value<-2) {
            setBackground(Color.RED);
            int height=-(Integer)value-3;
            setValue("P"+height);
        }
        if((Integer)value==-2) {
            setBackground(Color.GREEN);
            setValue("R");
        }

        if ((Integer)value>0) {
            if(colors.get( value)==null) {
                Random rand = new Random();
                Color randomColor = new Color(rand.nextFloat() / 2f + 0.5f, rand.nextFloat() / 2f + 0.5f, rand.nextFloat() / 2f + 0.5f);
                colors.put((Integer)value,randomColor);
            }
            setBackground(colors.get(value));
        }
        return c;
    }

}
