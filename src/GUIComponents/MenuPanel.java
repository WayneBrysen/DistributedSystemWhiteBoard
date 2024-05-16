package GUIComponents;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import Shapes.Shape;
import WhiteBoardInterface.WhiteBoardRemote;

public class MenuPanel extends JPanel {
    private JMenuBar menuBar = new JMenuBar();;
    private JMenu fileMenu = new JMenu("File");
    private JMenuItem newButton, openButton, saveButton, saveAsButton, closeButton;
    private DrawPanel drawPanel;
    private WhiteBoardRemote serverApp;
    private File currentCanvas;
    private String username;

    public MenuPanel(DrawPanel drawPanel, WhiteBoardRemote serverApp, String username) {
        this.drawPanel = drawPanel;
        this.serverApp = serverApp;
        this.username = username;

        topMenu();
    }

    private void topMenu() {
        this.setLayout(new BorderLayout());

        menuBar.add(fileMenu);

        // File menu buttons
        newButton = new JMenuItem("New");
        openButton = new JMenuItem("Open");
        saveButton = new JMenuItem("Save");
        saveAsButton = new JMenuItem("Save As");
        closeButton = new JMenuItem("Close");

        this.add(menuBar, BorderLayout.NORTH);

        fileMenu.add(newButton);
        fileMenu.add(openButton);
        fileMenu.add(saveButton);
        fileMenu.add(saveAsButton);
        fileMenu.add(closeButton);

        this.add(menuBar, BorderLayout.NORTH);

        newButton.addActionListener(e -> {
            createNewCanvas();
        });
        openButton.addActionListener(e -> {
            openExistingCanvas();
        });
        saveButton.addActionListener(e -> {
            saveCurrentCanvas();
        });
        saveAsButton.addActionListener(e -> {
            saveAsNewCanvas();
        });
        closeButton.addActionListener(e -> {
            closeApplication();
        });
    }

    private void createNewCanvas() {
        try {
            serverApp.setShapes(new ArrayList<>());
        } catch (RemoteException e) {
            drawPanel.addNotification("Error creating new canvas.");
        }
        currentCanvas = null;

    }

    private void openExistingCanvas() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Open File");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Serialized Canvas Files", "ser"));
        fileChooser.setApproveButtonText("Open");

        int userSelection = fileChooser.showOpenDialog(this);
        if(userSelection == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                FileInputStream fileIn = new FileInputStream(selectedFile);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                @SuppressWarnings("unchecked")
                List<Shape> loadedShapes = (List<Shape>) in.readObject();
                serverApp.setShapes(loadedShapes);
                currentCanvas = selectedFile;
                in.close();
                fileIn.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error opening file.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (ClassNotFoundException e) {
                JOptionPane.showMessageDialog(this, "Error loading file.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveCurrentCanvas() {
        if(currentCanvas == null) {
            saveAsNewCanvas();
        } else {
            try {
                FileOutputStream fileOut = new FileOutputStream(currentCanvas);
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(drawPanel.getShapes());
                out.close();
                fileOut.close();
                drawPanel.addNotification("Canvas saved successfully.");
            } catch(IOException e) {
                drawPanel.addNotification("Error saving canvas.");
            }
        }
    }

    private void saveAsNewCanvas() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save As");
        fileChooser.setApproveButtonText("Save");

        int userSelection = fileChooser.showSaveDialog(this);
        if(userSelection == JFileChooser.APPROVE_OPTION) {
            currentCanvas = fileChooser.getSelectedFile();
            currentCanvas = new File(currentCanvas.getParentFile(), currentCanvas.getName() + ".ser");
            try {
                FileOutputStream fileOut = new FileOutputStream(currentCanvas);
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(drawPanel.getShapes());
                out.close();
                fileOut.close();
                JOptionPane.showMessageDialog(this, "Canvas saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch(IOException e) {
                JOptionPane.showMessageDialog(this, "Error saving canvas.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void closeApplication() {
        try {
            serverApp.removeUser(username);
        } catch (RemoteException e)
        {
            System.out.println("Error removing user from server.");
        }
        System.exit(0);
    }

}
