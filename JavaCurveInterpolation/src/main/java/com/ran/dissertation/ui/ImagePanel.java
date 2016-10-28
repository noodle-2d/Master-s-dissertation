package com.ran.dissertation.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.util.Set;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    
    public ImagePanel() {
        initComponents();
        initListeners();
        setSize(WIDTH, HEIGHT);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void initListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent event) {
                imagePanelListenerSupport.fireMouseMoved(ImagePanel.this, event.getX(), event.getY());
            }
            @Override
            public void mouseDragged(MouseEvent event) {
                int x = event.getX();
                int y = event.getY();
                if ((event.getModifiersEx() & InputEvent.BUTTON1_DOWN_MASK) != 0) {
                    imagePanelListenerSupport.fireMouseDraggedLeftMouseButton(ImagePanel.this, x, y);
                } else if ((event.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK) != 0) {
                    imagePanelListenerSupport.fireMouseDraggedMiddleMouseButton(ImagePanel.this, x, y);
                } else if ((event.getModifiersEx() & InputEvent.BUTTON2_DOWN_MASK) != 0) {
                    imagePanelListenerSupport.fireMouseDraggedRightMouseButton(ImagePanel.this, x, y);
                }
            }
        });
        addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent event) {
                imagePanelListenerSupport.fireMouseWheelMoved(ImagePanel.this, event.getWheelRotation());
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    private ImagePanelListenerSupport imagePanelListenerSupport = new ImagePanelListenerSupport();
    private ImagePanelPaintStrategy imagePanelPaintStrategy = (graphics, width, height) -> { };
    
    public void addImagePanelListener(ImagePanelListener listener) {
        imagePanelListenerSupport.addImagePanelListener(listener);
    }
    
//    public Set<ImagePanelListener> getImagePanelListeners() {
//        return imagePanelListenerSupport.getImagePanelListeners();
//    }
    
    public void removeImagePanelListener(ImagePanelListener listener) {
        imagePanelListenerSupport.removeImagePanelListener(listener);
    }

    public ImagePanelPaintStrategy getImagePanelPaintStrategy() {
        return imagePanelPaintStrategy;
    }

    public void setImagePanelPaintStrategy(ImagePanelPaintStrategy imagePanelPaintStrategy) {
        this.imagePanelPaintStrategy = imagePanelPaintStrategy;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        int imageWidth = getWidth() + 1;
        int imageHeight = getHeight() + 1;
        BufferedImage image = (BufferedImage) createImage(imageWidth, imageHeight);
        Graphics2D graphics2D = (Graphics2D) image.getGraphics();
        imagePanelPaintStrategy.paint(graphics2D, imageWidth, imageHeight);
        graphics.drawImage(image, 0, 0, this);
    }
    
}