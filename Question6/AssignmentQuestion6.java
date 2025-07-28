// Traffic Signal Management System (Multithreaded)
// Functionality:
//  Queue (FIFO Scheduling): Manage vehicles at a traffic signal.
//  Priority Queue (Emergency Vehicles): Give priority to ambulances and fire trucks.
//  Multithreading:
// o Separate threads for traffic light changes, vehicle movement, and emergency
// handling.
// GUI:
//  An animated traffic intersection.
//  A queue showing waiting vehicles.
//  Buttons to:
// o Change Traffic Signal (Simulates signal changes in real-time).
// o Add Vehicles (Continuously add vehicles with a thread).
// o Enable Emergency Mode (Emergency vehicle gets priority in multithreaded execution).
// Implementation:
//  Main thread: Handles GUI and user inputs.
//  Traffic light thread: Changes signals at fixed intervals.
//  Vehicle queue thread: Processes vehicles using FIFO and priority queue logic.
// Data Structures:
//  Queue: Regular vehicle queue.
//  Priority Queue: Emergency vehicle handling.
// Multithreading Benefits:
//  Vehicles move in real-time without blocking GUI updates.
//  Traffic lights operate independently of vehicle movement.



package Question6;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

// Vehicle class
class Vehicle implements Comparable<Vehicle> {
    String type;
    boolean isEmergency;

    public Vehicle(String type, boolean isEmergency) {
        this.type = type;
        this.isEmergency = isEmergency;
    }

    public String toString() {
        return (isEmergency ? "🚨 " : "") + type;
    }

    @Override
    public int compareTo(Vehicle other) {
        return Boolean.compare(other.isEmergency, this.isEmergency); // emergency vehicles come first
    }
}

// VehicleManager class
class VehicleManager {
    private final Queue<Vehicle> regularQueue = new LinkedList<>();
    private final Queue<Vehicle> emergencyQueue = new LinkedList<>();

    public synchronized void addVehicle(Vehicle v) {
        if (v.isEmergency) emergencyQueue.offer(v);
        else regularQueue.offer(v);
    }

    public synchronized Vehicle getNextVehicle() {
        if (!emergencyQueue.isEmpty()) return emergencyQueue.poll();
        return regularQueue.poll();
    }

    public synchronized List<Vehicle> getAllVehicles() {
        List<Vehicle> all = new ArrayList<>();
        all.addAll(emergencyQueue);
        all.addAll(regularQueue);
        return all;
    }
}

// SignalController Thread
class SignalController extends Thread {
    private volatile boolean greenLight = true;

    public void run() {
        while (true) {
            greenLight = true;
            System.out.println("🟢 GREEN Light");
            try { Thread.sleep(3000); } catch (Exception e) {}
            greenLight = false;
            System.out.println("🔴 RED Light");
            try { Thread.sleep(3000); } catch (Exception e) {}
        }
    }

    public boolean isGreen() {
        return greenLight;
    }
}

// VehicleProcessor Thread
class VehicleProcessor extends Thread {
    private final VehicleManager manager;
    private final SignalController signal;
    private final JTextArea logArea;
    private final Runnable updateQueue;

    public VehicleProcessor(VehicleManager manager, SignalController signal,
                            JTextArea logArea, Runnable updateQueue) {
        this.manager = manager;
        this.signal = signal;
        this.logArea = logArea;
        this.updateQueue = updateQueue;
    }

    public void run() {
        while (true) {
            if (signal.isGreen()) {
                Vehicle v = manager.getNextVehicle();
                if (v != null) {
                    String msg = "⏩ " + v + " passed the intersection.\n";
                    SwingUtilities.invokeLater(() -> {
                        logArea.append(msg);
                        updateQueue.run();
                    });
                }
            }
            try { Thread.sleep(1000); } catch (Exception e) {}
        }
    }
}

// GUI class
public class AssignmentQuestion6 extends JFrame {
    private final VehicleManager manager = new VehicleManager();
    private final SignalController signalThread = new SignalController();
    private final JTextArea queueArea = new JTextArea(10, 40);
    private final JTextArea logArea = new JTextArea(10, 40);

    public AssignmentQuestion6() {
        super("🚦 Traffic Signal Management System");

        JButton addCarBtn = new JButton("Add Regular Vehicle");
        JButton addEmergencyBtn = new JButton("Add Emergency Vehicle");

        addCarBtn.addActionListener(_ -> {
            manager.addVehicle(new Vehicle("Car", false));
            updateQueueDisplay();
        });

        addEmergencyBtn.addActionListener(_ -> {
            manager.addVehicle(new Vehicle("Ambulance", true));
            updateQueueDisplay();
        });

        JPanel controlPanel = new JPanel();
        controlPanel.add(addCarBtn);
        controlPanel.add(addEmergencyBtn);

        queueArea.setEditable(false);
        logArea.setEditable(false);

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.add(new JScrollPane(queueArea));
        textPanel.add(new JScrollPane(logArea));

        add(controlPanel, BorderLayout.NORTH);
        add(textPanel, BorderLayout.CENTER);

        signalThread.start();
        new VehicleProcessor(manager, signalThread, logArea, this::updateQueueDisplay).start();

        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void updateQueueDisplay() {
        StringBuilder sb = new StringBuilder("🧾 Vehicles in Queue:\n");
        for (Vehicle v : manager.getAllVehicles()) {
            sb.append(v).append("\n");
        }
        queueArea.setText(sb.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AssignmentQuestion6::new);
    }
}
