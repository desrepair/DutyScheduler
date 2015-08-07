/*
 * Duty Scheduler: A scheduler for point based duty.
 * Copyright (c) 2015 Khetthai Laksanakorn
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * For source code and contact information for Khetthai Laksanakorn,
 * see <http://www.github.com/desrepair/DutyScheduler>.
 *
 */

package SchedulingHeuristic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by desrepair on 04-Aug-15.
 */
public class DutySchedulerGui {
    private JLabel StartDateRangeLabel;
    private JComboBox StartDateRangeDayComboBox;
    private JComboBox StartDateRangeMonthComboBox;
    private JComboBox StartDateRangeYearComboBox;
    private JPanel StartDateRangePanel;
    private JPanel EndDateRangePanel;
    private JLabel EndDateRangeLabel;
    private JComboBox EndDateRangeDayComboBox;
    private JComboBox EndDateRangeMonthComboBox;
    private JComboBox EndDateRangeYearComboBox;
    private JPanel DutyBlockLengthPanel;
    private JLabel DutyBlockLengthLabel;
    private JTextField DutyBlockLengthTextField;
    private JPanel RasPerDutyBlockPanel;
    private JLabel RasPerDutyBlockLabel;
    private JTextField RasPerDutyBlockTextField;
    private JPanel InitialSettingsPanel;
    private JPanel DutySchedulerGui;
    private JPanel DayValuePanel;
    private JLabel DayValueLabel;
    private JTextField MonDayValueTextField;
    private JLabel MonDayValueLabel;
    private JLabel TueDayValueLabel;
    private JTextField TueDayValueTextField;
    private JLabel WedDayValueLabel;
    private JTextField WedDayValueTextField;
    private JLabel ThursDayValueLabel;
    private JTextField ThursDayValueTextField;
    private JLabel FriDayValueLabel;
    private JTextField FriDayValueTextField;
    private JLabel SatDayValueLabel;
    private JTextField SatDayValueTextField;
    private JLabel SunDayValueLabel;
    private JTextField SunDayValueTextField;
    private JPanel RaPanel;
    private JList RaList;
    private JTextField AddRaTextField;
    private JButton AddRaButton;
    private JPanel AddRaPanel;
    private JButton RemoveRaButton;
    private JPanel BlackoutDatePanel;
    private JList BlackoutDateList;
    private JButton RemoveBlackoutDateButton;
    private JComboBox BlackoutDateDayComboBox;
    private JComboBox BlackoutDateMonthComboBox;
    private JComboBox BlackoutDateYearComboBox;
    private JButton AddBlackoutDateButton;
    private JPanel AddBlackoutDatePanel;
    private JButton ScheduleDutyButton;
    private JButton ExceptionDayValueButton;
    private JScrollPane RaListScrollPane;
    private JScrollPane BlackoutDateListScrollPane;
    private JTextArea OutputTextArea;
    private JPanel SubmitToGCalPanel;
    private JPanel ResultPanel;
    private JScrollPane OutputScrollPane;
    private JSplitPane RaInfoSplitPane;
    private JLabel CalendarNameLabel;
    private JTextField CalendarNameTextField;
    private JButton SubmitToGCalButton;

    private HashMap<String, ArrayList<LocalDate>> raBlackoutMap;
    private HashMap<LocalDate, Double> exceptionMap;
    private ArrayList<DutyBlock> schedule;

