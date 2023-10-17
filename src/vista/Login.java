package vista;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialLighterIJTheme;
import controlador.Ctrl_login;

public class Login extends javax.swing.JFrame {

    public Login() {
        initComponents();
        this.setTitle("Login - SalesWave");
        this.setResizable(false);
        this.setLocationRelativeTo(null);
//        Ctrl_login controladorLogin = new Ctrl_login(this);
//        this.setSize(700, 500);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel01 = new javax.swing.JPanel();
        logo = new javax.swing.JLabel();
        copyright = new javax.swing.JLabel();
        panel02 = new javax.swing.JPanel();
        IniciodeSesion = new javax.swing.JLabel();
        iconUser = new javax.swing.JLabel();
        separadorUser = new javax.swing.JSeparator();
        txt_user = new javax.swing.JTextField();
        iconPassword = new javax.swing.JLabel();
        separadorPassword = new javax.swing.JSeparator();
        txt_password = new javax.swing.JPasswordField();
        btn_IniciarSesion = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panel01.setBackground(new java.awt.Color(215, 214, 214));
        panel01.setPreferredSize(new java.awt.Dimension(350, 500));

        logo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/logo.png"))); // NOI18N

        copyright.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        copyright.setForeground(new java.awt.Color(102, 102, 102));
        copyright.setText("© Jhonatan Motta");

        javax.swing.GroupLayout panel01Layout = new javax.swing.GroupLayout(panel01);
        panel01.setLayout(panel01Layout);
        panel01Layout.setHorizontalGroup(
            panel01Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel01Layout.createSequentialGroup()
                .addGroup(panel01Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel01Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(copyright))
                    .addGroup(panel01Layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addComponent(logo)))
                .addContainerGap(46, Short.MAX_VALUE))
        );
        panel01Layout.setVerticalGroup(
            panel01Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel01Layout.createSequentialGroup()
                .addGap(121, 121, 121)
                .addComponent(logo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 150, Short.MAX_VALUE)
                .addComponent(copyright)
                .addContainerGap())
        );

        getContentPane().add(panel01, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        panel02.setBackground(new java.awt.Color(255, 255, 255));
        panel02.setMinimumSize(new java.awt.Dimension(350, 500));
        panel02.setPreferredSize(new java.awt.Dimension(350, 500));
        panel02.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        IniciodeSesion.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        IniciodeSesion.setForeground(new java.awt.Color(232, 158, 67));
        IniciodeSesion.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        IniciodeSesion.setText("Inicio de sesión");
        IniciodeSesion.setPreferredSize(new java.awt.Dimension(200, 29));
        panel02.add(IniciodeSesion, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 70, -1, -1));

        iconUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/userIcon.png"))); // NOI18N
        panel02.add(iconUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(75, 200, -1, 30));

        separadorUser.setPreferredSize(new java.awt.Dimension(180, 10));
        panel02.add(separadorUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(99, 230, -1, -1));

        txt_user.setBackground(new java.awt.Color(255, 255, 255));
        txt_user.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        txt_user.setForeground(new java.awt.Color(51, 51, 51));
        txt_user.setBorder(null);
        txt_user.setPreferredSize(new java.awt.Dimension(180, 30));
        panel02.add(txt_user, new org.netbeans.lib.awtextra.AbsoluteConstraints(99, 200, -1, -1));

        iconPassword.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/passwordIcon.png"))); // NOI18N
        panel02.add(iconPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(75, 280, -1, 30));

        separadorPassword.setPreferredSize(new java.awt.Dimension(180, 10));
        panel02.add(separadorPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(98, 310, 184, 10));

        txt_password.setBackground(new java.awt.Color(255, 255, 255));
        txt_password.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        txt_password.setForeground(new java.awt.Color(51, 51, 51));
        txt_password.setBorder(null);
        txt_password.setPreferredSize(new java.awt.Dimension(180, 30));
        txt_password.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_passwordKeyPressed(evt);
            }
        });
        panel02.add(txt_password, new org.netbeans.lib.awtextra.AbsoluteConstraints(98, 280, -1, -1));

        btn_IniciarSesion.setBackground(new java.awt.Color(0, 161, 199));
        btn_IniciarSesion.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_IniciarSesion.setForeground(new java.awt.Color(255, 255, 255));
        btn_IniciarSesion.setText("Iniciar Sesion");
        btn_IniciarSesion.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_IniciarSesion.setPreferredSize(new java.awt.Dimension(153, 33));
        panel02.add(btn_IniciarSesion, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 357, -1, -1));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(102, 102, 102));
        jLabel1.setText("¿Olvidaste tu contraseña?");
        jLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panel02.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 430, -1, -1));

        getContentPane().add(panel02, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 0, -1, -1));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txt_passwordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_passwordKeyPressed

    }//GEN-LAST:event_txt_passwordKeyPressed

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        FlatMaterialLighterIJTheme.setup();
        Login log = new Login();
        Ctrl_login controladorLogin = new Ctrl_login(log);
        log.setVisible(true);
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new Login().setVisible(true);
//            }
//        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel IniciodeSesion;
    public javax.swing.JButton btn_IniciarSesion;
    private javax.swing.JLabel copyright;
    private javax.swing.JLabel iconPassword;
    private javax.swing.JLabel iconUser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel logo;
    private javax.swing.JPanel panel01;
    private javax.swing.JPanel panel02;
    private javax.swing.JSeparator separadorPassword;
    private javax.swing.JSeparator separadorUser;
    public javax.swing.JPasswordField txt_password;
    public javax.swing.JTextField txt_user;
    // End of variables declaration//GEN-END:variables
}
