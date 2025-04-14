import edu.princeton.cs.algs4.Picture;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;

public class ImageResizerClient extends JFrame {
    private JLabel originalLabel;
    private JLabel resizedLabel;
    private JLabel originalSizeLabel;
    private JLabel resizedSizeLabel;
    private JTextField widthField;
    private JTextField heightField;
    private JButton resizeButton;
    private JButton loadImageButton;
    private JButton downloadButton;
    private Picture originalPicture;
    private Picture resizedPicture;
    private static final int MAX_DISPLAY_WIDTH = 400;
    private static final int MAX_DISPLAY_HEIGHT = 400;

    public ImageResizerClient() {
        setTitle("Image Resizer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // 图片显示区域
        JPanel imagePanel = new JPanel(new GridLayout(1, 2, 10, 10));
        originalLabel = createImageLabel("Original Image");
        resizedLabel = createImageLabel("Resized Image");
        originalSizeLabel = createSizeLabel();
        resizedSizeLabel = createSizeLabel();

        imagePanel.add(wrapInPanel(originalLabel, originalSizeLabel));
        imagePanel.add(wrapInPanel(resizedLabel, resizedSizeLabel));
        add(imagePanel, BorderLayout.CENTER);

        // 输入和按钮区域
        JPanel controlPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5); // 设置间距

        // 第一行: 宽度和高度输入框
        gbc.gridx = 0;
        gbc.gridy = 0;
        controlPanel.add(new JLabel("Width:"), gbc);

        gbc.gridx = 1;
        widthField = new JTextField(5);
        controlPanel.add(widthField, gbc);

        gbc.gridx = 2;
        controlPanel.add(new JLabel("Height:"), gbc);

        gbc.gridx = 3;
        heightField = new JTextField(5);
        controlPanel.add(heightField, gbc);

        // 第二行: 按钮
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 4; // 按钮占据整行
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Load Image 按钮
        loadImageButton = new JButton("Load Image");
        loadImageButton.setPreferredSize(new Dimension(100, 30));
        controlPanel.add(loadImageButton, gbc);

        // Resize 按钮
        gbc.gridy = 2;
        resizeButton = new JButton("Resize");
        resizeButton.setPreferredSize(new Dimension(100, 30));
        controlPanel.add(resizeButton, gbc);

        // Download 按钮
        gbc.gridy = 3;
        downloadButton = new JButton("Download");
        downloadButton.setPreferredSize(new Dimension(100, 30));
        downloadButton.setEnabled(false); // 初始禁用
        controlPanel.add(downloadButton, gbc);

        add(controlPanel, BorderLayout.SOUTH);

        // 添加拖放功能
        setupDragAndDrop();

        // 按钮功能
        setupButtonListeners();

        // 设置窗口大小
        setSize(900, 500);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setupDragAndDrop() {
        originalLabel.setTransferHandler(new TransferHandler() {
            @Override
            public boolean canImport(TransferSupport support) {
                return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
            }

            @Override
            @SuppressWarnings("unchecked")
            public boolean importData(TransferSupport support) {
                if (!canImport(support)) {
                    return false;
                }

                try {
                    List<File> files = (List<File>) support.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    if (files != null && !files.isEmpty()) {
                        File file = files.get(0);
                        if (isImageFile(file)) {
                            originalPicture = new Picture(file.getAbsolutePath());
                            BufferedImage originalImage = convertToBufferedImage(originalPicture);
                            originalLabel.setIcon(new ImageIcon(scaleImage(originalImage, MAX_DISPLAY_WIDTH, MAX_DISPLAY_HEIGHT)));
                            originalLabel.setText(null);
                            originalSizeLabel.setText(originalPicture.width() + " x " + originalPicture.height());
                            return true;
                        } else {
                            JOptionPane.showMessageDialog(null, "Please drop a valid image file.");
                        }
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error loading image: " + ex.getMessage());
                }
                return false;
            }
        });
    }

    private JLabel createImageLabel(String placeholderText) {
        JLabel label = new JLabel(placeholderText, SwingConstants.CENTER);
        label.setPreferredSize(new Dimension(MAX_DISPLAY_WIDTH, MAX_DISPLAY_HEIGHT));
        return label;
    }

    private JLabel createSizeLabel() {
        JLabel label = new JLabel("", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        return label;
    }

    private JPanel wrapInPanel(JLabel imageLabel, JLabel sizeLabel) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel.add(imageLabel, BorderLayout.CENTER);
        panel.add(sizeLabel, BorderLayout.SOUTH);
        return panel;
    }

    private void setupButtonListeners() {
        loadImageButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if (isImageFile(file)) {
                    originalPicture = new Picture(file.getAbsolutePath());
                    BufferedImage originalImage = convertToBufferedImage(originalPicture);
                    originalLabel.setIcon(new ImageIcon(scaleImage(originalImage, MAX_DISPLAY_WIDTH, MAX_DISPLAY_HEIGHT)));
                    originalLabel.setText(null);
                    originalSizeLabel.setText(originalPicture.width() + " x " + originalPicture.height());
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a valid image file.");
                }
            }
        });