    public DutySchedulerGui() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            System.exit(1);
        }

        raBlackoutMap = new HashMap<>();
        exceptionMap = new HashMap<>();
        schedule = new ArrayList<>();

        RaList.setModel(new DefaultListModel<>());
        BlackoutDateList.setModel(new DefaultListModel<>());
        LocalDate now = LocalDate.now();

        enableComponents(BlackoutDatePanel, false);

        //Populate day combo boxes.
        for (int i = 1; i < 32; i++) {
            StartDateRangeDayComboBox.addItem(String.valueOf(i));
            EndDateRangeDayComboBox.addItem(String.valueOf(i));
            BlackoutDateDayComboBox.addItem(String.valueOf(i));
        }

        //Populate month combo boxes.
        for (int i = 1; i < 13; i++) {
            StartDateRangeMonthComboBox.addItem(String.valueOf(i));
            EndDateRangeMonthComboBox.addItem(String.valueOf(i));
            BlackoutDateMonthComboBox.addItem(String.valueOf(i));
        }

        //Populate year combo boxes.
        int thisYear = now.getYear();
        for (int i = thisYear; i < thisYear + 10; i++) {
            StartDateRangeYearComboBox.addItem(String.valueOf(i));
            EndDateRangeYearComboBox.addItem(String.valueOf(i));
            BlackoutDateYearComboBox.addItem(String.valueOf(i));
        }

        ExceptionDayValueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = JOptionPane.showInputDialog(
                        null,
                        "Enter exceptions in this format: YYYY-MM-DD,<POINT VALUE>;YYYY-MM-DD,<POINT VALUE>;ect.",
                        "Enter exceptions",
                        JOptionPane.QUESTION_MESSAGE);

                if (input.isEmpty()) {
                    exceptionMap.clear();
                    return;
                }
                String[] tokens = input.split(";");
                for (String token : tokens) {
                    String[] pieces = token.split(",");
                    LocalDate date = LocalDate.parse(pieces[0]);
                    double point = Double.parseDouble(pieces[1]);
                    exceptionMap.put(date, point);
                }
            }
        });

        AddRaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String raName = AddRaTextField.getText();
                raBlackoutMap.put(raName, new ArrayList<>());
                DefaultListModel model = (DefaultListModel) RaList.getModel();
                model.addElement(raName);
                AddRaTextField.setText("");
                AddRaTextField.requestFocusInWindow();
            }
        });

        AddRaTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String raName = AddRaTextField.getText();
                raBlackoutMap.put(raName, new ArrayList<>());
                DefaultListModel model = (DefaultListModel) RaList.getModel();
                model.addElement(raName);
                AddRaTextField.setText("");
                AddRaTextField.requestFocusInWindow();
            }
        });

        RemoveRaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedRa = (String) RaList.getSelectedValue();
                raBlackoutMap.remove(selectedRa);
                DefaultListModel model = (DefaultListModel) RaList.getModel();
                model.remove(RaList.getSelectedIndex());
            }
        });

        RaList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                enableComponents(BlackoutDatePanel, !RaList.isSelectionEmpty());

                String selectedRa = (String) RaList.getSelectedValue();
                DefaultListModel model = (DefaultListModel) BlackoutDateList.getModel();
                model.clear();
                for (LocalDate blackoutDate : raBlackoutMap.get(selectedRa)) {
                    model.addElement(blackoutDate.toString());
                }
            }
        });

        AddBlackoutDateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int day = BlackoutDateDayComboBox.getSelectedIndex() + 1;
                int month = BlackoutDateMonthComboBox.getSelectedIndex() + 1;
                int year = BlackoutDateYearComboBox.getSelectedIndex() + now.getYear();
                LocalDate blackoutDate = LocalDate.of(year, month, day);

                DefaultListModel model = (DefaultListModel) BlackoutDateList.getModel();
                model.addElement(blackoutDate);
                raBlackoutMap.get(RaList.getSelectedValue()).add(blackoutDate);
            }
        });

        RemoveBlackoutDateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = BlackoutDateList.getSelectedIndex();
                DefaultListModel model = (DefaultListModel) BlackoutDateList.getModel();
                model.remove(selectedIndex);
                raBlackoutMap.get(RaList.getSelectedValue()).remove(selectedIndex);
            }
        });

        ScheduleDutyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalDate start = LocalDate.of(
                        StartDateRangeYearComboBox.getSelectedIndex() + now.getYear(),
                        StartDateRangeMonthComboBox.getSelectedIndex() + 1,
                        StartDateRangeDayComboBox.getSelectedIndex() + 1);

                LocalDate end = LocalDate.of(
                        EndDateRangeYearComboBox.getSelectedIndex() + now.getYear(),
                        EndDateRangeMonthComboBox.getSelectedIndex() + 1,
                        EndDateRangeDayComboBox.getSelectedIndex() + 1);

                if (end.isBefore(start)) {
                    JOptionPane.showMessageDialog(
                            null,
                            "The start date must be before the end date.",
                            "Invalid input.",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }


                int daysPerDutyBlock;
                int rasPerDutyBlock;

                try {
                    daysPerDutyBlock = Integer.valueOf(DutyBlockLengthTextField.getText());
                    rasPerDutyBlock = Integer.valueOf(RasPerDutyBlockTextField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Days per duty block and RAs per duty block must be whole numbers.",
                            "Invalid input.",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (daysPerDutyBlock < 1 || rasPerDutyBlock < 1) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Days per duty block and RAs per duty block must be whole numbers.",
                            "Invalid input.",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }


                HashMap<LocalDate, Double> dayValues = new HashMap<>();

                try {
                    for (LocalDate date = start; date.isBefore(end.plusDays(1)); date = date.plusDays(1)) {
                        if (exceptionMap.containsKey(date)) {
                            dayValues.put(date, exceptionMap.get(date));
                        } else {
                            switch (date.getDayOfWeek()) {
                                case MONDAY:
                                    dayValues.put(date, Double.parseDouble(MonDayValueTextField.getText()));
                                    break;
                                case TUESDAY:
                                    dayValues.put(date, Double.parseDouble(TueDayValueTextField.getText()));
                                    break;
                                case WEDNESDAY:
                                    dayValues.put(date, Double.parseDouble(WedDayValueTextField.getText()));
                                    break;
                                case THURSDAY:
                                    dayValues.put(date, Double.parseDouble(ThursDayValueTextField.getText()));
                                    break;
                                case FRIDAY:
                                    dayValues.put(date, Double.parseDouble(FriDayValueTextField.getText()));
                                    break;
                                case SATURDAY:
                                    dayValues.put(date, Double.parseDouble(SatDayValueTextField.getText()));
                                    break;
                                case SUNDAY:
                                    dayValues.put(date, Double.parseDouble(SunDayValueTextField.getText()));
                                    break;
                            }
                        }
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Error parsing day of week point values.",
                            "Invalid input.",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                DutyCalendar calendar = new DutyCalendar(start, end, daysPerDutyBlock, rasPerDutyBlock);
                calendar.setDayValues(dayValues);
                for (String raName : raBlackoutMap.keySet()) {
                    calendar.addRa(raName, raBlackoutMap.get(raName));
                }

                schedule = calendar.assignDuty();

                StringBuilder builder = new StringBuilder();
                builder.append(calendar.toString());
                builder.append("\n ________________________ \n");
                builder.append(calendar.printRaPointValues());

                OutputTextArea.setText("");
                OutputTextArea.append(builder.toString());

                ResultPanel.setVisible(true);
            }
        });

        SubmitToGCalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GCalAccessForLocalApplication.createNewDutyCalendar(CalendarNameTextField.getText(), schedule);
            }
        });
    }

    /**
     * Sets the enable state of all components within a container.
     *
     * @param container Container to set enable state in.
     * @param enable    Enable state.
     */
    private void enableComponents(Container container, boolean enable) {
        Component[] components = container.getComponents();
        for (Component component : components) {
            component.setEnabled(enable);
            if (component instanceof Container) {
                enableComponents((Container) component, enable);
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("DutySchedulerGui");
        frame.setContentPane(new DutySchedulerGui().DutySchedulerGui);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        DutySchedulerGui = new JPanel();
        DutySchedulerGui.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(5, 3, new Insets(0, 0, 0, 0), -1, -1));
        InitialSettingsPanel = new JPanel();
        InitialSettingsPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        DutySchedulerGui.add(InitialSettingsPanel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        StartDateRangePanel = new JPanel();
        StartDateRangePanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        InitialSettingsPanel.add(StartDateRangePanel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        StartDateRangeLabel = new JLabel();
        StartDateRangeLabel.setText("Start Date: ");
        StartDateRangePanel.add(StartDateRangeLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        StartDateRangeDayComboBox = new JComboBox();
        StartDateRangeDayComboBox.setToolTipText("Day");
        StartDateRangePanel.add(StartDateRangeDayComboBox, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        StartDateRangeMonthComboBox = new JComboBox();
        StartDateRangeMonthComboBox.setToolTipText("Month");
        StartDateRangePanel.add(StartDateRangeMonthComboBox, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        StartDateRangeYearComboBox = new JComboBox();
        StartDateRangeYearComboBox.setToolTipText("Year");
        StartDateRangePanel.add(StartDateRangeYearComboBox, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        EndDateRangePanel = new JPanel();
        EndDateRangePanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        InitialSettingsPanel.add(EndDateRangePanel, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        EndDateRangeLabel = new JLabel();
        EndDateRangeLabel.setText("End Date");
        EndDateRangePanel.add(EndDateRangeLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        EndDateRangeDayComboBox = new JComboBox();
        EndDateRangeDayComboBox.setToolTipText("Day");
        EndDateRangePanel.add(EndDateRangeDayComboBox, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        EndDateRangeMonthComboBox = new JComboBox();
        EndDateRangeMonthComboBox.setToolTipText("Month");
        EndDateRangePanel.add(EndDateRangeMonthComboBox, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        EndDateRangeYearComboBox = new JComboBox();
        EndDateRangeYearComboBox.setToolTipText("Year");
        EndDateRangePanel.add(EndDateRangeYearComboBox, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        DutyBlockLengthPanel = new JPanel();
        DutyBlockLengthPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        InitialSettingsPanel.add(DutyBlockLengthPanel, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        DutyBlockLengthLabel = new JLabel();
        DutyBlockLengthLabel.setText("Days Per Duty Block: ");
        DutyBlockLengthPanel.add(DutyBlockLengthLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        DutyBlockLengthTextField = new JTextField();
        DutyBlockLengthPanel.add(DutyBlockLengthTextField, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(75, -1), null, 0, false));
        RasPerDutyBlockPanel = new JPanel();
        RasPerDutyBlockPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        InitialSettingsPanel.add(RasPerDutyBlockPanel, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        RasPerDutyBlockLabel = new JLabel();
        RasPerDutyBlockLabel.setText("RAs Per Duty Block");
        RasPerDutyBlockPanel.add(RasPerDutyBlockLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        RasPerDutyBlockTextField = new JTextField();
        RasPerDutyBlockPanel.add(RasPerDutyBlockTextField, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        DayValuePanel = new JPanel();
        DayValuePanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 9, new Insets(0, 0, 0, 0), -1, -1));
        DutySchedulerGui.add(DayValuePanel, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        DayValueLabel = new JLabel();
        DayValueLabel.setText("Point Values Per Day");
        DayValuePanel.add(DayValueLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        MonDayValueLabel = new JLabel();
        MonDayValueLabel.setText("Mon");
        DayValuePanel.add(MonDayValueLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        MonDayValueTextField = new JTextField();
        DayValuePanel.add(MonDayValueTextField, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        TueDayValueLabel = new JLabel();
        TueDayValueLabel.setText("Tue");
        DayValuePanel.add(TueDayValueLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        TueDayValueTextField = new JTextField();
        DayValuePanel.add(TueDayValueTextField, new com.intellij.uiDesigner.core.GridConstraints(0, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        WedDayValueLabel = new JLabel();
        WedDayValueLabel.setText("Wed");
        DayValuePanel.add(WedDayValueLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 5, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        WedDayValueTextField = new JTextField();
        DayValuePanel.add(WedDayValueTextField, new com.intellij.uiDesigner.core.GridConstraints(0, 6, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        ThursDayValueLabel = new JLabel();
        ThursDayValueLabel.setText("Thurs");
        DayValuePanel.add(ThursDayValueLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 7, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ThursDayValueTextField = new JTextField();
        DayValuePanel.add(ThursDayValueTextField, new com.intellij.uiDesigner.core.GridConstraints(0, 8, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        FriDayValueLabel = new JLabel();
        FriDayValueLabel.setText("Fri");
        DayValuePanel.add(FriDayValueLabel, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(13, 24), null, 0, false));
        FriDayValueTextField = new JTextField();
        DayValuePanel.add(FriDayValueTextField, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, 24), null, 0, false));
        SatDayValueLabel = new JLabel();
        SatDayValueLabel.setText("Sat");
        DayValuePanel.add(SatDayValueLabel, new com.intellij.uiDesigner.core.GridConstraints(1, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(16, 24), null, 0, false));
        SatDayValueTextField = new JTextField();
        DayValuePanel.add(SatDayValueTextField, new com.intellij.uiDesigner.core.GridConstraints(1, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, 24), null, 0, false));
        SunDayValueLabel = new JLabel();
        SunDayValueLabel.setText("Sun");
        DayValuePanel.add(SunDayValueLabel, new com.intellij.uiDesigner.core.GridConstraints(1, 5, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(20, 24), null, 0, false));
        SunDayValueTextField = new JTextField();
        DayValuePanel.add(SunDayValueTextField, new com.intellij.uiDesigner.core.GridConstraints(1, 6, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, 24), null, 0, false));
        ExceptionDayValueButton = new JButton();
        ExceptionDayValueButton.setText("Exceptions");
        DayValuePanel.add(ExceptionDayValueButton, new com.intellij.uiDesigner.core.GridConstraints(1, 7, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        RaInfoSplitPane = new JSplitPane();
        DutySchedulerGui.add(RaInfoSplitPane, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        RaPanel = new JPanel();
        RaPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        RaInfoSplitPane.setLeftComponent(RaPanel);
        AddRaPanel = new JPanel();
        AddRaPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        RaPanel.add(AddRaPanel, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        AddRaTextField = new JTextField();
        AddRaPanel.add(AddRaTextField, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        AddRaButton = new JButton();
        AddRaButton.setText("Add RA");
        AddRaPanel.add(AddRaButton, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        RemoveRaButton = new JButton();
        RemoveRaButton.setText("Remove Selected RA");
        RaPanel.add(RemoveRaButton, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        RaListScrollPane = new JScrollPane();
        RaPanel.add(RaListScrollPane, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        RaList = new JList();
        RaList.setSelectionMode(0);
        RaListScrollPane.setViewportView(RaList);
        BlackoutDatePanel = new JPanel();
        BlackoutDatePanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        BlackoutDatePanel.setEnabled(true);
        RaInfoSplitPane.setRightComponent(BlackoutDatePanel);
        RemoveBlackoutDateButton = new JButton();
        RemoveBlackoutDateButton.setText("Remove Selected Blackout Date");
        BlackoutDatePanel.add(RemoveBlackoutDateButton, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        AddBlackoutDatePanel = new JPanel();
        AddBlackoutDatePanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        BlackoutDatePanel.add(AddBlackoutDatePanel, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        BlackoutDateDayComboBox = new JComboBox();
        BlackoutDateDayComboBox.setToolTipText("Day");
        AddBlackoutDatePanel.add(BlackoutDateDayComboBox, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        BlackoutDateMonthComboBox = new JComboBox();
        BlackoutDateMonthComboBox.setToolTipText("Month");
        AddBlackoutDatePanel.add(BlackoutDateMonthComboBox, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        BlackoutDateYearComboBox = new JComboBox();
        BlackoutDateYearComboBox.setToolTipText("Year");
        AddBlackoutDatePanel.add(BlackoutDateYearComboBox, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        AddBlackoutDateButton = new JButton();
        AddBlackoutDateButton.setText("Add Blackout Date");
        AddBlackoutDatePanel.add(AddBlackoutDateButton, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        BlackoutDateListScrollPane = new JScrollPane();
        BlackoutDatePanel.add(BlackoutDateListScrollPane, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        BlackoutDateList = new JList();
        BlackoutDateList.setSelectionMode(0);
        BlackoutDateListScrollPane.setViewportView(BlackoutDateList);
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        DutySchedulerGui.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(2, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        ScheduleDutyButton = new JButton();
        ScheduleDutyButton.setText("Schedule Duty");
        DutySchedulerGui.add(ScheduleDutyButton, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ResultPanel = new JPanel();
        ResultPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        ResultPanel.setVisible(false);
        DutySchedulerGui.add(ResultPanel, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        OutputScrollPane = new JScrollPane();
        ResultPanel.add(OutputScrollPane, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        OutputTextArea = new JTextArea();
        OutputTextArea.setEditable(false);
        OutputTextArea.setRows(10);
        OutputTextArea.setText("");
        OutputScrollPane.setViewportView(OutputTextArea);
        SubmitToGCalPanel = new JPanel();
        SubmitToGCalPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        ResultPanel.add(SubmitToGCalPanel, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        CalendarNameLabel = new JLabel();
        CalendarNameLabel.setText("Calendar Name");
        SubmitToGCalPanel.add(CalendarNameLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        CalendarNameTextField = new JTextField();
        SubmitToGCalPanel.add(CalendarNameTextField, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        SubmitToGCalButton = new JButton();
        SubmitToGCalButton.setText("Export Scheduled Calendar To Google Calendar (Browser Window Will Appear)");
        SubmitToGCalPanel.add(SubmitToGCalButton, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        DutySchedulerGui.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(4, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        DutyBlockLengthLabel.setLabelFor(DutyBlockLengthTextField);
        RasPerDutyBlockLabel.setLabelFor(RasPerDutyBlockTextField);
        MonDayValueLabel.setLabelFor(MonDayValueTextField);
        TueDayValueLabel.setLabelFor(TueDayValueTextField);
        WedDayValueLabel.setLabelFor(WedDayValueTextField);
        ThursDayValueLabel.setLabelFor(ThursDayValueTextField);
        FriDayValueLabel.setLabelFor(FriDayValueTextField);
        SatDayValueLabel.setLabelFor(SatDayValueTextField);
        SunDayValueLabel.setLabelFor(SunDayValueTextField);
        CalendarNameLabel.setLabelFor(DutyBlockLengthTextField);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return DutySchedulerGui;
    }
}
