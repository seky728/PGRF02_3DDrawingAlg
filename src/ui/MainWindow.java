package ui;

import Model.*;
import Render.Renderer;
import transforms.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;


import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.Timer;

public class MainWindow extends JFrame{


    private static final int FPS = 1000 / 30;
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 700;
    private static final Camera DEFAULT_CAMERA = new Camera(new Vec3D(5.6, 6.5, 4.7), -2.5,-0.48, 1.0, true);

    private BufferedImage img;
    private JPanel drawPanel, controlPanel;

    private Renderer renderer;

    private Camera camera;
    private List<Solid> solids;
    private Solid axis;
    private Solid selectedSolid;
    private Krivka curve;
    private Grid grid;

    private int beginX, beginY;
    private double moveX, moveY, moveZ;







    public MainWindow() {

        this.img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        setMainWindow();
    }


    private void setMainWindow(){
        setTitle("Secký - 3. úkol");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setVisible(true);

        drawPanel = new JPanel();
        drawPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                requestFocusInWindow();
            }
        });

        add(drawPanel, BorderLayout.CENTER);

        solids = new ArrayList<>();
        renderer = new Renderer(img);
        renderer.setProjection(new Mat4PerspRH(Math.PI / 4, (double) img.getHeight() / img.getWidth(), 0.1, 200));
        camera = DEFAULT_CAMERA;

        addSolids();

        controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        add(controlPanel, BorderLayout.WEST);
        initControlPanel();

        drawPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                beginX = e.getX();
                beginY = e.getY();
                super.mousePressed(e);
            }
        });
        drawPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                camera = camera.addAzimuth((Math.PI / 1000) * (beginX - e.getX()));
                camera = camera.addZenith((Math.PI / 1000) * (beginY - e.getY()));

                beginX = e.getX();
                beginY = e.getY();
                System.out.println(camera.getAzimuth() + " " + camera.getRadius() + " " + camera.getZenith());
                System.out.println(camera.getPosition().toString());
            }
        });

        addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getWheelRotation() < 0)
                    renderer.setModel(new Mat4Scale(renderer.getModel().get(0, 0) + 0.1));
                else
                    renderer.setModel(new Mat4Scale(renderer.getModel().get(0, 0) - 0.1));
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W:
                        camera = camera.forward(0.1);
                        break;
                    case KeyEvent.VK_S:
                        camera = camera.backward(0.1);
                        break;
                    case KeyEvent.VK_A:
                        camera = camera.left(0.1);
                        break;
                    case KeyEvent.VK_D:
                        camera = camera.right(0.1);
                        break;
                    case KeyEvent.VK_SPACE:
                        camera = camera.up(0.1);
                        break;
                    case KeyEvent.VK_CONTROL:
                        camera = camera.down(0.1);
                        break;
                }
                super.keyReleased(e);
            }
        });

        requestFocusInWindow();

        setLocationRelativeTo(null);
        Timer drawTimer = new Timer();
        drawTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                draw();
            }
        }, 100, FPS);


    }

    private void addSolids(){
        int count = 2;

        for (int i = 0; i < count; i++){
            Solid solid;
            if (i % 2 == 0){
                solid = new Krychle(1);
            } else {
                solid = new Jehlan(1);
            }


            for (int j = 0; j < solid.getVertexBuffer().size(); j++){
                Point3D point3D = solid.getVertexBuffer().get(j);
                Point3D newPoint = point3D.mul(new Mat4Transl(0,2,0)).mul(new Mat4RotZ((double) i * 2d * Math.PI / (double) count));
                solid.getVertexBuffer().set(j, newPoint);
            }

            solids.add(solid);

        }

        axis = new Axis();
        for (int i = 0; i < axis.getVertexBuffer().size(); i++){
            Point3D point3D = axis.getVertexBuffer().get(i);
            axis.getVertexBuffer().set(i, point3D.mul(new Mat4Scale(3)));
        }


        curve = new Krivka(Cubic.BEZIER);
        solids.add(curve);
        grid = new Grid(10,10);
        solids.add(grid);

    }

    private void initControlPanel(){
        JComboBox<Object> cbSolidsList = new JComboBox<>();
        cbSolidsList.setBorder(new TitledBorder("Choose: "));
        cbSolidsList.setMaximumSize(new Dimension(Integer.MAX_VALUE, cbSolidsList.getMinimumSize().height));
        cbSolidsList.addItem("All");
        for (Solid solid:solids){
            cbSolidsList.addItem(solid);
        }

        cbSolidsList.addItemListener(e->{
            if (e.getStateChange() == ItemEvent.SELECTED){
                if (Objects.equals(cbSolidsList.getSelectedItem(), "All")){
                    selectedSolid = null;
                } else if (!Objects.isNull(selectedSolid)){
                    selectedSolid = (Solid) cbSolidsList.getSelectedItem();
                } else {
                    selectedSolid = (Solid) cbSolidsList.getSelectedItem();
                }
            }
        });

        controlPanel.add(cbSolidsList);


        JPanel positionPanelX = new JPanel(new BorderLayout());
        positionPanelX.setMaximumSize(new Dimension(270, 40));

        JLabel lblModifyX = new JLabel("Modify X: ", SwingConstants.CENTER);
        positionPanelX.add(lblModifyX, BorderLayout.NORTH);

        JButton btnAddToX = new JButton("+");
        positionPanelX.add(btnAddToX, BorderLayout.WEST);
        JLabel lblValueX = new JLabel(String.valueOf(moveX), SwingConstants.CENTER);
        positionPanelX.add(lblValueX, BorderLayout.CENTER);
        btnAddToX.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                moveX += 0.5;
                changeSolidPosition(new Vec3D(0.5, 0, 0));
                lblValueX.setText(String.valueOf(moveX));
            }
        });


        JButton btnDevToX = new JButton("-");
        positionPanelX.add(btnDevToX, BorderLayout.EAST);
        btnDevToX.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveX -= 0.5;
                changeSolidPosition(new Vec3D(-0.5, 0, 0));
                lblValueX.setText(String.valueOf(moveX));
            }
        });
        controlPanel.add(positionPanelX);

        JPanel positionPanelY = new JPanel(new BorderLayout());
        positionPanelY.setMaximumSize(new Dimension(270, 40));

        JLabel lblModifyY = new JLabel("Modify Y:", SwingConstants.CENTER);
        positionPanelY.add(lblModifyY, BorderLayout.NORTH);

        JButton btnAddToY = new JButton("+");
        positionPanelY.add(btnAddToY, BorderLayout.WEST);
        JLabel lblValueY = new JLabel("0", SwingConstants.CENTER);
        positionPanelY.add(lblValueY, BorderLayout.CENTER);
        btnAddToY.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveY += 0.5;
                changeSolidPosition(new Vec3D(0, 0.5, 0));
                lblValueY.setText(String.valueOf(moveY));
            }
        });


        JButton btnDevToY = new JButton("-");
        positionPanelY.add(btnDevToY, BorderLayout.EAST);
        btnDevToY.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveY -= 0.5;
                changeSolidPosition(new Vec3D(0, -0.5, 0));
                lblValueY.setText(String.valueOf(moveY));
            }
        });
        controlPanel.add(positionPanelY);

        JPanel positionPanelZ = new JPanel(new BorderLayout());
        positionPanelZ.setMaximumSize(new Dimension(270, 40));

        JLabel lblModifyZ = new JLabel("Modify Z:", SwingConstants.CENTER);
        positionPanelZ.add(lblModifyZ, BorderLayout.NORTH);

        JButton btnAddToZ = new JButton("+");
        positionPanelZ.add(btnAddToZ, BorderLayout.WEST);
        JLabel lblValueZ = new JLabel("0", SwingConstants.CENTER);
        positionPanelZ.add(lblValueZ, BorderLayout.CENTER);
        btnAddToZ.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveZ += 0.5;
                changeSolidPosition(new Vec3D(0, 0, 0.5));
                lblValueZ.setText(String.valueOf(moveZ));
            }
        });


        JButton btnDevToZ = new JButton("-");
        positionPanelZ.add(btnDevToZ, BorderLayout.EAST);
        btnDevToZ.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveZ -= 0.5;
                changeSolidPosition(new Vec3D(0, 0, -0.5));
                lblValueZ.setText(String.valueOf(moveZ));
            }
        });
        controlPanel.add(positionPanelZ);

        JPanel viewButtons = new JPanel();
        viewButtons.setMaximumSize(new Dimension(270, 60));
        viewButtons.setBorder(new TitledBorder("Change View"));
        JButton btnPerspectiveView = new JButton("Perspective");
        btnPerspectiveView.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                renderer.setProjection(new Mat4PerspRH(Math.PI / 4, (double) img.getHeight() / img.getWidth(), 0.1, 200));
            }
        });
        viewButtons.add(btnPerspectiveView);

        JButton btnOrthogonalView = new JButton("Orthogonal");
        btnOrthogonalView.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // divide by 100 for bigger view
                renderer.setProjection(new Mat4OrthoRH(img.getWidth() / (double) 100, img.getHeight() / (double) 100, 0.1, 200));
            }
        });
        viewButtons.add(btnOrthogonalView);
        controlPanel.add(viewButtons);

        JButton btnRotateX = new JButton("Rotate X");
        btnRotateX.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRotateX.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rotateSolid(new Vec3D(0.5, 0, 0));
            }
        });
        controlPanel.add(btnRotateX);

        JButton btnRotateY = new JButton("Rotate Y");
        btnRotateY.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRotateY.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rotateSolid(new Vec3D(0, 0.5, 0));
            }
        });
        controlPanel.add(btnRotateY);

        JButton btnRotateZ = new JButton("Rotate Z");
        btnRotateZ.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRotateZ.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rotateSolid(new Vec3D(0, 0, 0.5));
            }
        });
        controlPanel.add(btnRotateZ);


        ButtonGroup curveGroup = new ButtonGroup();

        JRadioButton rbBezier = new JRadioButton("Bezier");
        rbBezier.setAlignmentX(Component.CENTER_ALIGNMENT);
        rbBezier.setSelected(true);
        rbBezier.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                solids.remove(curve);
                curve = new Krivka(Cubic.BEZIER);
                solids.add(curve);
            }
        });
        curveGroup.add(rbBezier);
        controlPanel.add(rbBezier);

        JRadioButton rbCoons = new JRadioButton("Coons");
        rbCoons.setAlignmentX(Component.CENTER_ALIGNMENT);
        rbCoons.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                solids.remove(curve);
                curve = new Krivka(Cubic.COONS);
                solids.add(curve);
            }
        });
        curveGroup.add(rbCoons);
        controlPanel.add(rbCoons);

        JRadioButton rbFerguson = new JRadioButton("Ferguson");
        rbFerguson.setAlignmentX(Component.CENTER_ALIGNMENT);
        rbFerguson.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                solids.remove(curve);
                curve = new Krivka(Cubic.FERGUSON);
                solids.add(curve);
            }
        });
        curveGroup.add(rbFerguson);
        controlPanel.add(rbFerguson);

        JButton btnRestart = new JButton("Restart camera");
        btnRestart.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRestart.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                camera = DEFAULT_CAMERA;
            }
        });
        controlPanel.add(btnRestart);


        JPanel jplDescription = new JPanel();
        jplDescription.setSize(new Dimension(controlPanel.getWidth()-10, 50));

        JLabel jlDesription = new JLabel("Description: ");
        jplDescription.add(jlDesription, BorderLayout.WEST);

        JTextArea txtDescription = new JTextArea("Pohyb kamerou: držení levého tlačítka\n" +
                                                 "Přiblížení / oddálení: scrolování\n" +
                                                 "Pohyb kamery: W/A/S/D");
        txtDescription.setToolTipText("Description");
        txtDescription.setEditable(false);
        jplDescription.add(txtDescription, BorderLayout.SOUTH);
        controlPanel.add(jplDescription);

    }

    private void changeSolidPosition(Vec3D vec3D){
        int size = !Objects.isNull(selectedSolid) ? 1 : solids.size();
        for (int i = 0; i < size; i++){
            Solid solid = !Objects.isNull(selectedSolid) ? selectedSolid : solids.get(i);
            for (int j = 0; j < solid.getVertexBuffer().size(); j++){
                Point3D newPoint = solid.getVertexBuffer().get(j).mul(new Mat4Transl(vec3D));

                solid.getVertexBuffer().set(j, newPoint);
            }
        }
    }


    private void rotateSolid(Vec3D vec3D){

        int size = !Objects.isNull(selectedSolid) ? 1 : (int) solids.stream().filter(s -> s instanceof Krychle || s instanceof Jehlan || s instanceof Grid).count();
        int anSize = (int) solids.stream().filter(s -> s instanceof Krychle || s instanceof  Jehlan || s instanceof Grid).count();
        for (int i = 0; i < size; i++){
            Solid solid = !Objects.isNull(selectedSolid) ? selectedSolid : solids.get(i);
            int index = !Objects.isNull(selectedSolid) ? solids.indexOf(selectedSolid) : i;
            for (int j = 0; j <solid.getVertexBuffer().size(); j++){
                Point3D newPoint = solid.getVertexBuffer().get(j)
                        .mul(new Mat4RotZ((double) -index * 2d * Math.PI / (double) anSize))
                        .mul(new Mat4Transl(0, -2, 0))
                        .mul(new Mat4RotXYZ(vec3D.getX(), vec3D.getY(), vec3D.getZ()))
                        .mul(new Mat4Transl(0,2,0))
                        .mul(new Mat4RotZ((double) index * 2d * Math.PI / (double) anSize));

                solid.getVertexBuffer().set(j, newPoint);


            }
        }

    }




    private void draw(){

       img.getGraphics().fillRect(0,0, img.getWidth(), img.getHeight());

       renderer.setView(camera.getViewMatrix());

       for (Solid solid : solids){
           renderer.drawWireFrame(solid);
       }

       renderer.drawWireFrame(axis);
       renderer.drawWireFrame(new Circle(5, 5, 5, 50));

       drawPanel.getGraphics().drawImage(img, 0, 0, null);
       drawPanel.paintComponents(getGraphics());
    }


}