        resizeButton.addActionListener(e -> {
            int targetWidth, targetHeight;
            try {
                targetWidth = Integer.parseInt(widthField.getText());
                targetHeight = Integer.parseInt(heightField.getText());

                if (originalPicture != null) {
                    resizedPicture = resizePicture(originalPicture, targetWidth, targetHeight);
                    BufferedImage resizedImage = convertToBufferedImage(resizedPicture);
                    resizedLabel.setIcon(new ImageIcon(scaleImage(resizedImage, MAX_DISPLAY_WIDTH, MAX_DISPLAY_HEIGHT)));
                    resizedLabel.setText(null);
                    resizedSizeLabel.setText(resizedPicture.width() + " x " + resizedPicture.height());
                    downloadButton.setEnabled(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Please load an image first.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid width or height.");
            }
        });

        downloadButton.addActionListener(e -> {
            if (resizedPicture != null) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Save Resized Image");
                if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try {
                        BufferedImage resizedImage = convertToBufferedImage(resizedPicture);
                        ImageIO.write(resizedImage, "png", file);
                        JOptionPane.showMessageDialog(null, "Image saved successfully!");
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Error saving image: " + ex.getMessage());
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "No resized image to save.");
            }
        });
    }

    private Picture resizePicture(Picture picture, int targetWidth, int targetHeight) {
        try {
            SeamCarver sc = new SeamCarver(picture);
            while (sc.height() > targetHeight) sc.removeHorizontalSeam(sc.findHorizontalSeam());
            while (sc.width() > targetWidth) sc.removeVerticalSeam(sc.findVerticalSeam());
            return sc.picture();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error resizing image: " + e.getMessage());
            return null;
        }
    }

    private BufferedImage convertToBufferedImage(Picture picture) {
        int width = picture.width();
        int height = picture.height();
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bufferedImage.setRGB(x, y, picture.getRGB(x, y));
            }
        }
        return bufferedImage;
    }

    private BufferedImage scaleImage(BufferedImage image, int maxWidth, int maxHeight) {
        int originalWidth = image.getWidth();
        int originalHeight = image.getHeight();

        if (originalWidth <= maxWidth && originalHeight <= maxHeight) return image;

        double widthRatio = maxWidth / (double) originalWidth;
        double heightRatio = maxHeight / (double) originalHeight;
        double scale = Math.min(widthRatio, heightRatio);

        int newWidth = (int) (originalWidth * scale);
        int newHeight = (int) (originalHeight * scale);

        Image scaledImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        BufferedImage bufferedScaledImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedScaledImage.createGraphics();
        g2d.drawImage(scaledImage, 0, 0, null);
        g2d.dispose();

        return bufferedScaledImage;
    }

    private boolean isImageFile(File file) {
        String[] imageExtensions = {"jpg", "jpeg", "png", "gif", "bmp"};
        String fileName = file.getName().toLowerCase();
        for (String extension : imageExtensions) {
            if (fileName.endsWith(extension)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ImageResizerClient());
    }
}