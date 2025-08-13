package com.example.app.ui;

import com.example.app.model.Message;
import com.example.app.model.MessageHistory;
import com.example.app.util.IconLoader;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.List;

/** Tela 3: Histórico de mensagens enviadas. */
public class HistoryPanel extends JPanel {

    private final MessageHistory history;
    private final MessageTableModel tableModel;
    private final JTable table;
    private final JButton btnBack = new JButton("Voltar");
    private final JButton btnClear = new JButton("Limpar Histórico");
    private final JLabel lblCount = new JLabel("0 mensagens");

    public HistoryPanel(MessageHistory history) {
        super(new BorderLayout(10, 10));
        this.history = history;
        this.tableModel = new MessageTableModel();
        this.table = new JTable(tableModel);
        setupUI();
    }

    private void setupUI() {
        // Cabeçalho
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ImageIcon historyIcon = IconLoader.load("/icons/history.png", 20);
        JLabel title = new JLabel("Histórico de Mensagens");
        if (historyIcon != null) title.setIcon(historyIcon);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        header.add(title);
        header.add(Box.createHorizontalStrut(20));
        header.add(lblCount);

        // Tabela
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(24);
        table.getColumnModel().getColumn(0).setPreferredWidth(150); // Nome
        table.getColumnModel().getColumn(1).setPreferredWidth(200); // Email
        table.getColumnModel().getColumn(2).setPreferredWidth(120); // Data
        table.getColumnModel().getColumn(3).setPreferredWidth(300); // Mensagem

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(600, 300));

        // Rodapé
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnBack.setMnemonic('V');
        btnClear.setMnemonic('L');
        btnClear.setForeground(Color.RED.darker());
        footer.add(btnClear);
        footer.add(btnBack);

        // Ações
        btnClear.addActionListener(e -> clearHistory());

        add(header, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
    }

    public void refreshHistory() {
        SwingUtilities.invokeLater(() -> {
            tableModel.fireTableDataChanged();
            updateCountLabel();
        });
    }

    private void updateCountLabel() {
        int count = history.getMessageCount();
        lblCount.setText(count + (count == 1 ? " mensagem" : " mensagens"));
    }

    private void clearHistory() {
        int result = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja limpar todo o histórico?",
                "Confirmar Limpeza",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            history.clearHistory();
            refreshHistory();
        }
    }

    public void setOnBack(ActionListener al) {
        for (var l : btnBack.getActionListeners()) btnBack.removeActionListener(l);
        btnBack.addActionListener(al);
    }

    // Modelo da tabela
    private class MessageTableModel extends AbstractTableModel {
        private final String[] columns = {"Nome", "E-mail", "Data/Hora", "Mensagem"};
        private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        @Override
        public int getRowCount() {
            return history.getMessageCount();
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public String getColumnName(int column) {
            return columns[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            List<Message> messages = history.getAllMessages();
            if (rowIndex >= messages.size()) return "";

            Message msg = messages.get(messages.size() - 1 - rowIndex); // Mais recentes primeiro

            return switch (columnIndex) {
                case 0 -> msg.getName();
                case 1 -> msg.getEmail();
                case 2 -> msg.getCreatedAt().format(formatter);
                case 3 -> msg.getBody().length() > 50 ?
                        msg.getBody().substring(0, 47) + "..." :
                        msg.getBody();
                default -> "";
            };
        }
    }
}
