/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Marco
 */
import java.util.concurrent.Semaphore;
import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.event.*;
public class Interfaz extends javax.swing.JFrame {
    
   class DoubleListener implements DocumentListener {
 
        public void insertUpdate(DocumentEvent e) {
            check(e);
        }
        public void removeUpdate(DocumentEvent e) {
            //check(e);
        }
        public void changedUpdate(DocumentEvent e) {
            //Plain text components do not fire these events
            check(e);
        }

        public void check(DocumentEvent e) {
            String content = "";
            try{
                Document doc = (Document)e.getDocument();
                content = doc.getText(0, doc.getLength());
            }catch(Exception exception){
                
            }
            
            try{
                Double.parseDouble(content);     
            }catch(NumberFormatException n){
                JOptionPane.showMessageDialog(null, "Por favor ingrese un número real positivo.", "Error Massage", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
   
   class IntegerListener implements DocumentListener {
 
        public void insertUpdate(DocumentEvent e) {
            check(e);
        }
        public void removeUpdate(DocumentEvent e) {
            //check(e);
        }
        public void changedUpdate(DocumentEvent e) {
            //Plain text components do not fire these events
            check(e);
        }

        public void check(DocumentEvent e) {
            String content = "";
            try{
                Document doc = (Document)e.getDocument();
                content = doc.getText(0, doc.getLength());
            }catch(Exception exception){
                
            }
            
            try{
                Integer.parseInt(content);     
            }catch(NumberFormatException n){
                JOptionPane.showMessageDialog(null, "Por favor ingrese un número entero positivo.", "Error Massage", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
   
   
    private Controlador controlador;
    private SistemaPintoDB sistema;
    private JTextField[] textFields;
    private Semaphore semEjecucion;
    
    /**
     * Creates new form Interfaz
     */
    public Interfaz(Controlador controlador, Semaphore semEjecucion) {
        initComponents();
        this.controlador = controlador;
        this.semEjecucion = semEjecucion;
        textFields = new JTextField[7];
        cantidadCorridasTF.getDocument().addDocumentListener(new IntegerListener());
        tiempoMaxTF.getDocument().addDocumentListener(new DoubleListener());
        kTF.getDocument().addDocumentListener(new IntegerListener());
        nTF.getDocument().addDocumentListener(new IntegerListener());
        pTF.getDocument().addDocumentListener(new IntegerListener());
        mTF.getDocument().addDocumentListener(new IntegerListener());
        tTF.getDocument().addDocumentListener(new DoubleListener());
        textFields[0] = cantidadCorridasTF;
        textFields[1] = tiempoMaxTF;
        textFields[2] = kTF;
        textFields[3] = nTF;
        textFields[4] = pTF;
        textFields[5] = mTF;
        textFields[6] = tTF;      
        
        this.setVisible(true);
    }
    
    private void updateUI() {
        SwingUtilities.updateComponentTreeUI(this);
    }
    
    public void actualizarInteractivo(){       
        
        this.relojL.setText("" + sistema.getReloj());
        this.conexionesDescartadasL.setText("" + sistema.getConexionesDescartadas());
        int[] tamanosColas = sistema.getTamanosColas();
        this.colaAP.setText("" + tamanosColas[0]);
        this.colaPC.setText("" + tamanosColas[1]);
        this.colaTAD.setText("" + tamanosColas[2]);
        this.colaES.setText("" + tamanosColas[3]);
    }
    
    public void corridaTerminada(){
        try{
            this.semEjecucion.acquire();
        }catch(Exception e){}
        
        double[] LQs = this.controlador.getLQs();
        this.colaAPPromedio.setText("" + LQs[0]);
        this.colaPCPromedio.setText("" + LQs[1]);
        this.colaTADPromedio.setText("" + LQs[2]);
        this.colaESPromedio.setText("" + LQs[3]);
        
        double[][] tiempoPromedioDeSentenciaPorModulo = controlador.getTiemposPasadosPorConsultaPorModulo();
        
        this.ddlTMAC.setText(Double.toString(tiempoPromedioDeSentenciaPorModulo[4][0]));
        this.ddlTMAP.setText("" + tiempoPromedioDeSentenciaPorModulo[0][0]);
        this.ddlTMPC.setText("" + tiempoPromedioDeSentenciaPorModulo[1][0]);
        this.ddlTMTAD.setText("" + tiempoPromedioDeSentenciaPorModulo[2][0]);
        this.ddlTMES.setText("" + tiempoPromedioDeSentenciaPorModulo[3][0]);
        
        this.selectTMAC.setText("" + tiempoPromedioDeSentenciaPorModulo[4][1]);
        this.selectTMAP.setText("" + tiempoPromedioDeSentenciaPorModulo[0][1]);
        this.selectTMPC.setText("" + tiempoPromedioDeSentenciaPorModulo[1][1]);
        this.selectTMTAD.setText("" + tiempoPromedioDeSentenciaPorModulo[2][1]);
        this.selectTMES.setText("" + tiempoPromedioDeSentenciaPorModulo[3][1]);
        
        this.joinTMAC.setText("" + tiempoPromedioDeSentenciaPorModulo[4][2]);
        this.joinTMAP.setText("" + tiempoPromedioDeSentenciaPorModulo[0][2]);
        this.joinTMPC.setText("" + tiempoPromedioDeSentenciaPorModulo[1][2]);
        this.joinTMTAD.setText("" + tiempoPromedioDeSentenciaPorModulo[2][2]);
        this.joinTMES.setText("" + tiempoPromedioDeSentenciaPorModulo[3][2]);
        
        this.updateTMAC.setText("" + tiempoPromedioDeSentenciaPorModulo[4][3]);
        this.updateTMAP.setText("" + tiempoPromedioDeSentenciaPorModulo[0][3]);
        this.updateTMPC.setText("" + tiempoPromedioDeSentenciaPorModulo[1][3]);
        this.updateTMTAD.setText("" + tiempoPromedioDeSentenciaPorModulo[2][3]);
        this.updateTMES.setText("" + tiempoPromedioDeSentenciaPorModulo[3][3]);
        
        double tiempoPromedioDeVida = this.controlador.getTiempoPromedioVidaConexion();
        this.tiempoPromVida.setText(""+ tiempoPromedioDeVida);
        this.conexDesc.setText(""+ this.sistema.getConexionesDescartadas());
        
        this.PanelInteractivo.setVisible(false);
        this.PanelFinCorridas.setVisible(true);
    }
    
    public void finSimulacion(){
        try{
            this.semEjecucion.acquire();
        }catch(Exception e){}
        
        double[] LQs = this.controlador.getLQsTotales();
        this.colaAPPromedioTotal.setText("" + LQs[0]);
        this.colaPCPromedioTotal.setText("" + LQs[1]);
        this.colaTADPromedioTotal.setText("" + LQs[2]);
        this.colaESPromedioTotal.setText("" + LQs[3]);
        
        double[][] tiempoPromedioDeSentenciaPorModulo = this.controlador.getTiemposPasadosPorConsultaPorModuloTotales();
        this.ddlTMACTotal.setText(Double.toString(tiempoPromedioDeSentenciaPorModulo[4][0]));
        this.ddlTMAPTotal.setText("" + tiempoPromedioDeSentenciaPorModulo[0][0]);
        this.ddlTMPCTotal.setText("" + tiempoPromedioDeSentenciaPorModulo[1][0]);
        this.ddlTMTADTotal.setText("" + tiempoPromedioDeSentenciaPorModulo[2][0]);
        this.ddlTMESTotal.setText("" + tiempoPromedioDeSentenciaPorModulo[3][0]);
        
        this.selectTMACTotal.setText("" + tiempoPromedioDeSentenciaPorModulo[4][1]);
        this.selectTMAPTotal.setText("" + tiempoPromedioDeSentenciaPorModulo[0][1]);
        this.selectTMPCTotal.setText("" + tiempoPromedioDeSentenciaPorModulo[1][1]);
        this.selectTMTADTotal.setText("" + tiempoPromedioDeSentenciaPorModulo[2][1]);
        this.selectTMESTotal.setText("" + tiempoPromedioDeSentenciaPorModulo[3][1]);
        
        this.joinTMACTotal.setText("" + tiempoPromedioDeSentenciaPorModulo[4][2]);
        this.joinTMAPTotal.setText("" + tiempoPromedioDeSentenciaPorModulo[0][2]);
        this.joinTMPCTotal.setText("" + tiempoPromedioDeSentenciaPorModulo[1][2]);
        this.joinTMTADTotal.setText("" + tiempoPromedioDeSentenciaPorModulo[2][2]);
        this.joinTMESTotal.setText("" + tiempoPromedioDeSentenciaPorModulo[3][2]);
        
        this.updateTMACTotal.setText("" + tiempoPromedioDeSentenciaPorModulo[4][3]);
        this.updateTMAPTotal.setText("" + tiempoPromedioDeSentenciaPorModulo[0][3]);
        this.updateTMPCTotal.setText("" + tiempoPromedioDeSentenciaPorModulo[1][3]);
        this.updateTMTADTotal.setText("" + tiempoPromedioDeSentenciaPorModulo[2][3]);
        this.updateTMESTotal.setText("" + tiempoPromedioDeSentenciaPorModulo[3][3]);
        
        double tiempoPromedioDeVida = this.controlador.getTiempoPromedioVidaConexionTotal();
        this.tiempoPromVidaTotal.setText(""+ tiempoPromedioDeVida);
        
        this.conexDescPromTotal.setText(""+ this.controlador.getConexionesDescartadasPromedioTotal());
        
        this.PanelInteractivo.setVisible(false);
        this.PanelFinal.setVisible(true);
        
    }
    
    
    public void setSistema(SistemaPintoDB sistema){
        this.sistema = sistema;
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PanelBienvenida = new javax.swing.JPanel();
        comenzarBTN = new javax.swing.JButton();
        BIENVENIDO = new javax.swing.JLabel();
        escritoPor = new javax.swing.JLabel();
        MarcoL = new javax.swing.JLabel();
        JavierL = new javax.swing.JLabel();
        ucr = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        PanelInitData = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        cantidadCorridasTF = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        tiempoMaxTF = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        ModoLentoCB = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        kTF = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        nTF = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        pTF = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        mTF = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        tTF = new javax.swing.JTextField();
        simularBTN = new javax.swing.JButton();
        PanelInteractivo = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        tablaColas = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        colaES = new javax.swing.JLabel();
        colaAP = new javax.swing.JLabel();
        colaPC = new javax.swing.JLabel();
        colaTAD = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        relojL = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        conexionesDescartadasL = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        corridaAct = new javax.swing.JLabel();
        PanelFinCorridas = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        conexDesc = new javax.swing.JLabel();
        tiempoPromVida = new javax.swing.JLabel();
        tablaColasPromedio = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        colaTADPromedio = new javax.swing.JLabel();
        colaPCPromedio = new javax.swing.JLabel();
        colaAPPromedio = new javax.swing.JLabel();
        colaESPromedio = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        selectTMES = new javax.swing.JLabel();
        selectTMAC = new javax.swing.JLabel();
        selectTMAP = new javax.swing.JLabel();
        selectTMPC = new javax.swing.JLabel();
        selectTMTAD = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        ddlTMES = new javax.swing.JLabel();
        ddlTMAC = new javax.swing.JLabel();
        ddlTMAP = new javax.swing.JLabel();
        ddlTMPC = new javax.swing.JLabel();
        ddlTMTAD = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        joinTMES = new javax.swing.JLabel();
        joinTMAC = new javax.swing.JLabel();
        joinTMAP = new javax.swing.JLabel();
        joinTMPC = new javax.swing.JLabel();
        joinTMTAD = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        updateTMES = new javax.swing.JLabel();
        updateTMAC = new javax.swing.JLabel();
        updateTMAP = new javax.swing.JLabel();
        updateTMPC = new javax.swing.JLabel();
        updateTMTAD = new javax.swing.JLabel();
        continuarSim = new javax.swing.JButton();
        PanelFinal = new javax.swing.JPanel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        conexDescPromTotal = new javax.swing.JLabel();
        tiempoPromVidaTotal = new javax.swing.JLabel();
        tablaColasPromedio1 = new javax.swing.JPanel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jLabel47 = new javax.swing.JLabel();
        colaTADPromedioTotal = new javax.swing.JLabel();
        colaPCPromedioTotal = new javax.swing.JLabel();
        colaAPPromedioTotal = new javax.swing.JLabel();
        colaESPromedioTotal = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jLabel48 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        selectTMESTotal = new javax.swing.JLabel();
        selectTMACTotal = new javax.swing.JLabel();
        selectTMAPTotal = new javax.swing.JLabel();
        selectTMPCTotal = new javax.swing.JLabel();
        selectTMTADTotal = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jLabel49 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        jLabel53 = new javax.swing.JLabel();
        jPanel22 = new javax.swing.JPanel();
        jLabel54 = new javax.swing.JLabel();
        jPanel23 = new javax.swing.JPanel();
        jLabel55 = new javax.swing.JLabel();
        jPanel24 = new javax.swing.JPanel();
        ddlTMESTotal = new javax.swing.JLabel();
        ddlTMACTotal = new javax.swing.JLabel();
        ddlTMAPTotal = new javax.swing.JLabel();
        ddlTMPCTotal = new javax.swing.JLabel();
        ddlTMTADTotal = new javax.swing.JLabel();
        jPanel25 = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        joinTMESTotal = new javax.swing.JLabel();
        joinTMACTotal = new javax.swing.JLabel();
        joinTMAPTotal = new javax.swing.JLabel();
        joinTMPCTotal = new javax.swing.JLabel();
        joinTMTADTotal = new javax.swing.JLabel();
        jPanel27 = new javax.swing.JPanel();
        jPanel28 = new javax.swing.JPanel();
        updateTMESTotal = new javax.swing.JLabel();
        updateTMACTotal = new javax.swing.JLabel();
        updateTMAPTotal = new javax.swing.JLabel();
        updateTMPCTotal = new javax.swing.JLabel();
        updateTMTADTotal = new javax.swing.JLabel();
        nuevaBTN = new javax.swing.JButton();
        salirBTN = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Simulación SistemaPintoDB");
        getContentPane().setLayout(new java.awt.CardLayout());

        PanelBienvenida.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        comenzarBTN.setBackground(new java.awt.Color(0, 255, 255));
        comenzarBTN.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        comenzarBTN.setText("Comenzar");
        comenzarBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comenzarBTNActionPerformed(evt);
            }
        });
        PanelBienvenida.add(comenzarBTN, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 220, -1, 50));

        BIENVENIDO.setFont(new java.awt.Font("American Typewriter", 1, 36)); // NOI18N
        BIENVENIDO.setForeground(new java.awt.Color(51, 51, 255));
        BIENVENIDO.setText("¡Bienvenido!");
        PanelBienvenida.add(BIENVENIDO, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 20, 260, 50));

        escritoPor.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        escritoPor.setText("Programa escrito por:");
        PanelBienvenida.add(escritoPor, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 120, 160, 30));

        MarcoL.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        MarcoL.setText("Marco Venegas   B67697");
        PanelBienvenida.add(MarcoL, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 150, 170, 20));

        JavierL.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        JavierL.setText("Javier Ruiz           B66384");
        PanelBienvenida.add(JavierL, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 170, 180, 30));

