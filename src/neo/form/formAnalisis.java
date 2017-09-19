/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neo.form;

import java.awt.EventQueue;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.swing.JFrame;
import neo.table.Dataset;
import neo.table.deskriptif;

/**
 *
 * @author SEED
 */
public class formAnalisis extends javax.swing.JPanel {

    /**
     * Creates new form formAnalisis
     */
    public formAnalisis() {
        initComponents();
//        List<Double> testData = IntStream.range(1, 100)
//                    .mapToDouble(d -> d)
//                    .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
//            deskriptif stat = new deskriptif();
//             stat.setNameDesc("foo");
//             list1.add(stat);
//             testData.forEach((v) -> stat.addValue(v));           
    }    

    public formAnalisis(List<Dataset> data) {
        initComponents();        
        Class<?> myType = Double.TYPE;
        for (Field field : Dataset.class.getDeclaredFields()) {
            Class<?> type = field.getType();
            if ( (type.equals(Double.class)) || (type.equals(Integer.class))) {
                deskriptif stat = new deskriptif();
                try {
                        List<Double> beanList = getBeanList(field.getName(), data);
                        beanList.forEach(stat::addValue);
                        Map<Double, Long> collect = beanList
                                .stream()
                                .collect( Collectors.groupingBy( d -> d, Collectors.counting()));
                        System.out.println("collect = " + collect);
                        Double key = collect.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();
                        stat.setModeValue(key);
                } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException ex) {
                    Logger.getLogger(formAnalisis.class.getName()).log(Level.SEVERE, null, ex);
                }
                stat.setNameDesc(field.getName());
                list1.add(stat);
           }
            }            

    }    
     private List<Double> getBeanList(String bean,List<Dataset> data) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException
     {
         System.out.println("bean = " + bean);
          List<Double> value = new LinkedList<>();
          List<String> nope = new LinkedList<>();
//          nope.add("changeSupport");
//          nope.add("serialVersionUID");
//          nope.add("id");
//          nope.add("division");
//          if (nope.contains(bean)) {
//              System.out.print("\tnope");
//             return null;
//         }

         for (Dataset dataset : data) {
             Field field = Dataset.class.getDeclaredField(bean);
             field.setAccessible(true);
             if (field.get(dataset) instanceof Double) {
                 value.add((Double) field.get(dataset));                 
             }
             if (field.get(dataset) instanceof Integer) {
                 value.add((Integer) field.get(dataset) * 1d);                 
             }
         }         
         return value;
     }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        list1 = org.jdesktop.observablecollections.ObservableCollections.observableList(new LinkedList<neo.table.deskriptif>());
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        setLayout(new java.awt.GridLayout(1, 0));

        jTable2.setAutoCreateRowSorter(true);

        org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, list1, jTable2);
        org.jdesktop.swingbinding.JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${nameDesc}"));
        columnBinding.setColumnName("Name Desc");
        columnBinding.setColumnClass(String.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${n}"));
        columnBinding.setColumnName("N");
        columnBinding.setColumnClass(Long.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${min}"));
        columnBinding.setColumnName("Min");
        columnBinding.setColumnClass(Double.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${quartal1}"));
        columnBinding.setColumnName("Quartal1");
        columnBinding.setColumnClass(Double.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${quartal2}"));
        columnBinding.setColumnName("Quartal2");
        columnBinding.setColumnClass(Double.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${quartal3}"));
        columnBinding.setColumnName("Quartal3");
        columnBinding.setColumnClass(Double.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${max}"));
        columnBinding.setColumnName("Max");
        columnBinding.setColumnClass(Double.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${mean}"));
        columnBinding.setColumnName("Mean");
        columnBinding.setColumnClass(Double.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${modeValue}"));
        columnBinding.setColumnName("Mode");
        columnBinding.setColumnClass(Double.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${sum}"));
        columnBinding.setColumnName("Sum");
        columnBinding.setColumnClass(Double.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${variance}"));
        columnBinding.setColumnName("Variance");
        columnBinding.setColumnClass(Double.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${standardDeviation}"));
        columnBinding.setColumnName("Standard Deviation");
        columnBinding.setColumnClass(Double.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${skewness}"));
        columnBinding.setColumnName("Skewness");
        columnBinding.setColumnClass(Double.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${quadraticMean}"));
        columnBinding.setColumnName("Quadratic Mean");
        columnBinding.setColumnClass(Double.class);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();
        jScrollPane2.setViewportView(jTable2);
        if (jTable2.getColumnModel().getColumnCount() > 0) {
            jTable2.getColumnModel().getColumn(2).setResizable(false);
            jTable2.getColumnModel().getColumn(3).setResizable(false);
        }

        add(jScrollPane2);

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String[] args) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(formOlahData.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(formOlahData.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(formOlahData.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(formOlahData.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame();

                EntityManager entityManager = java.beans.Beans.isDesignTime() ? null : javax.persistence.Persistence.createEntityManagerFactory("analisiKomparasiPU").createEntityManager();
                Query query = java.beans.Beans.isDesignTime() ? null : entityManager.createQuery("SELECT d FROM Dataset d ORDER BY d.id");
                List<Dataset> list = java.beans.Beans.isDesignTime() ? java.util.Collections.emptyList() : org.jdesktop.observablecollections.ObservableCollections.observableList(query.getResultList());         

                frame.setContentPane(new formAnalisis(list));
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private java.util.List<neo.table.deskriptif> list1;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}
