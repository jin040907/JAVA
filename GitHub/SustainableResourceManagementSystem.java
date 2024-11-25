package finalProject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.jfree.chart.*;
import org.jfree.chart.axis.AxisLabelLocation;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

public class SustainableResourceManagementSystem {
    private JFrame frame;
    private JTextField energyConsumptionField, co2EmissionFactorField, co2EmissionField, reductionPercentageField;
    private JTextArea calculationArea, reportArea;
    private JButton calculateButton, saveButton, generateReportButton, generateGraphButton;

    public SustainableResourceManagementSystem() {
        frame = new JFrame("Sustainable Resource Management System");
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS)); 
        frame.setSize(500, 700); 

        // Energy consumption input field
        JPanel energyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        energyPanel.add(new JLabel("Energy Consumption (kWh):"));
        energyConsumptionField = new JTextField(10);
        energyPanel.add(energyConsumptionField);
        frame.add(energyPanel);

        // CO2 emission factor input field
        JPanel co2FactorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        co2FactorPanel.add(new JLabel("CO2 Emission Factor (kg CO2/kWh):"));
        co2EmissionFactorField = new JTextField(10);
        co2FactorPanel.add(co2EmissionFactorField);
        frame.add(co2FactorPanel);

        // CO2 emission input field
        JPanel co2EmissionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        co2EmissionPanel.add(new JLabel("CO2 Emission (kg):"));
        co2EmissionField = new JTextField(10);
        co2EmissionPanel.add(co2EmissionField);
        frame.add(co2EmissionPanel);

        // Cloud power reduction percentage input field
        JPanel reductionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        reductionPanel.add(new JLabel("Cloud Power Reduction (%):"));
        reductionPercentageField = new JTextField(10);
        reductionPanel.add(reductionPercentageField);
        frame.add(reductionPanel);

        // Panel containing buttons
        JPanel buttonPanel1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        calculateButton = new JButton("Calculate Total Emission");
        calculateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calculateTotalEmission();
            }
        });
        buttonPanel1.add(calculateButton);

        saveButton = new JButton("Save Data");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveData();
            }
        });
        buttonPanel1.add(saveButton);

        JPanel buttonPanel2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        generateReportButton = new JButton("Generate Report");
        generateReportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generateReport();
            }
        });
        buttonPanel2.add(generateReportButton);

        generateGraphButton = new JButton("Generate Emission Graphs");
        generateGraphButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generateEmissionGraph();
            }
        });
        buttonPanel2.add(generateGraphButton);

        frame.add(buttonPanel1);
        frame.add(buttonPanel2);

        // Text area for displaying calculation results
        JPanel calculationPanel = new JPanel(new BorderLayout());
        calculationPanel.add(new JLabel("Calculation Results:"), BorderLayout.NORTH);
        calculationArea = new JTextArea(10, 40);
        calculationArea.setEditable(false);
        JScrollPane calculationScrollPane = new JScrollPane(calculationArea);
        calculationScrollPane.setPreferredSize(new Dimension(450, 150)); // Explicit size for calculation area
        calculationPanel.add(calculationScrollPane, BorderLayout.CENTER);
        frame.add(calculationPanel);

        // Text area for displaying reports
        JPanel reportPanel = new JPanel(new BorderLayout());
        reportPanel.add(new JLabel("Report:"), BorderLayout.NORTH);
        reportArea = new JTextArea(20, 80);
        reportArea.setEditable(false);
        JScrollPane reportScrollPane = new JScrollPane(reportArea);
        reportScrollPane.setPreferredSize(new Dimension(450, 300)); // Explicit size for report area
        reportPanel.add(reportScrollPane, BorderLayout.CENTER);
        frame.add(reportPanel);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    // Calculate Total Emission
    private void calculateTotalEmission() {
        try {
            double energyConsumption = Double.parseDouble(energyConsumptionField.getText());
            double co2EmissionFactor = Double.parseDouble(co2EmissionFactorField.getText());
            double co2Emission = Double.parseDouble(co2EmissionField.getText());
            double carbonEmissionFactor = co2EmissionFactor * 0.2727;
            double reductionPercentage = Double.parseDouble(reductionPercentageField.getText());

            // Calculate emissions before and after cloud adoption (CO2 and Carbon)
            double energyConsumptionAfterCloud = energyConsumption * (1 - reductionPercentage / 100);
            double totalEmissionCO2BeforeCloud = (energyConsumption * co2EmissionFactor) + co2Emission;
            double totalEmissionCO2AfterCloud = (energyConsumptionAfterCloud * co2EmissionFactor) + co2Emission;

            double totalEmissionCarbonBeforeCloud = (energyConsumption * carbonEmissionFactor) + co2Emission * 0.2727;  // 1 kg CO2 = 0.2727 kg Carbon
            double totalEmissionCarbonAfterCloud = (energyConsumptionAfterCloud * carbonEmissionFactor) + co2Emission * 0.2727;

            calculationArea.setText("CO2 Emissions:\nBefore Cloud Adoption: " + totalEmissionCO2BeforeCloud + " kg CO2\n" +
                    "After Cloud Adoption (Reduced Consumption): " + totalEmissionCO2AfterCloud + " kg CO2\n" +
                    "\nCarbon Emissions:\nBefore Cloud Adoption: " + totalEmissionCarbonBeforeCloud + " kg C\n" +
                    "After Cloud Adoption (Reduced Consumption): " + totalEmissionCarbonAfterCloud + " kg C");
        } catch (NumberFormatException e) {
            calculationArea.setText("Invalid input. Please enter valid numbers.");
        }
    }
    
    // Save Data to File
    private void saveData() {
        try {
            double energyConsumption = Double.parseDouble(energyConsumptionField.getText());
            double co2EmissionFactor = Double.parseDouble(co2EmissionFactorField.getText());
            double co2Emission = Double.parseDouble(co2EmissionField.getText());
            double carbonEmissionFactor = co2EmissionFactor * 0.2727;
            double reductionPercentage = Double.parseDouble(reductionPercentageField.getText());

            // Get current date for timestamp
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

            // Calculate emissions before and after cloud adoption
            double energyConsumptionAfterCloud = energyConsumption * (1 - reductionPercentage / 100);
            double totalEmissionCO2BeforeCloud = (energyConsumption * co2EmissionFactor) + co2Emission;
            double totalEmissionCO2AfterCloud = (energyConsumptionAfterCloud * co2EmissionFactor) + co2Emission;

            double totalEmissionCarbonBeforeCloud = (energyConsumption * carbonEmissionFactor) + co2Emission * 0.2727;
            double totalEmissionCarbonAfterCloud = (energyConsumptionAfterCloud * carbonEmissionFactor) + co2Emission * 0.2727;

            // Write to file
            FileWriter writer = new FileWriter("carbon_emission_data.txt", true);
            writer.write("Timestamp: " + timestamp + ", Energy Consumption: " + energyConsumption + " kWh, CO2 Emission Factor: " + co2EmissionFactor
                    + " kg CO2/kWh, CO2 Emission: " + co2Emission + " kg, Carbon Emission Factor: " + carbonEmissionFactor
                    + " kg C/kWh, Power Reduction: " + reductionPercentage + "%, "
                    + "Total CO2 Emission Before Cloud: " + totalEmissionCO2BeforeCloud + " kg CO2, "
                    + "Total CO2 Emission After Cloud: " + totalEmissionCO2AfterCloud + " kg CO2, "
                    + "Total Carbon Emission Before Cloud: " + totalEmissionCarbonBeforeCloud + " kg C, "
                    + "Total Carbon Emission After Cloud: " + totalEmissionCarbonAfterCloud + " kg C\n");
            writer.close();
            JOptionPane.showMessageDialog(frame, "Data saved successfully!");
        } catch (IOException | NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Error saving data. Please check input.");
        }
    }

    // Generate Report
    private void generateReport() {
        try {
            File file = new File("carbon_emission_data.txt");
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                StringBuilder report = new StringBuilder("CO2 and Carbon Emission Report:\n\n");

                while ((line = reader.readLine()) != null) {
                    report.append(line).append("\n");
                }

                reader.close();
                reportArea.setText(report.toString());
            } else {
                reportArea.setText("No data available for the report.");
            }
        } catch (IOException e) {
            reportArea.setText("Error generating report.");
        }
    }

    // Generate Graph of Emission Data
    private void generateEmissionGraph() {
        try {
            // Create a combined dataset
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            // Read the file and add data to the dataset
            BufferedReader reader = new BufferedReader(new FileReader("carbon_emission_data.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(", ");
                String timestamp = data[0].split(": ")[1];
                String emissionCO2BeforeStr = data[6].split(": ")[1].replace(" kg CO2", "");
                String emissionCO2AfterStr = data[7].split(": ")[1].replace(" kg CO2", "");
                String emissionCarbonBeforeStr = data[8].split(": ")[1].replace(" kg C", "");
                String emissionCarbonAfterStr = data[9].split(": ")[1].replace(" kg C", "");

                double emissionCO2Before = Double.parseDouble(emissionCO2BeforeStr);
                double emissionCO2After = Double.parseDouble(emissionCO2AfterStr);
                double emissionCarbonBefore = Double.parseDouble(emissionCarbonBeforeStr);
                double emissionCarbonAfter = Double.parseDouble(emissionCarbonAfterStr);

                // Add data to the dataset for all four series
                dataset.addValue(emissionCO2Before, "CO2 Emission Before Cloud", timestamp);
                dataset.addValue(emissionCO2After, "CO2 Emission After Cloud", timestamp);
                dataset.addValue(emissionCarbonBefore, "Carbon Emission Before Cloud", timestamp);
                dataset.addValue(emissionCarbonAfter, "Carbon Emission After Cloud", timestamp);
            }
            reader.close();

            // Create the chart with all data
            JFreeChart chart = createChart(dataset, "CO2 and Carbon Emission Graphs: Before and After Cloud", "Timestamp", "Total Emission (kg)");

            // Display the chart
            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(1, 1));  // Single chart panel

            panel.add(new ChartPanel(chart));

            // Create a new JFrame to display the chart
            JFrame chartFrame = new JFrame("Combined Emission Graph");
            chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            chartFrame.getContentPane().add(panel, BorderLayout.CENTER);
            chartFrame.pack();
            chartFrame.setVisible(true);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error reading data for graph.");
        }
    }

    // Method to create chart with point labels
    private JFreeChart createChart(DefaultCategoryDataset dataset, String title, String xAxisLabel, String yAxisLabel) {
        JFreeChart chart = ChartFactory.createLineChart(
                title,                     // Chart title
                xAxisLabel,                // X-axis label
                yAxisLabel,                // Y-axis label
                dataset,                   // Dataset
                PlotOrientation.VERTICAL,  // Orientation
                true,                      // Include legend
                true,                      // Include tooltips
                false                      // Include URLs
        );

        CategoryPlot plot = chart.getCategoryPlot();
        // plot.getDomainAxis().setLabelAngle(Math.PI / 2);  // Rotate labels by 90 degrees
        plot.getDomainAxis().setLabelLocation(AxisLabelLocation.HIGH_END);  // X-axis label
        plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.DOWN_90);  // Adjust label position
        plot.getRangeAxis().setLabelLocation(AxisLabelLocation.HIGH_END);  // Y-axis label

        // Increase space between axis and labels
        plot.getDomainAxis().setLowerMargin(0.05);  // Optional: Adjust space between the axis and the labels
        plot.getDomainAxis().setUpperMargin(0.05);  // Optional: Adjust space between the axis and the labels

        LineAndShapeRenderer renderer = new LineAndShapeRenderer();

        // Enable shapes and data point labels
        renderer.setShapesVisible(true);
        renderer.setItemLabelsVisible(true);
        renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setItemLabelFont(new Font("SansSerif", Font.PLAIN, 12));

        // Set different colors for each series
        renderer.setSeriesPaint(0, Color.RED);  // CO2 Emission Before Cloud
        renderer.setSeriesPaint(1, Color.YELLOW);  // CO2 Emission After Cloud
        renderer.setSeriesPaint(2, Color.GREEN);  // Carbon Emission Before Cloud
        renderer.setSeriesPaint(3, Color.BLUE);  // Carbon Emission After Cloud

        // Apply renderer to the plot
        plot.setRenderer(renderer);

        return chart;
    }

    public static void main(String[] args) {
        new SustainableResourceManagementSystem();
    }
}
