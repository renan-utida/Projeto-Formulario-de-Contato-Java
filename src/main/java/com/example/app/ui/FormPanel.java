package com.example.app.ui;

import com.example.app.model.Message;
import com.example.app.util.IconLoader;
import com.example.app.util.Validation;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.function.Consumer;

/** Tela 1: Formulário com validação em tempo real. */
public class FormPanel extends JPanel {

    private final JTextField txtName = new JTextField(20);
    private final JTextField txtEmail = new JTextField(20);
    private final JTextArea  txtBody = new JTextArea(6, 20);
    private final JButton btnSend   = new JButton("Enviar");
    private final JLabel lblNameError = new JLabel();
    private final JLabel lblEmailError = new JLabel();
    private final JLabel lblBodyError = new JLabel();

    private Consumer<Message> onSubmit = m -> {};

    // Bordas para indicar estado de validação
    private final Border normalBorder = UIManager.getBorder("TextField.border");
    private final Border errorBorder = BorderFactory.createLineBorder(Color.RED, 2);

    public FormPanel() {
        super(new BorderLayout(10, 10));
        setupValidation();
        setupUI();
    }

    private void setupValidation() {
        // Configura labels de erro
        lblNameError.setForeground(Color.RED);
        lblEmailError.setForeground(Color.RED);
        lblBodyError.setForeground(Color.RED);
        lblNameError.setFont(lblNameError.getFont().deriveFont(Font.PLAIN, 11f));
        lblEmailError.setFont(lblEmailError.getFont().deriveFont(Font.PLAIN, 11f));
        lblBodyError.setFont(lblBodyError.getFont().deriveFont(Font.PLAIN, 11f));

        // InputVerifier para Nome
        txtName.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                String text = ((JTextField) input).getText().trim();
                boolean valid = Validation.notBlank(text);
                updateFieldState(txtName, lblNameError, valid, "Nome é obrigatório");
                return true; // Sempre permite mudança de foco
            }
        });

        // InputVerifier para Email
        txtEmail.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                String text = ((JTextField) input).getText().trim();
                boolean valid = Validation.isEmail(text);
                updateFieldState(txtEmail, lblEmailError, valid, "E-mail inválido");
                return true;
            }
        });

        // FocusListener para TextArea (não suporta InputVerifier)
        txtBody.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                String text = txtBody.getText().trim();
                boolean valid = Validation.notBlank(text);
                updateTextAreaState(valid);
            }
        });
    }

    private void updateFieldState(JTextField field, JLabel errorLabel, boolean valid, String errorMsg) {
        SwingUtilities.invokeLater(() -> {
            if (valid) {
                field.setBorder(normalBorder);
                errorLabel.setText("");
            } else {
                field.setBorder(errorBorder);
                errorLabel.setText(errorMsg);
            }
            updateSubmitButton();
        });
    }

    private void updateTextAreaState(boolean valid) {
        SwingUtilities.invokeLater(() -> {
            if (valid) {
                txtBody.setBorder(normalBorder);
                lblBodyError.setText("");
            } else {
                txtBody.setBorder(errorBorder);
                lblBodyError.setText("Mensagem é obrigatória");
            }
            updateSubmitButton();
        });
    }

    private void updateSubmitButton() {
        boolean canSubmit = isFormValid();
        btnSend.setEnabled(canSubmit);
    }

    private boolean isFormValid() {
        return Validation.notBlank(txtName.getText().trim()) &&
                Validation.isEmail(txtEmail.getText().trim()) &&
                Validation.notBlank(txtBody.getText().trim());
    }

    private void setupUI() {
        // Centro: formulário com GridBagLayout
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,6,6,6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblName = new JLabel("Nome:");
        JLabel lblEmail = new JLabel("E-mail:");
        JLabel lblMsg = new JLabel("Mensagem:");

        txtBody.setLineWrap(true);
        txtBody.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(txtBody);

        int row = 0;
        // Nome
        gbc.gridx=0; gbc.gridy=row; gbc.weightx=0; form.add(lblName, gbc);
        gbc.gridx=1; gbc.gridy=row; gbc.weightx=1; form.add(txtName, gbc);
        row++;
        gbc.gridx=1; gbc.gridy=row; gbc.weightx=1; form.add(lblNameError, gbc);
        row++;

        // Email
        gbc.gridx=0; gbc.gridy=row; gbc.weightx=0; form.add(lblEmail, gbc);
        gbc.gridx=1; gbc.gridy=row; gbc.weightx=1; form.add(txtEmail, gbc);
        row++;
        gbc.gridx=1; gbc.gridy=row; gbc.weightx=1; form.add(lblEmailError, gbc);
        row++;

        // Mensagem
        gbc.gridx=0; gbc.gridy=row; gbc.weightx=0; form.add(lblMsg, gbc);
        gbc.gridx=1; gbc.gridy=row; gbc.weightx=1; form.add(scroll, gbc);
        row++;
        gbc.gridx=1; gbc.gridy=row; gbc.weightx=1; form.add(lblBodyError, gbc);

        // Rodapé: botão enviar
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        ImageIcon sendIcon = IconLoader.load("/icons/send.png", 18);
        if (sendIcon != null) btnSend.setIcon(sendIcon);
        btnSend.setToolTipText("Envia o formulário (Ctrl+Enter)");
        btnSend.setMnemonic('E');
        btnSend.setEnabled(false); // Inicia desabilitado
        footer.add(btnSend);

        // Ação do botão
        btnSend.addActionListener(e -> trySubmit());

        // Atalho Ctrl+Enter
        getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke("ctrl ENTER"), "send");
        getActionMap().put("send", new AbstractAction() {
            @Override public void actionPerformed(java.awt.event.ActionEvent e) {
                if (btnSend.isEnabled()) trySubmit();
            }
        });

        add(form, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
    }

    public void setOnSubmit(java.util.function.Consumer<Message> onSubmit) {
        this.onSubmit = onSubmit != null ? onSubmit : m -> {};
    }

    public void prefillName(String name) {
        txtName.setText(name == null ? "" : name);
        // Força validação do campo pré-preenchido
        SwingUtilities.invokeLater(() -> {
            txtName.getInputVerifier().verify(txtName);
        });
    }

    private void trySubmit() {
        if (!isFormValid()) {
            return; // Não deveria acontecer pois botão está desabilitado
        }

        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String body = txtBody.getText().trim();

        Message m = new Message(name, email, body);
        onSubmit.accept(m);

        // Limpeza dos campos
        txtBody.setText("");
        updateTextAreaState(false); // Força revalidação
    }
}