        ucr.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        ucr.setText("Universidad de Costa Rica");
        PanelBienvenida.add(ucr, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 130, 20));

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Este programa es una simulación del Data Base Management System, Pinto DB.");
        PanelBienvenida.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 540, 30));

        getContentPane().add(PanelBienvenida, "card6");

        PanelInitData.setBackground(new java.awt.Color(255, 255, 255));
        PanelInitData.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabel4.setText("Por favor ingrese los siguientes datos.");
        PanelInitData.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        jLabel5.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        jLabel5.setText("¿Cuántas veces desea correr la simulación?");
        PanelInitData.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, -1, -1));

        cantidadCorridasTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cantidadCorridasTFActionPerformed(evt);
            }
        });
        PanelInitData.add(cantidadCorridasTF, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 40, 80, -1));

        jLabel3.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        jLabel3.setText("Cantidad de tiempo en segundos que correrá cada iteración de la simulación.");
        PanelInitData.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, -1, -1));

        tiempoMaxTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tiempoMaxTFActionPerformed(evt);
            }
        });
        PanelInitData.add(tiempoMaxTF, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 70, 80, -1));

        jLabel7.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        jLabel7.setText("Modo en el que correrá la simulación.");
        PanelInitData.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, -1, -1));

        ModoLentoCB.setFont(new java.awt.Font("Lucida Grande", 0, 8)); // NOI18N
        ModoLentoCB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Rápido", "Lento" }));
        PanelInitData.add(ModoLentoCB, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 100, 80, 30));

        jLabel8.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        jLabel8.setText("Número de conexiones concurrentes (k).");
        PanelInitData.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, -1, -1));
        PanelInitData.add(kTF, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 130, 80, -1));

        jLabel9.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        jLabel9.setText("Número de procesos disponibles para el procesamiento de consultas concurrentes (n).");
        PanelInitData.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 165, -1, 20));
        PanelInitData.add(nTF, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 160, 80, -1));

        jLabel2.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        jLabel2.setText("Número de procesos disponibles para la ejecución de transacciones (p). ");
        PanelInitData.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, -1, -1));
        PanelInitData.add(pTF, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 190, 80, -1));

        jLabel10.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        jLabel10.setText("Número de procesos disponibles para ejecutar consultas (m).");
        PanelInitData.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, -1, -1));
        PanelInitData.add(mTF, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 220, 80, -1));

        jLabel11.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        jLabel11.setText("Cantidad de tiempo en segundos de timeout de una conexión (t).");
        PanelInitData.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, -1, -1));
        PanelInitData.add(tTF, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 250, 80, -1));

        simularBTN.setBackground(new java.awt.Color(0, 255, 255));
        simularBTN.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        simularBTN.setText("Simular");
        simularBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simularBTNActionPerformed(evt);
            }
        });
        PanelInitData.add(simularBTN, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 290, 100, 40));

        getContentPane().add(PanelInitData, "card3");

        PanelInteractivo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 0, 0));
        jLabel6.setText("¡Simulación en proceso!");
        PanelInteractivo.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 10, -1, -1));

        tablaColas.setBackground(new java.awt.Color(255, 255, 204));
        tablaColas.setBorder(new javax.swing.border.MatteBorder(null));
        tablaColas.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel12.setText("Tamaño de la cola en cada módulo:");
        tablaColas.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, -1, -1));

        jLabel13.setText("Administrador de Procesos:");
        tablaColas.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, -1, -1));

        jLabel14.setText("Procesador de Consultas:");
        tablaColas.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, -1, -1));

        jLabel15.setText("Transacciones y Acceso a Datos:");
        tablaColas.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, -1, -1));

        jLabel16.setText("Ejecutor de Sentencias:");
        tablaColas.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, -1, -1));

        colaES.setBackground(new java.awt.Color(255, 255, 255));
        colaES.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        colaES.setForeground(new java.awt.Color(51, 51, 255));
        colaES.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        colaES.setText("0");
        tablaColas.add(colaES, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 120, 60, -1));

        colaAP.setBackground(new java.awt.Color(255, 255, 255));
        colaAP.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        colaAP.setForeground(new java.awt.Color(51, 51, 255));
        colaAP.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        colaAP.setText("0");
        tablaColas.add(colaAP, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 30, 60, -1));

        colaPC.setBackground(new java.awt.Color(255, 255, 255));
        colaPC.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        colaPC.setForeground(new java.awt.Color(51, 51, 255));
        colaPC.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        colaPC.setText("0");
        tablaColas.add(colaPC, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 60, 60, -1));

        colaTAD.setBackground(new java.awt.Color(255, 255, 255));
        colaTAD.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        colaTAD.setForeground(new java.awt.Color(51, 51, 255));
        colaTAD.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        colaTAD.setText("0");
        tablaColas.add(colaTAD, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 90, 60, -1));

        PanelInteractivo.add(tablaColas, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 110, 280, 150));

        jLabel17.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        jLabel17.setText("Reloj:");
        PanelInteractivo.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, -1, -1));

        relojL.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        relojL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        relojL.setText("0");
        PanelInteractivo.add(relojL, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 100, 180, -1));

        jLabel18.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabel18.setText("Conexiones Descartadas:");
        PanelInteractivo.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, -1, -1));

        conexionesDescartadasL.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        conexionesDescartadasL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        conexionesDescartadasL.setText("0");
        PanelInteractivo.add(conexionesDescartadasL, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 200, 80, -1));

        jLabel20.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel20.setText("Corrida número:");
        PanelInteractivo.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 50, -1, -1));

        corridaAct.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        corridaAct.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        corridaAct.setText("0");
        PanelInteractivo.add(corridaAct, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 50, 80, -1));

        getContentPane().add(PanelInteractivo, "card4");

        PanelFinCorridas.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel21.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel21.setText("Tiempo promedio de vida de una conexión:");
        PanelFinCorridas.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, -1, -1));

        jLabel22.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel22.setText("Tiempo promedio en Modulo = TM");
        PanelFinCorridas.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, -1, -1));

        jLabel23.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 0, 51));
        jLabel23.setText("Corrida finalizada");
        PanelFinCorridas.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 10, -1, -1));

        conexDesc.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        conexDesc.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        conexDesc.setText("0");
        PanelFinCorridas.add(conexDesc, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 60, 100, -1));

        tiempoPromVida.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        tiempoPromVida.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tiempoPromVida.setText("0");
        PanelFinCorridas.add(tiempoPromVida, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 40, 100, -1));

        tablaColasPromedio.setBackground(new java.awt.Color(255, 255, 204));
        tablaColasPromedio.setBorder(new javax.swing.border.MatteBorder(null));
        tablaColasPromedio.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel25.setText("Administrador de Procesos:");
        tablaColasPromedio.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        jLabel26.setText("Procesador de Consultas:");
        tablaColasPromedio.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, -1, -1));

        jLabel27.setText("Transacciones y Acceso a Datos:");
        tablaColasPromedio.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, -1, -1));

        jLabel28.setText("Ejecutor de Sentencias:");
        tablaColasPromedio.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, -1, -1));

        jLabel29.setText("Administrador de Conexiones:");
        tablaColasPromedio.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        PanelFinCorridas.add(tablaColasPromedio, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, 220, 150));

        jPanel1.setBackground(new java.awt.Color(255, 255, 204));
        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 1, new java.awt.Color(0, 0, 0)));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel30.setText("N/A");
        jPanel1.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        colaTADPromedio.setBackground(new java.awt.Color(255, 255, 255));
        colaTADPromedio.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        colaTADPromedio.setForeground(new java.awt.Color(51, 51, 255));
        colaTADPromedio.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        colaTADPromedio.setText("0");
        jPanel1.add(colaTADPromedio, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 60, -1));

        colaPCPromedio.setBackground(new java.awt.Color(255, 255, 255));
        colaPCPromedio.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        colaPCPromedio.setForeground(new java.awt.Color(51, 51, 255));
        colaPCPromedio.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        colaPCPromedio.setText("0");
        jPanel1.add(colaPCPromedio, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 60, -1));

        colaAPPromedio.setBackground(new java.awt.Color(255, 255, 255));
        colaAPPromedio.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        colaAPPromedio.setForeground(new java.awt.Color(51, 51, 255));
        colaAPPromedio.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        colaAPPromedio.setText("0");
        jPanel1.add(colaAPPromedio, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 60, -1));

        colaESPromedio.setBackground(new java.awt.Color(255, 255, 255));
        colaESPromedio.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        colaESPromedio.setForeground(new java.awt.Color(51, 51, 255));
        colaESPromedio.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        colaESPromedio.setText("0");
        jPanel1.add(colaESPromedio, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 130, 60, -1));

        PanelFinCorridas.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 180, 60, 150));

        jPanel2.setBackground(new java.awt.Color(255, 255, 204));
        jPanel2.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 1, new java.awt.Color(0, 0, 0)));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel34.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel34.setText("JOIN TM");
        jPanel2.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, -1, -1));

        PanelFinCorridas.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 150, 70, 30));

        jPanel3.setBackground(new java.awt.Color(255, 255, 204));
        jPanel3.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 1, new java.awt.Color(0, 0, 0)));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel10.setBackground(new java.awt.Color(255, 255, 204));
        jPanel10.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 1, new java.awt.Color(0, 0, 0)));
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel3.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 180, 70, 150));

        selectTMES.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        selectTMES.setForeground(new java.awt.Color(51, 51, 255));
        selectTMES.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        selectTMES.setText("0");
        jPanel3.add(selectTMES, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 130, 80, -1));

        selectTMAC.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        selectTMAC.setForeground(new java.awt.Color(51, 51, 255));
        selectTMAC.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        selectTMAC.setText("0");
        jPanel3.add(selectTMAC, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 80, -1));

        selectTMAP.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        selectTMAP.setForeground(new java.awt.Color(51, 51, 255));
        selectTMAP.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        selectTMAP.setText("0");
        jPanel3.add(selectTMAP, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 80, -1));

        selectTMPC.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        selectTMPC.setForeground(new java.awt.Color(51, 51, 255));
        selectTMPC.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        selectTMPC.setText("0");
        jPanel3.add(selectTMPC, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 80, -1));

        selectTMTAD.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        selectTMTAD.setForeground(new java.awt.Color(51, 51, 255));
        selectTMTAD.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        selectTMTAD.setText("0");
        jPanel3.add(selectTMTAD, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 80, -1));

        PanelFinCorridas.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 180, 80, 150));

        jPanel4.setBackground(new java.awt.Color(255, 255, 204));
        jPanel4.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 0, 1, new java.awt.Color(0, 0, 0)));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel19.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel19.setText("Módulo");
        jPanel4.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 10, -1, -1));

        PanelFinCorridas.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 220, 30));

        jPanel5.setBackground(new java.awt.Color(255, 255, 204));
        jPanel5.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 1, new java.awt.Color(0, 0, 0)));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel33.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel33.setText("TC");
        jPanel5.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        PanelFinCorridas.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 150, 60, 30));

        jLabel31.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel31.setText("Número de conexiones descartadas por el servidor:");
        PanelFinCorridas.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, -1, -1));

        jLabel32.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel32.setText("Tamaño promedio de la cola = TC");
        PanelFinCorridas.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, -1, -1));

        jPanel6.setBackground(new java.awt.Color(255, 255, 204));
        jPanel6.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 1, new java.awt.Color(0, 0, 0)));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel35.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel35.setText("SELECT TM");
        jPanel6.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, -1, -1));

        PanelFinCorridas.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 150, 80, 30));

        jPanel7.setBackground(new java.awt.Color(255, 255, 204));
        jPanel7.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 1, new java.awt.Color(0, 0, 0)));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel36.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel36.setText("DDL TM");
        jPanel7.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, -1, -1));

        PanelFinCorridas.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 150, 60, 30));

        jPanel8.setBackground(new java.awt.Color(255, 255, 204));
        jPanel8.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 1, new java.awt.Color(0, 0, 0)));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel37.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel37.setText("UPDATE TM");
        jPanel8.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, -1, -1));

        PanelFinCorridas.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 150, 100, 30));

        jPanel9.setBackground(new java.awt.Color(255, 255, 204));
        jPanel9.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 1, new java.awt.Color(0, 0, 0)));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        ddlTMES.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        ddlTMES.setForeground(new java.awt.Color(51, 51, 255));
        ddlTMES.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ddlTMES.setText("0");
        jPanel9.add(ddlTMES, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 126, 60, 20));

        ddlTMAC.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        ddlTMAC.setForeground(new java.awt.Color(51, 51, 255));
        ddlTMAC.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ddlTMAC.setText("0");
        jPanel9.add(ddlTMAC, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 60, -1));

        ddlTMAP.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        ddlTMAP.setForeground(new java.awt.Color(51, 51, 255));
        ddlTMAP.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ddlTMAP.setText("0");
        jPanel9.add(ddlTMAP, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 60, -1));

        ddlTMPC.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        ddlTMPC.setForeground(new java.awt.Color(51, 51, 255));
        ddlTMPC.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ddlTMPC.setText("0");
        jPanel9.add(ddlTMPC, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 60, -1));

        ddlTMTAD.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        ddlTMTAD.setForeground(new java.awt.Color(51, 51, 255));
        ddlTMTAD.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ddlTMTAD.setText("0");
        jPanel9.add(ddlTMTAD, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 60, -1));

        PanelFinCorridas.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 180, 60, 150));

        jPanel11.setBackground(new java.awt.Color(255, 255, 204));
        jPanel11.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 1, new java.awt.Color(0, 0, 0)));
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel12.setBackground(new java.awt.Color(255, 255, 204));
        jPanel12.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 1, new java.awt.Color(0, 0, 0)));
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel11.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 180, 70, 150));

        joinTMES.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        joinTMES.setForeground(new java.awt.Color(51, 51, 255));
        joinTMES.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        joinTMES.setText("0");
        jPanel11.add(joinTMES, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 130, 70, -1));

        joinTMAC.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        joinTMAC.setForeground(new java.awt.Color(51, 51, 255));
        joinTMAC.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        joinTMAC.setText("0");
        jPanel11.add(joinTMAC, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 70, -1));

        joinTMAP.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        joinTMAP.setForeground(new java.awt.Color(51, 51, 255));
        joinTMAP.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        joinTMAP.setText("0");
        jPanel11.add(joinTMAP, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 70, -1));

        joinTMPC.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        joinTMPC.setForeground(new java.awt.Color(51, 51, 255));
        joinTMPC.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        joinTMPC.setText("0");
        jPanel11.add(joinTMPC, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 70, -1));

        joinTMTAD.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        joinTMTAD.setForeground(new java.awt.Color(51, 51, 255));
        joinTMTAD.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        joinTMTAD.setText("0");
        jPanel11.add(joinTMTAD, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 70, -1));

        PanelFinCorridas.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 180, 70, 150));

        jPanel13.setBackground(new java.awt.Color(255, 255, 204));
        jPanel13.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 1, new java.awt.Color(0, 0, 0)));
        jPanel13.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel14.setBackground(new java.awt.Color(255, 255, 204));
        jPanel14.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 1, new java.awt.Color(0, 0, 0)));
        jPanel14.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel13.add(jPanel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 180, 70, 150));

        updateTMES.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        updateTMES.setForeground(new java.awt.Color(51, 51, 255));
        updateTMES.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        updateTMES.setText("0");
        jPanel13.add(updateTMES, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 130, 100, -1));

        updateTMAC.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        updateTMAC.setForeground(new java.awt.Color(51, 51, 255));
        updateTMAC.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        updateTMAC.setText("0");
        jPanel13.add(updateTMAC, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 100, -1));

        updateTMAP.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        updateTMAP.setForeground(new java.awt.Color(51, 51, 255));
        updateTMAP.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        updateTMAP.setText("0");
        jPanel13.add(updateTMAP, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 100, -1));

        updateTMPC.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        updateTMPC.setForeground(new java.awt.Color(51, 51, 255));
        updateTMPC.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        updateTMPC.setText("0");
        jPanel13.add(updateTMPC, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 100, -1));

        updateTMTAD.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        updateTMTAD.setForeground(new java.awt.Color(51, 51, 255));
        updateTMTAD.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        updateTMTAD.setText("0");
        jPanel13.add(updateTMTAD, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 100, -1));

        PanelFinCorridas.add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 180, 100, 150));

        continuarSim.setText("Continuar");
        continuarSim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                continuarSimActionPerformed(evt);
            }
        });
        PanelFinCorridas.add(continuarSim, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 10, -1, 60));

        getContentPane().add(PanelFinCorridas, "card5");

        PanelFinal.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel38.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel38.setText("Tiempo promedio de vida de una conexión:");
        PanelFinal.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, -1, -1));

        jLabel39.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel39.setText("Tiempo promedio en Modulo = TM");
        PanelFinal.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, -1, -1));

        jLabel40.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(255, 0, 51));
        jLabel40.setText("Simulación finalizada");
        PanelFinal.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 10, -1, -1));

        conexDescPromTotal.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        conexDescPromTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        conexDescPromTotal.setText("0");
        PanelFinal.add(conexDescPromTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 60, 140, -1));

        tiempoPromVidaTotal.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        tiempoPromVidaTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tiempoPromVidaTotal.setText("0");
        PanelFinal.add(tiempoPromVidaTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 40, 140, -1));

        tablaColasPromedio1.setBackground(new java.awt.Color(255, 255, 204));
        tablaColasPromedio1.setBorder(new javax.swing.border.MatteBorder(null));
        tablaColasPromedio1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel42.setText("Administrador de Procesos:");
        tablaColasPromedio1.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        jLabel43.setText("Procesador de Consultas:");
        tablaColasPromedio1.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, -1, -1));

        jLabel44.setText("Transacciones y Acceso a Datos:");
        tablaColasPromedio1.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, -1, -1));

        jLabel45.setText("Ejecutor de Sentencias:");
        tablaColasPromedio1.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, -1, -1));

        jLabel46.setText("Administrador de Conexiones:");
        tablaColasPromedio1.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        PanelFinal.add(tablaColasPromedio1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, 220, 150));

        jPanel15.setBackground(new java.awt.Color(255, 255, 204));
        jPanel15.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 1, new java.awt.Color(0, 0, 0)));
        jPanel15.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel47.setText("N/A");
        jPanel15.add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        colaTADPromedioTotal.setBackground(new java.awt.Color(255, 255, 255));
        colaTADPromedioTotal.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        colaTADPromedioTotal.setForeground(new java.awt.Color(51, 51, 255));
        colaTADPromedioTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        colaTADPromedioTotal.setText("0");
        jPanel15.add(colaTADPromedioTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 60, -1));

        colaPCPromedioTotal.setBackground(new java.awt.Color(255, 255, 255));
        colaPCPromedioTotal.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        colaPCPromedioTotal.setForeground(new java.awt.Color(51, 51, 255));
        colaPCPromedioTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        colaPCPromedioTotal.setText("0");
        jPanel15.add(colaPCPromedioTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 60, -1));

        colaAPPromedioTotal.setBackground(new java.awt.Color(255, 255, 255));
        colaAPPromedioTotal.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        colaAPPromedioTotal.setForeground(new java.awt.Color(51, 51, 255));
        colaAPPromedioTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        colaAPPromedioTotal.setText("0");
        jPanel15.add(colaAPPromedioTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 60, -1));

        colaESPromedioTotal.setBackground(new java.awt.Color(255, 255, 255));
        colaESPromedioTotal.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        colaESPromedioTotal.setForeground(new java.awt.Color(51, 51, 255));
        colaESPromedioTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        colaESPromedioTotal.setText("0");
        jPanel15.add(colaESPromedioTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 130, 60, -1));

        PanelFinal.add(jPanel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 180, 60, 150));

        jPanel16.setBackground(new java.awt.Color(255, 255, 204));
        jPanel16.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 1, new java.awt.Color(0, 0, 0)));
        jPanel16.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel48.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel48.setText("JOIN TM");
        jPanel16.add(jLabel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, -1, -1));

        PanelFinal.add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 150, 70, 30));

        jPanel17.setBackground(new java.awt.Color(255, 255, 204));
        jPanel17.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 1, new java.awt.Color(0, 0, 0)));
        jPanel17.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel18.setBackground(new java.awt.Color(255, 255, 204));
        jPanel18.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 1, new java.awt.Color(0, 0, 0)));
        jPanel18.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel17.add(jPanel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 180, 70, 150));

        selectTMESTotal.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        selectTMESTotal.setForeground(new java.awt.Color(51, 51, 255));
        selectTMESTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        selectTMESTotal.setText("0");
        jPanel17.add(selectTMESTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 130, 80, -1));

        selectTMACTotal.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        selectTMACTotal.setForeground(new java.awt.Color(51, 51, 255));
        selectTMACTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        selectTMACTotal.setText("0");
        jPanel17.add(selectTMACTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 80, -1));

        selectTMAPTotal.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        selectTMAPTotal.setForeground(new java.awt.Color(51, 51, 255));
        selectTMAPTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        selectTMAPTotal.setText("0");
        jPanel17.add(selectTMAPTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 80, -1));

        selectTMPCTotal.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        selectTMPCTotal.setForeground(new java.awt.Color(51, 51, 255));
        selectTMPCTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        selectTMPCTotal.setText("0");
        jPanel17.add(selectTMPCTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 80, -1));

        selectTMTADTotal.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        selectTMTADTotal.setForeground(new java.awt.Color(51, 51, 255));
        selectTMTADTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        selectTMTADTotal.setText("0");
        jPanel17.add(selectTMTADTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 80, -1));

        PanelFinal.add(jPanel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 180, 80, 150));

        jPanel19.setBackground(new java.awt.Color(255, 255, 204));
        jPanel19.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 0, 1, new java.awt.Color(0, 0, 0)));
        jPanel19.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel49.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel49.setText("Módulo");
        jPanel19.add(jLabel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 10, -1, -1));

        PanelFinal.add(jPanel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 220, 30));

        jPanel20.setBackground(new java.awt.Color(255, 255, 204));
        jPanel20.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 1, new java.awt.Color(0, 0, 0)));
        jPanel20.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel50.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel50.setText("TC");
        jPanel20.add(jLabel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        PanelFinal.add(jPanel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 150, 60, 30));

        jLabel51.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel51.setText("Número pomedio de conexiones descartadas:");
        PanelFinal.add(jLabel51, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, -1, -1));

        jLabel52.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel52.setText("Tamaño promedio de la cola = TC");
        PanelFinal.add(jLabel52, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, -1, -1));

        jPanel21.setBackground(new java.awt.Color(255, 255, 204));
        jPanel21.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 1, new java.awt.Color(0, 0, 0)));
        jPanel21.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel53.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel53.setText("SELECT TM");
        jPanel21.add(jLabel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, -1, -1));

        PanelFinal.add(jPanel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 150, 80, 30));

        jPanel22.setBackground(new java.awt.Color(255, 255, 204));
        jPanel22.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 1, new java.awt.Color(0, 0, 0)));
        jPanel22.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel54.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel54.setText("DDL TM");
        jPanel22.add(jLabel54, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, -1, -1));

        PanelFinal.add(jPanel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 150, 60, 30));

        jPanel23.setBackground(new java.awt.Color(255, 255, 204));
        jPanel23.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 1, new java.awt.Color(0, 0, 0)));
        jPanel23.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel55.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel55.setText("UPDATE TM");
        jPanel23.add(jLabel55, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, -1, -1));

        PanelFinal.add(jPanel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 150, 100, 30));

        jPanel24.setBackground(new java.awt.Color(255, 255, 204));
        jPanel24.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 1, new java.awt.Color(0, 0, 0)));
        jPanel24.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        ddlTMESTotal.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        ddlTMESTotal.setForeground(new java.awt.Color(51, 51, 255));
        ddlTMESTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ddlTMESTotal.setText("0");
        jPanel24.add(ddlTMESTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 126, 60, 20));

        ddlTMACTotal.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        ddlTMACTotal.setForeground(new java.awt.Color(51, 51, 255));
        ddlTMACTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ddlTMACTotal.setText("0");
        jPanel24.add(ddlTMACTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 60, -1));

        ddlTMAPTotal.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        ddlTMAPTotal.setForeground(new java.awt.Color(51, 51, 255));
        ddlTMAPTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ddlTMAPTotal.setText("0");
        jPanel24.add(ddlTMAPTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 60, -1));

        ddlTMPCTotal.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        ddlTMPCTotal.setForeground(new java.awt.Color(51, 51, 255));
        ddlTMPCTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ddlTMPCTotal.setText("0");
        jPanel24.add(ddlTMPCTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 60, -1));

        ddlTMTADTotal.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        ddlTMTADTotal.setForeground(new java.awt.Color(51, 51, 255));
        ddlTMTADTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ddlTMTADTotal.setText("0");
        jPanel24.add(ddlTMTADTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 60, -1));

        PanelFinal.add(jPanel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 180, 60, 150));

        jPanel25.setBackground(new java.awt.Color(255, 255, 204));
        jPanel25.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 1, new java.awt.Color(0, 0, 0)));
        jPanel25.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel26.setBackground(new java.awt.Color(255, 255, 204));
        jPanel26.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 1, new java.awt.Color(0, 0, 0)));
        jPanel26.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel25.add(jPanel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 180, 70, 150));

        joinTMESTotal.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        joinTMESTotal.setForeground(new java.awt.Color(51, 51, 255));
        joinTMESTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        joinTMESTotal.setText("0");
        jPanel25.add(joinTMESTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 130, 70, -1));

        joinTMACTotal.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        joinTMACTotal.setForeground(new java.awt.Color(51, 51, 255));
        joinTMACTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        joinTMACTotal.setText("0");
        jPanel25.add(joinTMACTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 70, -1));

        joinTMAPTotal.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        joinTMAPTotal.setForeground(new java.awt.Color(51, 51, 255));
        joinTMAPTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        joinTMAPTotal.setText("0");
        jPanel25.add(joinTMAPTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 70, -1));

        joinTMPCTotal.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        joinTMPCTotal.setForeground(new java.awt.Color(51, 51, 255));
        joinTMPCTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        joinTMPCTotal.setText("0");
        jPanel25.add(joinTMPCTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 70, -1));

        joinTMTADTotal.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        joinTMTADTotal.setForeground(new java.awt.Color(51, 51, 255));
        joinTMTADTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        joinTMTADTotal.setText("0");
        jPanel25.add(joinTMTADTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 70, -1));

        PanelFinal.add(jPanel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 180, 70, 150));

        jPanel27.setBackground(new java.awt.Color(255, 255, 204));
        jPanel27.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 1, new java.awt.Color(0, 0, 0)));
        jPanel27.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel28.setBackground(new java.awt.Color(255, 255, 204));
        jPanel28.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 1, new java.awt.Color(0, 0, 0)));
        jPanel28.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel27.add(jPanel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 180, 70, 150));

        updateTMESTotal.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        updateTMESTotal.setForeground(new java.awt.Color(51, 51, 255));
        updateTMESTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        updateTMESTotal.setText("0");
        jPanel27.add(updateTMESTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 130, 100, -1));

        updateTMACTotal.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        updateTMACTotal.setForeground(new java.awt.Color(51, 51, 255));
        updateTMACTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        updateTMACTotal.setText("0");
        jPanel27.add(updateTMACTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 100, -1));

        updateTMAPTotal.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        updateTMAPTotal.setForeground(new java.awt.Color(51, 51, 255));
        updateTMAPTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        updateTMAPTotal.setText("0");
        jPanel27.add(updateTMAPTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 100, -1));

        updateTMPCTotal.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        updateTMPCTotal.setForeground(new java.awt.Color(51, 51, 255));
        updateTMPCTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        updateTMPCTotal.setText("0");
        jPanel27.add(updateTMPCTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 100, -1));

        updateTMTADTotal.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        updateTMTADTotal.setForeground(new java.awt.Color(51, 51, 255));
        updateTMTADTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        updateTMTADTotal.setText("0");
        jPanel27.add(updateTMTADTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 100, -1));

        PanelFinal.add(jPanel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 180, 100, 150));

        nuevaBTN.setFont(new java.awt.Font("Lucida Grande", 1, 10)); // NOI18N
        nuevaBTN.setText("Nueva Simulación");
        nuevaBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevaBTNActionPerformed(evt);
            }
        });
        PanelFinal.add(nuevaBTN, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 0, 110, 70));

        salirBTN.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        salirBTN.setText("Salir");
        salirBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salirBTNActionPerformed(evt);
            }
        });
        PanelFinal.add(salirBTN, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 80, 30));

        getContentPane().add(PanelFinal, "card2");

        setSize(new java.awt.Dimension(608, 363));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    
    
    private void cantidadCorridasTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cantidadCorridasTFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cantidadCorridasTFActionPerformed

    private void tiempoMaxTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tiempoMaxTFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tiempoMaxTFActionPerformed

    private void comenzarBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comenzarBTNActionPerformed
        // TODO add your handling code here:
        this.PanelBienvenida.setVisible(false);
        this.PanelInitData.setVisible(true);
    }//GEN-LAST:event_comenzarBTNActionPerformed

    private void simularBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simularBTNActionPerformed
        // TODO add your handling code here:
        boolean completos = true;
        for(int i = 0; i<7 && completos; ++i){
            //System.out.println("a" + textFields[i].getText());
            if(textFields[i].getText().isEmpty()){
                completos = false;
            }
        }
   
        if(completos){
            controlador.setCantidadCorridas(Integer.parseInt(cantidadCorridasTF.getText()));
            controlador.setTiempoMax(Double.parseDouble(tiempoMaxTF.getText()));
            if(ModoLentoCB.getSelectedItem() == "Rápido"){
                controlador.setModoLento(false);
            }else{
                controlador.setModoLento(true);
            }
            int[] parametros = new int[4];
            parametros[0] = Integer.parseInt(kTF.getText());
            parametros[1] = Integer.parseInt(nTF.getText());
            parametros[2] = Integer.parseInt(pTF.getText());
            parametros[3] = Integer.parseInt(mTF.getText());
            controlador.setParametros(parametros);
            controlador.setTimeout(Double.parseDouble(tTF.getText()));
            
            this.PanelInitData.setVisible(false);
            this.PanelInteractivo.setVisible(true);
            semEjecucion.release();
        }else{
            JOptionPane.showMessageDialog(null, "Debe llenar todos los campos.", "Error Massage", JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_simularBTNActionPerformed

    private void continuarSimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_continuarSimActionPerformed
        // TODO add your handling code here:
        this.PanelFinCorridas.setVisible(false);
        this.PanelInteractivo.setVisible(true);
        this.semEjecucion.release();
    }//GEN-LAST:event_continuarSimActionPerformed

    private void salirBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salirBTNActionPerformed
        // TODO add your handling code here:
        this.controlador.setExit(true);
        this.dispose();
        this.semEjecucion.release();
    }//GEN-LAST:event_salirBTNActionPerformed

    private void nuevaBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevaBTNActionPerformed
        // TODO add your handling code here:
        this.PanelFinal.setVisible(false);
        this.PanelInitData.setVisible(true);
    }//GEN-LAST:event_nuevaBTNActionPerformed

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new Interfaz().setVisible(true);
//            }
//        });
//    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel BIENVENIDO;
    private javax.swing.JLabel JavierL;
    private javax.swing.JLabel MarcoL;
    private javax.swing.JComboBox<String> ModoLentoCB;
    private javax.swing.JPanel PanelBienvenida;
    private javax.swing.JPanel PanelFinCorridas;
    private javax.swing.JPanel PanelFinal;
    private javax.swing.JPanel PanelInitData;
    private javax.swing.JPanel PanelInteractivo;
    private javax.swing.JTextField cantidadCorridasTF;
    private javax.swing.JLabel colaAP;
    private javax.swing.JLabel colaAPPromedio;
    private javax.swing.JLabel colaAPPromedioTotal;
    private javax.swing.JLabel colaES;
    private javax.swing.JLabel colaESPromedio;
    private javax.swing.JLabel colaESPromedioTotal;
    private javax.swing.JLabel colaPC;
    private javax.swing.JLabel colaPCPromedio;
    private javax.swing.JLabel colaPCPromedioTotal;
    private javax.swing.JLabel colaTAD;
    private javax.swing.JLabel colaTADPromedio;
    private javax.swing.JLabel colaTADPromedioTotal;
    private javax.swing.JButton comenzarBTN;
    private javax.swing.JLabel conexDesc;
    private javax.swing.JLabel conexDescPromTotal;
    private javax.swing.JLabel conexionesDescartadasL;
    private javax.swing.JButton continuarSim;
    private javax.swing.JLabel corridaAct;
    private javax.swing.JLabel ddlTMAC;
    private javax.swing.JLabel ddlTMACTotal;
    private javax.swing.JLabel ddlTMAP;
    private javax.swing.JLabel ddlTMAPTotal;
    private javax.swing.JLabel ddlTMES;
    private javax.swing.JLabel ddlTMESTotal;
    private javax.swing.JLabel ddlTMPC;
    private javax.swing.JLabel ddlTMPCTotal;
    private javax.swing.JLabel ddlTMTAD;
    private javax.swing.JLabel ddlTMTADTotal;
    private javax.swing.JLabel escritoPor;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JLabel joinTMAC;
    private javax.swing.JLabel joinTMACTotal;
    private javax.swing.JLabel joinTMAP;
    private javax.swing.JLabel joinTMAPTotal;
    private javax.swing.JLabel joinTMES;
    private javax.swing.JLabel joinTMESTotal;
    private javax.swing.JLabel joinTMPC;
    private javax.swing.JLabel joinTMPCTotal;
    private javax.swing.JLabel joinTMTAD;
    private javax.swing.JLabel joinTMTADTotal;
    private javax.swing.JTextField kTF;
    private javax.swing.JTextField mTF;
    private javax.swing.JTextField nTF;
    private javax.swing.JButton nuevaBTN;
    private javax.swing.JTextField pTF;
    private javax.swing.JLabel relojL;
    private javax.swing.JButton salirBTN;
    private javax.swing.JLabel selectTMAC;
    private javax.swing.JLabel selectTMACTotal;
    private javax.swing.JLabel selectTMAP;
    private javax.swing.JLabel selectTMAPTotal;
    private javax.swing.JLabel selectTMES;
    private javax.swing.JLabel selectTMESTotal;
    private javax.swing.JLabel selectTMPC;
    private javax.swing.JLabel selectTMPCTotal;
    private javax.swing.JLabel selectTMTAD;
    private javax.swing.JLabel selectTMTADTotal;
    private javax.swing.JButton simularBTN;
    private javax.swing.JTextField tTF;
    private javax.swing.JPanel tablaColas;
    private javax.swing.JPanel tablaColasPromedio;
    private javax.swing.JPanel tablaColasPromedio1;
    private javax.swing.JTextField tiempoMaxTF;
    private javax.swing.JLabel tiempoPromVida;
    private javax.swing.JLabel tiempoPromVidaTotal;
    private javax.swing.JLabel ucr;
    private javax.swing.JLabel updateTMAC;
    private javax.swing.JLabel updateTMACTotal;
    private javax.swing.JLabel updateTMAP;
    private javax.swing.JLabel updateTMAPTotal;
    private javax.swing.JLabel updateTMES;
    private javax.swing.JLabel updateTMESTotal;
    private javax.swing.JLabel updateTMPC;
    private javax.swing.JLabel updateTMPCTotal;
    private javax.swing.JLabel updateTMTAD;
    private javax.swing.JLabel updateTMTADTotal;
    // End of variables declaration//GEN-END:variables
}
